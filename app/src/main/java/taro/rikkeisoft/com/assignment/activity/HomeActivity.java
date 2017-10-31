package taro.rikkeisoft.com.assignment.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.adapter.NoteAdapter;
import taro.rikkeisoft.com.assignment.database.NoteDAO;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Common;
import taro.rikkeisoft.com.assignment.utils.Constant;
import taro.rikkeisoft.com.assignment.utils.GridSpacingItemDecoration;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 200;

    private RecyclerView rvNotes;
    private ProgressBar progressBar;
    private ArrayList<Note> mNotes;
    private NoteDAO mNoteDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();

        if (Common.isMarshMallow()) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                doMainWork();
            }
        } else {
            doMainWork();
        }
    }

    private void doMainWork() {
        new LoadNotes().execute();
    }

    private void initControls() {
        rvNotes = (RecyclerView) findViewById(R.id.rv_note);
        progressBar = (ProgressBar) findViewById(R.id.pb_load_note);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2);
        rvNotes.setLayoutManager(gridLayoutManager);
        rvNotes.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    private void settingToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle(getString(R.string.note));
            }
        }
    }

    private class LoadNotes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mNotes = (ArrayList<Note>) mNoteDAO.getAllElement();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            NoteAdapter noteAdapter = new NoteAdapter(mNotes, HomeActivity.this);
            rvNotes.setAdapter(noteAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PERMISSION_GRANTED && result1 == PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeAccepted && readAccepted) {
                        doMainWork();
                    } else {
                        Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                            showMessageOKCancel(getString(R.string.ask_permission),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermission();
                                        }
                                    });
                        }
                    }
                }
        }
    }

    private void showMessageOKCancel (String message, DialogInterface.OnClickListener onClickListener){
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.btn_ok), onClickListener)
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .create()
                .show();
    }

    private final BroadcastReceiver broadcastReceiverRefresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new LoadNotes().execute();
        }
    };

    private void registerBroadcastRefresh(){
        IntentFilter  intentFilter  = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_REFRESH_LIST);
        registerReceiver(broadcastReceiverRefresh, intentFilter);
    }

    private void unregisterBroadcastRefresh(){
        unregisterReceiver(broadcastReceiverRefresh);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(HomeActivity.this, HostActivity.class);
                if (mNotes != null && mNotes.size() > 0) {
                    intent.putExtra(Constant.KEY_LAST_NOTE_ID, mNotes.get(mNotes.size() - 1).getId());
                } else {
                    intent.putExtra(Constant.KEY_LAST_NOTE_ID, 0);
                }
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastRefresh();
    }
}

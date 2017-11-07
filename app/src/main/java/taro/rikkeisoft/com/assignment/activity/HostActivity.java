package taro.rikkeisoft.com.assignment.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.base.BaseActivity;
import taro.rikkeisoft.com.assignment.fragment.NewNoteFragment;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Common;
import taro.rikkeisoft.com.assignment.utils.Constant;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by VjrutNAT on 10/31/2017.
 */

public class HostActivity extends BaseActivity{

    private Note itemNote;
    private int lastNoteId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Common.isMarshMallow()) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                getDataFromIntent();
                NewNoteFragment newNoteFragment = NewNoteFragment.newInstance(itemNote, lastNoteId);
                showFragment(newNoteFragment, NewNoteFragment.TAG);
            }
        } else {
            getDataFromIntent();
            NewNoteFragment newNoteFragment = NewNoteFragment.newInstance(itemNote, lastNoteId);
            showFragment(newNoteFragment, NewNoteFragment.TAG);
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_host;
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        itemNote = (Note) intent.getExtras().getSerializable(Constant.KEY_NOTE_DETAIL);
        lastNoteId = intent.getExtras().getInt(Constant.KEY_LAST_NOTE_ID);
    }

    private void showFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_host, fragment, tag);
        transaction.commit();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PERMISSION_GRANTED && result1 == PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, Constant.PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeAccepted && readAccepted) {
                        getDataFromIntent();
                        NewNoteFragment newNoteFragment = NewNoteFragment.newInstance(itemNote, lastNoteId);
                        showFragment(newNoteFragment, NewNoteFragment.TAG);
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
        new AlertDialog.Builder(HostActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.btn_ok), onClickListener)
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .create()
                .show();
    }
}

package taro.rikkeisoft.com.assignment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.utils.Constant;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent(HomeActivity.this, HostActivity.class);
                intent.putExtra(Constant.KEY_LAST_NOTE_ID, )
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

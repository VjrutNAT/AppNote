package taro.rikkeisoft.com.assignment.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.activity.DetailActivity;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Constant;

/**
 * Created by VjrutNAT on 10/30/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{

    public static final String KEY_NOTE_TO_NOTIFY = "key_note_notify";

    @Override
    public void onReceive(Context context, Intent intent) {

        Note itemNoteReceiver;
        if (intent != null){
            itemNoteReceiver = (Note) intent.getExtras().getSerializable(KEY_NOTE_TO_NOTIFY);
            String title = "";
            long noteId = 0;

            if (itemNoteReceiver != null){
                title = itemNoteReceiver.getTitle();
                noteId = itemNoteReceiver.getId();
            }
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setAutoCancel(true);
            Intent intentToDetailActivity = new Intent(context, DetailActivity.class);
            ArrayList<Note> listNote = new ArrayList<>();
            listNote.add(itemNoteReceiver);
            intentToDetailActivity.putExtra(Constant.KEY_LIST_NOTE, listNote);
            intentToDetailActivity.putExtra(Constant.KEY_NOTE_POSITION, 0);
            intentToDetailActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) noteId, intentToDetailActivity, 0);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) noteId, builder.build());
        }
    }
}

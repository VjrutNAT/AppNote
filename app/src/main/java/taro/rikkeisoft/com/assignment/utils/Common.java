package taro.rikkeisoft.com.assignment.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import taro.rikkeisoft.com.assignment.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VjrutNAT on 10/30/2017.
 */

public class Common {

    public static final String TAG = "TARO";

    public static void writeLog(String tag, String content){
        Log.d(TAG + ": " + tag, content);
    }

    public static boolean isMarshMallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static String getRealPathFromURI(Context mContext, Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    public static String saveImage(Context mContext, Bitmap bitmap) {
        ContextWrapper wrapper = new ContextWrapper(mContext);
        File file = wrapper.getDir("Images", MODE_PRIVATE);
        file = new File(file, "Image-" + DateTimeUtils.getCurrentDateTimeInStr("yyyy-MM-dd$HH:mm:ss") + ".jpg");
        try {
            OutputStream stream;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

}

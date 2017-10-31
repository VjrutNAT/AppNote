package taro.rikkeisoft.com.assignment.base;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.adapter.ImageAdapter;
import taro.rikkeisoft.com.assignment.database.ImageDAO;
import taro.rikkeisoft.com.assignment.database.NoteDAO;
import taro.rikkeisoft.com.assignment.database.NoteDataBase;
import taro.rikkeisoft.com.assignment.interfaces.OnBackPressedListener;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.receiver.AlarmReceiver;
import taro.rikkeisoft.com.assignment.utils.Common;
import taro.rikkeisoft.com.assignment.utils.Constant;
import taro.rikkeisoft.com.assignment.utils.DateTimeUtils;
import taro.rikkeisoft.com.assignment.utils.GridSpacingItemDecoration;

/**
 * Created by VjrutNAT on 10/28/2017.
 */

public abstract class BaseFragment extends Fragment implements OnBackPressedListener, View.OnClickListener, Spinner.OnItemSelectedListener {

    protected int mLastNoteId, selectedColor;
    private Context mContext;
    private ViewGroup root;
    private BaseActivity mActivity;
    private View mView;
    private ImageView ivBackGroup;
    protected RecyclerView rvImageList;
    private EditText etTitle;
    private EditText etContent;
    protected TextView tvCurrentTime;
    private TextView tvAlarm;
    private LinearLayout llDate;
    private Spinner spDate;
    private Spinner spTime;
    private ImageView btCloseDateTime;
    private AlertDialog alertDialogColor, alertDialogPhotos;
    protected final NoteDAO mNoteDAO = NoteDAO.getInstance(getActivity());
    protected final ImageDAO mImageDAO = ImageDAO.getInstance(getActivity());
    protected ImageAdapter mImageAdapter;
    protected boolean isFirstTimeSpSelected, isFirstDateSpSelected;
    private boolean isChanged = false;
    protected ArrayAdapter spDateAdapter, spTimeAdapter;
    protected ArrayList<String> listDate, listTime;
    protected ArrayList<String> listImagePath = new ArrayList<>(), listImagePathOld = new ArrayList<>();
    protected Note mItemNote, itemNoteToSave;
    protected String strDateSelection = " ", strTimeSelection = " ";
    private AlarmManager alarmManager;

    public BaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

            mItemNote = (Note) bundle.getSerializable(Constant.KEY_NOTE_DETAIL);
            mLastNoteId = bundle.getInt(Constant.KEY_LAST_NOTE_ID);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = container;
        mActivity = (BaseActivity) getActivity();
        mActivity.setOnBackPressedListener(this);
        return inflater.inflate(getLayout(), container, false);
    }

    protected abstract int getLayout();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingToolbar(view);
        mView = view;
        initControls();
        initEvent();
        setUpImageList();
        setupSpinnerDateAndTime();
        setUpTextViewAndDateTime();
        isChanged = false;
    }

    private void initEvent() {
        tvAlarm.setOnClickListener(this);
        btCloseDateTime.setOnClickListener(this);
        spDate.setOnClickListener(this);
        spTime.setOnClickListener(this);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isChanged = true;
                Common.writeLog("changed = true", "changed = false");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etTitle.addTextChangedListener(textWatcher);
        etContent.addTextChangedListener(textWatcher);
    }

    private void setupSpinnerDateAndTime() {
        // Spinner Date
        listDate = new ArrayList<>();
        listDate.add(getString(R.string.today));
        listDate.add(getString(R.string.tomorrow));
        listDate.add(getString(R.string.next) + " " + DateTimeUtils.getCurrentDayOfWeek());
        listDate.add(getString(R.string.other));
        spDateAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_gallery_item, listDate);
        spDate.setAdapter(spDateAdapter);

        // Spinner Time
        listTime = new ArrayList<>();
        listTime.add(getString(R.string.sp_time_slot1));
        listTime.add(getString(R.string.sp_time_slot2));
        listTime.add(getString(R.string.sp_time_slot3));
        listTime.add(getString(R.string.other));
        spTimeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_gallery_item, listTime);
        spTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDate.setAdapter(spTimeAdapter);
    }

    protected abstract void setUpTextViewAndDateTime();

    private void initControls() {
        ivBackGroup = mView.findViewById(R.id.iv_background_detail);
        rvImageList = mView.findViewById(R.id.rv_image_list);
        etTitle = mView.findViewById(R.id.et_title);
        etContent = mView.findViewById(R.id.et_content);
        tvCurrentTime = mView.findViewById(R.id.tv_current_time);
        tvAlarm = mView.findViewById(R.id.tv_alarm);
        llDate = mView.findViewById(R.id.ll_date_time);
        spDate = mView.findViewById(R.id.sp_choose_date);
        spTime = mView.findViewById(R.id.sp_choose_time);
        btCloseDateTime = mView.findViewById(R.id.bt_close_date_time);
    }

    private void setUpImageList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        rvImageList.setLayoutManager(layoutManager);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(4, 30, false);
        rvImageList.addItemDecoration(itemDecoration);
        showImage(listImagePath);
    }

    protected abstract void showImage(ArrayList listImage);


    private void settingToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_more_vert_black_24dp);
            toolbar.setOverflowIcon(drawable);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = getActionBar();
            if (toolbar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                if (getHomeAsUpIndicator() != 0) {
                    actionBar.setHomeActionContentDescription(getHomeAsUpIndicator());
                }

                actionBar.setTitle(getActionBarName());
            }
        }
    }

    private String getActionBarName() {
        return getActivity().getResources().getString(R.string.note);
    }

    protected abstract int getHomeAsUpIndicator();

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void doBack() {

        if (listImagePath.size() != listImagePathOld.size()) {
            isChanged = true;
        }
        if (mItemNote != null) {
            String dateTime = strDateSelection + " " + strTimeSelection;
            long dateNotify = DateTimeUtils.parseStrDateTimeToMills(dateTime, NoteDataBase.SQL_DATE_FORMAT);
            if (dateNotify != mItemNote.getNotifyAlarm()) {
                isChanged = true;
            }
        }

        if (!isChanged) {
            mActivity.setOnBackPressedListener(null);
            mActivity.onBackPressed();
        } else {
            showConfirmSaveDialog();
        }
    }

    private void showConfirmSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.warning_title));
        builder.setMessage(getString(R.string.save_warning));
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (validateTitle()) {
                    saveNoteToDatabase();
                    setAlarm();
                    mActivity.setOnBackPressedListener(null);
                    mActivity.onBackPressed();
                }
            }
        });
        builder.setNegativeButton(getActivity().getString(R.string.discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                mActivity.setOnBackPressedListener(null);
                mActivity.onBackPressed();
            }
        });
    }

    protected abstract int getIdNoteToSave();

    protected abstract void setAlarm();

    private void saveNoteToDatabase() {
        String dateTime = strDateSelection + " " + strTimeSelection;
        long dateCreated = DateTimeUtils.parseStrDateTimeToMills(DateTimeUtils.getCurrentDateTimeInStr(NoteDataBase.SQL_DATE_FORMAT), NoteDataBase.SQL_DATE_FORMAT);
        long dateNotify = DateTimeUtils.parseStrDateTimeToMills(dateTime, NoteDataBase.SQL_DATE_FORMAT);
        itemNoteToSave = new Note(getIdNoteToSave(), etTitle.getText().toString(), etContent.getText().toString(),
                dateCreated, dateNotify, selectedColor);
        saveNote(itemNoteToSave);
    }

    private boolean validateTitle() {
        boolean isEntered = true;
        if (etTitle.getText().toString().isEmpty()) {
            Common.showDialog(getActivity(), getString(R.string.warning_title), getString(R.string.warning_content));
            isEntered = false;
        }
        return isEntered;
    }

    protected abstract void saveNote(Note itemNoteToSave);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                showPhotoChooserDialog();
                break;
            case R.id.action_color:
                showColorChooserDialog();
                break;
            case R.id.action_accept:
                if (!validateTitle()) {
                    break;
                }
                mActivity.setOnBackPressedListener(null);
                saveNoteToDatabase();
                setAlarm();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
//            case R.id.bt_new_note_menu:
//                Intent intent = new Intent(mContext, HostActivity.class);
//                intent.putExtra(Constant.KEY_LAST_NOTE_ID, lastNoteId);
//                mContext.startActivity(intent);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPhotoChooserDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.photos_chooser_dialog, root, false);  //inflate layout for dialog

        //find view from layout inflated to handle click event
        LinearLayout llTakePhotos = (LinearLayout) view.findViewById(R.id.ll_take_photos);
        LinearLayout llChoosePhotos = (LinearLayout) view.findViewById(R.id.ll_choose_photos);
        llTakePhotos.setOnClickListener(this);
        llChoosePhotos.setOnClickListener(this);

        alertDialogPhotos = new AlertDialog.Builder(getActivity()).create();
        alertDialogPhotos.setTitle(getString(R.string.insert_photo));
        alertDialogPhotos.setIcon(R.drawable.ic_camera_alt_black_24dp);
        alertDialogPhotos.setView(view);
        alertDialogPhotos.show();

    }

    private void showColorChooserDialog(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.chose_color, root, false);  //inflate layout for dialog

        ImageView imColor0 = view.findViewById(R.id.imv_color_0);
        ImageView imColor1 = view.findViewById(R.id.imv_color_1);
        ImageView imColor2 = view.findViewById(R.id.imv_color_2);
        ImageView imColor3 = view.findViewById(R.id.imv_color_3);

        imColor0.setOnClickListener(this);
        imColor1.setOnClickListener(this);
        imColor2.setOnClickListener(this);
        imColor3.setOnClickListener(this);

        alertDialogColor = new AlertDialog.Builder(getActivity()).create();
        alertDialogColor.setTitle(getString(R.string.select_color));
        alertDialogColor.setView(view);
        alertDialogColor.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_alarm:
                llDate.setVisibility(View.VISIBLE);
                tvAlarm.setVisibility(View.GONE);
                break;
            case R.id.bt_close_date_time:
                llDate.setVisibility(View.GONE);
                tvAlarm.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_take_photos:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constant.CAMERA_REQUEST);
                alertDialogPhotos.dismiss();
                break;
            case R.id.ll_choose_photos:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, Constant.GALLERY_REQUEST);
                alertDialogPhotos.dismiss();
                break;
            case R.id.imv_color_0:
                selectedColor = ContextCompat.getColor(getActivity(), R.color.color_0);
                ivBackGroup.setBackgroundColor(selectedColor);
                Common.writeLog("changed = true","change color");
                isChanged = true;
                break;
            case R.id.imv_color_1:
                selectedColor = ContextCompat.getColor(getActivity(), R.color.color_1);
                ivBackGroup.setBackgroundColor(selectedColor);
                Common.writeLog("changed = true","change color");
                isChanged = true;
                break;
            case R.id.imv_color_2:
                selectedColor = ContextCompat.getColor(getActivity(), R.color.color_2);
                ivBackGroup.setBackgroundColor(selectedColor);
                Common.writeLog("changed = true","change color");
                isChanged = true;
                break;
            case R.id.imv_color_3:
                selectedColor = ContextCompat.getColor(getActivity(), R.color.color_3);
                ivBackGroup.setBackgroundColor(selectedColor);
                Common.writeLog("changed = true","change color");
                isChanged = true;
                break;
        }
    }

    private final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dateOfMonth) {
            strDateSelection = year + "-" + (month + 1) + "-" + dateOfMonth;
            listDate.remove(3);
            listDate.add(strTimeSelection);
            spDateAdapter.notifyDataSetChanged();
        }
    };

    private final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            strTimeSelection = hourOfDay + ":" + minute;
            listTime.remove(3);
            listTime.add(strTimeSelection);
            spTimeAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        switch (adapterView.getId()){
            case R.id.sp_choose_date:
                switch (position){
                    case 0:
                        strDateSelection = year + "-" + month + "-" + dayOfMonth;
                        break;
                    case 1:
                        strDateSelection = year + "-" + month + "-" + dayOfMonth + 1;
                        break;
                    case 2:
                        strDateSelection = year + "-" + month + "-" + dayOfMonth + 7;
                        break;
                    case 3:
                        if (!isFirstDateSpSelected){
                            new DatePickerDialog(getActivity(), onDateSetListener, year, month, dayOfMonth).show();
                        }
                        isFirstDateSpSelected = false;
                        break;
                    default:
                        break;

                }
                break;
            case R.id.sp_choose_time:
                switch (position){
                    case 3:
                        if (!isFirstTimeSpSelected){
                            new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true).show();
                        }
                        isFirstTimeSpSelected = false;
                        break;
                    default:
                        strTimeSelection = spTime.getSelectedItem().toString();
                        break;

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void setNotify(){
        alarmManager = (AlarmManager) mContext.getSystemService(AppCompatActivity.ALARM_SERVICE);
        Intent intentToAlarmClass = new Intent(getActivity(), AlarmReceiver.class);
        Note itemNoteNotify;
        long createdDateTime = DateTimeUtils.parseStrDateTimeToMills(DateTimeUtils.getCurrentDateTimeInStr(NoteDataBase.SQL_DATE_FORMAT), NoteDataBase.SQL_DATE_FORMAT);
        long notifyDateTime = DateTimeUtils.parseStrDateTimeToMills(strDateSelection + " " + strTimeSelection, NoteDataBase.SQL_DATE_FORMAT);
        Common.writeLog("Notify", notifyDateTime + " ");
        itemNoteNotify = new Note(getIdNoteToSave(), etTitle.getText().toString(), etContent.getText().toString(), createdDateTime, notifyDateTime,
                selectedColor);
        intentToAlarmClass.putExtra(AlarmReceiver.KEY_NOTE_TO_NOTIFY, itemNoteNotify);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),getIdNoteToSave(), intentToAlarmClass, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notifyDateTime, pendingIntent);
    }

    protected void cancelNotify() {
        alarmManager = (AlarmManager) getActivity().getSystemService(AppCompatActivity.ALARM_SERVICE);
        Intent intentToAlarmClass = new Intent(getActivity(), AlarmReceiver.class);
        Note itemNoteToNotify;
        long createdDateTime = DateTimeUtils.parseStrDateTimeToMills(DateTimeUtils.getCurrentDateTimeInStr(NoteDataBase.SQL_DATE_FORMAT), NoteDataBase.SQL_DATE_FORMAT);
        long notifyDateTime = DateTimeUtils.parseStrDateTimeToMills(strDateSelection + " " + strTimeSelection, NoteDataBase.SQL_DATE_FORMAT);
        Common.writeLog("Notify", notifyDateTime + "");
        itemNoteToNotify = new Note(getIdNoteToSave(), etTitle.getText().toString(), etContent.getText().toString(),
                createdDateTime, notifyDateTime, selectedColor);
        intentToAlarmClass.putExtra(AlarmReceiver.KEY_NOTE_TO_NOTIFY, itemNoteToNotify);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), getIdNoteToSave(),
                intentToAlarmClass, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
        alarmManager.cancel(pendingIntent);
    }
}

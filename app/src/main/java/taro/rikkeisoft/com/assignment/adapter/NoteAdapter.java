package taro.rikkeisoft.com.assignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.activity.DetailActivity;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Constant;
import taro.rikkeisoft.com.assignment.utils.DateTimeUtils;

/**
 * Created by VjrutNAT on 10/25/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> mNotes;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public NoteAdapter(ArrayList<Note> mNotes, Context mContext) {
        this.mNotes = mNotes;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {

        Note note = mNotes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.cvNote.setCardBackgroundColor(note.getColor());
        holder.tvTime.setText(DateTimeUtils.getDateStrFromMilliseconds(note.getCreateAlarm(), "yyyy-MM-dd"));
        holder.pos = holder.getAdapterPosition();
        if (note.getNotifyAlarm() > 0) {
            holder.imvAlarm.setVisibility(View.VISIBLE);
        }else {
            holder.imvAlarm.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int pos;
        private CardView cvNote;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvTime;
        private ImageView imvAlarm;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            imvAlarm = itemView.findViewById(R.id.imv_alarm);
            cvNote = itemView.findViewById(R.id.cv_note);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Constant.KEY_LIST_NOTE, mNotes);
            intent.putExtra(Constant.KEY_NOTE_POSITION, pos);
            mContext.startActivity(intent);
        }
    }
}

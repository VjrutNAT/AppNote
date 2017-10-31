package taro.rikkeisoft.com.assignment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.SharedPreferencesCompat;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.fragment.EditNoteFragment;
import taro.rikkeisoft.com.assignment.model.Note;

/**
 * Created by VjrutNAT on 10/28/2017.
 */

public class NoteViewPagerAdapter extends FragmentPagerAdapter{

    private final ArrayList<Note> mNotes;

    public NoteViewPagerAdapter(FragmentManager fm, ArrayList<Note> mNotes) {
        super(fm);
        this.mNotes = mNotes;
    }

    @Override
    public Fragment getItem(int position) {
        return EditNoteFragment.newInstance(mNotes.get(position), (int) mNotes.get(mNotes.size() - 1).getId());
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mNotes.get(position).getTitle();
    }
}

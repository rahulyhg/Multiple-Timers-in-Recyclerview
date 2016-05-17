package com.roomadvantage.app.myapplication;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder> {
    private static final String TAG = "CountAdapter";

    private final int mDummyCount = 20;

    private final List<Long> mCountUpList = new ArrayList<>();
    private final ArrayList<Boolean> mSelectedRows = new ArrayList<>();

    public CountAdapter() {

        mCountUpList.clear();
        for (int i = 0; i < mDummyCount; i++) {
            mCountUpList.add(i, -1L);
        }
        mSelectedRows.clear();
        for (int i = 0; i < mDummyCount; i++) {
            mSelectedRows.add(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v, mCountUpList, mSelectedRows);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkTimerStatus();
        Log.d(TAG, "onBindViewHolder: " + position + " " + String.valueOf(mCountUpList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mDummyCount;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "ViewHolder";
        private final ArrayList<Boolean> mSelectedRows;
        private final Chronometer mTimeText;
        private final ImageButton mStartButton;
        private final CheckBox mCheckBox;
        private final List<Long> mCountUpList;

        public ViewHolder(View v, List<Long> mCountUp,
                          ArrayList<Boolean> mSelectedRows) {

            super(v);
            this.mCountUpList = mCountUp;
            this.mSelectedRows = mSelectedRows;
            mTimeText = (Chronometer) v.findViewById(R.id.itemText);
            mStartButton = (ImageButton) v.findViewById(R.id.ibPlay);
            mCheckBox = (CheckBox) v.findViewById(R.id.checkBox);
            mStartButton.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.ibPlay:

                    Long milliSec = mCountUpList.get(getAdapterPosition());
                    if (milliSec == -1L) {
                        milliSec = SystemClock.elapsedRealtime();
                        mTimeText.setBase(milliSec);
                        Log.d(TAG, "onClick " + milliSec);
                        mTimeText.start();
                    } else {
                        milliSec = -1L;
                        mTimeText.stop();
                        mTimeText.setText("00:00");
                    }
                    mCountUpList.set(getAdapterPosition(), milliSec);
                    break;

                case R.id.checkBox:

                    mSelectedRows.set(getAdapterPosition(),
                            !mSelectedRows.get(getAdapterPosition()));
                    break;
            }
        }

        public void checkTimerStatus() {

            Long startedTime = mCountUpList.get(getAdapterPosition());
            if (startedTime != -1L) {

                long currentTime = SystemClock.elapsedRealtime();
                long difference = currentTime - startedTime;
                mTimeText.setBase(currentTime - difference);
                Log.d(TAG, "difference " + difference);
                mTimeText.start();
            } else {

                mTimeText.stop();
                mTimeText.setText("00:00");
            }
            boolean state = mSelectedRows.get(getAdapterPosition());
            mCheckBox.setChecked(state);
        }
    }

} 
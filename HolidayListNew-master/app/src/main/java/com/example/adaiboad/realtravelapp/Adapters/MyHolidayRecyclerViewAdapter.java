package com.example.adaiboad.realtravelapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adaiboad.realtravelapp.Fragments.HolidayListFragment;
import com.example.adaiboad.realtravelapp.Model.Holiday;
import com.example.adaiboad.realtravelapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Holiday} and makes a call to the
 * specified {@link HolidayListFragment.OnEditHolidayListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHolidayRecyclerViewAdapter extends RecyclerView.Adapter<MyHolidayRecyclerViewAdapter.ViewHolder> {

    private final HolidayListFragment.OnEditHolidayListener mListener;


    //add for loop
    //create a class in here to store your holidays that will be private to this guy
    public MyHolidayRecyclerViewAdapter( HolidayListFragment.OnEditHolidayListener listener) {
        mListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_holiday, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = HolidayData.getHoliday(position);
        holder.mIdView.setText(holder.mItem.getTitle());
        holder.mContentView.setText(holder.mItem.getNotes());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    Holiday item = holder.mItem;
                    Log.i("INFO", "we clicked on holiday " + item.getTitle());
                    mListener.OnEditHolidayListener(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return HolidayData.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public Holiday mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.holidayitem);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.holidayImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}

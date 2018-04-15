package com.example.adaiboad.realtravelapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adaiboad.realtravelapp.Adapters.MyHolidayRecyclerViewAdapter;
import com.example.adaiboad.realtravelapp.Model.Holiday;
import com.example.adaiboad.realtravelapp.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnEditHolidayListener}
 * interface.
 */
public class HolidayListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnEditHolidayListener mEditHolidayListener;
    private OnAddNewHolidayListener mAddHolidayListener;
    public MyHolidayRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HolidayListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HolidayListFragment newInstance(int columnCount) {
        HolidayListFragment fragment = new HolidayListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holiday_list, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addHolidayButton);

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.holidaylist);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyHolidayRecyclerViewAdapter(mEditHolidayListener);
            recyclerView.setAdapter(adapter);

        //add trip button
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               mAddHolidayListener.OnAddNewHolidayListener();
                //Snackbar.make(view, "Holiday added to list", Snackbar.LENGTH_LONG) .setAction("Action", null).show();

            }});

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditHolidayListener) {
            mEditHolidayListener = (OnEditHolidayListener) context;
            mAddHolidayListener = (OnAddNewHolidayListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEditHolidayListener = null;
        mAddHolidayListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEditHolidayListener {
        // TODO: Update argument type and name
        void OnEditHolidayListener(Holiday item);
    }
    public interface OnAddNewHolidayListener {
        // TODO: Update argument type and name
        void OnAddNewHolidayListener();
    }
}

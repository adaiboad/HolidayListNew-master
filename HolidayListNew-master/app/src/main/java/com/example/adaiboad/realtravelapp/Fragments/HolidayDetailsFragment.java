package com.example.adaiboad.realtravelapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adaiboad.realtravelapp.Adapters.HolidayData;
import com.example.adaiboad.realtravelapp.Adapters.PlaceAutoCompleteAdapter;
import com.example.adaiboad.realtravelapp.MapsActivity;
import com.example.adaiboad.realtravelapp.Model.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.common.api.GoogleApiClient;

import com.example.adaiboad.realtravelapp.Model.Holiday;
import com.example.adaiboad.realtravelapp.Model.HolidayDatePicker;
import com.example.adaiboad.realtravelapp.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HolidayDetailsFragment.OnHolidayDetailsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HolidayDetailsFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class HolidayDetailsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String HOLIDAY = "Holiday";
    public static final String ADDNEW = "AddNew";
    private static final String TAG = "HolidayDetailsFragment" ;

    // TODO: Rename and change types of parameters
   private Holiday holiday;
private boolean addNew;


    private Calendar newStartDate;
    private Calendar newEndDate;


    private OnHolidayDetailsInteractionListener mListener;
    private Button startDate;
    private GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView location;
    private Button endDate;
    private PlaceAutoCompleteAdapter mAdapter;
    private PlaceInfo mPlace;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

  private EditText title;
    private  EditText notes;

    public HolidayDetailsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HolidayDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static HolidayDetailsFragment newInstance(String param1, String param2) {
//        HolidayDetailsFragment fragment = new HolidayDetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Check if we have a holiday passed to us");
        if (getArguments() != null) {
            Log.i("INFO", "We have a holiday passed to us");
            holiday = (Holiday) getArguments().getSerializable(HOLIDAY);
            addNew = getArguments().getBoolean(ADDNEW);
            Log.i("INFO", "The holoday's title is" + holiday.getTitle());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_holiday_details, container, false);

        Button dateButton = (Button)view.findViewById(R.id.dateButton);
        dateButton.setPaintFlags(dateButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button endDateButton = (Button)view.findViewById(R.id.endDate);
        endDateButton.setPaintFlags(endDateButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //create editable title
          title = view.findViewById(R.id.editTitle);
            title.setText(holiday.getTitle());

        //create editable notes
      notes = view.findViewById(R.id.editNotes);
        notes.setText(holiday.getNotes());


            //Change the date via the calendar
        startDate = (Button)view.findViewById(R.id.dateButton);
        startDate.setText(holiday.formatStartDate());
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, true);
            }
        });

        endDate = (Button)view.findViewById(R.id.endDate);
        endDate.setText(holiday.formatEndDate());
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, false);
            }
        });

        LinearLayout coverPhoto = view.findViewById(R.id.coverPhoto);
        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        //places auto complete when a user picks a place
        location = (AutoCompleteTextView)view.findViewById(R.id.locationPicker);
        location.setText(holiday.getLocation());



Button saveButton = view.findViewById(R.id.saveHolidayButton);
saveButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(addNew)
            HolidayData.addHoliday(holiday);


        holiday.setTitle(title.getText().toString());
        holiday.setNotes(notes.getText().toString());
        holiday.setLocation(location.getText().toString());
        holiday.setStartDate(newStartDate);
        holiday.setEndDate(newEndDate);
    }
});


        //set textview using the item the user picks


        init();

        return view;
    }


    public void geoLocation(){
        String searchString = location.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "onClick: IOException: " + e.getMessage() );

        }
        if(list.size() > 0){
            Address address = list.get(0);


    }

    }



    private void init() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        mAdapter = new PlaceAutoCompleteAdapter(getContext(), mGoogleApiClient, LAT_LNG_BOUNDS, null);
        location.setAdapter(mAdapter);
        location.setOnItemClickListener(mAutocompleteClickListener);

        location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocation();
                }
                return false;
            }

        });




    }




        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnHolidayDetailsInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHolidayDetailsInteractionListener) {
            mListener = (OnHolidayDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHolidayDetailsInteractionListener {
        // TODO: Update argument type and name
        void OnHolidayDetailsInteractionListener(Uri uri);
    }




    public void showDatePickerDialog(View v, Boolean isStartDate) {
        HolidayDatePicker newFragment = new HolidayDatePicker();
        newFragment.setHoliday(newStartDate,newEndDate);
        if(isStartDate) {
            Log.i("INFO", "setting startdate button");
            newFragment.setDateButton(startDate);

        }
        else{
            Log.i("INFO", "setting enddate button");
            newFragment.setDateButton(endDate);
        }
        newFragment.setStartDate(isStartDate);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

 /*
        --------------------------- google places API autocomplete suggestions -----------------
 */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            final AutocompletePrediction item = mAdapter.getItem(i);
            final String placeId = item.getPlaceId();



            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


        }

    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }


            places.release();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

}

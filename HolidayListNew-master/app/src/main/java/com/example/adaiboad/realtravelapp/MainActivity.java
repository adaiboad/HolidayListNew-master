package com.example.adaiboad.realtravelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;

import com.example.adaiboad.realtravelapp.Fragments.HolidayDetailsFragment;
import com.example.adaiboad.realtravelapp.Fragments.HolidayListFragment;
import com.example.adaiboad.realtravelapp.Model.Holiday;

public class MainActivity extends AppCompatActivity implements  HolidayListFragment.OnAddNewHolidayListener, HolidayListFragment.OnEditHolidayListener,        HolidayDetailsFragment.OnHolidayDetailsInteractionListener {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent1);
                case R.id.navigation_map:
                    Intent intent2 = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.navigation_gallery:
                    Intent intent3 = new Intent(MainActivity.this, GalleryActivity.class);
                    startActivity(intent3);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        HolidayListFragment holidayListFragment = new HolidayListFragment();
        // Create a new Fragment to be placed in the activity layout
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        holidayListFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, holidayListFragment).commit();

}

private void goToHoldayDetailsPage(Holiday item, boolean addNew)
{
    Toast.makeText(this, "You clicked " + item.toString(), Toast.LENGTH_LONG).show();
    // Create the new fragment,

    HolidayDetailsFragment holidayDetailsFragment = new HolidayDetailsFragment();
    // add an argument specifying the item it should show
    // note that the DummyItem class must implement Serializable
    Bundle args = new Bundle();
    Log.i("INFO", "storing the holiday in the arguments using the key Item");
    args.putSerializable(HolidayDetailsFragment.HOLIDAY, item);
    args.putBoolean(HolidayDetailsFragment.ADDNEW, addNew);
    holidayDetailsFragment.setArguments(args);

    FragmentTransaction transaction =
            getSupportFragmentManager().beginTransaction();

    // Replace whatever is in the fragment_container view with this fragment,
    // and add the transaction to the back stack so the user can navigate back
    transaction.replace(R.id.fragment_container, holidayDetailsFragment);
    transaction.addToBackStack(null);

    // Commit the transaction
    transaction.commit();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void OnEditHolidayListener(Holiday item) {
goToHoldayDetailsPage(item,false);
    }

    @Override
    public void OnAddNewHolidayListener() {
        Holiday item = new Holiday("title", "note");
        goToHoldayDetailsPage(item,true);
    }

    @Override
    public void OnHolidayDetailsInteractionListener(Uri uri) {

    }


}

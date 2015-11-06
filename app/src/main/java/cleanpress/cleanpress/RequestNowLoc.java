package cleanpress.cleanpress;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.codec.binary.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RequestNowLoc extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener
        ,LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    Typeface segoe;

    EditText address,unit,zip;
    Button Continue;
    TextView Title1,Title2,SpinnerTitle,date,day,hour;
    Spinner spinner;
    CheckBox Confirm;
    String[] items;
    MySpinnerAdapter adapter;
    ArrayList<String> Home,Work,Other;


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_now_first);
        setUpMapIfNeeded();
        addMenu();

        Home = new ArrayList<String>();
        Work = new ArrayList<String>();
        Other = new ArrayList<String>();

        ParseQuery<ParseObject> home = new ParseQuery<ParseObject>("Addresses");
        home.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location","Home");
        home.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                    for (ParseObject result : list){
                        String sAddress = result.getString("Address");
                        String sCity = result.getString("Unit_Suite");
                        String sZip = result.getString("ZipCode");

                        Home.add(0,sAddress);
                        Home.add(1,sCity);
                        Home.add(2,sZip);
                    }
                }

        });

        ParseQuery<ParseObject> work = new ParseQuery<ParseObject>("Addresses");
        work.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location","Work");
        work.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    Work.add(0, sAddress);
                    Work.add(1, sCity);
                    Work.add(2, sZip);
                }
            }

        });

        ParseQuery<ParseObject> other = new ParseQuery<ParseObject>("Addresses");
        other.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location","Other");
        other.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    Other.add(0, sAddress);
                    Other.add(1, sCity);
                    Other.add(2, sZip);
                }
            }

        });

        items = new String[]{"Don't Save","Home","Work","Other"};
        adapter = new MySpinnerAdapter(getApplicationContext(),R.layout.titles_spinner,items);


        address = (EditText) findViewById(R.id.pick_address);
        unit = (EditText) findViewById(R.id.pick_city);
        zip = (EditText) findViewById(R.id.pick_zip);
        Title1 = (TextView) findViewById(R.id.title1);
        Title2 = (TextView) findViewById(R.id.title2);
        SpinnerTitle = (TextView) findViewById(R.id.spinnerText);
        date = (TextView) findViewById(R.id.Date);
        day = (TextView) findViewById(R.id.Day);
        hour = (TextView) findViewById(R.id.Hour);
        Continue = (Button) findViewById(R.id.button);
        Confirm = (CheckBox) findViewById(R.id.Confirm);
        spinner = (Spinner) findViewById(R.id.select_loc);

        zip.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (zip.getText().toString().length() == 4){

                    ParseQuery<ParseObject> markers = new ParseQuery<ParseObject>("Driver");
                    markers.whereEqualTo("Zipcode",Integer.valueOf(zip.getText().toString()));
                    markers.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                for (ParseObject driver : list) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(driver.getDouble("DriverLatitude"), driver.getDouble("DriverLongitude")))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.tracking_green_circle))
                                    );
                                }

                            }
                        }
                    });
                } else {
                    mMap.clear();
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                    .title("I am here!"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        segoe = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");

        address.setTypeface(segoe);
        unit.setTypeface(segoe);
        zip.setTypeface(segoe);
        Title1.setTypeface(segoe);
        Title2.setTypeface(segoe);
        SpinnerTitle.setTypeface(segoe);
        date.setTypeface(segoe);
        day.setTypeface(segoe);
        hour.setTypeface(segoe);
        Continue.setTypeface(segoe);
        Confirm.setTypeface(segoe);

        spinner.setAdapter(adapter);

        disableEdit(address);
        disableEdit(unit);
        disableEdit(zip);

        Date today = new Date();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat dateFormat =  new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat compareFormat = new SimpleDateFormat("kk:mm");

        Calendar cal = Calendar.getInstance();

        String limitHour = "21:30";
        Date pickHour = new Date();
        try {
            Date limited = compareFormat.parse(limitHour);
            int compare = pickHour.compareTo(limited);
            if (compare < 1){
                int rDay = cal.get(Calendar.DAY_OF_MONTH);
                int rMonth = cal.get(Calendar.MONTH);
                int rYear = cal.get(Calendar.YEAR);
                String rHour = "8:00 am";
                if (rDay == 31){
                    String viewerDate = (rMonth+2)+"/"+(rDay-30)+"/"+rYear;
                    date.setText(viewerDate);
                    Date viewDate = dateFormat.parse(viewerDate);
                    day.setText(dayFormat.format(viewDate));
                    hour.setText(rHour);

                } else {
                    String viewerDate = (rMonth+1)+"/"+(rDay+1)+"/"+rYear;
                    date.setText(viewerDate);
                    Date viewDate = dateFormat.parse(viewerDate);
                    day.setText(dayFormat.format(viewDate));
                    hour.setText(rHour);
                }
            }
            else {

                Calendar hourCal = Calendar.getInstance();
                hourCal.setTime(pickHour);
                hourCal.add(Calendar.MINUTE, 30);
                    day.setText(dayFormat.format(today));
                    date.setText(dateFormat.format(today));
                String limHour = hourCal.get(Calendar.HOUR)+":"+hourCal.get(Calendar.MINUTE) +" "+ hourCal.get(Calendar.AM_PM);
                    hour.setText(hourFormat.format(today) + " - " + limHour);
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }



        //Set the Google Api client to get the callbacks.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object for the current location of the user.
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)// 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final android.location.LocationListener Listener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                    String sAddress = addresses.get(0).getThoroughfare();
                    String sZip = addresses.get(0).getPostalCode();

                    address.setText(sAddress);
                    zip.setText(sZip);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, Listener);
                        break;
                    case 1:
                        locationManager.removeUpdates(Listener);
                        if (Home.size() == 3){
                            address.setText(Home.get(0));
                            unit.setText(Home.get(1));
                            zip.setText(Home.get(2));
                        }
                        break;
                    case 2:
                        locationManager.removeUpdates(Listener);
                        if (Work.size() == 3){
                            address.setText(Work.get(0));
                            unit.setText(Work.get(1));
                            zip.setText(Work.get(2));
                        }
                        break;
                    case 3:
                        locationManager.removeUpdates(Listener);
                        if (Other.size() == 3){
                            address.setText(Other.get(0));
                            unit.setText(Other.get(1));
                            zip.setText(Other.get(2));
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, Listener);
            }
        });



    }

    public void toSecondScreen (View view){

        final String sUnit = unit.getText().toString();


        ParseQuery<ParseObject> zipcode = new ParseQuery<ParseObject>("ZipCode");
        zipcode.whereEqualTo("ZipCode", zip.getText().toString());
        zipcode.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                   final String city = parseObject.getString("City");

                    int pos = spinner.getSelectedItemPosition();

                    switch (pos) {
                        case 0:
                            Intent next = new Intent(RequestNowLoc.this, RequestNowAddress.class);
                            next.putExtra("pAddress", address.getText().toString());
                            next.putExtra("pZip", zip.getText().toString());
                            next.putExtra("pUnit", sUnit);
                            next.putExtra("city",city);
                            next.putExtra("pDate", date.getText().toString());
                            next.putExtra("pHour", hour.getText().toString());
                            startActivity(next);
                            break;
                        case 1:
                            ParseQuery<ParseObject> lHome = new ParseQuery<ParseObject>("Addresses");
                            lHome.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Home");
                            lHome.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (parseObject != null) {
                                        Intent next = new Intent(RequestNowLoc.this, RequestNowAddress.class);
                                        next.putExtra("pAddress", address.getText().toString());
                                        next.putExtra("pZip", zip.getText().toString());
                                        next.putExtra("pUnit", sUnit);
                                        next.putExtra("city",city);
                                        next.putExtra("pDate", date.getText().toString());
                                        next.putExtra("pHour", hour.getText().toString());
                                        startActivity(next);
                                    } else {
                                        ParseObject create = new ParseObject("Addresses");
                                        create.put("Location", "Home");
                                        create.put("username", ParseUser.getCurrentUser().getUsername());
                                        create.put("Address", address.getText().toString());
                                        create.put("ZipCode", zip.getText().toString());
                                        create.put("Unit_Suite", unit.getText().toString());
                                        create.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(getApplicationContext(), "Your Address will be saved as Home", Toast.LENGTH_SHORT).show();
                                                    Intent next = new Intent(RequestNowLoc.this, RequestNowAddress.class);
                                                    next.putExtra("pAddress", address.getText().toString());
                                                    next.putExtra("pZip", zip.getText().toString());
                                                    next.putExtra("pUnit", sUnit);
                                                    next.putExtra("city",city);
                                                    next.putExtra("pDate", date.getText().toString());
                                                    next.putExtra("pHour", hour.getText().toString());
                                                    startActivity(next);
                                                } else {
                                                    Log.e("Error HOME: ", e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            break;
                        case 2:
                            ParseQuery<ParseObject> lWork = new ParseQuery<ParseObject>("Addresses");
                            lWork.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Work");
                            lWork.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (parseObject != null) {
                                        Intent next = new Intent(RequestNowLoc.this, RequestNowAddress.class);
                                        next.putExtra("pAddress", address.getText().toString());
                                        next.putExtra("pZip", zip.getText().toString());
                                        next.putExtra("pUnit", sUnit);
                                        next.putExtra("city",city);
                                        next.putExtra("pDate", date.getText().toString());
                                        next.putExtra("pHour", hour.getText().toString());
                                        startActivity(next);
                                    } else {
                                        ParseObject create = new ParseObject("Addresses");
                                        create.put("Location", "Work");
                                        create.put("username", ParseUser.getCurrentUser().getUsername());
                                        create.put("Address", address.getText().toString());
                                        create.put("ZipCode", zip.getText().toString());
                                        create.put("Unit_Suite", unit.getText().toString());
                                        create.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(getApplicationContext(), "Your Address will be saved as Work", Toast.LENGTH_SHORT).show();
                                                    Intent next = new Intent(RequestNowLoc.this, RequestNowAddress.class);
                                                    next.putExtra("pAddress", address.getText().toString());
                                                    next.putExtra("pZip", zip.getText().toString());
                                                    next.putExtra("pUnit", sUnit);
                                                    next.putExtra("city",city);
                                                    next.putExtra("pDate", date.getText().toString());
                                                    next.putExtra("pHour", hour.getText().toString());
                                                    startActivity(next);
                                                } else {
                                                    Log.e("Error Work: ", e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            break;
                        case 3:
                            ParseQuery<ParseObject> lOther = new ParseQuery<ParseObject>("Addresses");
                            lOther.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Other");
                            lOther.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (parseObject != null) {
                                        Intent next = new Intent(RequestNowLoc.this, RequestNowAddress.class);
                                        next.putExtra("pAddress", address.getText().toString());
                                        next.putExtra("pZip", zip.getText().toString());
                                        next.putExtra("pUnit", sUnit);
                                        next.putExtra("city",city);
                                        next.putExtra("pDate", date.getText().toString());
                                        next.putExtra("pHour", hour.getText().toString());
                                        startActivity(next);
                                    } else {
                                        ParseObject create = new ParseObject("Addresses");
                                        create.put("Location", "Other");
                                        create.put("username", ParseUser.getCurrentUser().getUsername());
                                        create.put("Address", address.getText().toString());
                                        create.put("ZipCode", zip.getText().toString());
                                        create.put("Unit_Suite", unit.getText().toString());
                                        create.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(getApplicationContext(), "Your Address will be saved as Other", Toast.LENGTH_SHORT).show();
                                                    Intent next = new Intent(RequestNowLoc.this, RequestNowAddress.class);
                                                    next.putExtra("pAddress", address.getText().toString());
                                                    next.putExtra("pZip", zip.getText().toString());
                                                    next.putExtra("pUnit", sUnit);
                                                    next.putExtra("city",city);
                                                    next.putExtra("pDate", date.getText().toString());
                                                    next.putExtra("pHour", hour.getText().toString());
                                                    startActivity(next);
                                                } else {
                                                    Log.e("Error Other: ", e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            break;
                    }

                }
            }
        });
    }

    private void disableEdit (EditText editText){

        editText.setFocusable(false);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setKeyListener(null);
        editText.setTextColor(Color.rgb(193,194,199));
        editText.setCursorVisible(false);
        editText.setEnabled(false);

    }

    public void confirmStep (View view) {

        if (Confirm.isChecked()){
            Continue.setEnabled(true);
            colorButton();
        } else {
            Continue.setEnabled(false);
            colorButton();
        }

    }

    private void colorButton () {
        if (Continue.isEnabled()){
            Continue.setBackgroundColor(Color.rgb(127,230,139));
        } else {
            Continue.setBackgroundColor(Color.rgb(193,194,199));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle bundle) {

        //Here we can get a user Location, just to know if GPS is working.
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        //Location will be null only if GPS is disabled. So, we have to ask to the user for turning it ON.
        if (location == null) {
            //Keep on searching for updates in the location of the user through the method requestLocationUpdates.
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


            //Show a dialog when the user has disabled the GPS on the device.
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
            dialog.setMessage("Location services are turned off.");

            //Make a button in the dialog that will show the Location settings when clicked
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps settings
                }
            });

            //Make another button to dismiss the dialog.
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();

        }
        else {
            //When the GPS is working just call the method to get the current location.
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {

            Log.i("Request Log: " , "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    //This is where we handle the user current location on the Map.
    private void handleNewLocation(Location location) {
        Log.e("Request Log: ", location.toString());

        //Declare variables to get the Latitude and Longitude and unite
        // them to use it for the Android class named LatLng.
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //Create a Marker to pin the user current Location and handle the
        // rotation, bearing and other configs for the camera position on the map.
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");

        mMap.addMarker(options);



        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(14)
                .build();

        //Make the Camera animation works in a specific way.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



    }

    private void addMenu() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.login_title, null);
        TextView textView = (TextView) view.findViewById(R.id.mytext);
        textView.setWidth(getWindow().getWindowManager().getDefaultDisplay().getWidth());
        textView.setPadding(0, 0, 50, 0);
        textView.setGravity(Gravity.CENTER);

        Typeface segoeui = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        textView.setTypeface(segoeui);
        textView.setText("Your Order");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {

        public MySpinnerAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        Typeface segoeuit = Typeface.createFromAsset(getContext().getAssets(),"fonts/segoeui.ttf");
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(segoeuit);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(segoeuit);
            return view;
        }

    }

}

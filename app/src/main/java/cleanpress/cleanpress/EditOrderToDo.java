package cleanpress.cleanpress;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EditOrderToDo extends ActionBarActivity {

    String pAddress,pZip,pUnit,pSpecial,pDate,pHour,dAddress,dZip,dUnit,dSpecial,dDate,dHour,oNumber;


    Animation fadeIn,fadeOut;
    LinearLayout pickAddressLay,pickHourLay,delivAddressLay,delivHourLay;
    Typeface segoe;
    ArrayList<String> Home,Work,Other;
    ScrollView scrollView;
    Button placeOrder;


    /**
     *                  pickAddressUI
     * */

    TextView titlePick,spinnerPickText;
    CheckBox pickCurrentLoc;
    EditText pickAddress,pickUnit,pickZip,pickSpecial;
    Spinner pickLocation;
    MySpinnerAdapter adapter;
    String[] items;

    /**
     *                  pickHourUI
     * */

    TextView pickTimeTitle,pickDate,pickDay;
    CheckBox sameAddress;
    Spinner pickHour;
    MyHoursSpinnerAdapter HourAdapter;

    /**
     *                  delivAddressUI
     * */

    TextView delivAddTitle,spinnerDelivText;
    CheckBox delivCurrentLoc;
    EditText delivAddress,delivUnit,delivZip,delivSpecial;
    Spinner delivLocation;

    /**
     *                  delivHourUI
     * */

    TextView delivTimeTitle,delivDate,delivDay;
    Spinner delivHour;
    MyHoursSpinnerAdapter delivHourAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order_to_do);
        addMenu();

        //  String pAddress,pZip,pUnit,pSpecial,pDate,pHour,dAddress,dZip,dUnit,dSpecial,dDate,dHour,oNumber;

        pAddress = getIntent().getExtras().getString("pAddress");
        pZip = getIntent().getExtras().getString("pZip");
        pUnit = getIntent().getExtras().getString("pUnit");
        pSpecial = getIntent().getExtras().getString("pSpecial");
        pDate = getIntent().getExtras().getString("pDate");
        pHour = getIntent().getExtras().getString("pHour");

        dAddress = getIntent().getExtras().getString("dAddress");
        dZip = getIntent().getExtras().getString("dZip");
        dUnit = getIntent().getExtras().getString("dUnit");
        dSpecial = getIntent().getExtras().getString("dSpecial");
        dDate = getIntent().getExtras().getString("dDate");
        dHour = getIntent().getExtras().getString("dHour");

        oNumber = getIntent().getExtras().getString("oNumber");

        Home = new ArrayList<String>();
        Work = new ArrayList<String>();
        Other = new ArrayList<String>();

        ParseQuery<ParseObject> home = new ParseQuery<ParseObject>("Addresses");
        home.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location","Home");
        home.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sUnit = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    Home.add(0, sAddress);
                    Home.add(1, sUnit);
                    Home.add(2, sZip);
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
                    String sUnit = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    Work.add(0, sAddress);
                    Work.add(1, sUnit);
                    Work.add(2, sZip);
                }
            }

        });

        ParseQuery<ParseObject> other = new ParseQuery<ParseObject>("Addresses");
        other.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Other");
        other.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sUnit = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    Other.add(0, sAddress);
                    Other.add(1, sUnit);
                    Other.add(2, sZip);
                }
            }

        });

        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

        pickAddressLay = (LinearLayout) findViewById(R.id.EpickAddressLay);
        pickHourLay = (LinearLayout) findViewById(R.id.EpickHourLay);
        delivAddressLay = (LinearLayout) findViewById(R.id.EdelivAddressLay);
        delivHourLay = (LinearLayout) findViewById(R.id.EdelivHourLay);
        placeOrder = (Button) findViewById(R.id.EbuttonPlace);

        scrollView = (ScrollView) findViewById(R.id.scrollEdit);

        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");


        /**
         * First Section (Pick Address)
         * */

        titlePick = (TextView) findViewById(R.id.EtitlePick);
        spinnerPickText = (TextView) findViewById(R.id.EspinnerPickText);
        pickCurrentLoc = (CheckBox) findViewById(R.id.EpickCurrent);
        pickAddress = (EditText) findViewById(R.id.Epick_address);
        pickUnit = (EditText) findViewById(R.id.Epick_unit);
        pickZip = (EditText) findViewById(R.id.Epick_zip);
        pickSpecial = (EditText) findViewById(R.id.Epick_special);
        pickLocation = (Spinner) findViewById(R.id.EpickSelect_loc);

        titlePick.setTypeface(segoe);
        spinnerPickText.setTypeface(segoe);
        pickCurrentLoc.setTypeface(segoe);
        pickAddress.setTypeface(segoe);
        pickUnit.setTypeface(segoe);
        pickZip.setTypeface(segoe);
        pickSpecial.setTypeface(segoe);

        pickAddress.setText(pAddress);
        pickZip.setText(pZip);
        pickUnit.setText(pUnit);
        pickSpecial.setText(pSpecial);

        items = new String[]{"Don't Save","Home","Work","Other"};
        adapter = new MySpinnerAdapter(getApplicationContext(),R.layout.titles_spinner,items);
        pickLocation.setAdapter(adapter);

        pickZip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pickZip.getText().toString();
                if (text.length() == 5) {
                    pickHourLay.startAnimation(fadeIn);
                    fadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            pickHourLay.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    if (pickHourLay.isShown()) {
                        pickHourLay.startAnimation(fadeOut);
                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                pickHourLay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pickLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        enableEdit(pickAddress);
                        enableEdit(pickZip);
                        enableEdit(pickUnit);


                        break;
                    case 1:

                        if (Home.size() == 3) {
                            pickAddress.setText(Home.get(0));
                            pickUnit.setText(Home.get(1));
                            pickZip.setText(Home.get(2));

                            disableEdit(pickAddress);
                            disableEdit(pickZip);
                            disableEdit(pickUnit);

                            pickCurrentLoc.setChecked(false);
                        }
                        break;
                    case 2:

                        if (Work.size() == 3) {
                            pickAddress.setText(Work.get(0));
                            pickUnit.setText(Work.get(1));
                            pickZip.setText(Work.get(2));

                            disableEdit(pickAddress);
                            disableEdit(pickZip);
                            disableEdit(pickUnit);

                            pickCurrentLoc.setChecked(false);
                        }
                        break;
                    case 3:

                        if (Other.size() == 3) {
                            pickAddress.setText(Other.get(0));
                            pickUnit.setText(Other.get(1));
                            pickZip.setText(Other.get(2));

                            disableEdit(pickAddress);
                            disableEdit(pickZip);
                            disableEdit(pickUnit);

                            pickCurrentLoc.setChecked(false);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Second Section (Pick Hour)
         * */

        pickTimeTitle = (TextView) findViewById(R.id.EpickTimeTitle);
        pickDate = (TextView) findViewById(R.id.EpickDate);
        pickDay = (TextView) findViewById(R.id.EpickDay);
        pickHour = (Spinner) findViewById(R.id.EpickHour);
        sameAddress = (CheckBox) findViewById(R.id.EuseSameAddress);

        pickTimeTitle.setTypeface(segoe);
        pickDate.setTypeface(segoe);
        pickDay.setTypeface(segoe);
        sameAddress.setTypeface(segoe);
        pickDate.setText(pDate);

        String[] splitted = pDate.split("/");
        int nDay = Integer.valueOf(splitted[1]);
        int nMonth = Integer.valueOf(splitted[0]);
        int nYear = Integer.valueOf(splitted[2]);
        pickDay.setText(DateFormat.format("EEEE",new Date(nYear,nMonth,nDay)).toString());

        ParseQuery<ParseObject> pickHours = new ParseQuery<ParseObject>("Horarios");
        pickHours.orderByAscending("createdAt");
        pickHours.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ArrayList<String> ch = new ArrayList<String>();
                    for (ParseObject object : list) {
                        String hour = object.getString("AM");
                        ch.add(hour);
                    }
                    HourAdapter = new MyHoursSpinnerAdapter(getApplicationContext(), R.layout.titles_spinner, ch);
                    pickHour.setAdapter(HourAdapter);
                } else {
                    Log.e("Error: ", e.getMessage());
                }
            }
        });

        pickDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!pickDate.getText().toString().trim().isEmpty()) {

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date today = new Date();
                    try {
                        Date picked = sdf.parse(pickDate.getText().toString());
                        int comp = today.compareTo(picked);
                        if (comp <= 0){
                            Toast.makeText(getApplicationContext(), "true" + today.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"false" + today.toString(),Toast.LENGTH_SHORT).show();
                        }

                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }


                    if (delivHourLay.isShown()){

                    } else {
                        delivAddressLay.setVisibility(View.VISIBLE);
                        delivAddressLay.startAnimation(fadeIn);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                } else {
                    if (delivAddressLay.isShown()) {
                        delivAddressLay.startAnimation(fadeOut);
                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                delivAddressLay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * Third Section (Delivery Address)
         * */

        delivAddTitle = (TextView) findViewById(R.id.EtitleDelivery);
        spinnerDelivText = (TextView) findViewById(R.id.EdelivSpinnerText);
        delivCurrentLoc = (CheckBox) findViewById(R.id.EdelivCurrent);
        delivAddress = (EditText) findViewById(R.id.Edeliv_address);
        delivUnit = (EditText) findViewById(R.id.Edeliv_unit);
        delivZip = (EditText) findViewById(R.id.Edeliv_zip);
        delivSpecial = (EditText) findViewById(R.id.Edeliv_special);
        delivLocation = (Spinner) findViewById(R.id.EdelivSelect_loc);

        delivLocation.setAdapter(adapter);

        delivAddTitle.setTypeface(segoe);
        spinnerDelivText.setTypeface(segoe);
        delivCurrentLoc.setTypeface(segoe);
        delivAddress.setTypeface(segoe);
        delivZip.setTypeface(segoe);
        delivUnit.setTypeface(segoe);
        delivSpecial.setTypeface(segoe);

        delivAddress.setText(dAddress);
        delivZip.setText(dZip);
        delivUnit.setText(dUnit);
        delivSpecial.setText(dSpecial);

        delivLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        enableEdit(delivAddress);
                        enableEdit(delivZip);
                        enableEdit(delivUnit);

                        break;
                    case 1:
                        if (Home.size() == 3) {
                            delivAddress.setText(Home.get(0));
                            delivUnit.setText(Home.get(1));
                            delivZip.setText(Home.get(2));

                            disableEdit(delivAddress);
                            disableEdit(delivZip);
                            disableEdit(delivUnit);

                            delivCurrentLoc.setChecked(false);
                        }
                        break;
                    case 2:

                        if (Work.size() == 3) {
                            delivAddress.setText(Work.get(0));
                            delivUnit.setText(Work.get(1));
                            delivZip.setText(Work.get(2));

                            disableEdit(delivAddress);
                            disableEdit(delivZip);
                            disableEdit(delivUnit);

                            delivCurrentLoc.setChecked(false);
                        }
                        break;
                    case 3:

                        if (Other.size() == 3) {
                            delivAddress.setText(Other.get(0));
                            delivUnit.setText(Other.get(1));
                            delivZip.setText(Other.get(2));

                            disableEdit(delivAddress);
                            disableEdit(delivZip);
                            disableEdit(delivUnit);

                            delivCurrentLoc.setChecked(false);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        delivAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!sameAddress.isChecked()) {
                    if (delivAddress.getText().toString().length() > 1) {
                        sameAddress.startAnimation(fadeOut);
                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                sameAddress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    } else {
                        sameAddress.setVisibility(View.VISIBLE);
                        sameAddress.startAnimation(fadeIn);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        delivZip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (delivZip.getText().toString().length() == 5) {
                    delivHourLay.setVisibility(View.VISIBLE);
                    delivHourLay.startAnimation(fadeIn);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                } else {
                    if (delivHourLay.isShown()) {
                        delivHourLay.startAnimation(fadeOut);
                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                delivHourLay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * Fourth Section (Delivery Hour)
         * */

        delivHour = (Spinner) findViewById(R.id.EdelivHour);
        delivTimeTitle = (TextView) findViewById(R.id.EdelivHourTitle);
        delivDate = (TextView) findViewById(R.id.EdelivDate);
        delivDay = (TextView) findViewById(R.id.EdelivDay);

        delivTimeTitle.setTypeface(segoe);
        delivDate.setTypeface(segoe);
        delivDay.setTypeface(segoe);
        delivDate.setText(dDate);
        String[] data = dDate.split("/");
        String dayString = data[1];
        int day = Integer.valueOf(dayString);
        String monthString = data[0];
        int month = Integer.valueOf(monthString);
        String yearString = data[2];
        int year = Integer.valueOf(yearString);
        delivDay.setText(DateFormat.format("EEEE",new Date(year,month,day)).toString());


        ParseQuery<ParseObject> delivHours = new ParseQuery<ParseObject>("Hours");
        delivHours.orderByAscending("createdAt");
        delivHours.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ArrayList<String> ch = new ArrayList<String>();
                    for (ParseObject object : list) {
                        String hour = object.getString("PM");
                        ch.add(hour);
                    }
                    delivHourAdapter = new MyHoursSpinnerAdapter(getApplicationContext(), R.layout.titles_spinner, ch);
                    delivHour.setAdapter(delivHourAdapter);
                } else {
                    Log.e("Error: ", e.getMessage());
                }
            }
        });

        delivDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!delivDate.getText().toString().trim().isEmpty()){
                    placeOrder.setVisibility(View.VISIBLE);
                    placeOrder.setEnabled(true);
                    placeOrder.setBackgroundColor(Color.parseColor("#7fe68b"));
                    placeOrder.setTextColor(Color.parseColor("#ffffff"));
                    placeOrder.startAnimation(fadeIn);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                } else {
                    if (placeOrder.isShown()){
                        placeOrder.startAnimation(fadeOut);
                        placeOrder.setEnabled(false);
                        placeOrder.setBackgroundColor(Color.parseColor("#c1c2c7"));
                        placeOrder.setTextColor(Color.parseColor("#444444"));
                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                placeOrder.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private static class MyHoursSpinnerAdapter extends ArrayAdapter<String> {

        public MyHoursSpinnerAdapter(Context context, int resource, ArrayList<String> objects) {
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

    public void EcurrentLocationPick (View view) {

        if (pickCurrentLoc.isChecked()){

            new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                        String sAddress = addresses.get(0).getThoroughfare();
                        String sZip = addresses.get(0).getPostalCode();

                        pickAddress.setText(sAddress);
                        pickZip.setText(sZip);


                        disableEdit(pickAddress);
                        disableEdit(pickZip);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            };
        } else {

            enableEdit(pickAddress);
            enableEdit(pickZip);


        }

    }

    public void EcurrentLocationDeliv (View view) {

        if (delivCurrentLoc.isChecked()){

            new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                        String sAddress = addresses.get(0).getThoroughfare();
                        String sZip = addresses.get(0).getPostalCode();


                        delivAddress.setText(sAddress);
                        delivZip.setText(sZip);


                        disableEdit(delivAddress);
                        disableEdit(delivZip);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            };
        } else {

            enableEdit(pickAddress);
            enableEdit(pickZip);


        }

    }

    private void disableEdit (EditText editText){

        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.rgb(193, 194, 199));
        editText.setEnabled(false);

    }

    private void enableEdit (EditText editText){

        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.parseColor("#444444"));
        editText.setEnabled(true);

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

    public void setPickupDate (View view){
        Calendar cal = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(this, datePickerListenerPickup,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        //Create a cancel button and set the title of the dialog.
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();
    }

    public void setDelivDate (View view) {
        Calendar cal = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(this, datePickerListenerDdelivery,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        //Create a cancel button and set the title of the dialog.
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();
    }

    public void EuseSameAddress (View view){
        if (sameAddress.isChecked()){
            delivAddress.setText(pickAddress.getText().toString());
            delivHour.setSelection(pickHour.getSelectedItemPosition());
            delivUnit.setText(pickUnit.getText().toString());
            delivZip.setText(pickZip.getText().toString());
            delivSpecial.setText(pickSpecial.getText().toString());
            delivAddressLay.startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    delivAddressLay.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            disableEdit(delivAddress);
            disableEdit(delivUnit);
            disableEdit(delivZip);
            disableEdit(delivSpecial);

        } else {
            delivAddressLay.setVisibility(View.VISIBLE);
            delivAddressLay.startAnimation(fadeIn);
            delivAddress.setText("");
            delivHour.setSelection(0);
            delivUnit.setText("");
            delivZip.setText("");
            delivSpecial.setText("");
            enableEdit(delivAddress);
            enableEdit(delivUnit);
            enableEdit(delivZip);
            enableEdit(delivSpecial);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerPickup = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is called, below method will be called.
        // The arguments will be working to get the Day of Week to show it in a special TextView for it.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            pickDate.setText(month1 + "/" + day1 + "/" + year1);
            pickDay.setText(DateFormat.format("EEEE", new Date(selectedYear, selectedMonth, selectedDay - 1)).toString());
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerDdelivery = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is called, below method will be called.
        // The arguments will be working to get the Day of Week to show it in a special TextView for it.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            delivDate.setText(month1 + "/" + day1 + "/" + year1);
            delivDay.setText(DateFormat.format("EEEE", new Date(selectedYear, selectedMonth, selectedDay - 1)).toString());
        }
    };

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

    public void placeOrderEdited (View view){

        Date today = new Date();
        int compareToday,compareDates;
        Date dPickDate,dDelivDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String pickD,delivD;
        pickD = pickDate.getText().toString();
        delivD = delivDate.getText().toString();

        try {
            /**
             * compare today with pick
             * */
            dPickDate = simpleDateFormat.parse(pickD);
            compareToday = dPickDate.compareTo(today);
            if (compareToday < 1){
                Toast.makeText(getApplicationContext(),"Error pick Date",Toast.LENGTH_SHORT).show();

            } else {
                dDelivDate = simpleDateFormat.parse(delivD);

                compareDates = dPickDate.compareTo(dDelivDate);
                if (compareDates >= 0){
                    Toast.makeText(getApplicationContext(),"Error deliv Date",Toast.LENGTH_SHORT).show();

                } else {

                    /**
                     * Start Order Process
                     * */
                    ParseQuery<ParseObject> validatePickZip = new ParseQuery<ParseObject>("ZipCode");
                    validatePickZip.whereEqualTo("ZipCode",pickZip.getText().toString());
                    validatePickZip.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject zip1, ParseException error1) {
                            if (error1==null){
                                String city = zip1.getString("City");

                                ParseQuery<ParseObject> validateDelivZip = new ParseQuery<ParseObject>("ZipCode");
                                validateDelivZip.whereEqualTo("ZipCode",delivZip.getText().toString()).whereEqualTo("City",city);
                                validateDelivZip.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject zip2, ParseException error2) {
                                        if (error2==null){
                                            final int dLoc = delivLocation.getSelectedItemPosition();
                                            int pLoc = pickLocation.getSelectedItemPosition();
                                            switch (pLoc){
                                                case 0:
                                                    switch (dLoc){
                                                        case 0:
                                                            submitOrder();
                                                            break;
                                                        case 1:
                                                            saveHome(delivAddress,delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 2:
                                                            saveWork(delivAddress, delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 3:
                                                            saveOther(delivAddress, delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                    }
                                                    break;
                                                case 1:
                                                    saveHome(pickAddress,pickZip,pickUnit);
                                                    switch (dLoc){
                                                        case 0:
                                                            submitOrder();
                                                            break;
                                                        case 1:
                                                            saveHome(delivAddress, delivZip, delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 2:
                                                            saveWork(delivAddress, delivZip, delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 3:
                                                            saveOther(delivAddress, delivZip, delivUnit);
                                                            submitOrder();
                                                            break;
                                                    }
                                                    break;
                                                case 2:
                                                    saveWork(pickAddress, pickZip, pickUnit);
                                                    switch (dLoc){
                                                        case 0:
                                                            submitOrder();
                                                            break;
                                                        case 1:
                                                            saveHome(delivAddress,delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 2:
                                                            saveWork(delivAddress,delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 3:
                                                            saveOther(delivAddress, delivZip, delivUnit);
                                                            submitOrder();
                                                            break;
                                                    }
                                                    break;
                                                case 3:
                                                    saveOther(pickAddress, pickZip, pickUnit);
                                                    switch (dLoc){
                                                        case 0:
                                                            submitOrder();
                                                            break;
                                                        case 1:
                                                            saveHome(delivAddress,delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 2:
                                                            saveWork(delivAddress,delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                        case 3:
                                                            saveOther(delivAddress,delivZip,delivUnit);
                                                            submitOrder();
                                                            break;
                                                    }
                                                    break;
                                            }



                                        } else {
                                            Toast.makeText(getApplicationContext(),"Invalid Delivery Location",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(),"Invalid Pickup Location",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }


        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    }

    private void saveHome (final EditText address,final EditText zip,final EditText Unit){

        ParseQuery<ParseObject> home = new ParseQuery<ParseObject>("Addresses");
        home.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location","Home");
        home.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null){

                    ParseObject Home = new ParseObject("Addresses");
                    Home.put("Address",address.getText().toString());
                    Home.put("Location","Home");
                    Home.put("Unit_Suite",Unit.getText().toString());
                    Home.put("ZipCode",zip.getText().toString());
                    Home.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                Toast.makeText(getApplicationContext(),"Your address is saved",Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Error saving Home: ",e.getLocalizedMessage());
                            }
                        }
                    });

                }
            }
        });
    }

    private void saveWork(final EditText address, final EditText zip,final EditText Unit) {
        ParseQuery<ParseObject> work = new ParseQuery<ParseObject>("Addresses");
        work.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Work");
        work.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                    ParseObject Work = new ParseObject("Addresses");
                    Work.put("Address", address.getText().toString());
                    Work.put("Location", "Work");
                    Work.put("Unit_Suite",Unit.getText().toString());
                    Work.put("ZipCode",zip.getText().toString());
                    Work.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                Toast.makeText(getApplicationContext(), "Your address is saved", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e("Error saving Home: ", e.getLocalizedMessage());
                            }
                        }
                    });

                }
            }
        });
    }

    private void saveOther(final EditText address, final EditText zip, final EditText Unit) {
        ParseQuery<ParseObject> Other = new ParseQuery<ParseObject>("Addresses");
        Other.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Other");
        Other.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                    ParseObject Other = new ParseObject("Addresses");
                    Other.put("Address", address.getText().toString());
                    Other.put("Location", "Other");
                    Other.put("Unit_Suite", Unit.getText().toString());
                    Other.put("ZipCode",zip.getText().toString());
                    Other.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                Toast.makeText(getApplicationContext(),"Your address is saved",Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e("Error saving Home: ",e.getLocalizedMessage());
                            }
                        }
                    });

                }
            }
        });
    }

    private void submitOrder (){

        ParseQuery<ParseObject> orderNumber = new ParseQuery<ParseObject>("Order");
        orderNumber.orderByDescending("OrderNumber");
        orderNumber.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e==null){
                    long orderNumber = parseObject.getLong("OrderNumber");

                    parseObject.put("DeliveryAddress",delivAddress.getText().toString());
                    parseObject.put("DeliveryUnit",delivUnit.getText().toString());
                    parseObject.put("DeliverySpecialNotes",delivSpecial.getText().toString());
                    parseObject.put("DeliveryZipcode",delivZip.getText().toString());
                    parseObject.put("Deliveryhour",delivHour.getSelectedItem().toString());
                    parseObject.put("Dropoffdate",delivDate.getText().toString());

                    parseObject.put("PickupAddress",pickAddress.getText().toString());
                    parseObject.put("PickupUnit",pickUnit.getText().toString());
                    parseObject.put("PickupSpecialNotes",pickSpecial.getText().toString());
                    parseObject.put("PickupZipcode",pickZip.getText().toString());
                    parseObject.put("PickHour",pickHour.getSelectedItem().toString());
                    parseObject.put("Pickupdate",pickDate.getText().toString());

                    parseObject.put("OrderNumber",orderNumber);
                    parseObject.put("Paid",false);
                    parseObject.put("Verify",false);
                    parseObject.put("Done",false);
                    parseObject.put("Todo",false);
                    parseObject.put("WashFold",false);
                    parseObject.put("DryCleaning",false);

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                startActivity(new Intent(EditOrderToDo.this,MyOrders.class));
                                Toast.makeText(getApplicationContext(),"Your order is being processed",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });






                }
            }
        });

    }
}

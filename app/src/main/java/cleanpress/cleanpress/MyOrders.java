package cleanpress.cleanpress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;


public class MyOrders extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders);

        addMenu();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setTitle(Html.fromHtml("<font color='#afafaf'>ActionBartitle </font>"));
        actionBar.show();

        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }

    public void requestPick (View view) {
        startActivity(new Intent(MyOrders.this,ScheduleScreen.class));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private void addMenu() {

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.order_title, null);

        final ImageView basket = (ImageView) view.findViewById(R.id.basket);

        ParseQuery<ParseObject> cestica = new ParseQuery<ParseObject>("Order");
        cestica.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Verify", true);
        cestica.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null){
                    basket.setVisibility(View.VISIBLE);
                }else {
                    basket.setVisibility(View.GONE);
                }
            }
        });
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyOrders.this, OrderStatus.class);
                startActivity(i);
            }
        });

        TextView textView = (TextView) view.findViewById(R.id.mytext);
        textView.setWidth(getWindow().getWindowManager().getDefaultDisplay().getWidth());
        textView.setPadding(0, 0, 50, 0);
        textView.setGravity(Gravity.CENTER);

        Typeface segoeui = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        textView.setTypeface(segoeui);
        textView.setText("My Orders");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return PlaceholderFragment1.newInstance(position + 1);
                case 1:
                    return PlaceholderFragment2.newInstance(position + 1);
                case 2:
                    return PlaceholderFragment3.newInstance(position + 1);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


    public static class PlaceholderFragment1 extends Fragment {

        Typeface segoe;
        ProgressCircle progressCircle;
        ProgressCircle2 progressCircle2;
        ProgressCircle3 progressCircle3;
        ImageView icon, check1, check2, check3, dPic;
        TextView text, titleTracking, titlePick, titleDeli;
        private Button btnFeedback;
        float val = 0, val2 = 0, val3 = 0;
        int delay = 0; //milliseconds
        String pDay,pDate,pHour,pAddress,dDay,dDate,dHour,dAddress, pCity, dCity, driverUser;
        TextView pickDay,pickDate,pickHour,pickAddress,delivDay,delivDate,delivHour,delivAddress, pickCity, deliCity, driverName;
        boolean isPickupBarActive = false;
        boolean isDryBarActive = false;
        boolean isDeliBarActive = false;
        LinearLayout trackingLayout, requestLayout, infoLayout;
        RelativeLayout progressLayout;

        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getUsername();


        private static final String ARG_SECTION_NUMBER = "section_number";


        public static PlaceholderFragment1 newInstance(int sectionNumber) {
            PlaceholderFragment1 fragment = new PlaceholderFragment1();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment1() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.doing_screen, container, false);

            segoe = Typeface.createFromAsset(getActivity().getAssets(),"fonts/segoeui.ttf");
            icon = (ImageView)  rootView.findViewById(R.id.imgV_icon);
            check1 = (ImageView) rootView.findViewById(R.id.check1);
            check2 = (ImageView) rootView.findViewById(R.id.check2);
            check3 = (ImageView) rootView.findViewById(R.id.check3);
            dPic = (ImageView) rootView.findViewById(R.id.driverpic);
            text = (TextView)  rootView.findViewById(R.id.tv_phase);
            btnFeedback = (Button) rootView.findViewById(R.id.Feedback);
            driverName = (TextView) rootView.findViewById(R.id.driverName);
            titleTracking = (TextView) rootView.findViewById(R.id.titleTracking);
            titlePick = (TextView) rootView.findViewById(R.id.titlePick);
            titleDeli = (TextView) rootView.findViewById(R.id.titleDeli);

            text.setTypeface(segoe);
            driverName.setTypeface(segoe);
            titleTracking.setTypeface(segoe);
            titlePick.setTypeface(segoe);
            titleDeli.setTypeface(segoe);
            btnFeedback.setTypeface(segoe);

            pickDay = (TextView) rootView.findViewById(R.id.day_pick);
            pickDate = (TextView) rootView.findViewById(R.id.date_pick);
            pickHour = (TextView) rootView.findViewById(R.id.hour_pick);
            pickAddress = (TextView) rootView.findViewById(R.id.Address_pick);
            pickCity = (TextView) rootView.findViewById(R.id.pick_city);
            delivDay = (TextView) rootView.findViewById(R.id.day_deliv);
            delivDate = (TextView) rootView.findViewById(R.id.date_deliv);
            delivHour = (TextView) rootView.findViewById(R.id.hour_deliv);
            delivAddress = (TextView) rootView.findViewById(R.id.Address_deliv);
            deliCity = (TextView) rootView.findViewById(R.id.deli_city);

            trackingLayout = (LinearLayout) rootView.findViewById(R.id.trackingLayout);
            requestLayout = (LinearLayout) rootView.findViewById(R.id.cocnao);
            infoLayout = (LinearLayout) rootView.findViewById(R.id.infoLayout);
            progressLayout = (RelativeLayout) rootView.findViewById(R.id.progressLayout);

            pickDay.setTypeface(segoe);
            pickDate.setTypeface(segoe);
            pickHour.setTypeface(segoe);
            pickAddress.setTypeface(segoe);
            pickCity.setTypeface(segoe);
            delivDay.setTypeface(segoe);
            delivDate.setTypeface(segoe);
            delivHour.setTypeface(segoe);
            delivAddress.setTypeface(segoe);
            deliCity.setTypeface(segoe);

            progressCircle = (ProgressCircle) rootView.findViewById(R.id.progress_circle);
            progressCircle2 = (ProgressCircle2) rootView.findViewById(R.id.progress_circle2);
            progressCircle3 = (ProgressCircle3) rootView.findViewById(R.id.progress_circle3);

            //progressCircle.setProgress(0);
            progressCircle.setTextColor(android.R.color.transparent);
            //progressCircle2.setProgress(0);
            progressCircle2.setTextColor(android.R.color.transparent);
            //progressCircle3.setProgress(0);
            progressCircle3.setTextColor(android.R.color.transparent);

            check1.setImageDrawable(getResources().getDrawable(R.drawable.tracking_grey_circle));
            check2.setImageDrawable(getResources().getDrawable(R.drawable.tracking_grey_circle));
            check3.setImageDrawable(getResources().getDrawable(R.drawable.tracking_grey_circle));

            ParseQuery<ParseObject> get_date = new ParseQuery<ParseObject>("Order");
            get_date.whereEqualTo("username", username).orderByAscending("createdAt");
            get_date.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e==null){

                        for (ParseObject result : list){

                        }
                    }
                }
            });

            //Make a query to the Order class in parse to get the name of the assigned driver
            ParseQuery<ParseObject> searchOrder = new ParseQuery<ParseObject>("Order");
            searchOrder.whereEqualTo("username", username);
            searchOrder.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {

                        driverUser = parseObject.getString("DriverUsername");

                        //Make a new query with the driver's name in the Driver class in parse to get the picture and the info
                        ParseQuery<ParseObject> searchPic = new ParseQuery<ParseObject>("Driver");
                        searchPic.whereEqualTo("DriverUsername", driverUser);
                        searchPic.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {

                                    //Set and show the driver's info
                                    driverName.setText(driverUser);

                                    //Create a ParseFile with a callback to get the picture
                                    ParseFile driverPic = (ParseFile) parseObject.getParseFile("PhotoDriver");
                                    driverPic.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] bytes, ParseException e) {
                                            //Decode the image through Bitmap from parse so we can show it
                                            Bitmap btm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            dPic.setImageBitmap(btm);

                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });

            ParseQuery<ParseObject> trackAdress = new ParseQuery<ParseObject>("Order");
            trackAdress.whereEqualTo("username", username);
            trackAdress.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {

                        for (ParseObject result : list) {

                            pDate = result.getString("Pickupdate");
                            dDate = result.getString("Dropoffdate");

                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                            Date one, two;
                            try {
                                one = sdf.parse(pDate);
                                two = sdf.parse(dDate);

                                pHour = result.getString("PickHour");
                                pAddress = result.getString("PickupAddress");
                                pCity = result.getString("PickupCityState");
                                pickDate.setText(pDate);
                                pickHour.setText(pHour);
                                pickAddress.setText(pAddress);
                                pickCity.setText(pCity);
                                pickDay.setText(android.text.format.DateFormat.format("EEEE", one));

                                dHour = result.getString("Deliveryhour");
                                dAddress = result.getString("DeliveryAddress");
                                dCity = result.getString("DeliveryCityState");
                                delivDate.setText(dDate);
                                delivHour.setText(dHour);
                                delivAddress.setText(dAddress);
                                deliCity.setText(dCity);
                                delivDay.setText(android.text.format.DateFormat.format("EEEE", two));


                            } catch (java.text.ParseException e1) {
                                e1.printStackTrace();
                            }

                        }

                    } else {

                    }

                }
            });

            btnFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnFeedback.getText().equals("Cancel Pickup")){
                        //Make queries for the Order and TrackingStatus classes in Parse to delete the rows of the current user
                        final ParseQuery<ParseObject> cancelOrder = new ParseQuery<ParseObject>("Order");
                        final ParseQuery<ParseObject> cancelTracking = new ParseQuery<ParseObject>("TrackingStatus");
                        cancelTracking.whereEqualTo("Client", username);
                        cancelTracking.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                try {
                                    //Delete the row in TrackingStatus
                                    parseObject.delete();

                                    //Make the query to delete the row in Order
                                    cancelOrder.whereEqualTo("username", username);
                                    cancelOrder.getFirstInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject parseObject, ParseException e) {
                                            try {
                                                //Delete the row
                                                parseObject.delete();
                                                //Show the message to the user
                                                Toast.makeText(getActivity(), "Your order has been cancelled", Toast.LENGTH_SHORT).show();
                                                //Go to Dashboard Screen
                                                startActivity(new Intent(getActivity(), Dashboard.class));
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    }
                    //When the user clicks the Feedback button, it will take him to the Feedback screen
                    if (btnFeedback.getText().equals("Feedback")){
                        startActivity(new Intent(getActivity(), Feedback.class));

                        btnFeedback.setText("Cancel Pickup");
                    }
                }
            });


            //Run a background process polling the database for changes in the Order and TrackingStatus classes from Parse
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //This while loop will run the queries inside of it as long as the expression is "true"
                    while (true){

                        //Make a query in the Order class from parse to check if the "Verify" column is true and then executes the initPickupBar method (see below), that
                        //allows the first progress bar to start and change the button to Feedback instead of cancel pickup
                        ParseQuery<ParseObject> order_status = new ParseQuery<ParseObject>("Order");
                        order_status.whereEqualTo("username", username).whereEqualTo("Verify", true);
                        order_status.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    initPickupBar();
                                    btnFeedback.setText("Feedback");
                                    btnFeedback.setBackgroundColor(Color.rgb(127, 230, 139));
                                    btnFeedback.setTextColor(Color.rgb(255, 255, 255));
                                } else {
                                    btnFeedback.setText("Cancel Pickup");
                                    btnFeedback.setBackgroundResource(R.drawable.address_button);
                                    btnFeedback.setTextColor(Color.rgb(175,175,175));
                                }
                            }
                        });

                        //Make a query in the TrackingStatus class from parse to check if the "LaundryDrop" is true to execute the method initDryBar (see below) to start
                        //the second progress bar.
                        ParseQuery < ParseObject > laundry_drop = new ParseQuery < ParseObject > ("TrackingStatus");
                        laundry_drop.whereEqualTo("Client", username).whereEqualTo("LaundryDrop", true);
                        laundry_drop.getFirstInBackground(new GetCallback < ParseObject > () {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    //We save the value of the DeliveryVerify as a boolean, since the third progress bar can't start if the second bar's not done.
                                    boolean hasDelivered = parseObject.getBoolean("DeliveryVerify");

                                    //Use a conditional. If hasDelivered is false in the database, we call the method initDryBar, that starts the second progress bar
                                    if (!hasDelivered) {
                                        initDryBar();
                                    }
                                    //If the DeliveryVerify is true, show the the second progress bar at 100% with the initCompletedDryBar method (see below)
                                    else {
                                        initCompletedDryBar();
                                    }

                                }
                            }
                        });

                        //Make the query in the TrackingStatus class from parse to check if the "DeliveryVerify" is true and if the order has been paid
                        // to execute the method initDeliveryBar to start the third progress bar

                        ParseQuery<ParseObject> delivery_verify = new ParseQuery<ParseObject>("TrackingStatus");
                        delivery_verify.whereEqualTo("Client", username).whereEqualTo("DeliveryVerify", true);
                        delivery_verify.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null){
                                    ParseQuery<ParseObject> paid_verify = new ParseQuery<ParseObject>("Order");
                                    paid_verify.whereEqualTo("username", username).whereEqualTo("Paid", true);
                                    paid_verify.getFirstInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject parseObject, ParseException e) {
                                            if (e == null) {
                                                initDeliveryBar();
                                                //When the third progress bar is completed, take the user to the Order screen
                                                if (check3.isShown()){

                                                    infoLayout.setVisibility(View.GONE);
                                                    progressLayout.setVisibility(View.GONE);
                                                    requestLayout.setVisibility(View.VISIBLE);
                                                }
                                                else
                                                {
                                                    infoLayout.setVisibility(View.VISIBLE);
                                                    progressLayout.setVisibility(View.VISIBLE);
                                                    requestLayout.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });

                        try {
                            //Check in the database every 2 minutes to see if the Order and TrackingStatus statuses are True
                            Thread.sleep(1 * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            return rootView;
        }


        private void initPickupBar(){
            //Make a query in the TrackingStatus class from parse to get the starting time of the progress bar with getCreatedAt and the pickup time the driver
            // is going to specify in his app and save it as an int to calculate the total time

            ParseQuery<ParseObject> pick_time = new ParseQuery<ParseObject>("TrackingStatus");
            pick_time.orderByDescending("createdAt").whereEqualTo("Client", username);
            pick_time.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {

                        int END = parseObject.getInt("TimePick"); //Time the driver specifies
                        Date startTime = parseObject.getCreatedAt(); //Starting time
                        Date currTime = new Date(); //Current time so we can calculate the progress of the bar
                        Date endTime = new Date(startTime.getTime() + END * 60 * 1000); //The end time

                        int deltaTime = (int) (endTime.getTime() - startTime.getTime()); //Total time
                        int elapsedTime = (int) (currTime.getTime() - startTime.getTime()); //time that has passed since the bar started
                        int remainingTime = (int) (endTime.getTime() - currTime.getTime()); //remaining time to finish the progress

                        Log.i("elpased: ", elapsedTime+"");
                        Log.i("delta: ", deltaTime+"");
                        Log.i("remaining: ", remainingTime+"");
                        Log.i("start: ", startTime.toString());
                        Log.i("currenttime: ", currTime.toString());
                        Log.i("endtime: ", endTime.toString());
                        Log.i("END: ", END+"");
                        Log.i("start.getTime: ", startTime.getTime()+"");
                        Log.i("curr.getTime: ", currTime.getTime()+"");
                        Log.i("end.getTime: ", endTime.getTime()+"");

                        float currPercentage = (elapsedTime * 100 / deltaTime);
                        progressCircle.setAnimDuration(50);
                        progressCircle.startAnimation();
                        Log.i("currPercentage: ", currPercentage + "");

                        if (elapsedTime >= deltaTime){

                            if (val2 == 0.0f && val3 == 0.0f){

                                val = 1.0f;
                                progressCircle.setProgress(val);
                                check1.setImageDrawable(getResources().getDrawable(R.drawable.tracking_green_circle));
                                icon.setImageDrawable(getResources().getDrawable(R.drawable.tracking_car));
                                text.setText(" We're here!");
                                progressCircle.startAnimation();
                            }
                            else {
                                val = 1.0f;
                                progressCircle.setProgress(val);
                                progressCircle.startAnimation();
                                check1.setImageDrawable(getResources().getDrawable(R.drawable.tracking_green_circle));
                            }

                        }
                        else {

                            val = currPercentage / 100f;
                            progressCircle.setProgress(val);
                            final float interval = 1.0f / (deltaTime / 1000.0f);
                            Log.d("interval: ", interval+"");
                            delay = 1000;

                            if (!isPickupBarActive){

                                final Handler h = new Handler();
                                h.postDelayed(new Runnable() {
                                    public void run() {
                                        if (isAdded()) {
                                            getResources().getString(R.string.app_name);

                                            val += interval;
                                            icon.setImageDrawable(getResources().getDrawable(R.drawable.tracking_car));

                                            text.setText("   On our \n     way!");
                                            progressCircle.setProgress(val);
                                            progressCircle.startAnimation();
                                        }
                                        h.postDelayed(this, delay);
                                    }
                                }, delay);

                                isPickupBarActive = true;
                            }

                        }

                    }

                }
            });
        }

        private void initDryBar(){

        ParseQuery<ParseObject> get_hour = new ParseQuery<ParseObject>("Order");
        get_hour.whereEqualTo("username", username).whereEqualTo("Verify", true);
        get_hour.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if (e == null){

                    String hourRange = parseObject.getString("PickHour");
                    String pickDate = parseObject.getString("Pickupdate");

                    String hour = hourRange.split("-")[0] + " AM";

                    String fullDate = pickDate + " " +hour;
                    Log.d("Hour: ", hour+"");

                    SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
                    try {

                        final Date one = dt.parse(fullDate);

                        ParseQuery<ParseObject> laundry_drop = new ParseQuery<ParseObject>("TrackingStatus");
                        laundry_drop.orderByDescending("createdAt").whereEqualTo("Client", username);
                        laundry_drop.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                Date startTime = parseObject.getUpdatedAt(); //Start time

                                long difference = startTime.getTime() - one.getTime();

                                int min = (int) difference /(1000*60);

                                Log.d("startime.getTime2: ", startTime.getTime()+"");
                                Log.d("hour.getTime2: ", one.getTime()+"");
                                Log.d("mins2: ", min+"");
                                Log.d("hour.toString2: ", one.toString());

                                int END = 1440 - min; //24hours it takes the laundry to do the washing and dry cleaning services
                                Date currTime = new Date(); //Current time to calculate the progress
                                Date endTime = new Date(startTime.getTime() + END * 60 * 1000); //End time

                                final int deltaTime = (int) (endTime.getTime() - startTime.getTime()); //Total time
                                final int elapsedTime = (int) (currTime.getTime() - startTime.getTime()); //Time that has passed since the bar started
                                final int remainingTime = (int) (endTime.getTime() - currTime.getTime()); //remaining time

                                Log.i("elpased2: ", elapsedTime+"");
                                Log.i("delta2: ", deltaTime+"");
                                Log.i("remaining2: ", remainingTime+"");
                                Log.i("start2: ", startTime.toString());
                                Log.i("currenttime2: ", currTime.toString());
                                Log.i("endtime2: ", endTime.toString());
                                Log.i("END2: ", END+"");

                                float currPercentage = (elapsedTime * 100 / deltaTime);
                                progressCircle2.setAnimDuration(50);
                                progressCircle2.startAnimation();
                                Log.i("currPercentage2: ", currPercentage + "");

                                if (elapsedTime >= deltaTime){

                                    val2 = 1.0f;
                                    progressCircle2.setProgress(val2);
                                    check2.setImageDrawable(getResources().getDrawable(R.drawable.tracking_green_circle));
                                    icon.setImageDrawable(getResources().getDrawable(R.drawable.tracking_process));
                                    text.setText("     Washing/ \n  Dry Cleaning");
                                    progressCircle2.startAnimation();

                                }
                                else {

                                    val2 = currPercentage / 100f;
                                    progressCircle2.setProgress(val2);
                                    final float interval = 1.0f / (deltaTime / 1000.0f);
                                    Log.d("interval2: ", interval+"");
                                    delay = 1000;

                                    if (!isDryBarActive){

                                        final Handler h = new Handler();
                                        h.postDelayed(new Runnable(){
                                            public void run(){
                                                if (isAdded()){
                                                    getResources().getString(R.string.app_name);

                                                    val2 += interval;
                                                    icon.setImageDrawable(getResources().getDrawable(R.drawable.tracking_process));

                                                    text.setText("     Washing/ \n  Dry Cleaning");
                                                    progressCircle2.setProgress(val2);
                                                    progressCircle2.startAnimation();
                                                }
                                                h.postDelayed(this, delay);
                                            }
                                        }, delay);

                                        isDryBarActive = true;
                                    }

                                }

                            }
                        });

                    } catch (java.text.ParseException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });

    }

    private void initCompletedDryBar() {
        //Method that shows the second progress bar completed at 100% so the third bar can start when it's called
        val2 = 1.0f;
        progressCircle2.setProgress(val2);
        text.setText("     Washing/ \n  Dry Cleaning");
        icon.setImageDrawable(getResources().getDrawable(R.drawable.tracking_process));
        check2.setImageDrawable(getResources().getDrawable(R.drawable.tracking_green_circle));
        progressCircle2.startAnimation();

    }

    private void initDeliveryBar(){
        //Make a query in the TrackingStatus class from parse to get the starting time of the progress bar with getUpdated and the delivery time the driver
        // is going to specify in his app and save it as an int to calculate the total time
        ParseQuery<ParseObject> deli_time = new ParseQuery<ParseObject>("TrackingStatus");
        deli_time.orderByAscending("createdAt").whereEqualTo("Client", username);
        deli_time.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {

                    int END = parseObject.getInt("DeliveryTime"); //Time the driver specifies
                    Date startTime = parseObject.getUpdatedAt(); //Start time
                    Date currTime = new Date(); //Current time
                    Date endTime = new Date(startTime.getTime() + END * 60 * 1000); //End time

                    final int deltaTime = (int) (endTime.getTime() - startTime.getTime()); //Total time
                    final int elapsedTime = (int) (currTime.getTime() - startTime.getTime()); //Time that has passed since the progress bar started
                    final int remainingTime = (int) (endTime.getTime() - currTime.getTime()); //remaining time

                    Log.i("elpased3: ", elapsedTime+"");
                    Log.i("delta3: ", deltaTime+"");
                    Log.i("remaining3: ", remainingTime+"");
                    Log.i("start3: ", startTime.toString());
                    Log.i("currenttime3: ", currTime.toString());
                    Log.i("endtime3: ", endTime.toString());
                    Log.i("END3: ", END+"");
                    Log.i("currTime.getTime: ", currTime.getTime()+"");
                    Log.i("startTime.getTime: ", startTime.getTime()+"");

                    float currPercentage = (elapsedTime * 100 / deltaTime);
                    progressCircle3.setAnimDuration(50);
                    progressCircle3.startAnimation();
                    Log.i("currPercentage3: ", currPercentage + "");

                    if (elapsedTime >= deltaTime){

                            val3 = 1.0f;
                            progressCircle3.setProgress(val3);
                            check3.setImageDrawable(getResources().getDrawable(R.drawable.tracking_green_circle));
                            icon.setImageDrawable(getResources().getDrawable(R.drawable.tracking_home));
                            text.setText(" Done!");
                            progressCircle3.startAnimation();

                    }
                    else {

                        val3 = currPercentage / 100f;
                        progressCircle3.setProgress(val3);
                        final float interval = 1.0f / (deltaTime / 1000.0f);
                        Log.d("interval3: ", interval+"");
                        delay = 1000;

                        if (!isDeliBarActive){

                            final Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    if (isAdded()) {
                                        getResources().getString(R.string.app_name);

                                        val3 += interval;
                                        icon.setImageDrawable(getResources().getDrawable(R.drawable.tracking_home));

                                        text.setText("     Ready for \n  your clothes?");
                                        progressCircle3.setProgress(val3);
                                        progressCircle3.startAnimation();
                                    }
                                    h.postDelayed(this, delay);
                                }
                            }, delay);

                            isDeliBarActive = true;
                        }

                    }
                }
            }
        });
    }



    }


    public static class PlaceholderFragment2 extends Fragment {

        TextView pendingTitle;
        ListView todoList;
        Typeface segoe;
        List<PlaceholderFragment2.ListViewTodo> items;
        String pickdate, delidate;
        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getUsername();

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment2 newInstance(int sectionNumber) {
            PlaceholderFragment2 fragment = new PlaceholderFragment2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment2() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.to_do_screen, container, false);

            segoe = Typeface.createFromAsset(getActivity().getAssets(),"fonts/segoeui.ttf");

            pendingTitle = (TextView) rootView.findViewById(R.id.titlePending);

            todoList = (ListView) rootView.findViewById(R.id.list_todo);

            pendingTitle.setTypeface(segoe);

            items = new ArrayList<MyOrders.PlaceholderFragment2.ListViewTodo>();

            ParseQuery<ParseObject> order_number = new ParseQuery<ParseObject>("Order");
            order_number.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Todo", true);
            order_number.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null){

                        for (final ParseObject result : list){

                            final String pickhour = result.getString("PickHour");
                            pickdate = result.getString("Pickupdate");
                            delidate = result.getString("Dropoffdate");

                            try {

                                final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                final SimpleDateFormat dt = new SimpleDateFormat("hh:mm aa");
                                final Date pickup, delivery, hour;
                                pickup = dateFormat.parse(pickdate);
                                delivery = dateFormat.parse(delidate);
                                hour = dt.parse(pickhour);
                                items.add(new ListViewTodo(){
                                    {
                                        toNumber = result.getLong("OrderNumber");

                                        tpaddress = result.getString("PickupAddress");
                                        tpcity = result.getString("PickupCityState");
                                        tpday = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(pickup);
                                        tpdate = pickdate;
                                        tphour = result.getString("PickHour");

                                        tdaddress = result.getString("DeliveryAddress");
                                        tdcity = result.getString("DeliveryCityState");
                                        tdday = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(delivery);
                                        tddate = delidate;
                                        tdhour = result.getString("Deliveryhour");

                                        pickHour = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH).format(hour);
                                        Log.e("Stuff: ", pickHour+"");
                                    }
                                });

                            } catch (java.text.ParseException e1) {
                                e1.printStackTrace();
                            }

                        }

                        AdapterTodo adapter = new AdapterTodo(getActivity(), items);
                        todoList.setAdapter(adapter);
                    }
                }
            });

            return rootView;
        }

        public class ListViewTodo {
            public long toNumber;
            public String tpaddress;
            public String tpcity;
            public String tpday;
            public String tpdate;
            public String tphour;

            public String tdaddress;
            public String tdcity;
            public String tdday;
            public String tddate;
            public String tdhour;
            public String pickHour;
        }

    }


    public static class PlaceholderFragment3 extends Fragment {

        TextView orderTitle;
        ListView doneList;
        Typeface segoe;
        List<PlaceholderFragment3.ListViewDone> things;
        String pickDate, deliDate;
        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getUsername();
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment3 newInstance(int sectionNumber) {
            PlaceholderFragment3 fragment = new PlaceholderFragment3();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment3() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.done_screen, container, false);

            segoe = Typeface.createFromAsset(getActivity().getAssets(),"fonts/segoeui.ttf");

            orderTitle = (TextView) rootView.findViewById(R.id.titleHistory);

            doneList = (ListView) rootView.findViewById(R.id.list_done);

            orderTitle.setTypeface(segoe);

            things = new ArrayList<MyOrders.PlaceholderFragment3.ListViewDone>();

            ParseQuery<ParseObject> order_number = new ParseQuery<ParseObject>("Historial");
            order_number.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).orderByDescending("createdAt");
            order_number.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    if (e == null) {

                        for (final ParseObject result : list) {

                            pickDate = result.getString("Pickupdate");
                            deliDate = result.getString("Dropoffdate");

                            try {

                                final SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                                final Date one, two;
                                one = date.parse(pickDate);
                                two = date.parse(deliDate);
                                things.add(new ListViewDone() {
                                    {
                                        oNumber = result.getLong("OrderNumber");

                                        paddress = result.getString("PickupAddress");
                                        pcity = result.getString("PickupCityState");
                                        pday = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(one);
                                        pdate = pickDate;
                                        phour = result.getString("PickHour");

                                        daddress = result.getString("DeliveryAddress");
                                        dcity = result.getString("DeliveryCityState");
                                        dday = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(two);
                                        ddate = deliDate;
                                        dhour = result.getString("Deliveryhour");
                                    }
                                });

                            } catch (java.text.ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        AdapterDone adapter = new AdapterDone(getActivity(), things);
                        doneList.setAdapter(adapter);

                    }
                }
            });
            return rootView;
        }

        public class ListViewDone {
            public long oNumber;
            public String paddress;
            public String pcity;
            public String pday;
            public String pdate;
            public String phour;
            public String daddress;
            public String dcity;
            public String dday;
            public String ddate;
            public String dhour;
        }
    }

}

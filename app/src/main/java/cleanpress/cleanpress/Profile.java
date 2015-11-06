package cleanpress.cleanpress;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.model.Card;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Profile extends ActionBarActivity {


    /***********************************************************************************************
     *                          Animators Variables
     * *********************************************************************************************
     * */

    LinearLayout L1,L2,L3,L4;
    Animation fadeInLeft,fadeOutLeft,fadeInRight,fadeOutRight;
    Button btn1,btn2,btn3;
    int pos;
    Typeface segoe,segoeb;
    String sL1 = "Your Information",sL2 = "Credit Card Info",sL3 = "Addresses Directory",sL4 = "Preferences";


    /***********************************************************************************************
     *                          Personal Information Variables
     * *********************************************************************************************
     * */

    TextView TitlePersonal;
    EditText Name,Lastname,Email,Phone;
    Button editPers,savePers,cancelPers;
    String sName,sLast;
    Long lPhone;
    LinearLayout buttonsPers;


    /***********************************************************************************************
     *                              Credit Card Information Variables
     * *********************************************************************************************
     * */

    EditText CCnumber,CCcvc,CCexpMonth,CCexpYear,CCzip;
    TextView TitleCC;
    Button btnSaveCC,btnCancelCC,btnEditCC;
    Boolean vCCnumber,vCCcvc,vCCzip,vCCexpMonth,vCCexpYear;
    Integer iCCexpMonth,iCCexpYear;
    String sCCnumber,sCCcvc,sCCexpMonth,sCCexpYear,sCCzip;
    LinearLayout buttonsCC;
    Card card;

    /***********************************************************************************************
     *                              Addresses Information Variables
     *
     *                                         HOME
     * *********************************************************************************************
     * */

    TextView TitleAddress,homeTitle,homeRef;
    ImageView arrowhome;
    EditText homeAddress,homeCity,homeZip;
    Button editHome,cancelHome,saveHome;
    LinearLayout buttonsHome,homeFields;
    String sHomeAd,sHomeCity,sHomeZip;


    /***********************************************************************************************
     *                              Addresses Information Variables
     *
     *                                         Work
     * *********************************************************************************************
     * */

    TextView workTitle,workRef;
    ImageView arrowWork;
    EditText workAddress,workCity,workZip;
    Button editWork,cancelWork,saveWork;
    LinearLayout buttonsWork,workFields;
    String sWorkAd,sWorkCity,sWorkZip;



    /***********************************************************************************************
     *                              Addresses Information Variables
     *
     *                                         Other
     * *********************************************************************************************
     * */

    TextView otherTitle,otherRef;
    ImageView arrowOther;
    EditText otherAddress,otherCity,otherZip;
    Button editOther,cancelOther,saveOther;
    LinearLayout buttonsOther,otherFields;
    String sOtherAd,sOtherCity,sOtherZip;


    /***********************************************************************************************
     *                             Personal Preferences Information Variables
     * *********************************************************************************************
     * */

    TextView titlePrefs,titleDress,titleDetergent,titleStarch;
    Spinner spinnerDress,spinnerDetergent,spinnerStarch;
    Button editPref,cancelPref,savePref;
    LinearLayout buttonsPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        segoeb = Typeface.createFromAsset(getAssets(),"fonts/seguisb.ttf");

        addMenu();

        /***********************************************************************************************
         *                                  Screen Animators
         ***********************************************************************************************
         * */

        btn1 = (Button) findViewById(R.id.boton1);
        btn2 = (Button) findViewById(R.id.boton2);
        btn3 = (Button) findViewById(R.id.boton3);

        btn1.setText(sL2);
        btn2.setText(sL3);
        btn3.setText(sL4);

        btn1.setTypeface(segoe);
        btn2.setTypeface(segoe);
        btn3.setTypeface(segoe);

        L1 = (LinearLayout) findViewById(R.id.Linear1);
        L2 = (LinearLayout) findViewById(R.id.Linear2);
        L3 = (LinearLayout) findViewById(R.id.Linear3);
        L4 = (LinearLayout) findViewById(R.id.Linear4);


        fadeInLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_left);
        fadeOutLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out_left);
        fadeInRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_right);
        fadeOutRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out_right);


        /*********************************************************************************************
         *                              Personal Information Layout
         *********************************************************************************************
         * */

        TitlePersonal = (TextView) findViewById(R.id.personal_info);
        Name = (EditText) findViewById(R.id.edt_name);
        Lastname = (EditText) findViewById(R.id.edt_lastname);
        Email = (EditText) findViewById(R.id.edt_email);
        Phone = (EditText) findViewById(R.id.edt_phone);
        editPers = (Button) findViewById(R.id.edit_personal_info);
        cancelPers = (Button) findViewById(R.id.cancelInfoEdit);
        savePers = (Button) findViewById(R.id.SaveInfo);
        buttonsPers = (LinearLayout) findViewById(R.id.editButtonsPers);

        TitlePersonal.setTypeface(segoe);
        Lastname.setTypeface(segoe);
        Email.setTypeface(segoe);
        Phone.setTypeface(segoe);
        editPers.setTypeface(segoe);
        cancelPers.setTypeface(segoe);
        savePers.setTypeface(segoe);

        disableEdit(Name);
        disableEdit(Lastname);
        disableEdit(Email);
        disableEdit(Phone);

        ParseQuery<ParseObject> PersonalInfo = new ParseQuery<ParseObject>("User");
        PersonalInfo.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        PersonalInfo.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Name.setText(parseObject.getString("FirstName"));
                    Lastname.setText(parseObject.getString("LastName"));
                    Email.setText(ParseUser.getCurrentUser().getEmail());
                    Phone.setText(parseObject.getLong("Phone") + "");
                }
            }
        });

        /***********************************************************************************************
         *                                  Credit Card Info Layout
         ***********************************************************************************************
         *  */

        TitleCC = (TextView) findViewById(R.id.title);
        CCnumber = (EditText) findViewById(R.id.cc_number);
        CCcvc = (EditText) findViewById(R.id.cc_cvc);
        CCexpMonth = (EditText) findViewById(R.id.cc_mm);
        CCexpYear = (EditText) findViewById(R.id.cc_yy);
        CCzip = (EditText) findViewById(R.id.cc_zip);
        btnCancelCC = (Button) findViewById(R.id.cancelCCEdit);
        btnEditCC = (Button) findViewById(R.id.edit_CC_info);
        btnSaveCC = (Button) findViewById(R.id.SaveCC);
        buttonsCC = (LinearLayout) findViewById(R.id.buttonsEditCC);

        disableEdit(CCnumber);
        disableEdit(CCcvc);
        disableEdit(CCexpMonth);
        disableEdit(CCexpYear);
        disableEdit(CCzip);

        final String hideNumber = "**** **** **** ";

        ParseQuery<ParseObject> CCInfo = new ParseQuery<ParseObject>("User");
        CCInfo.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        CCInfo.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {

                    String realNumber = Long.toString(parseObject.getLong("CCnumber"));
                    String toShow = realNumber.substring(Math.max(realNumber.length() - 4, 0));


                    CCnumber.setText(hideNumber + toShow);
                    CCcvc.setText(Integer.toString(parseObject.getInt("cvc")));
                    CCexpMonth.setText(Integer.toString(parseObject.getInt("expMonth")));
                    CCexpYear.setText(Integer.toString(parseObject.getInt("expYear")));
                    CCzip.setText(Integer.toString(parseObject.getInt("CCzipcode")));
                }
            }
        });

        TitleCC.setTypeface(segoe);
        CCnumber.setTypeface(segoe);
        CCcvc.setTypeface(segoe);
        CCzip.setTypeface(segoe);
        CCexpMonth.setTypeface(segoe);
        CCexpYear.setTypeface(segoe);
        btnEditCC.setTypeface(segoe);
        btnSaveCC.setTypeface(segoe);
        btnCancelCC.setTypeface(segoe);


        /***********************************************************************************************
         *                                        Addresses Info Layout
         *                                                (HOME)
         ***********************************************************************************************
         *  */

        TitleAddress = (TextView) findViewById(R.id.TitleAddress);
        homeTitle = (TextView) findViewById(R.id.homeBtn);
        homeRef = (TextView) findViewById(R.id.homeViewAd);
        arrowhome = (ImageView) findViewById(R.id.homeArrow);
        homeFields = (LinearLayout) findViewById(R.id.homeFields);

        homeAddress = (EditText) findViewById(R.id.homeAddress);
        homeCity = (EditText) findViewById(R.id.homeCity);
        homeZip = (EditText) findViewById(R.id.homeZip);

        disableEdit(homeAddress);
        disableEdit(homeCity);
        disableEdit(homeZip);

        editHome = (Button) findViewById(R.id.edit_home_address);
        cancelHome = (Button) findViewById(R.id.cancelHomeAddressEdit);
        saveHome = (Button) findViewById(R.id.SaveHomeAddress);
        buttonsHome = (LinearLayout) findViewById(R.id.btnsHomeAddress);

        TitleAddress.setTypeface(segoe);
        homeTitle.setTypeface(segoe);
        homeRef.setTypeface(segoe);
        homeAddress.setTypeface(segoe);
        homeZip.setTypeface(segoe);
        homeCity.setTypeface(segoe);
        editHome.setTypeface(segoe);
        cancelHome.setTypeface(segoe);
        saveHome.setTypeface(segoe);


        ParseQuery<ParseObject> findHome = new ParseQuery<ParseObject>("Addresses");
        findHome.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Home");
        findHome.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    homeRef.setText(sAddress + ", " + sCity + ", " + sZip);
                    homeAddress.setText(sAddress);
                    homeCity.setText(sCity);
                    homeZip.setText(sZip);
                }
            }
        });


        /***********************************************************************************************
         *                                        Addresses Info Layout
         *                                                (WORK)
         ***********************************************************************************************
         *  */

        workTitle = (TextView) findViewById(R.id.workBtn);
        workRef = (TextView) findViewById(R.id.workViewAd);
        arrowWork = (ImageView) findViewById(R.id.workArrow);
        workFields = (LinearLayout) findViewById(R.id.workFields);

        workAddress = (EditText) findViewById(R.id.workAddress);
        workCity = (EditText) findViewById(R.id.workCity);
        workZip = (EditText) findViewById(R.id.workZip);

        disableEdit(workAddress);
        disableEdit(workCity);
        disableEdit(workZip);

        editWork = (Button) findViewById(R.id.edit_work_address);
        cancelWork = (Button) findViewById(R.id.cancelWorkAddressEdit);
        saveWork = (Button) findViewById(R.id.SaveWorkAddress);
        buttonsWork = (LinearLayout) findViewById(R.id.btnsWorkAddress);

        workTitle.setTypeface(segoe);
        workRef.setTypeface(segoe);
        workAddress.setTypeface(segoe);
        workZip.setTypeface(segoe);
        workCity.setTypeface(segoe);
        editWork.setTypeface(segoe);
        cancelWork.setTypeface(segoe);
        saveWork.setTypeface(segoe);


        ParseQuery<ParseObject> findWork = new ParseQuery<ParseObject>("Addresses");
        findWork.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Work");
        findWork.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    workRef.setText(sAddress + ", " + sCity + ", " + sZip);
                    workAddress.setText(sAddress);
                    workCity.setText(sCity);
                    workZip.setText(sZip);
                }
            }
        });

        /***********************************************************************************************
         *                                        Addresses Info Layout
         *                                                (OTHER)
         ***********************************************************************************************
         *  */

        otherTitle = (TextView) findViewById(R.id.otherBtn);
        otherRef = (TextView) findViewById(R.id.otherViewAd);
        arrowOther = (ImageView) findViewById(R.id.otherArrow);
        otherFields = (LinearLayout) findViewById(R.id.otherFields);

        otherAddress = (EditText) findViewById(R.id.otherAddress);
        otherCity = (EditText) findViewById(R.id.otherCity);
        otherZip = (EditText) findViewById(R.id.otherZip);

        disableEdit(otherAddress);
        disableEdit(otherCity);
        disableEdit(otherZip);

        editOther = (Button) findViewById(R.id.edit_other_address);
        cancelOther = (Button) findViewById(R.id.cancelOtherAddressEdit);
        saveOther = (Button) findViewById(R.id.SaveOtherAddress);
        buttonsOther = (LinearLayout) findViewById(R.id.btnsOtherAddress);

        otherTitle.setTypeface(segoe);
        otherRef.setTypeface(segoe);
        otherAddress.setTypeface(segoe);
        otherZip.setTypeface(segoe);
        otherCity.setTypeface(segoe);
        editOther.setTypeface(segoe);
        cancelOther.setTypeface(segoe);
        saveOther.setTypeface(segoe);


        ParseQuery<ParseObject> findOther = new ParseQuery<ParseObject>("Addresses");
        findOther.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Other");
        findOther.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    otherRef.setText(sAddress + ", " + sCity + ", " + sZip);
                    otherAddress.setText(sAddress);
                    otherCity.setText(sCity);
                    otherZip.setText(sZip);
                }
            }
        });

        /***********************************************************************************************
         *                                        Personal Preferences Layout
         ***********************************************************************************************
         *  */

        titlePrefs = (TextView) findViewById(R.id.titlePref);
        titleDetergent = (TextView) findViewById(R.id.titleDeter);
        titleDress = (TextView) findViewById(R.id.titleDress);
        titleStarch = (TextView) findViewById(R.id.titleStarch);
        spinnerDetergent = (Spinner) findViewById(R.id.spinnerDeter);
        spinnerDress = (Spinner) findViewById(R.id.spinnerDress);
        spinnerStarch = (Spinner) findViewById(R.id.spinnerStarch);
        editPref = (Button) findViewById(R.id.edit_preferences);
        cancelPref = (Button) findViewById(R.id.cancelPrefsEdit);
        savePref = (Button) findViewById(R.id.SavePrefs);
        buttonsPref = (LinearLayout) findViewById(R.id.btnsPref);

        titlePrefs.setTypeface(segoe);
        titleDetergent.setTypeface(segoe);
        titleDress.setTypeface(segoe);
        titleStarch.setTypeface(segoe);
        editPref.setTypeface(segoe);
        cancelPref.setTypeface(segoe);
        savePref.setTypeface(segoe);

        spinnerDetergent.setEnabled(false);
        spinnerDress.setEnabled(false);
        spinnerStarch.setEnabled(false);

        final String[] detergents = {"Select","Scented","Unscented"};
        MySpinnerAdapter adapter = new MySpinnerAdapter(getApplicationContext(),R.layout.titles_spinner,detergents);
        spinnerDetergent.setAdapter(adapter);

        final String[] dress_shirt = {"Select","Launder","Dry Cleaning","Cleanpress Choose"};
        MySpinnerAdapter adapter1 = new MySpinnerAdapter(getApplicationContext(),R.layout.titles_spinner,dress_shirt);
        spinnerDress.setAdapter(adapter1);

        final String[] starch = {"Select","Light","Heavy"};
        MySpinnerAdapter adapter2 = new MySpinnerAdapter(getApplicationContext(),R.layout.titles_spinner,starch);
        spinnerStarch.setAdapter(adapter2);



        ParseQuery<ParseObject> finDeter = new ParseQuery<ParseObject>("User");
        finDeter.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        finDeter.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    String found = parseObject.getString("PrefDetergent");
                    switch (found){
                        case "Scented":
                            spinnerDetergent.setSelection(1);
                            break;
                        case "Unscented":
                            spinnerDetergent.setSelection(2);
                            break;

                    }
                }
            }
        });

        ParseQuery<ParseObject> findStarch = new ParseQuery<ParseObject>("User");
        findStarch.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        findStarch.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    String found = parseObject.getString("PrefStarch");
                    switch (found){
                        case "Light":
                            spinnerStarch.setSelection(1);
                            break;
                        case "Heavy":
                            spinnerStarch.setSelection(2);
                            break;

                    }
                }
            }
        });

        ParseQuery<ParseObject> finDress = new ParseQuery<ParseObject>("User");
        finDress.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        finDress.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    String found = parseObject.getString("PrefDressShirt");

                    switch (found){
                        case "Launder":
                            spinnerDress.setSelection(1);
                            break;
                        case "Dry Cleaning":
                            spinnerDress.setSelection(2);
                            break;
                        case "Cleanpress Choice":
                            spinnerDress.setSelection(3);
                    }

                }
            }
        });

    }



    /**
     *                  METHODS TO ANIMATE PROFILE SCREEN
     * */

    public void Button1 (View view){

        if (btn1.getText().toString().equals(sL2) && L1.isShown()){
            pos = 1;
            pickAnimations();

        }
        if (btn1.getText().toString().equals(sL1) && L2.isShown()){
            pos = 2;
            pickAnimations();
        }
        if (btn1.getText().toString().equals(sL1) && L3.isShown()){
            pos = 3;
            pickAnimations();
        }
        if (btn1.getText().toString().equals(sL1) && L4.isShown()){
            pos = 4;
            pickAnimations();
        }
    }

    public void Button2 (View view) {
        if (btn2.getText().toString().equals(sL3) && L1.isShown()){
            pos = 5;
            pickAnimations();
        }
        if (btn2.getText().toString().equals(sL3) && L2.isShown()){
            pos = 6;
            pickAnimations();
        }
        if (btn2.getText().toString().equals(sL2) && L3.isShown()){
            pos = 7;
            pickAnimations();
        }
        if (btn2.getText().toString().equals(sL2) && L4.isShown()){
            pos = 8;
            pickAnimations();
        }
    }

    public void Button3 (View view){
        if (btn3.getText().toString().equals(sL4) && L1.isShown()){
            pos = 9;
            pickAnimations();
        }
        if (btn3.getText().toString().equals(sL4) && L2.isShown()){
            pos = 10;
            pickAnimations();
        }
        if (btn3.getText().toString().equals(sL4) && L3.isShown()){
            pos = 11;
            pickAnimations();
        }
        if (btn3.getText().toString().equals(sL3) && L4.isShown()){
            pos = 12;
            pickAnimations();
        }
    }

    public void pickAnimations (){
        switch(pos){

            case 1:
                L1.startAnimation(fadeOutLeft);
                fadeOutLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L1.setVisibility(View.GONE);
                        L2.setVisibility(View.VISIBLE);
                        L2.startAnimation(fadeInLeft);
                        btn1.setText(sL1);
                        btn2.setText(sL3);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_right,0);


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 2:
                L2.startAnimation(fadeOutRight);
                fadeOutRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L2.setVisibility(View.GONE);
                        L1.setVisibility(View.VISIBLE);
                        L1.startAnimation(fadeInRight);
                        btn1.setText(sL2);
                        btn2.setText(sL3);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 3:
                L3.startAnimation(fadeOutRight);
                fadeOutRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L3.setVisibility(View.GONE);
                        L1.setVisibility(View.VISIBLE);
                        L1.startAnimation(fadeInRight);
                        btn1.setText(sL2);
                        btn2.setText(sL3);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 4:
                L4.startAnimation(fadeOutRight);
                fadeOutRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L4.setVisibility(View.GONE);
                        L1.setVisibility(View.VISIBLE);
                        L1.startAnimation(fadeInRight);
                        btn1.setText(sL2);
                        btn2.setText(sL3);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 5:
                L1.startAnimation(fadeOutLeft);
                fadeOutLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L1.setVisibility(View.GONE);
                        L3.setVisibility(View.VISIBLE);
                        L3.startAnimation(fadeInLeft);
                        btn1.setText(sL1);
                        btn2.setText(sL2);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 6:
                L2.startAnimation(fadeOutLeft);
                fadeOutLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L2.setVisibility(View.GONE);
                        L3.setVisibility(View.VISIBLE);
                        L3.startAnimation(fadeInLeft);
                        btn1.setText(sL1);
                        btn2.setText(sL2);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 7:
                L3.startAnimation(fadeOutRight);
                fadeOutRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L3.setVisibility(View.GONE);
                        L2.setVisibility(View.VISIBLE);
                        L2.startAnimation(fadeInRight);
                        btn1.setText(sL1);
                        btn2.setText(sL3);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 8:
                L4.startAnimation(fadeOutRight);
                fadeOutRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L4.setVisibility(View.GONE);
                        L2.setVisibility(View.VISIBLE);
                        L2.startAnimation(fadeInRight);
                        btn1.setText(sL1);
                        btn2.setText(sL3);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 9:
                L1.startAnimation(fadeOutLeft);
                fadeOutLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L1.setVisibility(View.GONE);
                        L4.setVisibility(View.VISIBLE);
                        L4.startAnimation(fadeInLeft);
                        btn1.setText(sL1);
                        btn2.setText(sL2);
                        btn3.setText(sL3);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 10:
                L2.startAnimation(fadeOutLeft);
                fadeOutLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L2.setVisibility(View.GONE);
                        L4.setVisibility(View.VISIBLE);
                        L4.startAnimation(fadeInLeft);
                        btn1.setText(sL1);
                        btn2.setText(sL2);
                        btn3.setText(sL3);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 11:
                L3.startAnimation(fadeOutLeft);
                fadeOutLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L3.setVisibility(View.GONE);
                        L4.setVisibility(View.VISIBLE);
                        L4.startAnimation(fadeInLeft);
                        btn1.setText(sL1);
                        btn2.setText(sL2);
                        btn3.setText(sL3);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case 12:
                L4.startAnimation(fadeOutRight);
                fadeOutRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        L4.setVisibility(View.GONE);
                        L3.setVisibility(View.VISIBLE);
                        L3.startAnimation(fadeInRight);
                        btn1.setText(sL1);
                        btn2.setText(sL2);
                        btn3.setText(sL4);
                        btn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
                        btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;

        }

    }

    /**
     *                  METHODS TO ENABLE/DISABLE EDIT MODE
     * */

    private void disableEdit (EditText editText) {

        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.rgb(193, 194, 199));
        editText.setCursorVisible(false);
        editText.setEnabled(false);
    }

    private void enableEdit (EditText editText) {

        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.rgb(68, 68, 68));
        editText.setCursorVisible(true);
        editText.setEnabled(true);
        editText.setText("");

    }


    /**
     *                  METHODS FOR PERSONAL INFO
     * */


    public void beAbleToEditPers (View view) {

        editPers.setVisibility(View.GONE);
        buttonsPers.setVisibility(View.VISIBLE);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);

        enableEdit(Name);
        enableEdit(Lastname);
        enableEdit(Phone);

    }

    public void cancelInfoEdit (View view) {
        disableEdit(Name);
        disableEdit(Lastname);
        disableEdit(Phone);

        editPers.setVisibility(View.VISIBLE);
        buttonsPers.setVisibility(View.GONE);

        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);

        ParseQuery<ParseObject> PersonalInfo = new ParseQuery<ParseObject>("User");
        PersonalInfo.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        PersonalInfo.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Name.setText(parseObject.getString("FirstName"));
                    Lastname.setText(parseObject.getString("LastName"));
                    Email.setText(ParseUser.getCurrentUser().getEmail());
                    Phone.setText(parseObject.getLong("Phone") + "");
                }
            }
        });

    }

    public void saveInfo (View view) {

        if (Name.getText().toString().trim().isEmpty()
                || Lastname.getText().toString().trim().isEmpty()
                || Phone.getText().toString().trim().isEmpty()){

        } else {
            sName = Name.getText().toString();
            sLast = Lastname.getText().toString();
            lPhone = Long.valueOf(Phone.getText().toString());

            ParseQuery<ParseObject> saveUser = new ParseQuery<ParseObject>("User");
            saveUser.whereEqualTo("Username",ParseUser.getCurrentUser().getUsername());
            saveUser.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e==null){
                        parseObject.put("FirstName",sName);
                        parseObject.put("LastName",sLast);
                        parseObject.put("Phone",lPhone);
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){
                                    disableEdit(Name);
                                    disableEdit(Lastname);
                                    disableEdit(Phone);

                                    buttonsPers.setVisibility(View.GONE);
                                    editPers.setVisibility(View.VISIBLE);

                                    btn1.setEnabled(true);
                                    btn2.setEnabled(true);
                                    btn3.setEnabled(true);

                                    Toast.makeText(getApplicationContext(),"Your information has been saved",Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("Error save info:", e.getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        Log.e("Error find info:", e.getLocalizedMessage());
                    }
                }
            });

        }
    }

    /**
     *                  METHOD TO ADD AN ACTION BAR
     * */

    private void addMenu() {

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.login_title, null);
        TextView textView = (TextView) view.findViewById(R.id.mytext);
        textView.setWidth(getWindow().getWindowManager().getDefaultDisplay().getWidth());
        textView.setPadding(0, 0, 50, 0);
        textView.setGravity(Gravity.CENTER);

        Typeface segoeui = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        textView.setTypeface(segoeui);
        textView.setText("Profile");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

    /**
     *                  METHODS FOR CREDIT CARD
     * */

    public void beAbleToEditCC (View view) {

        btnEditCC.setVisibility(View.GONE);
        buttonsCC.setVisibility(View.VISIBLE);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);

        enableEdit(CCnumber);
        enableEdit(CCcvc);
        enableEdit(CCexpMonth);
        enableEdit(CCexpYear);
        enableEdit(CCzip);
    }

    public void saveCC (View view){

        if (CCnumber.getText().toString().trim().isEmpty()
                || CCcvc.getText().toString().trim().isEmpty()
                || CCzip.getText().toString().trim().isEmpty()
                || CCexpMonth.getText().toString().trim().isEmpty()
                || CCexpYear.getText().toString().trim().isEmpty()){

            Toast.makeText(getApplicationContext(),"Please enter valid info in the fields",Toast.LENGTH_SHORT).show();
        } else {
            sCCnumber = CCnumber.getText().toString();
            sCCcvc = CCcvc.getText().toString();
            sCCexpMonth = CCexpMonth.getText().toString();
            sCCexpYear = CCexpYear.getText().toString();
            sCCzip = CCzip.getText().toString();

            iCCexpMonth = Integer.valueOf(sCCexpMonth);
            iCCexpYear = Integer.valueOf(sCCexpYear);

            card = new Card(sCCnumber, iCCexpMonth, iCCexpYear, sCCcvc);

            vCCnumber = card.validateNumber();
            vCCcvc = card.validateCVC();
            vCCexpMonth = card.validateExpMonth();
            vCCexpYear = card.validateExpYear();

            if (!vCCnumber){
                if (!vCCcvc){
                    Toast.makeText(getApplication(),"Invalid CVC",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(),"Invalid Credit Card Number",Toast.LENGTH_SHORT).show();
            } else {

                if (sCCzip.length() == 5) {
                    vCCzip = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Zipcode", Toast.LENGTH_SHORT).show();
                    vCCzip = false;
                }

                Calendar calendar = Calendar.getInstance();
                if (sCCexpYear.length() == 4) {

                    SimpleDateFormat year = new SimpleDateFormat("yyyy");

                    Date actual_year = calendar.getTime();

                    try {
                        Date input_year = year.parse(sCCexpYear);

                        if (actual_year.getYear() > input_year.getYear()) {
                            vCCexpYear = false;
                            Toast.makeText(getApplicationContext(), "Invalid Year", Toast.LENGTH_SHORT).show();
                        } else {
                            if (actual_year.getYear() == input_year.getYear()) {
                                if (sCCexpMonth.length() == 2) {

                                    SimpleDateFormat month = new SimpleDateFormat("MM");

                                    Date actual_month = calendar.getTime();

                                    Date input_month = month.parse(sCCexpMonth);

                                    if (actual_month.getMonth() + 1 > input_month.getMonth()) {
                                        Toast.makeText(getApplicationContext(), "Invalid Month", Toast.LENGTH_SHORT).show();
                                        vCCexpMonth = false;
                                    } else {
                                        vCCexpMonth = true;
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Month", Toast.LENGTH_SHORT).show();
                                    vCCexpMonth = false;
                                }
                                vCCexpYear = true;
                            } else if (actual_year.getYear() < input_year.getYear()) {
                                if (sCCexpMonth.length() == 2) {
                                    if (Integer.valueOf(sCCexpMonth) < 13 && Integer.valueOf(sCCexpMonth) > 0) {
                                        vCCexpMonth = true;
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid month", Toast.LENGTH_SHORT).show();
                                        vCCexpMonth = false;
                                    }
                                    vCCexpYear = true;
                                } else {
                                    vCCexpMonth = false;
                                    Toast.makeText(getApplicationContext(), "Ivalid Month", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    vCCexpYear = false;
                    Toast.makeText(getApplicationContext(), "Invalid Year", Toast.LENGTH_SHORT).show();
                }

            }


            if (vCCnumber && vCCcvc && vCCexpMonth && vCCexpYear && vCCzip){
                ParseQuery<ParseObject> createCard = new ParseQuery<ParseObject>("User");
                createCard.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
                createCard.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, com.parse.ParseException e) {
                        if (e == null) {
                            parseObject.put("CCnumber", Long.valueOf(CCnumber.getText().toString()));
                            parseObject.put("cvc", Integer.valueOf(CCcvc.getText().toString()));
                            parseObject.put("expMonth", Integer.valueOf(CCexpMonth.getText().toString()));
                            parseObject.put("expYear", Integer.valueOf(CCexpYear.getText().toString()));
                            parseObject.put("CCzipcode", Integer.valueOf(CCzip.getText().toString()));
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        disableEdit(CCnumber);
                                        disableEdit(CCcvc);
                                        disableEdit(CCexpMonth);
                                        disableEdit(CCexpYear);
                                        disableEdit(CCzip);

                                        String number = CCnumber.getText().toString();
                                        String toShow = number.substring(Math.max(number.length() - 4,0));
                                        CCnumber.setText("**** **** **** "+ toShow);

                                        buttonsCC.setVisibility(View.GONE);
                                        btnEditCC.setVisibility(View.VISIBLE);

                                        btn1.setEnabled(true);
                                        btn2.setEnabled(true);
                                        btn3.setEnabled(true);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "There was an error during saving, please try it later", Toast.LENGTH_LONG).show();
                                        Log.e("Error save: ", e.getLocalizedMessage());
                                    }
                                }
                            });
                        }
                    }
                });

            }

        }


    }

    public void cancelCCEdit (View view) {
        disableEdit(CCnumber);
        disableEdit(CCcvc);
        disableEdit(CCexpMonth);
        disableEdit(CCexpYear);
        disableEdit(CCzip);

        final String hideNumber = "**** **** **** ";

        btnEditCC.setVisibility(View.VISIBLE);
        buttonsCC.setVisibility(View.GONE);

        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);

        ParseQuery<ParseObject> cancelCCInfo = new ParseQuery<ParseObject>("User");
        cancelCCInfo.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        cancelCCInfo.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {

                    String realNumber = Long.toString(parseObject.getLong("CCnumber"));
                    String toShow = realNumber.substring(Math.max(realNumber.length() - 4, 0));


                    CCnumber.setText(hideNumber + toShow);
                    CCcvc.setText(Integer.toString(parseObject.getInt("cvc")));
                    CCexpMonth.setText(Integer.toString(parseObject.getInt("expMonth")));
                    CCexpYear.setText(Integer.toString(parseObject.getInt("expYear")));
                    CCzip.setText(Integer.toString(parseObject.getInt("CCzipcode")));
                }
            }
        });

    }


    /**
     *                  METHODS FOR HOME ADDRESS
     * */

    public void homeSelected (View view) {
        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        segoeb = Typeface.createFromAsset(getAssets(),"fonts/seguisb.ttf");

        if (homeFields.isShown()){
            homeFields.setVisibility(View.GONE);

            homeTitle.setTypeface(segoe);
            homeTitle.setTextColor(Color.parseColor("#555555"));

            otherTitle.setTypeface(segoe);
            otherTitle.setTextColor(Color.parseColor("#555555"));

            workTitle.setTypeface(segoe);
            workTitle.setTextColor(Color.parseColor("#555555"));

            ParseQuery<ParseObject> findHome = new ParseQuery<ParseObject>("Addresses");
            findHome.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Home");
            findHome.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (ParseObject result : list) {
                        String sAddress = result.getString("Address");
                        String sCity = result.getString("Unit_Suite");
                        String sZip = result.getString("ZipCode");

                        homeRef.setText(sAddress + ", " + sCity + ", " + sZip);
                    }
                }
            });

            arrowhome.setImageResource(R.drawable.arrow_blue_down);
        } else {

            if (workFields.isShown()){
                workFields.setVisibility(View.GONE);
                arrowWork.setImageResource(R.drawable.arrow_blue_down);
            }

            homeFields.setVisibility(View.VISIBLE);
            homeRef.setText("");
            arrowhome.setImageResource(R.drawable.arrow_blue_up);

            homeTitle.setTypeface(segoeb);
            homeTitle.setTextColor(Color.parseColor("#555555"));

            workTitle.setTypeface(segoe);
            workTitle.setTextColor(Color.parseColor("#c1c2c7"));

            otherTitle.setTypeface(segoe);
            otherTitle.setTextColor(Color.parseColor("#c1c2c7"));
        }

    }

    public void beAbleToEditHomeAddress (View view) {

        editHome.setVisibility(View.GONE);
        buttonsHome.setVisibility(View.VISIBLE);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);

        enableEdit(homeAddress);
        enableEdit(homeCity);
        enableEdit(homeZip);

        workTitle.setEnabled(false);
        arrowWork.setEnabled(false);

        homeTitle.setEnabled(false);
        arrowhome.setEnabled(false);

        otherTitle.setEnabled(false);
        arrowOther.setEnabled(false);
    }

    public void cancelHomeAddressEdit (View view) {

        disableEdit(homeAddress);
        disableEdit(homeCity);
        disableEdit(homeZip);

        ParseQuery<ParseObject> findHome = new ParseQuery<ParseObject>("Addresses");
        findHome.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Home");
        findHome.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    homeAddress.setText(sAddress);
                    homeCity.setText(sCity);
                    homeZip.setText(sZip);
                }
            }
        });

        editHome.setVisibility(View.VISIBLE);
        buttonsHome.setVisibility(View.GONE);

        workTitle.setEnabled(true);
        arrowWork.setEnabled(true);

        homeTitle.setEnabled(true);
        arrowhome.setEnabled(true);

        otherTitle.setEnabled(true);
        arrowOther.setEnabled(true);

        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);

    }

    public void saveHomeAddress (View view) {

        if (homeAddress.getText().toString().trim().isEmpty()
                && homeCity.getText().toString().trim().isEmpty()
                && homeZip.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please fill correctly",Toast.LENGTH_SHORT).show();
        } else {
            sHomeAd = homeAddress.getText().toString();
            sHomeCity = homeCity.getText().toString();
            sHomeZip = homeZip.getText().toString();

            ParseQuery<ParseObject> saveHome = new ParseQuery<ParseObject>("Addresses");
            saveHome.whereEqualTo("username",ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Home");
            saveHome.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        parseObject.put("Address", sHomeAd);
                        parseObject.put("Unit_Suite", sHomeCity);
                        parseObject.put("ZipCode", sHomeZip);
                        parseObject.put("Location","Home");
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    disableEdit(homeAddress);
                                    disableEdit(homeCity);
                                    disableEdit(homeZip);

                                    btn1.setEnabled(true);
                                    btn2.setEnabled(true);
                                    btn3.setEnabled(true);

                                    workTitle.setEnabled(true);
                                    arrowWork.setEnabled(true);

                                    homeTitle.setEnabled(true);
                                    arrowhome.setEnabled(true);

                                    otherTitle.setEnabled(true);
                                    arrowOther.setEnabled(true);

                                    editHome.setVisibility(View.VISIBLE);
                                    buttonsHome.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(), "Your address has been saved", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "There was an error during saving, please try it later", Toast.LENGTH_SHORT).show();
                                    Log.e("Error Save:", e.getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        Log.e("Error Find:", e.getLocalizedMessage());
                    }
                }
            });
        }

    }

    /**
     *                  METHODS FOR WORK ADDRESS
     * */

    public void workSelected (View view) {
        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        segoeb = Typeface.createFromAsset(getAssets(),"fonts/seguisb.ttf");

        if (workFields.isShown()){
            workFields.setVisibility(View.GONE);

            workTitle.setTypeface(segoe);
            workTitle.setTextColor(Color.parseColor("#555555"));

            homeTitle.setTypeface(segoe);
            homeTitle.setTextColor(Color.parseColor("#555555"));

            otherTitle.setTypeface(segoe);
            otherTitle.setTextColor(Color.parseColor("#555555"));

            ParseQuery<ParseObject> findWork = new ParseQuery<ParseObject>("Addresses");
            findWork.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Work");
            findWork.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (ParseObject result : list) {
                        String sAddress = result.getString("Address");
                        String sCity = result.getString("Unit_Suite");
                        String sZip = result.getString("ZipCode");

                        workRef.setText(sAddress + ", " + sCity + ", " + sZip);
                    }
                }
            });

            arrowWork.setImageResource(R.drawable.arrow_blue_down);
        } else {

            if (homeFields.isShown()){
                homeFields.setVisibility(View.GONE);
                arrowhome.setImageResource(R.drawable.arrow_blue_down);
            }

            workFields.setVisibility(View.VISIBLE);
            workRef.setText("");
            arrowWork.setImageResource(R.drawable.arrow_blue_up);

            workTitle.setTypeface(segoeb);
            workTitle.setTextColor(Color.parseColor("#555555"));

            otherTitle.setTypeface(segoe);
            otherTitle.setTextColor(Color.parseColor("#c1c2c7"));

            homeTitle.setTypeface(segoe);
            homeTitle.setTextColor(Color.parseColor("#c1c2c7"));
        }
    }

    public void beAbleToEditWorkAddress(View view) {

        editWork.setVisibility(View.GONE);
        buttonsWork.setVisibility(View.VISIBLE);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);

        enableEdit(workAddress);
        enableEdit(workCity);
        enableEdit(workZip);

        workTitle.setEnabled(false);
        arrowWork.setEnabled(false);

        homeTitle.setEnabled(false);
        arrowhome.setEnabled(false);

        otherTitle.setEnabled(false);
        arrowOther.setEnabled(false);

    }

    public void cancelWorkAddressEdit (View view) {

        disableEdit(workAddress);
        disableEdit(workCity);
        disableEdit(workZip);

        ParseQuery<ParseObject> findWork = new ParseQuery<ParseObject>("Addresses");
        findWork.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Work");
        findWork.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    workAddress.setText(sAddress);
                    workCity.setText(sCity);
                    workZip.setText(sZip);
                }
            }
        });

        editWork.setVisibility(View.VISIBLE);
        buttonsWork.setVisibility(View.GONE);

        workTitle.setEnabled(true);
        arrowWork.setEnabled(true);

        homeTitle.setEnabled(true);
        arrowhome.setEnabled(true);

        otherTitle.setEnabled(true);
        arrowOther.setEnabled(true);

        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);

    }

    public void saveWorkAddress (View view) {

        if (workAddress.getText().toString().trim().isEmpty()
                && workCity.getText().toString().trim().isEmpty()
                && workZip.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please fill correctly",Toast.LENGTH_SHORT).show();
        } else {
            sWorkAd = workAddress.getText().toString();
            sWorkCity = workCity.getText().toString();
            sWorkZip = workZip.getText().toString();

            ParseQuery<ParseObject> saveWork = new ParseQuery<ParseObject>("Addresses");
            saveWork.whereEqualTo("username",ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location","Work");
            saveWork.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e==null){
                        parseObject.put("Address",sWorkAd);
                        parseObject.put("Unit_Suite",sWorkCity);
                        parseObject.put("ZipCode",sWorkZip);
                        parseObject.put("Location","Work");
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){

                                    disableEdit(workAddress);
                                    disableEdit(workCity);
                                    disableEdit(workZip);

                                    btn1.setEnabled(true);
                                    btn2.setEnabled(true);
                                    btn3.setEnabled(true);

                                    workTitle.setEnabled(true);
                                    arrowWork.setEnabled(true);

                                    homeTitle.setEnabled(true);
                                    arrowhome.setEnabled(true);

                                    otherTitle.setEnabled(true);
                                    arrowOther.setEnabled(true);

                                    editWork.setVisibility(View.VISIBLE);
                                    buttonsWork.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(),"Your address has been saved",Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(),"There was an error during saving, please try it later",Toast.LENGTH_SHORT).show();
                                    Log.e("Error Save:",e.getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        Log.e("Error Find:",e.getLocalizedMessage());
                    }
                }
            });
        }

    }

    /**
     *                  METHODS FOR OTHER ADDRESS
     * */

    public void otherSelected (View view) {
        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        segoeb = Typeface.createFromAsset(getAssets(),"fonts/seguisb.ttf");

        if (otherFields.isShown()){
            otherFields.setVisibility(View.GONE);

            otherTitle.setTypeface(segoe);
            otherTitle.setTextColor(Color.parseColor("#555555"));

            workTitle.setTypeface(segoe);
            workTitle.setTextColor(Color.parseColor("#555555"));

            homeTitle.setTypeface(segoe);
            homeTitle.setTextColor(Color.parseColor("#555555"));

            ParseQuery<ParseObject> findOther = new ParseQuery<ParseObject>("Addresses");
            findOther.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Other");
            findOther.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (ParseObject result : list) {
                        String sAddress = result.getString("Address");
                        String sCity = result.getString("Unit_Suite");
                        String sZip = result.getString("ZipCode");

                        otherRef.setText(sAddress + ", " + sCity + ", " + sZip);
                    }
                }
            });

            arrowOther.setImageResource(R.drawable.arrow_blue_down);
        } else {

            if (homeFields.isShown() || workFields.isShown()){
                homeFields.setVisibility(View.GONE);
                arrowhome.setImageResource(R.drawable.arrow_blue_down);
                workFields.setVisibility(View.GONE);
                arrowWork.setImageResource(R.drawable.arrow_blue_down);
            }

            otherFields.setVisibility(View.VISIBLE);
            otherRef.setText("");
            arrowOther.setImageResource(R.drawable.arrow_blue_up);

            otherTitle.setTypeface(segoeb);
            otherTitle.setTextColor(Color.parseColor("#555555"));

            workTitle.setTypeface(segoe);
            workTitle.setTextColor(Color.parseColor("#c1c2c7"));

            homeTitle.setTypeface(segoe);
            homeTitle.setTextColor(Color.parseColor("#c1c2c7"));
        }
    }

    public void beAbleToEditOtherAddress (View view) {

        editOther.setVisibility(View.GONE);
        buttonsOther.setVisibility(View.VISIBLE);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);

        enableEdit(otherAddress);
        enableEdit(otherCity);
        enableEdit(otherZip);

        workTitle.setEnabled(false);
        arrowWork.setEnabled(false);

        homeTitle.setEnabled(false);
        arrowhome.setEnabled(false);

        otherTitle.setEnabled(false);
        arrowOther.setEnabled(false);

    }

    public void cancelOtherAddressEdit (View view) {

        disableEdit(otherAddress);
        disableEdit(otherCity);
        disableEdit(otherZip);

        ParseQuery<ParseObject> findOther = new ParseQuery<ParseObject>("Addresses");
        findOther.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location", "Other");
        findOther.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject result : list) {
                    String sAddress = result.getString("Address");
                    String sCity = result.getString("Unit_Suite");
                    String sZip = result.getString("ZipCode");

                    otherAddress.setText(sAddress);
                    otherCity.setText(sCity);
                    otherZip.setText(sZip);
                }
            }
        });

        editOther.setVisibility(View.VISIBLE);
        buttonsOther.setVisibility(View.GONE);

        workTitle.setEnabled(true);
        arrowWork.setEnabled(true);

        homeTitle.setEnabled(true);
        arrowhome.setEnabled(true);

        otherTitle.setEnabled(true);
        arrowOther.setEnabled(true);

        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);

    }

    public void saveOtherAddress (View view) {

        if (otherAddress.getText().toString().trim().isEmpty()
                && otherCity.getText().toString().trim().isEmpty()
                && otherZip.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please fill correctly",Toast.LENGTH_SHORT).show();
        } else {
            sOtherAd = otherAddress.getText().toString();
            sOtherCity = otherCity.getText().toString();
            sOtherZip = otherZip.getText().toString();

            ParseQuery<ParseObject> saveOther = new ParseQuery<ParseObject>("Addresses");
            saveOther.whereEqualTo("username",ParseUser.getCurrentUser().getUsername()).whereEqualTo("Location","Other");
            saveOther.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e==null){
                        parseObject.put("Address",sOtherAd);
                        parseObject.put("Unit_Suite",sOtherCity);
                        parseObject.put("ZipCode",sOtherZip);
                        parseObject.put("Location","Other");
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){

                                    disableEdit(otherAddress);
                                    disableEdit(otherCity);
                                    disableEdit(otherZip);

                                    btn1.setEnabled(true);
                                    btn2.setEnabled(true);
                                    btn3.setEnabled(true);

                                    workTitle.setEnabled(true);
                                    arrowWork.setEnabled(true);

                                    homeTitle.setEnabled(true);
                                    arrowhome.setEnabled(true);

                                    otherTitle.setEnabled(true);
                                    arrowOther.setEnabled(true);

                                    editOther.setVisibility(View.VISIBLE);
                                    buttonsOther.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(),"Your address has been saved",Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(),"There was an error during saving, please try it later",Toast.LENGTH_SHORT).show();
                                    Log.e("Error Save:",e.getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        Log.e("Error Find:",e.getLocalizedMessage());
                    }
                }
            });
        }

    }

    /**
     *                  METHODS FOR PERSONAL PREFERENCES
     * */

    public void beAbleToEditPrefs(View view) {

        buttonsPref.setVisibility(View.VISIBLE);
        editPref.setVisibility(View.GONE);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);

        spinnerDetergent.setEnabled(true);
        spinnerDress.setEnabled(true);
        spinnerStarch.setEnabled(true);
    }

    public void cancelPrefs (View view){

        buttonsPref.setVisibility(View.GONE);
        editPref.setVisibility(View.VISIBLE);

        spinnerDetergent.setEnabled(false);
        spinnerDress.setEnabled(false);
        spinnerStarch.setEnabled(false);

        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);


        ParseQuery<ParseObject> finDeter = new ParseQuery<ParseObject>("User");
        finDeter.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        finDeter.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    String found = parseObject.getString("PrefDetergent");
                    switch (found){
                        case "Scented":
                            spinnerDetergent.setSelection(1);
                            break;
                        case "Unscented":
                            spinnerDetergent.setSelection(2);
                            break;

                    }
                }
            }
        });

        ParseQuery<ParseObject> findStarch = new ParseQuery<ParseObject>("User");
        findStarch.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        findStarch.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    String found = parseObject.getString("PrefStarch");
                    switch (found){
                        case "Light":
                            spinnerStarch.setSelection(1);
                            break;
                        case "Heavy":
                            spinnerStarch.setSelection(2);
                            break;

                    }
                }
            }
        });

        ParseQuery<ParseObject> finDress = new ParseQuery<ParseObject>("User");
        finDress.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        finDress.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    String found = parseObject.getString("PrefDressShirt");

                    switch (found){
                        case "Launder":
                            spinnerDress.setSelection(1);
                            break;
                        case "Dry Cleaning":
                            spinnerDress.setSelection(2);
                            break;
                        case "Cleanpress Choice":
                            spinnerDress.setSelection(3);
                    }

                }
            }
        });

    }

    public void  savePrefs (View view) {

        if (spinnerDetergent.getSelectedItemPosition() == 0
                || spinnerDress.getSelectedItemPosition() == 0
                || spinnerStarch.getSelectedItemPosition() == 0){

            Toast.makeText(getApplicationContext(),"Please select a valid option.",Toast.LENGTH_SHORT).show();

        } else {

            ParseQuery<ParseObject> savePref = new ParseQuery<ParseObject>("User");
            savePref.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
            savePref.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        parseObject.put("PrefDetergent",spinnerDetergent.getSelectedItem().toString());
                        parseObject.put("PrefStarch",spinnerStarch.getSelectedItem().toString());
                        parseObject.put("PrefDressShirt",spinnerDress.getSelectedItem().toString());
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){
                                    buttonsPref.setVisibility(View.GONE);
                                    editPref.setVisibility(View.VISIBLE);

                                    spinnerDetergent.setEnabled(false);
                                    spinnerDress.setEnabled(false);
                                    spinnerStarch.setEnabled(false);

                                    btn1.setEnabled(true);
                                    btn2.setEnabled(true);
                                    btn3.setEnabled(true);

                                    Toast.makeText(getApplicationContext(),"Your Preferences have been saved",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"There was an error during saving please try it later",Toast.LENGTH_SHORT).show();
                                    Log.e("SAVE: ",e.getLocalizedMessage());
                                }
                            }
                        });

                    } else {

                        Log.e("FIND: ",e.getLocalizedMessage());
                    }
                }
            });
        }
    }


    /**
     *                  ADAPTER FOR SPINNER
     * */

    private static class MySpinnerAdapter extends ArrayAdapter<String> {

        public MySpinnerAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        Typeface segoeuit = Typeface.createFromAsset(getContext().getAssets(),"fonts/segoeui.ttf");
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(segoeuit);
            view.setTextSize(18);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(segoeuit);
            view.setTextSize(18);
            return view;
        }

    }


}


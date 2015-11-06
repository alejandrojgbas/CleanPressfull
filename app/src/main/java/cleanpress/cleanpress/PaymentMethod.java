package cleanpress.cleanpress;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.model.Card;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PaymentMethod extends ActionBarActivity {

    Typeface segoe;
    EditText CCnumber,CCcvc,CCexpMonth,CCexpYear,CCzip;
    TextView title;
    Button btnAuth,btnCont;
    Boolean vCCnumber,vCCcvc,vCCzip,vCCexpMonth,vCCexpYear;
    Integer iCCexpMonth,iCCexpYear;
    String sCCnumber,sCCcvc,sCCexpMonth,sCCexpYear,sCCzip;

    Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method);
        addMenu();



        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        title = (TextView) findViewById(R.id.title);
        CCnumber = (EditText) findViewById(R.id.cc_number);
        CCcvc = (EditText) findViewById(R.id.cc_cvc);
        CCexpMonth = (EditText) findViewById(R.id.cc_mm);
        CCexpYear = (EditText) findViewById(R.id.cc_yy);
        CCzip = (EditText) findViewById(R.id.cc_zip);
        btnAuth = (Button) findViewById(R.id.authorize_cc);
        btnCont = (Button) findViewById(R.id.Schedule);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        title.setTypeface(segoe);
        CCnumber.setTypeface(segoe);
        CCcvc.setTypeface(segoe);
        CCzip.setTypeface(segoe);
        CCexpMonth.setTypeface(segoe);
        CCexpYear.setTypeface(segoe);
        btnCont.setTypeface(segoe);
        btnAuth.setTypeface(segoe);



    }

    public void toSchedule (View view){

        ParseQuery<ParseObject> createCard = new ParseQuery<ParseObject>("User");
        createCard.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        createCard.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e==null){
                    parseObject.put("CCnumber",Long.valueOf(CCnumber.getText().toString()));
                    parseObject.put("cvc",Integer.valueOf(CCcvc.getText().toString()));
                    parseObject.put("expMonth",Integer.valueOf(CCexpMonth.getText().toString()));
                    parseObject.put("expYear",Integer.valueOf(CCexpYear.getText().toString()));
                    parseObject.put("CCzipcode",Integer.valueOf(CCzip.getText().toString()));
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e==null){
                                startActivity(new Intent(PaymentMethod.this, ScheduleScreen.class));
                            } else {
                                Toast.makeText(getApplicationContext(),"there was an error during saving, please try it later",Toast.LENGTH_LONG).show();
                                Log.e("Error save: ",e.getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });


    }

    public void authorizeCC (View view){

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

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                vCCexpYear = false;
                Toast.makeText(getApplicationContext(), "Invalid Year", Toast.LENGTH_SHORT).show();
            }

        }


            if (vCCnumber && vCCcvc && vCCexpMonth && vCCexpYear && vCCzip){
                btnAuth.setBackgroundColor(Color.rgb(127,230,139));
                btnCont.setBackgroundColor(Color.rgb(127,230,139));
                btnAuth.setEnabled(false);
                btnCont.setEnabled(true);

                disableEdit(CCnumber);
                disableEdit(CCcvc);
                disableEdit(CCexpMonth);
                disableEdit(CCexpYear);
                disableEdit(CCzip);
            }

        }


    }

    private void disableEdit (EditText editText){

        editText.setFocusable(false);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setKeyListener(null);
        editText.setTextColor(Color.rgb(193,194,199));
        editText.setCursorVisible(false);
        editText.setEnabled(false);

    }

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
        textView.setText("Payment Method");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

}

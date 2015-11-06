package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.model.Card;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SignPayment extends ActionBarActivity {

    Typeface segoe;
    EditText CCnumber,CCcvc,CCexpMonth,CCexpYear,CCzip;
    TextView title;
    Button btnAuth,btnCont,btnSkip;
    Boolean vCCnumber,vCCcvc,vCCzip,vCCexpMonth,vCCexpYear;
    Integer iCCexpMonth,iCCexpYear;
    String sCCnumber,sCCcvc,sCCexpMonth,sCCexpYear,sCCzip;

    Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_payment_screen);

        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        title = (TextView) findViewById(R.id.titleSign);
        CCnumber = (EditText) findViewById(R.id.cc_numberSign);
        CCcvc = (EditText) findViewById(R.id.cc_cvcSign);
        CCexpMonth = (EditText) findViewById(R.id.cc_mmSign);
        CCexpYear = (EditText) findViewById(R.id.cc_yySign);
        CCzip = (EditText) findViewById(R.id.cc_zipSign);
        btnAuth = (Button) findViewById(R.id.auth_btn);
        btnCont = (Button) findViewById(R.id.dash_btn);
        btnSkip = (Button) findViewById(R.id.skipBtn);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        title.setTypeface(segoe);
        CCnumber.setTypeface(segoe);
        CCcvc.setTypeface(segoe);
        CCzip.setTypeface(segoe);
        CCexpMonth.setTypeface(segoe);
        CCexpYear.setTypeface(segoe);
        btnCont.setTypeface(segoe);
        btnAuth.setTypeface(segoe);
        btnSkip.setTypeface(segoe);
    }

    public void toDash (View view){

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
                                startActivity(new Intent(SignPayment.this, ScheduleScreen.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "there was an error during saving, please try it later", Toast.LENGTH_LONG).show();
                                Log.e("Error save: ", e.getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });


    }

    public void signAuthorizeCC (View view){

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

    public void skip (View view){
        startActivity(new Intent(SignPayment.this,Dashboard.class));
    }

    private void disableEdit (EditText editText){

        editText.setFocusable(false);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setKeyListener(null);
        editText.setTextColor(Color.rgb(193,194,199));
        editText.setCursorVisible(false);
        editText.setEnabled(false);

    }


}

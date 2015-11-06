package cleanpress.cleanpress;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class Sign extends ActionBarActivity {

    Typeface segoe;
    EditText Name,Last,Email,Pass,Phone;
    String sName,sLast,sEmail,sPass;
    long iPhone;
    Button bSignUp;
    TextView linkTerms,title,linkPolicy,TitleLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_screen);
        addMenu();


        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        Typeface segoeb = Typeface.createFromAsset(getAssets(),"fonts/seguisb.ttf");

        Name = (EditText) findViewById(R.id.SignName);
        Last = (EditText) findViewById(R.id.SignLastname);
        Email = (EditText) findViewById(R.id.SignEmail);
        Pass = (EditText) findViewById(R.id.SignPass);
        bSignUp = (Button) findViewById(R.id.bSign);
        TitleLinks = (TextView) findViewById(R.id.titleTerms);
        linkTerms = (TextView) findViewById(R.id.TermsLink);
        title = (TextView) findViewById(R.id.titleSign);
        linkPolicy = (TextView) findViewById(R.id.PolicyLink);
        Phone = (EditText) findViewById(R.id.SignPhone);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Name.setTypeface(segoe);
        Last.setTypeface(segoe);
        Email.setTypeface(segoe);
        Pass.setTypeface(segoe);
        Phone.setTypeface(segoe);

        bSignUp.setTypeface(segoe);
        linkTerms.setTypeface(segoeb);
        title.setTypeface(segoe);
        linkPolicy.setTypeface(segoeb);
        TitleLinks.setTypeface(segoe);

        Pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bSignUp.setEnabled(
                        !Name.getText().toString().trim().isEmpty()
                        && !Last.getText().toString().trim().isEmpty()
                        && !Email.getText().toString().trim().isEmpty()
                        && !Phone.getText().toString().trim().isEmpty()
                        && !Pass.getText().toString().trim().isEmpty()
                );
                if(bSignUp.isEnabled()){
                    bSignUp.setBackgroundColor(Color.rgb(127,230,139));
                }
                else {
                    bSignUp.setBackgroundColor(Color.rgb(193,194,199));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void SignUp(View view){


            sName = Name.getText().toString();
            sLast = Last.getText().toString();
            sEmail = Email.getText().toString();
            sPass = Pass.getText().toString();
            iPhone = Long.valueOf(Phone.getText().toString());


                ParseUser user = new ParseUser();
                user.setUsername(sEmail);
                user.setPassword(sPass);
                user.setEmail(sEmail);

                final ParseObject User = new ParseObject("User");
                User.put("Username", sEmail);
                User.put("FirstName", sName);
                User.put("LastName", sLast);
                User.put("Phone",iPhone);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            User.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Intent i = new Intent(Sign.this, SignPayment.class);
                                        startActivity(i);
                                        Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Log.e("Error in class User: ", e.getMessage());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "The email " + sEmail + " is already taken", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    public void showTerms(View view){


    }

    public void showPolicy (View view){

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
        textView.setText("Sign up");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }
}

package cleanpress.cleanpress;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class Login extends ActionBarActivity {

    EditText Email,Password;
    String sEmail,sPass;
    FrameLayout frameLayout;
    Typeface segoe;
    TextView title,forgotTitle;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        addMenu();


        frameLayout = (FrameLayout) findViewById(R.id.popuplayoutPw);
        frameLayout.getForeground().setAlpha(0);

        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        Email = (EditText) findViewById(R.id.logEmail);
        Password = (EditText) findViewById(R.id.logPass);
        title = (TextView) findViewById(R.id.Title);
        forgotTitle = (TextView) findViewById(R.id.forgotTitle);
        login = (Button) findViewById(R.id.LoginApp);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Email.setTypeface(segoe);
        Password.setTypeface(segoe);
        title.setTypeface(segoe);
        forgotTitle.setTypeface(segoe);
        login.setTypeface(segoe);



    }


    public void showPopupForgot (View view) {

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.popup_forgotpw, null);
            final AlertDialog infoDialog = new AlertDialog.Builder(Login.this)
                    .setView(dialog)
                    .create();
            infoDialog.show();
        TextView message = (TextView) dialog.findViewById(R.id.Message);
        final EditText pwForgot = (EditText) dialog.findViewById(R.id.pwForgot);
        Button SendPw = (Button) dialog.findViewById(R.id.AcceptSend);
        Button cancelPw = (Button) dialog.findViewById(R.id.CancelSend);
            SendPw.setTypeface(segoe);
            message.setTypeface(segoe);
        frameLayout.getForeground().setAlpha(220);

        cancelPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
                frameLayout.getForeground().setAlpha(0);
                pwForgot.setText("");
            }
        });


        SendPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = pwForgot.getText().toString();
                ParseUser.requestPasswordResetInBackground(sEmail, new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            infoDialog.dismiss();
                            frameLayout.getForeground().setAlpha(0);
                            Toast.makeText(getApplicationContext(), "You will receive an email soon", Toast.LENGTH_LONG).show();

                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            Toast.makeText(getApplicationContext(), "The email is invalid or not registered", Toast.LENGTH_LONG).show();
                            infoDialog.dismiss();
                            frameLayout.getForeground().setAlpha(0);
                        }
                    }
                });
            }
        });






    }

    public void LoginApp (View view){

        if (Email.getText().toString().trim().isEmpty()){
            if (Password.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(),"Please enter an email",Toast.LENGTH_SHORT).show();

        }
        else {


            sEmail = Email.getText().toString();
            sPass = Password.getText().toString();

            ParseUser.logInInBackground(sEmail, sPass, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null){

                        ParseQuery<ParseObject> findCard = new ParseQuery<ParseObject>("User");
                        findCard.whereEqualTo("Username", parseUser.getUsername());
                        findCard.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    long i = parseObject.getLong("CCnumber");
                                    String card = String.valueOf(i);

                                    Log.e("parse: ",card);
                                    if (card.equals("0")) {
                                        startActivity(new Intent(Login.this, SignPayment.class));
                                    } else {
                                        startActivity(new Intent(Login.this, Dashboard.class));
                                    }
                                }
                            }
                        });

                        Toast.makeText(getApplicationContext(),"Welcome!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Invalid username",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
        textView.setText("Log in");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

}

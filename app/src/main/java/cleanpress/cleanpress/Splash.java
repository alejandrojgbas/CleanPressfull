package cleanpress.cleanpress;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class Splash extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "vHjieX1nOgzeOy0hFcZVOecTMLSWBaubIkHW0a7X", "7W0EalICPRyz5ybOnCAm1nhUHYttZtkMv0vJKZ7U");
        ParseAnalytics.trackAppOpened(getIntent());
        ParseFacebookUtils.initialize(getApplicationContext());


        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> findUser = new ParseQuery<ParseObject>("User");
                if (currentUser == null){
                    startActivity(new Intent(Splash.this, WelcomeScreen.class));
                    Splash.this.finish();
                } else {
                    findUser.whereEqualTo("Username",currentUser.getUsername());
                    findUser.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (parseObject == null) {
                                ParseUser.logOutInBackground(new LogOutCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            startActivity(new Intent(Splash.this, WelcomeScreen.class));
                                            Splash.this.finish();
                                        }
                                    }
                                });
                            } else {
                                Intent mainintent = new Intent(Splash.this,Dashboard.class);
                                Splash.this.startActivity(mainintent);
                                Splash.this.finish();
                            }
                        }
                    });
                }

            }
        },3000);


    }
}

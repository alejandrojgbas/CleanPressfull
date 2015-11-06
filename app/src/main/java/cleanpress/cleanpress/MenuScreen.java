package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MenuScreen extends ActionBarActivity {

    Typeface segoe;
    TextView t1,t2,t3,t4,t5,t6,t7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);
        addMenu();

        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        t6 = (TextView) findViewById(R.id.t6);
        t7 = (TextView) findViewById(R.id.t7);

        t1.setTypeface(segoe);
        t2.setTypeface(segoe);
        t3.setTypeface(segoe);
        t4.setTypeface(segoe);
        t5.setTypeface(segoe);
        t6.setTypeface(segoe);
        t7.setTypeface(segoe);

    }

    public void toOrders (View view){        startActivity(new Intent(MenuScreen.this,MyOrders.class));    }

    public void toProfile (View view){        startActivity(new Intent(MenuScreen.this,Profile.class));    }

    public void toPrices (View view){        startActivity(new Intent(MenuScreen.this,Prices.class));    }

    public void toSupport (View view){        startActivity(new Intent(MenuScreen.this,Support.class));    }

    public void toFeedback (View view){        startActivity(new Intent(MenuScreen.this,Feedback.class));    }

    public void toAbout (View view){        startActivity(new Intent(MenuScreen.this,About.class));    }

    public void logOut (View view){
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    startActivity(new Intent(MenuScreen.this,WelcomeScreen.class));
                }
            }
        });
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
        textView.setText("Clean Press");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }
}

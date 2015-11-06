package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class Dashboard extends ActionBarActivity {

    TextView username;
    Button toMenu,toRequest;
    Typeface segoe;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_screen);

        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        toMenu = (Button) findViewById(R.id.toDash);
        toRequest = (Button) findViewById(R.id.toRequest);


        username = (TextView) findViewById(R.id.username);

        ParseQuery<ParseObject> name = new ParseQuery<ParseObject>("User");
        name.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        name.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject != null) {
                    user = parseObject.getString("FirstName");
                    username.setText("Welcome, " + user);
                }
            }
        });


        username.setTypeface(segoe);
        toMenu.setTypeface(segoe);
        toRequest.setTypeface(segoe);
    }

    public void showDash (View view){
        startActivity(new Intent(Dashboard.this, MenuScreen.class));
    }

    public void Schedule (View view) {

        ParseQuery<ParseObject> findCC = new ParseQuery<ParseObject>("User");
        findCC.whereEqualTo("Username",ParseUser.getCurrentUser().getUsername()).whereGreaterThan("CCnumber", 10);
        findCC.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    startActivity(new Intent(Dashboard.this, PaymentMethod.class));
                    Log.e("Error: ","no hay tarjeta");
                } else {
                    startActivity(new Intent(Dashboard.this,ScheduleScreen.class));
                    Log.e("Number: ",parseObject.getLong("CCnumber")+"");
                }
            }
        });

    }

}

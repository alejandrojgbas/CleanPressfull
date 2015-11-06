package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ScheduleScreen extends ActionBarActivity {

    Typeface segoe;
    TextView title;
    Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_screen);
        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        btn1 = (Button) findViewById(R.id.btnLater);
        btn2 = (Button) findViewById(R.id.btnNow);
        title = (TextView) findViewById(R.id.Title);

        title.setTypeface(segoe);
        btn1.setTypeface(segoe);
        btn2.setTypeface(segoe);

    }

    public void RequestLater (View view){
        startActivity(new Intent(ScheduleScreen.this,RequestLater.class));

    }

    public void RequestNow (View view){

        startActivity(new Intent(ScheduleScreen.this,RequestNowLoc.class));
    }

}

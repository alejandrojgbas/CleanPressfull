package cleanpress.cleanpress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class Support extends ActionBarActivity {

    TextView showFaqs, faqs1, faqs2, faqs3, t_faqs1, t_faqs2, t_faqs3;
    Button twitterSupport, callSupport,fbSupport;
    String number = "+584128318785";
    LinearLayout faqsLayout;
    //String uri = "facebook://facebook.com/wall?user=417079614970109";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_screen);
        addMenu();


        fbSupport = (Button) findViewById(R.id.fb_support);
        showFaqs = (TextView) findViewById(R.id.show_faqs);
        twitterSupport = (Button) findViewById(R.id.twitter_support);
        callSupport = (Button) findViewById(R.id.call_support);


        }

    public void call (View view){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/1465514210413452")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/pages/Clean-Press-Inc/1465514210413452?fref=ts")); //catches and opens a url to the desired page
        }
    }

    public static Intent getOpenTwitterIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.twitter.android", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=cleanpressinc")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/cleanpressinc")); //catches and opens a url to the desired page
        }

    }

    public void openTweet (View view) {
        Intent tweet = getOpenTwitterIntent(getApplicationContext());
        startActivity(tweet);
    }

    public void openFB (View view) {
        Intent fb= getOpenFacebookIntent(getApplicationContext());
        startActivity(fb);
    }

    public void showFaqs (View view) {
        if (faqsLayout.isShown()) {
            faqsLayout.setVisibility(View.GONE);
            showFaqs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_pr, 0);

        } else {
            faqsLayout.setVisibility(View.VISIBLE);
            showFaqs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0);
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
        textView.setText("Support");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }


}

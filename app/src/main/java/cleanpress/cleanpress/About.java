package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;


public class About extends ActionBarActivity {
    TextView  sWho,title;
    WebView mWebView;
    String videoID = "YqeW9_5kURI";
    Button Terms,Work,Tuto;
    Typeface segoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen);
        addMenu();

        segoe = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");
        Terms = (Button) findViewById(R.id.terms_btn);
        Work = (Button) findViewById(R.id.wp_btn);
        Tuto = (Button) findViewById(R.id.tuto_btn);
        title = (TextView) findViewById(R.id.Title);
        sWho = (TextView) findViewById(R.id.who_txt);

        Terms.setTypeface(segoe);
        Work.setTypeface(segoe);
        Tuto.setTypeface(segoe);
        title.setTypeface(segoe);
        sWho.setTypeface(segoe);

        mWebView = (WebView) findViewById(R.id.webView);


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.loadUrl("http://www.youtube.com/embed/" + videoID + "?autoplay=1&vq=small");
        mWebView.setWebChromeClient(new WebChromeClient());


    }
    @Override
    public void onPause() {
        super.onPause();

        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(mWebView, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {

        } catch(NoSuchMethodException nsme) {

        } catch(InvocationTargetException ite) {

        } catch (IllegalAccessException iae) {

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
        textView.setText("About");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

    public void toTerms (View view){        startActivity(new Intent(About.this,Terms.class)); }
    public void toPolicy(View view){        startActivity(new Intent(About.this,WorkingPolicy.class)); }
    public void toTutorial (View view){     startActivity(new Intent(About.this,Tutorials.class)); }

}


package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.tv.TvInputService;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.List;
import java.util.Vector;


public class WelcomeScreen extends FragmentActivity {

    Button LogIn,Sign;
    LoginButton LoginFB;
    ViewPager viewPager;
    List<Fragment> fragmentList;
    PageAdapter pageAdapter;
    Typeface segoe;
    RadioButton tut1,tut2,tut3,tut4;
    LinearLayout Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        Layout = (LinearLayout) findViewById(R.id.Layout);

        LogIn = (Button) findViewById(R.id.Login);
        Sign = (Button) findViewById(R.id.Sign);
        LoginFB = (LoginButton) findViewById(R.id.FBLogin);

        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        LogIn.setTypeface(segoe);
        Sign.setTypeface(segoe);
        LoginFB.setTypeface(segoe);

        fragmentList = new Vector<Fragment>();
        fragmentList.add(Fragment.instantiate(this, Tutorial1.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Tutorial2.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Tutorial3.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Tutorial4.class.getName()));

        tut1 = (RadioButton) findViewById(R.id.tut1);
        tut2 = (RadioButton) findViewById(R.id.tut2);
        tut3 = (RadioButton) findViewById(R.id.tut3);
        tut4 = (RadioButton) findViewById(R.id.tut4);


        pageAdapter = new PageAdapter(this.getSupportFragmentManager(),fragmentList);
        viewPager = (ViewPager) findViewById(R.id.tutorialPages);
        viewPager.setAdapter(pageAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == 0){
                    tut1.setChecked(true);

                }
                if (viewPager.getCurrentItem() == 1){
                    tut2.setChecked(true);

                }
                if (viewPager.getCurrentItem() == 2){
                    tut3.setChecked(true);

                }
                if (viewPager.getCurrentItem() == 3){
                    tut4.setChecked(true);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    public void showPage (View view){
        if (tut1.isChecked()){
            viewPager.setCurrentItem(0);
        }
        if (tut2.isChecked()){
            viewPager.setCurrentItem(1);
        }
        if (tut3.isChecked()){
            viewPager.setCurrentItem(2);
        }
        if (tut4.isChecked()){
            viewPager.setCurrentItem(3);
        }
    }

    public void Login (View view){
        Intent i = new Intent(this,Login.class);
        startActivity(i);

    }

    public void Sign (View view){
        Intent i = new Intent(this,Sign.class);
        startActivity(i);

    }
    public void LogFB (View view){





    }

}

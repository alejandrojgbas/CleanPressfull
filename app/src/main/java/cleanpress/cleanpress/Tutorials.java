package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

public class Tutorials extends ActionBarActivity {

    ViewPager viewPager;
    List<Fragment> fragmentList;
    PageAdapter pageAdapter;
    RadioButton tut1,tut2,tut3,tut4;
    LinearLayout Layout;
    Button buttonSkip;
    Typeface segoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorials_screen);


        segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        buttonSkip = (Button) findViewById(R.id.btnSkip);

        buttonSkip.setTypeface(segoe);

        Layout = (LinearLayout) findViewById(R.id.Layout);

        fragmentList = new Vector<Fragment>();
        fragmentList.add(Fragment.instantiate(this, Tutorial1.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Tutorial2.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Tutorial3.class.getName()));
        fragmentList.add(Fragment.instantiate(this, Tutorial4.class.getName()));

        tut1 = (RadioButton) findViewById(R.id.tut1);
        tut2 = (RadioButton) findViewById(R.id.tut2);
        tut3 = (RadioButton) findViewById(R.id.tut3);
        tut4 = (RadioButton) findViewById(R.id.tut4);



        pageAdapter = new PageAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager = (ViewPager) findViewById(R.id.tutorialPages);
        viewPager.setAdapter(pageAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == 0) {
                    tut1.setChecked(true);
                }
                if (viewPager.getCurrentItem() == 1) {
                    tut2.setChecked(true);
                }
                if (viewPager.getCurrentItem() == 2) {
                    tut3.setChecked(true);
                }
                if (viewPager.getCurrentItem() == 3) {
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



    public void goBack (View view){
        startActivity(new Intent(Tutorials.this,About.class));
    }
}

package cleanpress.cleanpress;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Prices extends ActionBarActivity {

    ArrayList<String> prices;
    ArrayList<Integer> backs;
    ArrayList<String> names;
    Typeface segoebold,segoef;
    TextView PriceTitle,title_price,text_wash,dry_cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prices_list);

        addMenu();

        prices = new ArrayList<String>();
        names = new ArrayList<String>();
        backs = new ArrayList<Integer>();


        PriceTitle = (TextView) findViewById(R.id.title);
        title_price = (TextView) findViewById(R.id.price_title);
        text_wash = (TextView) findViewById(R.id.textWash);
        dry_cl = (TextView) findViewById(R.id.dry_clean);

        segoebold = Typeface.createFromAsset(getAssets(),"fonts/seguisb.ttf");
        segoef = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        PriceTitle.setTypeface(segoef);
        text_wash.setTypeface(segoef);
        title_price.setTypeface(segoebold);
        dry_cl.setTypeface(segoef);

        names.add("Jeans");
        names.add("Dress Pants");
        names.add("Jackets");
        names.add("Shirts");
        names.add("Blouses");
        names.add("Bras");
        names.add("Underwear");
        names.add("Dresses");
        names.add("Suits");

        backs.add(R.drawable.prices__jeans);
        backs.add(R.drawable.prices__dress_pants);
        backs.add(R.drawable.prices__jackets);
        backs.add(R.drawable.prices__shirts);
        backs.add(R.drawable.prices__blouses);
        backs.add(R.drawable.prices__bras);
        backs.add(R.drawable.prices__underwear);
        backs.add(R.drawable.prices__dresses);
        backs.add(R.drawable.prices__suits);

        final ListView listView = (ListView) findViewById(R.id.price_list);

        listView.setDividerHeight(0);

        ParseQuery<ParseObject> c_prices = new ParseQuery<ParseObject>("Prices");
        c_prices.orderByAscending("createdAt").setSkip(1).setLimit(9);
        c_prices.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject object : list) {
                        prices.add("$" + String.format("%.2f", object.getDouble("Price")));
                    }
                    AdapterPrices adapter = new AdapterPrices(Prices.this, prices,backs,names);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(2);
                    listView.setDivider(new ColorDrawable(Color.parseColor("#ffffff")));
                    final ScrollView vw = (ScrollView) findViewById(R.id.scroll);
                    vw.post(new Runnable() {
                        public void run() {
                            vw.fullScroll(View.FOCUS_UP);
                        }
                    });
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
        textView.setText("Prices");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }


}

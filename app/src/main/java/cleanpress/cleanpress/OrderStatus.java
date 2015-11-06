package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

//Made by Vanessa
public class OrderStatus extends ActionBarActivity {

    TextView tWash, tBag, tDry, tFee, tSub, tTotal;
    TextView iWash, iDry;
    TextView pWash, pDry, pFee, pSub, pTotal;
    TextView titleStatus, oNumber;
    Button gBack;
    ImageView showDown;
    Typeface segoe;
    long order;
    String numberdry;
    int nBags, nDry = 0;
    double sBag, sDry, mSub, mFee, sSub, sTot;
    ListView statusList;
    List<ListViewOrder> stuffs;
    ParseUser user = ParseUser.getCurrentUser();
    String username = user.getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_status);
        addMenu();

        segoe = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");

        titleStatus = (TextView) findViewById(R.id.titleStatus);
        oNumber = (TextView) findViewById(R.id.orderNumber);

        tWash = (TextView) findViewById(R.id.textWash);
        tBag = (TextView) findViewById(R.id.textBags);
        tDry = (TextView) findViewById(R.id.textDry);
        tFee = (TextView) findViewById(R.id.textFee);
        tSub = (TextView) findViewById(R.id.textSub);
        tTotal = (TextView) findViewById(R.id.textTotal);

        iWash = (TextView) findViewById(R.id.bagsIcon);
        iDry = (TextView) findViewById(R.id.bagsIcon2);

        pWash = (TextView) findViewById(R.id.priceWash);
        pDry = (TextView) findViewById(R.id.priceDry);
        pFee = (TextView) findViewById(R.id.priceFee);
        pSub = (TextView) findViewById(R.id.priceSub);
        pTotal = (TextView) findViewById(R.id.priceTotal);

        gBack = (Button) findViewById(R.id.buttonBack);

        statusList = (ListView) findViewById(R.id.list_status);
        stuffs = new ArrayList<ListViewOrder>();

        showDown = (ImageView) findViewById(R.id.show);

        titleStatus.setTypeface(segoe);
        oNumber.setTypeface(segoe);
        tWash.setTypeface(segoe);
        tBag.setTypeface(segoe);
        tDry.setTypeface(segoe);
        tFee.setTypeface(segoe);
        tSub.setTypeface(segoe);
        tTotal.setTypeface(segoe);
        iWash.setTypeface(segoe);
        iDry.setTypeface(segoe);
        pWash.setTypeface(segoe);
        pDry.setTypeface(segoe);
        pFee.setTypeface(segoe);
        pSub.setTypeface(segoe);
        pTotal.setTypeface(segoe);
        gBack.setTypeface(segoe);

        AdapterStatus adapter = new AdapterStatus(OrderStatus.this,stuffs);
        statusList.setAdapter(adapter);

        final DecimalFormat df;

        df = new DecimalFormat("#,###.00");
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setMinimumIntegerDigits(1);

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');

        df.setDecimalFormatSymbols(formatSymbols);

        ParseQuery<ParseObject> number = new ParseQuery<ParseObject>("TrackingStatus");
        number.whereEqualTo("Client", ParseUser.getCurrentUser().getUsername());
        number.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {

                    order = parseObject.getLong("OrderNumber");
                    oNumber.setText("No.   " + order);

                    ParseQuery<ParseObject> wash = new ParseQuery<ParseObject>("Order");
                    wash.whereEqualTo("OrderNumber", order).whereEqualTo("WashFold", true);
                    wash.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject num, ParseException e) {
                            if (e == null) {

                                iWash.setText("" + num.getInt("W_F_Bags"));
                                nBags = num.getInt("W_F_Bags");
                                sBag = nBags * 3.5;
                                pWash.setText("$"+df.format(sBag));

                                final ArrayList<ProductItem> productList = new ArrayList<ProductItem> ();

                                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Prices");
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {
                                        if (e==null){

                                            for (ParseObject item : list) {

                                                ProductItem product = new ProductItem(item.getInt("Piece_Code"), item.getString("Piece_Name"), item.getDouble("Price"));
                                                productList.add(product);
                                            }

                                            ParseQuery<ParseObject> dry = new ParseQuery<ParseObject>("Order");
                                            dry.whereEqualTo("OrderNumber", order).whereEqualTo("DryCleaning", true);
                                            dry.getFirstInBackground(new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject numdry, ParseException e) {
                                                    if (e == null) {

                                                        numberdry = numdry.getString("Dry_Cleaning_Pieces");

                                                        String [] code = numberdry.split(",");


                                                        switch (code.length){

                                                            case 0:
                                                                break;

                                                            case 1:
                                                                ViewGroup.LayoutParams params0 = statusList.getLayoutParams();
                                                                params0.height = 155;
                                                                statusList.setLayoutParams(params0);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 2:
                                                                ViewGroup.LayoutParams params1 = statusList.getLayoutParams();
                                                                params1.height = 310;
                                                                statusList.setLayoutParams(params1);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 3:
                                                                ViewGroup.LayoutParams params2 = statusList.getLayoutParams();
                                                                params2.height = 465;
                                                                statusList.setLayoutParams(params2);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 4:
                                                                ViewGroup.LayoutParams params3 = statusList.getLayoutParams();
                                                                params3.height = 620;
                                                                statusList.setLayoutParams(params3);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 5:
                                                                ViewGroup.LayoutParams params4 = statusList.getLayoutParams();
                                                                params4.height = 775;
                                                                statusList.setLayoutParams(params4);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 6:
                                                                ViewGroup.LayoutParams params5 = statusList.getLayoutParams();
                                                                params5.height = 930;
                                                                statusList.setLayoutParams(params5);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 7:
                                                                ViewGroup.LayoutParams params6 = statusList.getLayoutParams();
                                                                params6.height = 1085;
                                                                statusList.setLayoutParams(params6);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 8:
                                                                ViewGroup.LayoutParams params7 = statusList.getLayoutParams();
                                                                params7.height = 1240;
                                                                statusList.setLayoutParams(params7);
                                                                statusList.requestLayout();
                                                                break;

                                                            case 9:
                                                                ViewGroup.LayoutParams params8 = statusList.getLayoutParams();
                                                                params8.height = 1395;
                                                                statusList.setLayoutParams(params8);
                                                                statusList.requestLayout();
                                                                break;

                                                        }

                                                        for(int i = 0; i < code.length; i++) {

                                                            int id = Integer.parseInt(code[i].substring(0, 3));
                                                            final int quantity = Integer.parseInt(code[i].substring(4));

                                                            nDry += quantity;

                                                            for( final ProductItem product : productList) {

                                                                if(id == product.codes) {

                                                                    stuffs.add(new ListViewOrder(){{

                                                                        garment = product.name;
                                                                        piece = String.valueOf(quantity);
                                                                        prices = String.valueOf(df.format(product.price));

                                                                    }
                                                                    });

                                                                    sDry += product.price;

                                                                    break;
                                                                }
                                                            }
                                                        }

                                                        iDry.setText(nDry+"");

                                                        pDry.setText("$" + df.format(sDry));

                                                        mSub = sBag + sDry;

                                                        if (mSub < 25){

                                                            mFee = 3.00;
                                                            pFee.setText("$3.00");

                                                        }else{

                                                            mFee = 0.00;
                                                            pFee.setText("$0.00");
                                                        };

                                                        sSub = sBag + sDry;
                                                        pSub.setText("$"+df.format(sSub));

                                                        sTot = sSub + mFee;
                                                        pTotal.setText("$"+df.format(sTot));

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });



        gBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderStatus.this, MyOrders.class);
                startActivity(i);
            }
        });

        showDown.setImageResource(R.drawable.arrow_blue_down);
        showDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statusList.isShown()){

                    statusList.setVisibility(View.GONE);
                    showDown.setImageResource(R.drawable.arrow_blue_down);

                }
                else {

                    statusList.setVisibility(View.VISIBLE);
                    showDown.setImageResource(R.drawable.arrow_blue_up);

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
        textView.setText("My Orders");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

    public class ListViewOrder{
        public String garment;
        public String piece;
        public String prices;
    }
}

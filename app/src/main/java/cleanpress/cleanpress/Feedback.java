package cleanpress.cleanpress;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Feedback extends ActionBarActivity {

    EditText edtReview;
    String rev_cont,mDay,pDay,currentUser;
    Button sendReview,myReview;
    ListView listView;
    List<ListViewItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_screen);

        addMenu();

        currentUser = ParseUser.getCurrentUser().getUsername();
        edtReview = (EditText) findViewById(R.id.review_content);
        sendReview = (Button) findViewById(R.id.btn_sub_review);
        myReview = (Button) findViewById(R.id.my_review);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Typeface segoe = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");

        edtReview.setTypeface(segoe);
        sendReview.setTypeface(segoe);
        myReview.setTypeface(segoe);

        mDay = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

        edtReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendReview.setEnabled(!edtReview.getText().toString().trim().isEmpty());
                if(sendReview.isEnabled()){
                    sendReview.setBackgroundColor(Color.rgb(127, 230, 139));
                    sendReview.setTextColor(Color.rgb(255, 255, 255));
                }
                else{
                    sendReview.setBackgroundResource(R.drawable.address_button);
                    sendReview.setTextColor(Color.rgb(175,175,175));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        listView = (ListView) findViewById(R.id.list_review);

        items = new ArrayList<ListViewItem>();



                    ParseQuery<ParseObject> reviewsOther = new ParseQuery<ParseObject>("FeedBack");
                    reviewsOther.whereEqualTo("username", currentUser).orderByDescending("createdAt").setLimit(5);
                    reviewsOther.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                for (final ParseObject result : list) {

                                    pDay = result.getString("Today");

                                    try {
                                        final SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                                        final Date one;
                                        one = date.parse(pDay);
                                        items.add(new ListViewItem() {
                                            {
                                                review = result.getString("Review");
                                                day = pDay;
                                                daily = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(one);
                                                rtext = "Read More";
                                                answer = result.getString("Answer");
                                            }

                                        });
                                        listView.invalidateViews();
                                        listView.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                listView.smoothScrollToPosition(0);
                                            }
                                        });

                                    } catch (java.text.ParseException e1) {
                                        e1.printStackTrace();
                                    }


                                }
                            }
                        }
                    });


        ItemAdapter adapter = new ItemAdapter(Feedback.this,items);
        listView.setAdapter(adapter);


    }

    public void myReview (View view){
        if (listView.isShown()){
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
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
        textView.setText("Feedback");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

    public void sendReview (View view){
        rev_cont = edtReview.getText().toString();
        //final List<ListViewItem> items = new ArrayList<Feedback.ListViewItem>();

        Calendar cal = Calendar.getInstance();
        final Date one = cal.getTime();

        ParseObject review_user = new ParseObject("FeedBack");
        review_user.put("username", currentUser);
        review_user.put("Review", rev_cont);
        review_user.put("Today", mDay);

        review_user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    items.add(0, new ListViewItem() {
                        {
                            review = rev_cont;
                            day = mDay;
                            daily = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(one);
                            rtext = "Read More";
                        }

                    });
                    listView.invalidateViews();
                    edtReview.setText("");

                }
            }
        });
    }

    public class ListViewItem {
        public String review;
        public String day;
        public String daily;
        public String rtext;
        public String answer;

    }

}

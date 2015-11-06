package cleanpress.cleanpress;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cleanpress.cleanpress.Feedback;
import cleanpress.cleanpress.R;

//Made by Vanessa

public class ItemAdapter  extends BaseAdapter {
    LayoutInflater inflater;
    List<Feedback.ListViewItem> items;

    public ItemAdapter(Activity context, List<Feedback.ListViewItem> items) {
        super();
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Feedback.ListViewItem item = items.get(position);

        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.list_row,null);

        final TextView Review = (TextView) vi.findViewById(R.id.review);
        TextView Day = (TextView) vi.findViewById(R.id.date);
        TextView Daily = (TextView) vi.findViewById(R.id.day);
        final TextView Readmore = (TextView) vi.findViewById(R.id.readmore);
        final int n = Review.getText().toString().length();
        final LinearLayout AnswerLay = (LinearLayout) vi.findViewById(R.id.answerLay);
        final TextView Answer = (TextView) vi.findViewById(R.id.text_answer);
        TextView showAnswer = (TextView) vi.findViewById(R.id.answer);

        Readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AnswerLay.isShown()) {
                    Readmore.setText("Read Less");
                    Review.setMaxLines(Integer.MAX_VALUE);
                    AnswerLay.setVisibility(View.VISIBLE);
                    Readmore.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_green_up,0);

                } else {
                    Readmore.setText("Read More");
                    Review.setMaxLines(3);
                    AnswerLay.setVisibility(View.GONE);
                    Readmore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_green_down, 0);
                }
            }
        });

        Typeface segoe = Typeface.createFromAsset(vi.getContext().getAssets(),"fonts/segoeui.ttf");

        Answer.setTypeface(segoe);
        Day.setTypeface(segoe);
        Daily.setTypeface(segoe);
        Review.setTypeface(segoe);
        Readmore.setTypeface(segoe);
        showAnswer.setTypeface(segoe);


        Answer.setText(item.answer);
        Review.setText(item.review);
        Day.setText(item.day);
        Daily.setText(item.daily);
        Readmore.setText(item.rtext);
        return vi;
    }
}

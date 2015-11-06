package cleanpress.cleanpress;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miguelprogrammer on 8/20/15.
 */
public class AdapterPrices extends BaseAdapter {

    private final ArrayList<String> prices;
    private final ArrayList<Integer> backs;
    private final ArrayList<String> names;
    private final Activity context;

    public AdapterPrices(Activity context, ArrayList<String> prices, ArrayList<Integer> backs, ArrayList<String> names) {
        this.prices = prices;
        this.backs = backs;
        this.context = context;
        this.names = names;
    }

    @Override
    public int getCount() {
        return 9;
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
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.prices_listview, null);
        TextView Prices = (TextView) v.findViewById(R.id.Price);
        TextView Names = (TextView) v.findViewById(R.id.Name);
        ImageView layout = (ImageView) v.findViewById(R.id.ImgBack);

        Typeface segoe = Typeface.createFromAsset(context.getAssets(),"fonts/segoeui.ttf");
        Typeface segoeb = Typeface.createFromAsset(context.getAssets(),"fonts/seguisb.ttf");

        Prices.setTypeface(segoeb);
        Names.setTypeface(segoe);

        Prices.setText(prices.get(position));
        Names.setText(names.get(position));
        layout.setBackgroundResource(backs.get(position));


        return v;
    }
}


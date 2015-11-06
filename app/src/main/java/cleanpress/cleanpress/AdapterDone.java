package cleanpress.cleanpress;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ProgrammerVanessa on 8/31/15.
 */
public class AdapterDone extends BaseAdapter {

    LayoutInflater inflater;
    List<MyOrders.PlaceholderFragment3.ListViewDone> things;

    public AdapterDone(Activity context, List<MyOrders.PlaceholderFragment3.ListViewDone> thing){
        super();
        this.things = thing;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return things.size();
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

        MyOrders.PlaceholderFragment3.ListViewDone thing = things.get(position);

        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.done_list,null);

        final TextView orderNumber, pickTitle, pAddress, pCity, pDay, pDate, pHour, deliTitle, dAddress, dCity, dDay, dDate, dHour;
        final ImageView showmore;
        final LinearLayout deliLayout;
        Typeface segoe;

        segoe = Typeface.createFromAsset(vi.getContext().getAssets(),"fonts/segoeui.ttf");

        orderNumber = (TextView) vi.findViewById(R.id.order);
        pickTitle = (TextView) vi.findViewById(R.id.titlePick);
        pAddress = (TextView) vi.findViewById(R.id.addressPick);
        pCity = (TextView) vi.findViewById(R.id.cityPick);
        pDay = (TextView) vi.findViewById(R.id.dayPick);
        pDate = (TextView) vi.findViewById(R.id.datePick);
        pHour = (TextView) vi.findViewById(R.id.timePick);
        deliTitle = (TextView) vi.findViewById(R.id.titleDeli);
        dAddress = (TextView) vi.findViewById(R.id.addressDeli);
        dCity = (TextView) vi.findViewById(R.id.cityDeli);
        dDay = (TextView) vi.findViewById(R.id.dayDeli);
        dDate = (TextView) vi.findViewById(R.id.dateDeli);
        dHour = (TextView) vi.findViewById(R.id.timeDeli);

        showmore = (ImageView) vi.findViewById(R.id.showmore);

        deliLayout = (LinearLayout) vi.findViewById(R.id.deliLayout);

        orderNumber.setTypeface(segoe);
        pickTitle.setTypeface(segoe);
        pAddress.setTypeface(segoe);
        pCity.setTypeface(segoe);
        pDay.setTypeface(segoe);
        pDate.setTypeface(segoe);
        pHour.setTypeface(segoe);
        deliTitle.setTypeface(segoe);
        dAddress.setTypeface(segoe);
        dCity.setTypeface(segoe);
        dDay.setTypeface(segoe);
        dDate.setTypeface(segoe);
        dHour.setTypeface(segoe);

        showmore.setImageResource(R.drawable.button_expand_grey);
        showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deliLayout.isShown()) {
                    pickTitle.setVisibility(View.GONE);
                    deliLayout.setVisibility(View.GONE);
                    showmore.setImageResource(R.drawable.button_expand_grey);
                } else {
                    pickTitle.setVisibility(View.VISIBLE);
                    deliLayout.setVisibility(View.VISIBLE);
                    showmore.setImageResource(R.drawable.button_reduce_grey);
                }
            }
        });

        orderNumber.setText("No.   "+thing.oNumber+"");
        pickTitle.setText("Pickup Time");
        pAddress.setText(thing.paddress);
        pCity.setText(thing.pcity);
        pDay.setText(thing.pday);
        pDate.setText(thing.pdate);
        pHour.setText(thing.phour);
        deliTitle.setText("Delivery Time");
        dAddress.setText(thing.daddress);
        dCity.setText(thing.dcity);
        dDay.setText(thing.dday);
        dDate.setText(thing.ddate);
        dHour.setText(thing.dhour);

        return vi;
    }
}

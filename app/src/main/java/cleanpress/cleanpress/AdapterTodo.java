package cleanpress.cleanpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by ProgrammerVanessa on 9/1/15.
 */
public class AdapterTodo extends BaseAdapter {

    LayoutInflater inflater;
    List<MyOrders.PlaceholderFragment2.ListViewTodo> items;

    public AdapterTodo(Activity context, List<MyOrders.PlaceholderFragment2.ListViewTodo> items){

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

        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.to_do_list,null);

        final MyOrders.PlaceholderFragment2.ListViewTodo item = items.get(position);

        final TextView torderNumber, tpickTitle, tpAddress, tpCity, tpDay, tpDate, tpHour, tdeliTitle, tdAddress, tdCity, tdDay, tdDate, tdHour;
        final ImageView tshowmore;
        final LinearLayout tdeliLayout, tbuttonsLayout;
        final Button edit, cancel;
        Typeface segoe;
        final ParseUser user = ParseUser.getCurrentUser();
        final String username = user.getUsername();

        segoe = Typeface.createFromAsset(vi.getContext().getAssets(),"fonts/segoeui.ttf");

        torderNumber = (TextView) vi.findViewById(R.id.torder);
        tpickTitle = (TextView) vi.findViewById(R.id.ttitlePick);
        tpAddress = (TextView) vi.findViewById(R.id.taddressPick);
        tpCity = (TextView) vi.findViewById(R.id.tcityPick);
        tpDay = (TextView) vi.findViewById(R.id.tdayPick);
        tpDate = (TextView) vi.findViewById(R.id.tdatePick);
        tpHour = (TextView) vi.findViewById(R.id.ttimePick);
        tdeliTitle = (TextView) vi.findViewById(R.id.ttitleDeli);
        tdAddress = (TextView) vi.findViewById(R.id.taddressDeli);
        tdCity = (TextView) vi.findViewById(R.id.tcityDeli);
        tdDay = (TextView) vi.findViewById(R.id.tdayDeli);
        tdDate = (TextView) vi.findViewById(R.id.tdateDeli);
        tdHour = (TextView) vi.findViewById(R.id.ttimeDeli);

        tshowmore = (ImageView) vi.findViewById(R.id.showmoreblue);

        tdeliLayout = (LinearLayout) vi.findViewById(R.id.tdeliLayout);

        tbuttonsLayout = (LinearLayout) vi.findViewById(R.id.buttonsLayout);

        edit = (Button) vi.findViewById(R.id.editOrder);
        cancel = (Button) vi.findViewById(R.id.cancelOrder);

        torderNumber.setTypeface(segoe);
        tpickTitle.setTypeface(segoe);
        tpAddress.setTypeface(segoe);
        tpCity.setTypeface(segoe);
        tpDay.setTypeface(segoe);
        tpDate.setTypeface(segoe);
        tpHour.setTypeface(segoe);
        tdeliTitle.setTypeface(segoe);
        tdAddress.setTypeface(segoe);
        tdCity.setTypeface(segoe);
        tdDay.setTypeface(segoe);
        tdDate.setTypeface(segoe);
        tdHour.setTypeface(segoe);

        tshowmore.setImageResource(R.drawable.button_expand_grey);
        tshowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tdeliLayout.isShown()) {
                    tdeliLayout.setVisibility(View.GONE);
                    tbuttonsLayout.setVisibility(View.GONE);
                    tpickTitle.setVisibility(View.GONE);
                    tshowmore.setImageResource(R.drawable.button_expand_grey);
                } else {
                    tdeliLayout.setVisibility(View.VISIBLE);
                    tbuttonsLayout.setVisibility(View.VISIBLE);
                    tpickTitle.setVisibility(View.VISIBLE);
                    tshowmore.setImageResource(R.drawable.button_reduce_grey);
                }
            }
        });

        torderNumber.setText("No.   " + item.toNumber + "");
        tpickTitle.setText("Pickup Time");
        tpAddress.setText(item.tpaddress);
        tpCity.setText(item.tpcity);
        tpDay.setText(item.tpday);
        tpDate.setText(item.tpdate);
        tpHour.setText(item.tphour);
        tdeliTitle.setText("Delivery Time");
        tdAddress.setText(item.tdaddress);
        tdCity.setText(item.tdcity);
        tdDay.setText(item.tdday);
        tdDate.setText(item.tddate);
        tdHour.setText(item.tdhour);

        if ((position % 2) == 0) {
            vi.setBackgroundResource(R.color.white);
        }
        else
        {
            vi.setBackgroundResource(R.color.gray);
        }

        final View finalVi = vi;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                ParseQuery<ParseObject> editOrder = new ParseQuery<ParseObject>("Order");
                editOrder.whereEqualTo("username", username).whereEqualTo("OrderNumber", item.toNumber);
                editOrder.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {

                            Intent toDo = new Intent(finalVi.getContext(),EditOrderToDo.class);

                            toDo.putExtra("pAddress",parseObject.getString("PickupAddress"));
                            toDo.putExtra("pZip",parseObject.getString("PickupZipcode"));
                            toDo.putExtra("pUnit",parseObject.getString("PickupUnit"));
                            toDo.putExtra("pSpecial",parseObject.getString("PickupSpecialNotes"));
                            toDo.putExtra("pDate",parseObject.getString("Pickupdate"));
                            toDo.putExtra("pHour",parseObject.getString("PickHour"));

                            toDo.putExtra("dAddress",parseObject.getString("DeliveryAddress"));
                            toDo.putExtra("dZip",parseObject.getString("DeliveryZipcode"));
                            toDo.putExtra("dUnit",parseObject.getString("DeliveryUnit"));
                            toDo.putExtra("dSpecial",parseObject.getString("DeliverySpecialNotes"));
                            toDo.putExtra("dDate",parseObject.getString("Dropoffdate"));
                            toDo.putExtra("dHour",parseObject.getString("Deliveryhour"));

                            toDo.putExtra("oNumber",String.valueOf(item.toNumber));
                            v.getContext().startActivity(toDo);
                        }
                    }
                });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ParseQuery<ParseObject> cancelOrder = new ParseQuery<ParseObject>("Order");
                cancelOrder.whereEqualTo("username", username).whereEqualTo("OrderNumber", item.toNumber);
                cancelOrder.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {

                        if (e == null) {

                            try {

                                parseObject.delete();
                                Toast.makeText(v.getContext(), "Your order has been cancelled", Toast.LENGTH_SHORT).show();
                                v.getContext().startActivity(new Intent(v.getContext(), Dashboard.class));

                            } catch (ParseException e1) {

                                e1.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        return vi;
    }
}

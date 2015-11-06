package cleanpress.cleanpress;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ProgrammerVanessa on 8/26/15.
 */
public class AdapterStatus extends BaseAdapter{

    LayoutInflater inflater;
    List<OrderStatus.ListViewOrder> stuffs;

    public AdapterStatus (Activity context, List<OrderStatus.ListViewOrder> stuff){
        super();
        this.stuffs = stuff;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return stuffs.size();
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

        OrderStatus.ListViewOrder stuff = stuffs.get(position);

        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.status_list,null);

        final TextView tGarment = (TextView) vi.findViewById(R.id.textGarment);
        final TextView iGarment = (TextView) vi.findViewById(R.id.garmentIcon);

        Typeface segoe = Typeface.createFromAsset(vi.getContext().getAssets(),"fonts/segoeui.ttf");

        tGarment.setTypeface(segoe);
        iGarment.setTypeface(segoe);

        tGarment.setText(stuff.garment);
        iGarment.setText(stuff.piece);

        return vi;
    }
}

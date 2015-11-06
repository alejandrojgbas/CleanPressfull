package cleanpress.cleanpress;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tutorial3 extends Fragment {

    Typeface segoe;
    TextView Title,content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial3, container, false);

        segoe = Typeface.createFromAsset(getActivity().getAssets(), "fonts/segoeui.ttf");
        Title = (TextView) view.findViewById(R.id.Title);
        content = (TextView) view.findViewById(R.id.Content);

        Title.setTypeface(segoe);
        content.setTypeface(segoe);
        return view;
    }


}

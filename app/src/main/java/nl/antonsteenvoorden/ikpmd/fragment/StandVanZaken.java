package nl.antonsteenvoorden.ikpmd.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.antonsteenvoorden.ikpmd.R;


public class StandVanZaken extends Fragment {
    @Bind(R.id.section_label)
    TextView textView;

    public StandVanZaken() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StandVanZaken newInstance() {
        StandVanZaken fragment = new StandVanZaken();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vakken, container, false);
        ButterKnife.bind(this, rootView);
        textView.setText("Stand van zaken");
        return rootView;
    }
}

package nl.antonsteenvoorden.ikpmd.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

import nl.antonsteenvoorden.ikpmd.R;
import nl.antonsteenvoorden.ikpmd.orm.Module;


/**
 * Created by Anton on 03/01/2016.
 */
public class VakkenAdapter extends ArrayAdapter {

    public VakkenAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Module module = (Module) getItem(position);

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.vakken_list_item, parent, false);
        }
        TextView listName = (TextView) view.findViewById(R.id.vakken_list_item_name);
        TextView listECTS = (TextView) view.findViewById(R.id.vakken_list_item_ects);
        TextView listGrade = (TextView) view.findViewById(R.id.vakken_list_item_grade);
        listName.setText(module.getName());
        listECTS.setText(Integer.toString(module.getEcts()));
        listGrade.setText(Integer.toString(module.getGrade()));
        return view;
    }
}
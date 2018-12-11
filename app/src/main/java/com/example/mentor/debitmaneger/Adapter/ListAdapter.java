package com.example.mentor.debitmaneger.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mentor.debitmaneger.Model.Planet;
import com.example.mentor.debitmaneger.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Planet> implements Filterable {
    private final List<Planet> orgplanetList;
    private Context mContext;
    private List<Planet> mPlanetlist;
    private PlanetFilter filter;


    public ListAdapter(@NonNull Context mContext, List<Planet> mPlanetlist) {
        super(mContext, R.layout.itemlist);
        this.mContext = mContext;
        this.mPlanetlist = mPlanetlist;
        this.orgplanetList = mPlanetlist;


    }

    @Override
    public int getCount() {
        return mPlanetlist.size();
    }

    @Override
    public Planet getItem(int position) {
        return mPlanetlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPlanetlist.get(position).getId();
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.itemlist, null);
        TextView txtname = (TextView) v.findViewById(R.id.tvname);
        TextView txtamount = (TextView) v.findViewById(R.id.tvamt);


        txtname.setText(mPlanetlist.get(position).getName());
        txtamount.setText(mPlanetlist.get(position).getAmount());



        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PlanetFilter();
        }
        return filter;

    }

    private class PlanetFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = orgplanetList;
                results.values = orgplanetList.size();
            } else {
                String perfixString = constraint.toString().toLowerCase();
                List<Planet> nPlanetList = new ArrayList<Planet>();
                List<Planet> nPlanetListLocal = new ArrayList<Planet>();
                nPlanetListLocal.addAll(orgplanetList);
                final int count = nPlanetListLocal.size();
                for (int i = 0; i < count; i++) {
                    final Planet item = nPlanetListLocal.get(i);
                    final String itemName = item.getName().toLowerCase();
                    if (itemName.contains(perfixString)) {
                        nPlanetList.add(item);
                    }

                }
                results.values = nPlanetList;
                results.count = nPlanetList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraints, FilterResults results) {

            if (results.count == 0)

            {
                notifyDataSetInvalidated();
            } else {
                mPlanetlist = (List<Planet>) results.values;
                notifyDataSetInvalidated();
            }
        }
    }
}




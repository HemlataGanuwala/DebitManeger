package com.example.mentor.debitmaneger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mentor.debitmaneger.Adapter.ListAdapter;
import com.example.mentor.debitmaneger.Model.Planet;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GivenFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    ListView list;
    double total;
    TextView textViewtotamt;
    private List<Planet> mPlanetlist;
    private ListAdapter listAdapter;
    DatabaseHelpher helpher;
    SwipeRefreshLayout refreshLayout;


    public GivenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_given, container, false);
        helpher = new DatabaseHelpher(getContext());

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipefragmentgive);
        refreshLayout.setOnRefreshListener(this);

        list = (ListView)view.findViewById(R.id.list);

        textViewtotamt = (TextView)view.findViewById(R.id.tvtotamt);

        mPlanetlist = helpher.getListgive("GIVEN");
        listAdapter = new ListAdapter(getActivity(), mPlanetlist);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Planet planet1 = listAdapter.getItem(position);
                int ti = 0;

                ti = list.getAdapter().getCount();

                for (int i1 = 0; i1 < ti; i1++)
                {
                    id = (int) planet1.getId();
                }
                Intent intent = new Intent(getActivity(),EditDeleteActivity.class);
                intent.putExtra("a1",id);
                startActivity(intent);
            }
        });

        getTotal();


        return view;
    }

    public void getTotal()
    {
        total = 0.0;
        for(int i=0; i<mPlanetlist.size(); i++){
            total = total +Double.parseDouble(mPlanetlist.get(i).getAmount());
            textViewtotamt.setText(Integer.toString((int) total) );

        }
        //return total;
    }

    @Override
    public void onRefresh() {
        mPlanetlist = helpher.getListgive("GIVEN");
        listAdapter = new ListAdapter(getActivity(), mPlanetlist);
        list.setAdapter(listAdapter);
        refreshLayout.setRefreshing(false);
    }
}

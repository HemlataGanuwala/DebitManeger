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

import com.example.mentor.debitmaneger.Adapter.AdapterTake;
import com.example.mentor.debitmaneger.Model.Planettake;

import java.util.List;


public class TakeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;
    ListView list;
    EditText edit;
    double total;
    TextView textViewtottake;
    private List<Planettake> Planetlist;
    private AdapterTake adapterTake;
    DatabaseHelpher helpher;
    SwipeRefreshLayout refreshLayout;

    public TakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_take, container, false);
        helpher = new DatabaseHelpher(getContext());

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipefragmenttake);
        refreshLayout.setOnRefreshListener(this);
        list = (ListView) view.findViewById(R.id.listtake);
        textViewtottake = (TextView) view.findViewById(R.id.tvtottake);


        Planetlist = helpher.getListtake("TAKEN");
        adapterTake = new AdapterTake(getActivity(),Planetlist);
        list.setAdapter(adapterTake);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Planettake planet1 = adapterTake.getItem(position);
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
        for(int i=0; i<Planetlist.size(); i++){
            total = total +Double.parseDouble(Planetlist.get(i).getAmount1());
            textViewtottake.setText(Integer.toString((int) total) );

        }
        //return total;
    }


    @Override
    public void onRefresh() {
        Planetlist = helpher.getListtake("TAKEN");
        adapterTake = new AdapterTake(getActivity(),Planetlist);
        list.setAdapter(adapterTake);
        refreshLayout.setRefreshing(false);
    }
}

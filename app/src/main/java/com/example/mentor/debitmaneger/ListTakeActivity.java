package com.example.mentor.debitmaneger;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mentor.debitmaneger.Adapter.AdapterTake;
import com.example.mentor.debitmaneger.Model.Planet;
import com.example.mentor.debitmaneger.Model.Planettake;

import java.util.List;

public class ListTakeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView list;

    private List<Planettake> Planetlist;
    private AdapterTake adapterTake;
    DatabaseHelpher helpher;
    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_take);
        helpher = new DatabaseHelpher(this);

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipetakelist);
        refreshLayout.setOnRefreshListener(this);

        list = (ListView) findViewById(R.id.listtake);

        Planetlist = helpher.getListtake("TAKEN");
        adapterTake = new AdapterTake(this,Planetlist);
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
                Intent intent = new Intent(ListTakeActivity.this,EditDeleteActivity.class);
                intent.putExtra("a1",id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        Planetlist = helpher.getListtake("TAKEN");
        adapterTake = new AdapterTake(this,Planetlist);
        list.setAdapter(adapterTake);
        refreshLayout.setRefreshing(false);
    }
}

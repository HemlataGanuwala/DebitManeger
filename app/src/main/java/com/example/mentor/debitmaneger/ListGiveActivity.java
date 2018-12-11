package com.example.mentor.debitmaneger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mentor.debitmaneger.Adapter.ListAdapter;
import com.example.mentor.debitmaneger.Model.Planet;

import java.util.List;

public class ListGiveActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    EditText edit;
    double total;
    TextView textViewtotamt;
    private List<Planet> mPlanetlist;
    private ListAdapter listAdapter;
    DatabaseHelpher helpher;
    private Toolbar toolbar;
    String name,amount,description,dt,duedt,id,type;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_give);

        helpher = new DatabaseHelpher(this);

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipegivelist);
        refreshLayout.setOnRefreshListener(this);

        list = (ListView) findViewById(R.id.list);
        toolbar = (Toolbar)findViewById(R.id.givelisttoolbar);
        textViewtotamt = (TextView)findViewById(R.id.tvtotamt);

        mPlanetlist = helpher.getListgive("GIVEN");
        listAdapter = new ListAdapter(this, mPlanetlist);
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
                Intent intent = new Intent(ListGiveActivity.this,EditDeleteActivity.class);
                intent.putExtra("a1",id);
                startActivity(intent);
            }
        });



        //getTotal();




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

    public void passing()
    {
        Intent intent = new Intent(ListGiveActivity.this,userdashboard.class);
        intent.putExtra("t1",total);
        startActivity(intent);
    }


    @Override
    public void onRefresh() {
        mPlanetlist = helpher.getListgive("GIVEN");
        listAdapter = new ListAdapter(this, mPlanetlist);
        list.setAdapter(listAdapter);
        refreshLayout.setRefreshing(false);
    }
}

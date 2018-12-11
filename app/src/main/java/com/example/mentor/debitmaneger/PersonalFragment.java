package com.example.mentor.debitmaneger;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mentor.debitmaneger.Adapter.AdapterTake;
import com.example.mentor.debitmaneger.Adapter.ListAdapter;
import com.example.mentor.debitmaneger.Adapter.ReportAdapter;
import com.example.mentor.debitmaneger.Model.Planet;
import com.example.mentor.debitmaneger.Model.Planettake;
import com.example.mentor.debitmaneger.Model.RepoetPlanet;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    View view;
    private List<RepoetPlanet> mReportPlanetlist;
    private ReportAdapter reportAdapter;
    DatabaseHelpher helpher;
    ListView listView;
    private List<Planet> mPlanetgivelist;
    private List<Planettake> mPlanettakelist;
    private ListAdapter listAdapter;
    private AdapterTake adapterTake;
    String nameg, amountgive, amounttake,passname;
    Cursor namelist;
    int amountGive,amountTake,totalbal;
    TextView textViewtotpersonal;
    double total,commontot;


    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        helpher = new DatabaseHelpher(getContext());
        listView = (ListView) view.findViewById(R.id.lvreport);
        textViewtotpersonal = (TextView) view.findViewById(R.id.tvtotpersonal);

        filldata();

        mReportPlanetlist = helpher.getcombList();
        reportAdapter = new ReportAdapter(getActivity(), mReportPlanetlist);
        listView.setAdapter(reportAdapter);

        getTotal();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ReportDetailsActivity.class);
                passname = ((TextView)view.findViewById(R.id.tvreportname)).getText().toString();
                intent.putExtra("a1",passname);
                startActivity(intent);
            }
        });
        return view;
    }

    public void filldata() {
        namelist = helpher.filllist();

        helpher.DeleteData();

        if (namelist.moveToFirst()) {

            do {
                nameg = namelist.getString(namelist.getColumnIndex("Name"));
                amountGive = helpher.UsersumDataGive(nameg,"GIVEN");
                amountTake = helpher.UsersumDataTake(nameg,"TAKEN");
                totalbal = amountGive - amountTake;

                helpher.UserReportData(nameg,amountGive,amountTake,totalbal);


            }
            while (namelist.moveToNext());


        }


    }

    public void getTotal()
    {
        total = 0.0;
        for(int i=0; i<mReportPlanetlist.size(); i++){
            commontot = Double.parseDouble(mReportPlanetlist.get(i).getGiveAmount()) - Double.parseDouble(mReportPlanetlist.get(i).getTakeAmount());
            total = total + commontot;
            textViewtotpersonal.setText(Integer.toString((int) total) );

        }
        //return total;
    }

}

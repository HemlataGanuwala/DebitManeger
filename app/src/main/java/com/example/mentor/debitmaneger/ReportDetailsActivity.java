package com.example.mentor.debitmaneger;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mentor.debitmaneger.Adapter.ReportDetailsAdapter;
import com.example.mentor.debitmaneger.Model.ReportDetailsPlanet;

import java.util.List;

public class ReportDetailsActivity extends AppCompatActivity {

    private List<ReportDetailsPlanet> mReportPlanetDetaillist;
    private ReportDetailsAdapter reportAdapter;
    DatabaseHelpher helpher;
    ListView listView;
    Cursor namelist;
    String nameg,contactname;
    TextView textViewdetailtotamt;
    double takentotal,giventotal,commontot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        helpher = new DatabaseHelpher(this);
        listView = (ListView) findViewById(R.id.lvreportdetails);
        textViewdetailtotamt = (TextView) findViewById(R.id.tvdetailtotamt);

       Display();

        mReportPlanetDetaillist = helpher.getReportDetailList(contactname);
        reportAdapter = new ReportDetailsAdapter(this, mReportPlanetDetaillist);
        listView.setAdapter(reportAdapter);

        getTotal();
    }



    public void Display()
    {
        Intent intent = getIntent();
        Bundle bg1 = intent.getExtras();
        if(bg1!=null) {
            contactname = (String) bg1.get("a1");
        }
    }

    public void getTotal()
    {
        commontot = 0.0;
        takentotal = 0;
        giventotal = 0;
        for(int i=0; i<mReportPlanetDetaillist.size(); i++){


            String tp = mReportPlanetDetaillist.get(i).getTypeDetail();
            if(tp.equals("TAKEN"))
            {
                takentotal = takentotal +Double.parseDouble(mReportPlanetDetaillist.get(i).getAmount());
            }
            else
            {
                giventotal = giventotal +Double.parseDouble(mReportPlanetDetaillist.get(i).getAmount());
            }


        }
        commontot = (giventotal - takentotal);
        textViewdetailtotamt.setText(Integer.toString((int) commontot) );
        //return total;
    }
}

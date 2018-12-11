package com.example.mentor.debitmaneger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class userdashboard extends AppCompatActivity {

    Button btnreport,btngive,btntake,buttonbackup,buttonimport;
    TextView txtmoney,textViewtotgive,textViewtottake;
    DatabaseHelpher helpher;
    int totaltake,totalgive,totaldata;

    public static final int REQUEST_CODE_SIGN_IN = 1;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;
    public static final int REQUEST_CODE_PERMISSIONS = 2;

    private boolean isBackup = true;

    private userdashboard activity;

    private RemoteBackup remoteBackup;
//    private LocalBackup localBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_userdashboard);
        helpher = new DatabaseHelpher(this);

        remoteBackup = new RemoteBackup(this);

        txtmoney = (TextView)findViewById(R.id.txtmoney);
        textViewtotgive = (TextView)findViewById(R.id.tvtotalgive);
        textViewtottake = (TextView)findViewById(R.id.tvtotaltake);
        btnreport=(Button)findViewById(R.id.btnre);
        btngive=(Button)findViewById(R.id.btngive);
        btntake=(Button)findViewById(R.id.btntake);
        buttonbackup=(Button)findViewById(R.id.btnbackup);
        buttonimport=(Button)findViewById(R.id.btnimport);

        buttonimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackup = false;
                remoteBackup.connectToDrive(isBackup);
            }
        });

        buttonbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackup = true;
                remoteBackup.connectToDrive(isBackup);
            }
        });

        btngive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent give=new Intent(userdashboard.this,GivemoneyActivity.class);
                startActivity(give);

            }
        });

        btntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent take=new Intent(userdashboard.this,TakeMoneyActivity.class);
                startActivity(take);

            }
        });


        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent report =new Intent(userdashboard.this,ReportActivity.class);
                startActivity(report);

            }
        });

        getsumdata();
        getsumdatagive();
        getsumdatatotal();
        helpher.closeDB();
    }

    public void getsumdata()
    {
        totalgive = helpher.sumData("GIVEN");
        textViewtotgive.setText(String.valueOf(totalgive));
    }

    public void getsumdatagive()
    {
        totaltake = helpher.sumDatagive("TAKEN");
        textViewtottake.setText(String.valueOf(totaltake));
    }

    public void getsumdatatotal()
    {
        totaldata = totalgive - totaltake;
        txtmoney.setText(String.valueOf(totaldata));
    }


}

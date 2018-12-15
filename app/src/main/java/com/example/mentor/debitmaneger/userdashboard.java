package com.example.mentor.debitmaneger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class userdashboard extends AppCompatActivity {

    Button btnreport,btngive,btntake;
    TextView txtmoney,textViewtotgive,textViewtottake;
    DatabaseHelpher helpher;
    int totaltake,totalgive,totaldata;
    ImageView imageViewsetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdashboard);

        helpher = new DatabaseHelpher(this);

        txtmoney = (TextView)findViewById(R.id.txtmoney);
        textViewtotgive = (TextView)findViewById(R.id.tvtotalgive);
        textViewtottake = (TextView)findViewById(R.id.tvtotaltake);
        btnreport=(Button)findViewById(R.id.btnre);
        btngive=(Button)findViewById(R.id.btngive);
        btntake=(Button)findViewById(R.id.btntake);
        imageViewsetting=(ImageView)findViewById(R.id.imgbackup);

        imageViewsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userdashboard.this,BackupActivity.class);
                startActivity(intent);
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

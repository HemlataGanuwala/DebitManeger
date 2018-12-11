package com.example.mentor.debitmaneger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentor.debitmaneger.Model.Planet;

public class EditDeleteActivity extends AppCompatActivity {

    String gname,gamount,gdescription,gdt,duedt,type;
    EditText editTextname,editTextamount,editTextdescription;
    TextView textViewdt,textViewduedt,textViewid,textViewtype;
    private Toolbar toolbar;
    DatabaseHelpher databaseHelpher;
    Button buttonedit,buttondelete;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        databaseHelpher = new DatabaseHelpher(this);
        toolbar = (Toolbar)findViewById(R.id.givelisttoolbar);
        setSupportActionBar(toolbar);
        editTextname = (EditText)findViewById(R.id.edteditname);
        editTextamount = (EditText)findViewById(R.id.edteditamount);
        editTextdescription = (EditText)findViewById(R.id.edteditdes);
        textViewdt = (TextView)findViewById(R.id.tveditcdate);
        textViewduedt = (TextView)findViewById(R.id.tveditduedate);
        textViewid = (TextView)findViewById(R.id.tveditduedate);
        textViewtype = (TextView)findViewById(R.id.tvedittype);
        buttonedit = (Button) findViewById(R.id.btnedit);
        buttondelete = (Button)findViewById(R.id.btndelete);

        Display();

        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = databaseHelpher.UpdateGiveTakeData(String.valueOf(id),editTextname.getText().toString(),editTextamount.getText().toString(),editTextdescription.getText().toString(),textViewdt.getText().toString(),textViewduedt.getText().toString(),textViewtype.getText().toString());
                if (isUpdated == true)
                {
                    Toast.makeText(EditDeleteActivity.this,"Edit Successfully",Toast.LENGTH_LONG).show();
                    editTextname.setText("");
                    editTextamount.setText("");
                    editTextdescription.setText("");
                    textViewdt.setText("");
                    textViewduedt.setText("");
                    textViewtype.setText("");
                }
                else
                {
                    Toast.makeText(EditDeleteActivity.this,"Edit Failed",Toast.LENGTH_LONG).show();
                }
            }
        });

        buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer isDeleted = databaseHelpher.DeleteData(String.valueOf(id));
                if (isDeleted == 0)
                {
                    Toast.makeText(EditDeleteActivity.this,"Delete Successfully",Toast.LENGTH_LONG).show();
                    editTextname.setText("");
                    editTextamount.setText("");
                    editTextdescription.setText("");
                    textViewdt.setText("");
                    textViewduedt.setText("");
                    textViewtype.setText("");

                }
                else
                {
                    Toast.makeText(EditDeleteActivity.this,"Delete Failed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void Display()
    {
        Intent inn= getIntent();
        Bundle bg1 = inn.getExtras();
        if(bg1!=null) {
            id = (long) bg1.get("a1");

            Planet product = databaseHelpher.getProductById((int) id);
            textViewid.setText(String.valueOf(product.getId()));
            editTextname.setText(product.getName());
            editTextamount.setText(product.getAmount());
            editTextdescription.setText(product.getDescription());
            textViewdt.setText(product.getGDate());
            textViewduedt.setText(product.getDueDate());
            textViewtype.setText(product.getGiveType());

        }



    }


}

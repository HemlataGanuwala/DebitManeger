package com.example.mentor.debitmaneger;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TakeMoneyActivity extends AppCompatActivity {

    private static final int RQS_PICK_CONTACT = 1;
    private static final int PERMISSION_REQUEST_CONTACT = 1;
    DatabaseHelpher helpher;
    EditText editTextname, editTextamount, editTextdescription;
    TextView textViewcdate,textViewduedate;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    Button buttoncdate,buttonduedate, btnsubmit1;
    private int year, month, day,yeardue,monthdue,daydue;
    ImageView imageView,imageViewtakecont;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DatePickerDialog.OnDateSetListener dateSetListenerdue;

    //contact list
    //private static final int RESULT_PICK_CONTACT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_money);
        helpher = new DatabaseHelpher(this);

        buttoncdate = (Button) findViewById(R.id.btnsetdatetake);
        buttonduedate = (Button) findViewById(R.id.btnsetduedatetake);
        btnsubmit1 = (Button) findViewById(R.id.btnsub1);
        editTextname = (EditText) findViewById(R.id.edtname);
        editTextamount = (EditText) findViewById(R.id.edtamount);
        editTextdescription = (EditText) findViewById(R.id.edtdes);
        textViewcdate = (TextView) findViewById(R.id.tvcdatetake);
        textViewduedate = (TextView) findViewById(R.id.tvduedatetake);

        imageView =(ImageView)findViewById(R.id.imageView);
        imageViewtakecont = (ImageView)findViewById(R.id.imgtakecontact);
//        Display();

        imageViewtakecont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askForContactPermission();
////                startActivity(new Intent(TakeMoneyActivity.this,ContactActivity.class));
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//                startActivityForResult(intent, 1);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img = new Intent(TakeMoneyActivity.this,ListTakeActivity.class);
                startActivity(img);
            }
        });

        buttoncdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TakeMoneyActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                textViewcdate.setText(date);
            }
        };

        buttonduedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                yeardue = calendar.get(Calendar.YEAR);
                monthdue = calendar.get(Calendar.MONTH);
                daydue = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TakeMoneyActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListenerdue,yeardue,monthdue,daydue);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListenerdue = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                monthdue = monthdue + 1;
                String datedue = daydue + "/" + monthdue + "/" + yeardue;
                textViewduedate.setText(datedue);
            }
        };

        btnsubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = editTextname.getText().toString();
                int Amount = Integer.parseInt(editTextamount.getText().toString());
                String Description = editTextdescription.getText().toString();
                String Cdate = textViewcdate.getText().toString();
                String DueDate = textViewduedate.getText().toString();
                String Type = "TAKEN";

                if (Name.length() !=0 && Amount != 0) {
                    InsertData(Name,Amount, Description,Cdate,DueDate,Type);

                    editTextname.setText("");
                    editTextamount.setText("");
                    editTextdescription.setText("");
                    textViewcdate.setText("");
                    textViewduedate.setText("");
                    Intent intent1 = new Intent(TakeMoneyActivity.this, userdashboard.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(TakeMoneyActivity.this, "Enter Blank Feild", Toast.LENGTH_LONG).show();
                }


            }

        });
    }

    private void getContact(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, RQS_PICK_CONTACT);
    }

    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(TakeMoneyActivity.this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(TakeMoneyActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TakeMoneyActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(TakeMoneyActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                getContact();
            }
        }
        else{
            getContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //ToastMaster.showMessage(GivemoneyActivity.this,"No permission for contacts");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RQS_PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor =  managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                editTextname.setText(name);
                //name.setText(number);
                //contactEmail.setText(email);
            }
        }
    }

    private void InsertData (String name, int amount, String description,String cdate,String duedate,String type)
    {
        boolean isInserted = helpher.GiveTakeData(name,amount,description, cdate, duedate,type);
        if (isInserted == true) {

            Toast.makeText(TakeMoneyActivity.this, "Submit Successfully", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(TakeMoneyActivity.this, "Submit Failed", Toast.LENGTH_LONG).show();
        }


    }

    public void Display()
    {
        Intent intent = getIntent();
        Bundle bg1 = intent.getExtras();
        if(bg1!=null) {
            String contactname = (String) bg1.get("a1");
            editTextname.setText(contactname);
        }
    }


}
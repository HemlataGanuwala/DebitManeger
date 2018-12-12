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
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class GivemoneyActivity extends FragmentActivity {


    private static final int RQS_PICK_CONTACT = 1;
    //private static final int PICK_CONTACT = 1;
    private static final int PERMISSION_REQUEST_CONTACT = 1;
    EditText editTextamount,editTextdescription;
    AutoCompleteTextView edittextname;

    ImageView imageView,img,imageViewcontact;
    Button buttoncdate,buttonduedate,btnsubmit;

    TextView textViewcdate,textViewduedate;
    DatabaseHelpher helpher;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DatePickerDialog.OnDateSetListener dateSetListenerdue;
    int year,month,day,yeardue,monthdue,daydue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_givemoney);

        helpher=new DatabaseHelpher(this);

        buttoncdate = (Button) findViewById(R.id.btncdate);
        buttonduedate = (Button) findViewById(R.id.btnduedate);
        btnsubmit = (Button) findViewById(R.id.btnsub);

        textViewduedate = (TextView) findViewById(R.id.tvgiveduedate);
        imageView = (ImageView)findViewById(R.id.image);
        img =(ImageView)findViewById(R.id.imgstatus);
        edittextname = (AutoCompleteTextView) findViewById(R.id.etname);
        editTextamount = (EditText) findViewById(R.id.etgiveamount);
        editTextdescription = (EditText) findViewById(R.id.etgivedes);
        imageViewcontact = (ImageView)findViewById(R.id.imgcontact);
        textViewcdate = (TextView)findViewById(R.id.tvgivecdate);

//        final String[] AndroidDesk= helpher.getAllLabels();
        List<String> lables = helpher.getAllLabels();
        ArrayAdapter<String> My_arr_adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,lables);
        edittextname = (AutoCompleteTextView) findViewById(R.id.etname);
        edittextname.setThreshold(2);
        edittextname.setAdapter(My_arr_adapter);
//        Display();

        imageViewcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    askForContactPermission();
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//                startActivityForResult(intent, 1);
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(GivemoneyActivity.this,ListGiveActivity.class);
                startActivity(i);
            }
        });

        buttoncdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(GivemoneyActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year,month,day);
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

                DatePickerDialog dialog = new DatePickerDialog(GivemoneyActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListenerdue,yeardue,monthdue,daydue);
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


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username=edittextname.getText().toString();
                int Amount=Integer.parseInt(editTextamount.getText().toString());
                String Description=editTextdescription.getText().toString();
                String Cdate=textViewcdate.getText().toString();
                String Duedate=textViewduedate.getText().toString();
                String Type="GIVEN";

                if(Username.length() != 0 && Amount !=0)
                {
                    InsertData(Username,Amount,Description,Cdate,Duedate,Type);

                    edittextname.setText("");
                    editTextamount.setText("");
                    editTextdescription.setText("");
                    textViewcdate.setText("");
                    textViewduedate.setText("");
                    Intent intent1 = new Intent(GivemoneyActivity.this, userdashboard.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(GivemoneyActivity.this, "Enter Blank Feild", Toast.LENGTH_LONG).show();
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
            if (ContextCompat.checkSelfPermission(GivemoneyActivity.this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(GivemoneyActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GivemoneyActivity.this);
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

                    ActivityCompat.requestPermissions(GivemoneyActivity.this,
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

                edittextname.setText(name);
                //name.setText(number);
                //contactEmail.setText(email);
            }
        }
    }


    private void InsertData(String name, int amount, String description,String cdate,String duedate,String type) {
        boolean isInserted = helpher.GiveTakeData(name, amount, description,cdate,duedate,type);

        Cursor cursor = helpher.fillname(name);
        if (cursor.getCount() > 0)
        {
            Toast.makeText(GivemoneyActivity.this, "User Already Exist", Toast.LENGTH_LONG).show();
        }
        else
        {
            helpher.AddUserName(name);
        }


        if (isInserted == true) {
            Toast.makeText(GivemoneyActivity.this, "Submit Successfully", Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(GivemoneyActivity.this, "Submit Failed", Toast.LENGTH_LONG).show();
        }

    }

//    public void Display()
//    {
//        Intent intent = getIntent();
//        Bundle bg1 = intent.getExtras();
//        if(bg1!=null) {
//            String contactname = (String) bg1.get("a1");
//            editTextname.setText(contactname);
//        }
//    }
}



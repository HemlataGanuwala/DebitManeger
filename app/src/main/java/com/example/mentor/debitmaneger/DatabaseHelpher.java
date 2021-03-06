package com.example.mentor.debitmaneger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.example.mentor.debitmaneger.Model.Planet;
import com.example.mentor.debitmaneger.Model.Planettake;
import com.example.mentor.debitmaneger.Model.RepoetPlanet;
import com.example.mentor.debitmaneger.Model.ReportDetailsPlanet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelpher extends SQLiteOpenHelper {

public static final String DATABSE_NAME= "DebitManeger.db";
public static final String DBLOCATIONBACKUP = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ "Backup";
public static final String DBLOCATION = Environment.getExternalStorageDirectory().getAbsolutePath();


    //private final String MY_QUERY = "SELECT Amount,Amount1 FROM GIVEMONEY a INNER JOIN TAKEMONEY b ON a.Id=b.Id WHERE a.Nameg=?";
//GiveMoney Table

    public static final String TABLE_GIVETAKE="GIVETAKE";
    public static final String COL0_GIVETAKE="Id";
    public static final String COL1_GIVETAKE="Name";
    public static final String COL2_GIVETAKE="Amount";
    public static final String COL3_GIVETAKE="Description";
    public static final String COL4_GIVETAKE="CDate";
    public static final String COL5_GIVETAKE="DueDate";
    public static final String COL6_GIVETAKE="Type";
    public static final String COL7_GIVETAKE="Image";

    //Userreport Table
    public static final String TABLE_UserReport="USERREPORT";
    public static final String COL0_UserReport="User_Id";
    public static final String COL1_UserReport="User_Name";
    public static final String COL2_UserReport="User_GiveAmount";
    public static final String COL3_UserReport="User_TakeAmount";
    public static final String COL4_UserReport="User_Balance";

    //Add Name
    public static final String TABLE_Add_User_Name="ADDUSERNAME";
    public static final String COL0_Add_User_Name="User_Id";
    public static final String COL1_Add_User_Name="User_Name";


    public DatabaseHelpher(Context context) {
        super(context, DATABSE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table " +TABLE_GIVETAKE+ "(Id Integer  primary key autoincrement, Name Text ,Amount int, Description Text,CDate Text,DueDate Text,Type Text,Image blob)");
    db.execSQL(" create table " +TABLE_UserReport+ "(User_Id Integer primary key autoincrement, User_Name Text, User_GiveAmount Text, User_TakeAmount Text,User_Balance Text)");
    db.execSQL(" create table " +TABLE_Add_User_Name+ "(User_Id Integer primary key autoincrement, User_Name Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE " +TABLE_GIVETAKE);
        db.execSQL(" DROP TABLE " +TABLE_UserReport);
        db.execSQL(" DROP TABLE " +TABLE_Add_User_Name);
    }


public Boolean GiveTakeData(String name,int amount,String des,String cdate,String duedate,String type,byte image)
{
    SQLiteDatabase db= getWritableDatabase();

    ContentValues contentValues=new ContentValues();

    contentValues.put(COL1_GIVETAKE,name);
    contentValues.put(COL2_GIVETAKE,amount);
    contentValues.put(COL3_GIVETAKE,des);
    contentValues.put(COL4_GIVETAKE,cdate);
    contentValues.put(COL5_GIVETAKE,duedate);
    contentValues.put(COL6_GIVETAKE,type);
    contentValues.put(COL6_GIVETAKE,image);

    long result = db.insert(TABLE_GIVETAKE, null, contentValues);

    if (result == -1) {
        return false;
    } else {
        return true;
    }
}

    public Boolean AddUserName(String name)
    {
        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL1_Add_User_Name,name);

        long result = db.insert(TABLE_Add_User_Name, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor fillname(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from ADDUSERNAME where User_Name = ?",new String[]{name});
        return cursor;

    }

    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Add_User_Name;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();


        // returning lables
        return labels;
    }

    public Integer DeleteData(String id)
    {
        SQLiteDatabase db= getWritableDatabase();

        db.delete(TABLE_GIVETAKE,"Id = ?",new String[]{id});

        return 0;
    }


    public Boolean UpdateGiveTakeData(String id, String name, String amount, String des, String cdate, String duedate, String type)
    {
        SQLiteDatabase db= getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(COL0_GIVETAKE,id);
        contentValues.put(COL1_GIVETAKE,name);
        contentValues.put(COL2_GIVETAKE,amount);
        contentValues.put(COL3_GIVETAKE,des);
        contentValues.put(COL4_GIVETAKE,cdate);
        contentValues.put(COL5_GIVETAKE,duedate);
        contentValues.put(COL6_GIVETAKE,type);

        db.update(TABLE_GIVETAKE, contentValues, "Id = ?", new String[]{id});

       return true;
    }

    public Planet getProductById(int id) {
         SQLiteDatabase db = this.getWritableDatabase();

        Planet planet = null;
        Cursor cursor = db.rawQuery("select Id,Name,Amount,Description,CDate,DueDate,Type from GIVETAKE Where Id = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        planet = new Planet(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        cursor.close();

        return planet;
    }


    public Boolean UserReportData(String name,int giveamount, int takeamt, int balance)
    {
        SQLiteDatabase db= getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put(COL1_UserReport,name);
        contentValues.put(COL2_UserReport,giveamount);
        contentValues.put(COL3_UserReport,takeamt);
        contentValues.put(COL4_UserReport,balance);


        long result = db.insert(TABLE_UserReport, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


//Planet List GiveMoney

    public List<Planet> getListgive(String type) {
        SQLiteDatabase db = getWritableDatabase();

        Planet planet = null;
        List<Planet>  mPlanetlis = new ArrayList<>();
        Cursor cursor = db.rawQuery("select Id,Name,Amount,Description,CDate,DueDate,Type from GIVETAKE Where Type = ?", new String[]{type});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            planet = new Planet(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
            mPlanetlis.add(planet);
            cursor.moveToNext();
        }

        cursor.close();

        return  mPlanetlis;

    }

    public List<Planettake> getListtake(String type) {
        SQLiteDatabase db = getWritableDatabase();

        Planettake planet = null;
        List<Planettake>  mPlanetlis = new ArrayList<>();
        Cursor cursor = db.rawQuery("select Id,Name,Amount,Description,CDate,DueDate,Type from GIVETAKE Where Type = ?", new String[]{type});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            planet = new Planettake(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getInt(7));
            mPlanetlis.add(planet);
            cursor.moveToNext();
        }

        cursor.close();

        return  mPlanetlis;

    }
// Combine List
    public List<RepoetPlanet> getcombList() {
        SQLiteDatabase db = getWritableDatabase();
        RepoetPlanet planet = null;
        List<RepoetPlanet>  mPlanetlis = new ArrayList<>();


//        Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(name)});
//        Cursor cursor = db.rawQuery("select Combine_Nameg,Combine_Amountg,Combine_Amount from " + TABLE_GT, null);
        Cursor cursor = db.rawQuery("select User_Name,User_GiveAmount,User_TakeAmount,User_Balance from " + TABLE_UserReport, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            planet = new RepoetPlanet(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3));
            mPlanetlis.add(planet);
            cursor.moveToNext();
        }

        cursor.close();
        return  mPlanetlis;

    }

    // Report Details List
    public List<ReportDetailsPlanet> getReportDetailList(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ReportDetailsPlanet planet = null;
        List<ReportDetailsPlanet>  mPlanetlis = new ArrayList<>();


//        Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(name)});
//        Cursor cursor = db.rawQuery("select Combine_Nameg,Combine_Amountg,Combine_Amount from " + TABLE_GT, null);
        Cursor cursor = db.rawQuery("select Type,CDate,DueDate,Amount from GIVETAKE Where Name = ? order by Id asc", new String[]{name});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            planet = new ReportDetailsPlanet(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3));
            mPlanetlis.add(planet);
            cursor.moveToNext();
        }

        cursor.close();
        return  mPlanetlis;

    }
    //Take List...........


     int amount;
    public int sumData(String typegive)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("select sum(Amount) from GIVETAKE Where Type = ?", new String[]{typegive});
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();
        //return c;
        return amount;
    }

    int amount1;
    public int sumDatagive(String typetake)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("select sum(Amount) from GIVETAKE Where Type = ?", new String[]{typetake});
        if(c.moveToFirst())
            amount1 = c.getInt(0);
        else
            amount1 = -1;
        c.close();
        //return c;
        return amount1;
    }

    int takenamt;
    public int UsersumDataTake(String takename,String type)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("select sum(Amount) from GIVETAKE Where Name = ? And Type = ?", new String[]{takename,type});
        if(c.moveToFirst())
            takenamt = c.getInt(0);
        else
            takenamt = -1;
        c.close();
        //return c;
        return takenamt;
    }

    int givenamt;
    public int UsersumDataGive(String givename,String type)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("select sum(Amount) from GIVETAKE Where Name = ? And Type = ?", new String[]{givename,type});
        if(c.moveToFirst())
            givenamt = c.getInt(0);
        else
            givenamt = -1;
        c.close();
        //return c;
        return givenamt;
    }

    public Cursor filllist()
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select distinct Name from " + TABLE_GIVETAKE, null);
        return cursor;
    }

    public Cursor filllistname()
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select Name from " + TABLE_GIVETAKE, null);
        return cursor;
    }

    public void DeleteData()
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_UserReport, null,null);
    }




}

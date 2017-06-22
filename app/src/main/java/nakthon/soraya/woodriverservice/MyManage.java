package nakthon.soraya.woodriverservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 6/22/2017 AD.
 */

public class MyManage {

    private Context context;
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public MyManage(Context context) {
        this.context = context;
        myOpenHelper = new MyOpenHelper(context);
        sqLiteDatabase = myOpenHelper.getWritableDatabase();
    }

    public long addValueToSQLite(String strName, String strPhone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", strName);
        contentValues.put("Phone", strPhone);
        return sqLiteDatabase.insert("passengerTABLE", null, contentValues);
    }

}   // Main Class

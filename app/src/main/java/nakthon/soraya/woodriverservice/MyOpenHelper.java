package nakthon.soraya.woodriverservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by masterUNG on 6/22/2017 AD.
 */

public class MyOpenHelper extends SQLiteOpenHelper{

    private Context context;
    public static final String database_name = "Woodriver.db";
    private static final int database_version = 1;
    private static final String create_passengerTABLE = "CREATE TABLE passengerTABLE (" +
            "id INTEGER PRIMARY KEY," +
            "Name TEXT," +
            "Phone TEXT);";

    public MyOpenHelper(Context context) {
        super(context, database_name, null, database_version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_passengerTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}   // Main Class

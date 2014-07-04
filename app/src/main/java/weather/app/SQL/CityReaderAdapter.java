package weather.app.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public final class CityReaderAdapter {

    static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "cityname";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitude";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "cities";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE =
            "create table cities (_id integer primary key autoincrement, "
            + "cityname text not null, latitude text not null, longitude text not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public CityReaderAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public CityReaderAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertContact(String cityName, String latitude, String longitude)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, cityName);
        initialValues.put(KEY_LAT, latitude);
        initialValues.put(KEY_LONG, longitude);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteContact(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor GetAllCities()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_LAT, KEY_LONG}, null, null, null, null, null);
    }

    public Cursor GetContact(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_LAT, KEY_LONG}, KEY_ROWID + "=" + rowId, null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public boolean updateContact(long rowId, String cityName, String latitude, String longitude)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, cityName);
        args.put(KEY_LAT, latitude);
        args.put(KEY_LONG, longitude);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
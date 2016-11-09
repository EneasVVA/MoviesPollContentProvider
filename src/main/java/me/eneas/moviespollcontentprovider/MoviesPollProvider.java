package me.eneas.moviespollcontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import static me.eneas.moviespollcontentprovider.MoviesPollContract.AUTHORITY;

/**
 * Created by a67281303 on 7/11/16.
 */

public class MoviesPollProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String ENTITY = "movies";

    private static final int ID_URI_MOVIES = 1;
    private SQLiteDatabase sqlDB;
    static final String DATABASE_NAME = "movies";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_TABLE_DB = "CREATE TABLE " + MoviesPollContract.Movie.TABLE_NAME +
            " ( " + MoviesPollContract.Movie.COL_ID_MOVIE +" INTEGER PRIMARY KEY, " +
            "  " + MoviesPollContract.Movie.COL_TITLE + " VARCHAR(255) NOT NULL," +
            "  " + MoviesPollContract.Movie.COL_YEAR + " VARCHAR(255) NOT NULL," +
            "  " + MoviesPollContract.Movie.COL_POPULARITY +" DECIMAL" +
            "  )";

    static final String CREATE_TABLE_DB2 = "CREATE TABLE " + MoviesPollContract.Poll.TABLE_NAME +
            " ( "+ MoviesPollContract.Poll.COL_ID_POLL+" INTEGER AUTO_INCREMENT," +
            "   "+ MoviesPollContract.Poll.COL_ID_MOVIE_FK+" INTEGER, " +
            "   "+ MoviesPollContract.Poll.COL_VALUE+" INTEGER NOT NULL," +
            "   FOREIGN KEY ("+MoviesPollContract.Poll.COL_ID_MOVIE_FK+") REFERENCES "+MoviesPollContract.Movie.TABLE_NAME+"("+MoviesPollContract.Poll.COL_VALUE+")" +
            "   PRIMARY KEY("+MoviesPollContract.Poll.COL_ID_POLL+","+MoviesPollContract.Poll.COL_ID_MOVIE_FK+"))";

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ENTITY, ID_URI_MOVIES);
    }

    private HashMap<String, String> values = new HashMap<>();

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();

        if(sqlDB != null) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MoviesPollContract.Movie.TABLE_NAME);

        switch(uriMatcher.match(uri)) {
            case ID_URI_MOVIES:
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);

        }

        Cursor cursor = queryBuilder.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case ID_URI_MOVIES:
                return "vnd.android.cursor.dir/"+MoviesPollContract.Movie.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unsopported URI" + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = sqlDB.insert(MoviesPollContract.Movie.TABLE_NAME, null, contentValues);

        if(rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);

            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }
        else {
            Log.d(getClass().getName(), "row insertion failed");
            return null;
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = 0;

        switch (uriMatcher.match(uri)) {
            case ID_URI_MOVIES:
                rowsDeleted = sqlDB.delete(MoviesPollContract.Movie.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw  new IllegalArgumentException("Unknown URI");

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        switch (uriMatcher.match(uri)) {
            case ID_URI_MOVIES:
                rowsUpdated = sqlDB.update(MoviesPollContract.Movie.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw  new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null );
        return rowsUpdated;
    }

    final static private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqlDB) {
            sqlDB.execSQL(CREATE_TABLE_DB);
            sqlDB.execSQL(CREATE_TABLE_DB2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqlDB, int i, int i1) {
            sqlDB.execSQL("DROP TABLE IF EXISTS " + MoviesPollContract.Movie.TABLE_NAME);
            sqlDB.execSQL("DROP TABLE IF EXISTS " + MoviesPollContract.Poll.TABLE_NAME);
            onCreate(sqlDB);
        }
    }
}

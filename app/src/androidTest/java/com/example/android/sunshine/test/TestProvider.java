package com.example.android.sunshine.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract.LocationEntry;
import com.example.android.sunshine.data.WeatherContract.WeatherEntry;
import com.example.android.sunshine.data.WeatherDbHelper;

/**
 * Created by niiamon on 22/07/2015.
 */
public class TestProvider extends AndroidTestCase {

  public static final String LOG_TAG = TestProvider.class.getSimpleName();

  public void testDeleteDb() throws Throwable {
    mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
  }

  public void testInsertReadDb() {

    // If there's an error in those massive SQL table creation Strings,
    // errors will be thrown here when you try to get a writable database.
    WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    ContentValues testValues = TestDb.createNorthPoleLocationValues();

    long locationRowId;
    locationRowId = db.insert(LocationEntry.TABLE_NAME, null, testValues);

    // Verify we got a row back.
    assertTrue(locationRowId != -1);
    Log.d(LOG_TAG, "New row id: " + locationRowId);

    // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
    // the round trip.

    // A cursor is your primary interface to the query results.
    Cursor cursor = db.query(
        LocationEntry.TABLE_NAME,  // Table to Query
        null, // all columns
        null, // Columns for the "where" clause
        null, // Values for the "where" clause
        null, // columns to group by
        null, // columns to filter by row groups
        null // sort order
    );

    TestDb.validateCursor(cursor, testValues);

    // Fantastic.  Now that we have a location, add some weather!
    ContentValues weatherValues = TestDb.createWeatherValues(locationRowId);

    long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
    assertTrue(weatherRowId != -1);

    // A cursor is your primary interface to the query results.
    Cursor weatherCursor = db.query(
        WeatherEntry.TABLE_NAME,  // Table to Query
        null, // leaving "columns" null just returns all the columns.
        null, // cols for "where" clause
        null, // values for "where" clause
        null, // columns to group by
        null, // columns to filter by row groups
        null  // sort order
    );

    TestDb.validateCursor(weatherCursor, weatherValues);

    dbHelper.close();
  }

  public void testGetType() {
    // content://com.example.android.sunshine.app/weather/
    String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
    // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
    assertEquals(WeatherEntry.CONTENT_TYPE, type);

    String testLocation = "94074";
    // content://com.example.android.sunshine.app/weather/94074
    type = mContext.getContentResolver().getType(
        WeatherEntry.buildWeatherLocation(testLocation));
    // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
    assertEquals(WeatherEntry.CONTENT_TYPE, type);

    String testDate = "20140612";
    // content://com.example.android.sunshine.app/weather/94074/20140612
    type = mContext.getContentResolver().getType(
        WeatherEntry.buildWeatherLocationWithDate(testLocation, testDate));
    // vnd.android.cursor.item/com.example.android.sunshine.app/weather
    assertEquals(WeatherEntry.CONTENT_TYPE_ITEM, type);

    // content://com.example.android.sunshine.app/location/
    type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
    // vnd.android.cursor.dir/com.example.android.sunshine.app/location
    assertEquals(LocationEntry.CONTENT_TYPE, type);

    // content://com.example.android.sunshine.app/location/1
    type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
    // vnd.android.cursor.item/com.example.android.sunshine.app/location
    assertEquals(LocationEntry.CONTENT_TYPE_ITEM, type);
  }
}

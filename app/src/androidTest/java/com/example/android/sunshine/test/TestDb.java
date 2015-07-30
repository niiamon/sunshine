package com.example.android.sunshine.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract.LocationEntry;
import com.example.android.sunshine.data.WeatherContract.WeatherEntry;
import com.example.android.sunshine.data.WeatherDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by niiamon on 22/07/2015.
 */
public class TestDb extends AndroidTestCase {

  private static final String LOG_TAG = TestDb.class.getSimpleName();

  public void testCreateDb() throws Throwable {
    mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    SQLiteDatabase db = new WeatherDbHelper(
        this.mContext).getWritableDatabase();
    assertEquals(true, db.isOpen());
    db.close();
  }

  public void testInsertReadDb() {
    // if there's an error in those massive SQL table creation strings,
    // errors will be thrown here when you try to get a writable database
    WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    // create a new map of values, where column names are the keys
    ContentValues values = createNorthPoleLocationValues();

    long locationRowId;
    locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);

    assertTrue(locationRowId != -1);
    Log.e(LOG_TAG, "New row id: " + locationRowId);

    // a cursor is your primary interface to the query results
    Cursor cursor = db.query(
        LocationEntry.TABLE_NAME,
        null, // all columns
        null, // columns for the where clause
        null, // values for the where clause
        null, // columns to group by
        null, // columns to filter by row groups
        null // sort order
    );

    validateCursor(cursor, values);

    // Fantastic.  Now that we have a location, add some weather!
    ContentValues weatherValues = createWeatherValues(locationRowId);

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

    validateCursor(weatherCursor, weatherValues);

    dbHelper.close();
  }

  static ContentValues createWeatherValues(long locationRowId) {
    ContentValues weatherValues = new ContentValues();
    weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
    weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
    weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
    weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
    weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
    weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
    weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
    weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
    weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
    weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);
    return weatherValues;
  }

  static ContentValues createNorthPoleLocationValues() {
    ContentValues values = new ContentValues();
    values.put(LocationEntry.COLUMN_CITY_NAME, "North Pole");
    values.put(LocationEntry.COLUMN_LOCATION_SETTING, "99705");
    values.put(LocationEntry.COLUMN_COORD_LAT, 64.772);
    values.put(LocationEntry.COLUMN_COORD_LONG, -142.355);
    return values;
  }

  static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {
    assertTrue(valueCursor.moveToFirst());

    Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
    for (Map.Entry<String, Object> entry : valueSet) {
      String columnName = entry.getKey();
      int idx = valueCursor.getColumnIndex(columnName);
      assertFalse(idx == -1);
      String expectedValue = entry.getValue().toString();
      assertEquals(expectedValue, valueCursor.getString(idx));
    }
    valueCursor.close();
  }
}

package com.example.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by niiamon on 22/07/2015.
 */
public class WeatherProvider extends ContentProvider {

  private static final int WEATHER = 100;
  private static final int WEATHER_WITH_LOCATION = 101;
  private static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
  private static final int LOCATION = 300;
  private static final int LOCATION_ID = 301;

  private WeatherDbHelper weatherDbHelper;

  // the uri matcher used by this content provider
  private static final UriMatcher uriMatcher = buildUriMatcher();

  private static UriMatcher buildUriMatcher() {
    final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER, WEATHER);
    uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
    uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);
    uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_LOCATION, LOCATION);
    uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_LOCATION + "/#", LOCATION_ID);
    return uriMatcher;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    return 0;
  }

  @Override
  public boolean onCreate() {
    weatherDbHelper = new WeatherDbHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return null;
  }

  @Override
  public String getType(Uri uri) {
    final int match = uriMatcher.match(uri);
    switch (match) {
      case WEATHER_WITH_LOCATION_AND_DATE:
        return WeatherContract.WeatherEntry.CONTENT_TYPE_ITEM;
      case WEATHER_WITH_LOCATION:
        return WeatherContract.WeatherEntry.CONTENT_TYPE;
      case WEATHER:
        return WeatherContract.WeatherEntry.CONTENT_TYPE;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    return null;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return 0;
  }
}

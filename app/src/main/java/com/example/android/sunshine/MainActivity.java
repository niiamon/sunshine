package com.example.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

  private final String LOG_TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e(LOG_TAG, "Inside the OnCreate");
    setContentView(R.layout.activity_main);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  private void openPreferredLocationInMap() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

    // create a Uri for sending to the intent handler
    Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
        .appendQueryParameter("q", location)
        .build();

    Intent mapViewIntent = new Intent(Intent.ACTION_VIEW);
    mapViewIntent.setData(geoLocation);
    if (mapViewIntent.resolveActivity(getPackageManager()) != null) {
      startActivity(mapViewIntent);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      Intent settingsActivityIntent = new Intent(this, new SettingsActivity().getClass());
      startActivity(settingsActivityIntent);
      return true;
    }

    if (id == R.id.action_location_on_map) {
      openPreferredLocationInMap();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.e(LOG_TAG, "Inside the OnPause");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.e(LOG_TAG, "Inside the OnStop");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.e(LOG_TAG, "Inside the OnResume");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.e(LOG_TAG, "Inside the OnStart");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.e(LOG_TAG, "Inside the OnDestroy");
  }
}

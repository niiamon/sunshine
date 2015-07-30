package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

  private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
  private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
  private String mForecastStr;
  
  public DetailActivityFragment() {
    setHasOptionsMenu(true);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // read forecast data from the intent
    Intent intent = getActivity().getIntent();

    // display it
    View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

    if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
      mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
      TextView weatherStrTextView = (TextView) rootView.findViewById(R.id.detail_text);
      weatherStrTextView.setText(mForecastStr);
    }
    return rootView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //super.onCreateOptionsMenu(menu, inflater);
    // inflate the menu; this adds items to the action bar if it is present
    inflater.inflate(R.menu.detail_fragment, menu);

    // retrieve the share menu
    MenuItem menuItem = menu.findItem(R.id.action_share);

    // get the provider and hold on to it to set/change the share intent
    ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

    // attach an intent to this ShareActionProvider. You can update this at any time,
    // like when the user selects a new pirce of data they might like to share
    if (mShareActionProvider != null) {
      mShareActionProvider.setShareIntent(createShareForecastIntent());
    } else {
      Log.e(LOG_TAG, "Share Action Provider is null?");
    }
  }

  private Intent createShareForecastIntent() {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);

    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);

    return shareIntent;
  }
}

package edu.incense.ui;

import edu.incense.R;
import edu.incense.R.id;
import edu.incense.R.menu;
import edu.incense.results.ResultsListActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class MainMenuActivity extends Activity {
    private Intent mainIntent, settingsIntent, resultsIntent;

    @Override
    public void onStart() {
        super.onStart();
        // Apply any required UI change now that the Activity is visible.

        mainIntent = new Intent(this, MainActivity.class);
        settingsIntent = new Intent(this, SettingsActivity.class);
        resultsIntent = new Intent(this, ResultsListActivity.class);
    }

    // This inflates/populates your menu resource (convert the XML resource into
    // a programmable object)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    // This method passes the MenuItem that the user selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
        case R.id.new_recording:
            startActivity(mainIntent);
            return true;
        case R.id.settings:
            startActivity(settingsIntent);
            // startActivityForResult(settingsIntent, SHOW_PREFERENCES);
            return true;
        case R.id.results:
            startActivity(resultsIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}

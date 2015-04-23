package com.e13engineering.animallift;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import at.markushi.ui.CircleButton;

public class MainActivity extends ActionBarActivity {

    double input = -1;
    int max;
    EditText weightInput;
    TextView results,future;
    CircleButton button;
    RadioButton lbs,kg;
	ImageView image;
    ScrollView bigContainer;
    LiftObjects.LiftObject[] thisLift;
    LiftObjects objects;
	InputMethodManager imm; // for hiding keyboard
	ShareActionProvider mShareActionProvider; // for the sharing menu
	private static final String PREFS = "prefs";
	private static final String PREF_UNIT_SET = "unit_set";
	private static final String PREF_UNIT = "unit";
    private static final String SAVE_STATE = "state";
	SharedPreferences mSharedPreferences;
	SharedPreferences.Editor prefsEditor;
	String defaultUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Bind the editText, viewText, imageView, and Button
        results = (TextView) findViewById(R.id.results);
	    future = (TextView) findViewById(R.id.future);

        button = (CircleButton) findViewById(R.id.button);
        lbs = (RadioButton) findViewById(R.id.lbs);
        kg = (RadioButton) findViewById(R.id.kg);
	    image = (ImageView) findViewById(R.id.animalPicture);
        bigContainer = (ScrollView) findViewById(R.id.scrollview);

	    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //for hiding the keyboard

	    // The savedPreferences hook and editor
	    mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
	    prefsEditor = mSharedPreferences.edit();

	    // Display about popup with default units setter
	    displayWelcome();

	    defaultUnit = mSharedPreferences.getString(PREF_UNIT, "");
	    if (defaultUnit.equals("kg")) {
		    kg.setChecked(true);
	    }
        // Instantiate the work class
        objects = new LiftObjects();

        // hook the weight input editText
        weightInput = (EditText) findViewById(R.id.weightInput);

        if (savedInstanceState != null) {
            double input = savedInstanceState.getDouble(SAVE_STATE);
            if (input > 0) {
                this.input = input;
                giveFeedback(this.input);
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

	    // Access the Share Item defined in menu XML
	    MenuItem shareItem = menu.findItem(R.id.menu_item_share);

	    // Access the object responsible for
	    // putting together the sharing submenu
	    if (shareItem != null) {
		    mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
	    }
	    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
	        // add - set units default set back to false, call the about dialog to popup the about & units setter
	        prefsEditor.putString(PREF_UNIT_SET, "false");
	        prefsEditor.commit();
	        displayWelcome();

            return true;
        } else if (id == R.id.menu_item_share) {
            Log.d("DEBUG","Share selected");
	        setShareIntent(); // update the share intent if there's been an input
        }
        return super.onOptionsItemSelected(item);
    }

	private void setShareIntent() {
		// this builds the share text, does an error check for no input yet, then lets you share
		if (mShareActionProvider != null) {
	        // Create an Intent to share your content
			if (thisLift[0].name.length() == 0) {
				// Should show a toast if there's been no input but it's broken??
				// WHY IS IT BROKEN
				Toast.makeText(getApplicationContext(), "You haven't even lifted anything yet!", Toast.LENGTH_LONG).show();
			} else {
                Log.d("DEBUG","Setting share");
				// create an Intent with the contents of the output TextView
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				// comment out the share subject to get it within twitter limit shareIntent.putExtra(Intent.EXTRA_SUBJECT, "What Animal Can You Lift?");
				shareIntent.putExtra(Intent.EXTRA_TEXT, "Bros, I can lift a " + thisLift[0].name + ". What animal can you even lift? Find out: http://goo.gl/Vg5Wh0 #AnimalLifts");

				// Make sure the provider knows
				// it should work with that Intent
				mShareActionProvider.setShareIntent(shareIntent);
			}
		} else {
            Log.d("DEBUG", "mShareActionProvider == null");
        }
	}

	// This is the button's onClick class, it does all the work
    public void AnimalLift(View view) {

	    // Do the units check
	    // TODO: Need to make this autodetect locale and adjust accordingly!
	    if (lbs.isChecked()) {
		    max = 6001; // define the max here so it can be adjusted for future values added
	    } else {
		    max = (int) (6001/2.2);
	    }


	    // require some input in the text box before proceeding
	    if (weightInput.getText().toString().equals("")) {
		    return;
	    }
	    // Do some input error checking
	    // Disallow < 0, > 6000 lbs
        if (weightInput.getText().toString().length() <= 0) {
	        weightInput.setError("Enter a weight");
        } else if (Long.parseLong(weightInput.getText().toString()) <= 0) {
            weightInput.setError("That's a negative lift bro.");
        } else if (Long.parseLong(weightInput.getText().toString()) >= max) {
	        weightInput.setError("Whoa there Atlas, that's a bit high.");
        } else { // If the input is valid, grab it here
            input = Double.parseDouble(weightInput.getText().toString()); // grab the input str from the editText and parse it to an double
	        if (kg.isChecked()) { // if kg is checked, we turn the input from kg to lbs
		        input = input*2.2;
	        }

            giveFeedback(input);

        }
    }

    private void giveFeedback(double input) {
        // Hide the keyboard here
        imm.hideSoftInputFromWindow(weightInput.getWindowToken(), 0);

        thisLift = objects.getLift((int) input); // pass the weight input to the work class
        if (thisLift != null) {
            results.setText("Wow, you can lift a " + thisLift[0].name + "!"); // put the animal name in output

            int resID = getResources().getIdentifier(thisLift[0].image, "drawable", getPackageName()); // this tricky bit gets the formatted name of the animal and pulls the resourceID for the same name png in the drawable folder
            image.setImageResource(resID); // display the mentioned png
            Palette.generateAsync(BitmapFactory.decodeResource(getApplicationContext().getResources(), resID), new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    int color = palette.getLightVibrantColor(palette.getDarkVibrantColor(R.color.MaterialTealA700));
                    bigContainer.setBackgroundColor(color);
                }
            });

            if (thisLift[1] != null) {
                future.setVisibility(View.VISIBLE);
                future.setText("Keep it up and soon you'll be lifting a " + thisLift[1].name + "!");
            } else {
                future.setVisibility(View.GONE);
            }

            setShareIntent(); // update all the text that we will share to the Intent
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putDouble(SAVE_STATE,input);
        super.onSaveInstanceState(bundle);
    }

	public void displayWelcome() {
		String state = mSharedPreferences.getString(PREF_UNIT_SET, "");
		if (!state.equals("true")) {
			// set the dialog as viewed and the default units as set
			prefsEditor.putString(PREF_UNIT_SET, "true");
			prefsEditor.commit();

			// call the dialog builder and display the aboutFragment dialog
			DialogFragment aboutDialog = new AboutFragment();
			aboutDialog.show(getFragmentManager(),"AboutFragment");
		}
	}

}
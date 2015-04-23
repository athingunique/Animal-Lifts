package com.e13engineering.animallift;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by evan on 6/11/14.
 */

public class AboutFragment extends DialogFragment {
	SharedPreferences mSharedPreferences;
	private static final String PREFS = "prefs";
	private static final String PREF_UNIT = "unit";
	String defaultUnit;
	RadioButton defaultKgs;


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// use the builder class to construct the dialog easily
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// get the layout inflater to inflate the custom dialog layout
		LayoutInflater inflater = getActivity().getLayoutInflater();
		// open the shared preferences
		mSharedPreferences = getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		final SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();


		// Inflate the layout and attach it to the dialog
		View view = (inflater.inflate(R.layout.about_dialog, null)); // inflate the view for this dialog
		// This *SHOULD* make the dialog popup have clickable links
		// But it doesn't.
		TextView link1 = (TextView) view.findViewById(R.id.link1);
		TextView link2 = (TextView) view.findViewById(R.id.link2);
		Linkify.addLinks(link2,Linkify.ALL);
		Linkify.addLinks(link1,Linkify.ALL);

		builder.setView(view) // pass the view to the builder
			.setTitle(R.string.aboutDialogTitle) // make title and okay button
			.setPositiveButton("Okay", new DialogInterface.OnClickListener() {


				@Override // the onClick for the Okay button is here
				public void onClick(DialogInterface dialogInterface, int i) {
					defaultUnit = mSharedPreferences.getString(PREF_UNIT, ""); // hook the default unit pref

					// checks for which radio button is set
					if (defaultKgs.isChecked()) { // if kg gets selected, set the pref to kg
						prefsEditor.putString(PREF_UNIT, "kg");
						prefsEditor.commit();
						RadioButton kg = (RadioButton) getActivity().findViewById(R.id.kg);
						kg.setChecked(true);
					} else { // otherwise, set the pref to lb
						prefsEditor.putString(PREF_UNIT, "lbs");
						prefsEditor.commit();
						RadioButton lb = (RadioButton) getActivity().findViewById(R.id.lbs);
						lb.setChecked(true);
					}
					Toast defaultSet = Toast.makeText(getActivity().getApplicationContext(), "Default weight unit set", Toast.LENGTH_LONG);
					defaultSet.show();

				}
			});

		defaultKgs = (RadioButton) view.findViewById(R.id.defaultKgs);
		defaultUnit = mSharedPreferences.getString(PREF_UNIT, "");
		if (defaultUnit.equals("kg")) { // if the default is kg, leave kg checked
			defaultKgs.setChecked(true); // (it defaults to lbs if there is no default unit set)
		}

	// create the dialog object and return it
		return builder.create();
	}
}

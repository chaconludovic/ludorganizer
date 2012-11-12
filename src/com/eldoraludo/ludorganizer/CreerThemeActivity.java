package com.eldoraludo.ludorganizer;

import com.eldoraludo.ludorganizer.db.DatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreerThemeActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creation_theme);

		final TextView nomDuThemeTxt = (TextView) findViewById(R.id.nomDuThemeTxt);

		final Button valierNomDuThemeBt = (Button) findViewById(R.id.valierNomDuThemeBt);
		valierNomDuThemeBt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// stocker le résultat
				DatabaseHelper dbHelper = new DatabaseHelper(
						getApplicationContext());
				dbHelper.addTheme(nomDuThemeTxt.getText().toString());
				// revenir à l'activité précédente
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_draw_the_rest, menu);
		return true;
	}
}

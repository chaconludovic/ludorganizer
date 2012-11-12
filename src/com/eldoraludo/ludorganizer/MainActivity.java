package com.eldoraludo.ludorganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.eldoraludo.ludorganizer.db.DatabaseHelper;

public class MainActivity extends Activity {
	private DatabaseHelper dbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_principal);

		dbHelper = new DatabaseHelper(getApplicationContext());
		refresh();

		final Button creerThemeBt = (Button) findViewById(R.id.creertheme);

		creerThemeBt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent drawIntent = new Intent(MainActivity.this,
						CreerThemeActivity.class);
				startActivity(drawIntent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		LinearLayout listeThemeLayout = (LinearLayout) findViewById(R.id.listeThemeLayout);
		listeThemeLayout.removeAllViews();
		String[] data = dbHelper.getThemes();
		for (int i = 0; i < data.length; i++) {
			Button b1 = new Button(this);
			b1.setText(data[i]);
			listeThemeLayout.addView(b1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

package com.eldoraludo.ludorganizer.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// TODO continuer a creer une db
public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "";
	private static String THEME_TABLE = "theme";
	private static final String DB_NAME = "LudorganizerDB.sqlite";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase ludorganizerDB;
	private final Context myContext;
	private static final String DATABASE_CREATE_THEME_TABLE = "create table "
			+ THEME_TABLE + "(_id integer primary key autoincrement, "
			+ "nomTheme text not null);";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
		try {
			createDataBase(context);
		} catch (IOException e) {
			Log.e("DatabaseHelper.onCreate", "" + e.getMessage());
		}
	}

	/**
	 * Creates an empty database on the system and rewrites it with your own
	 * database.
	 **/
	public void createDataBase(Context context) throws IOException {
		// lets check if a database already exists at the specified
		// location, if it doesn't, lets copy our db
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.e("DatabaseHelper.createDataBase", "" + e.getMessage());
			}
			try {
				createTables();
			} catch (SQLiteException e) {
				Log.e("DatabaseHelper.createTables", "" + e.getMessage()
						+ ", Cause: " + e.getCause());
			}
		}

	}

	private void createTables() {
		String myPath = DB_PATH + DB_NAME;
		ludorganizerDB = SQLiteDatabase.openOrCreateDatabase(myPath, null);
		ludorganizerDB.execSQL(DATABASE_CREATE_THEME_TABLE);

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY
							| SQLiteDatabase.NO_LOCALIZED_COLLATORS);

		} catch (SQLiteException e) {
			Log.e("DatabaseHelper.checkDataBase", "" + e.getMessage()
					+ ", Cause: " + e.getCause());
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies database from local assets-folder to the just created empty
	 * database in the system folder, from where it can be accessed and handled.
	 * This is done by transferring byte-streams.
	 * */
	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Open the database en lecture
	 * 
	 * @throws SQLException
	 */
	public void openDataBaseEnLecture() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		ludorganizerDB = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY
						| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	/**
	 * Open the database en ecriture
	 * 
	 * @throws SQLException
	 */
	public void openDataBaseEnEcriture() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		ludorganizerDB = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE
						| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	/**
	 * Close the database if exist
	 */
	@Override
	public synchronized void close() {
		if (ludorganizerDB != null)
			ludorganizerDB.close();
		super.close();
	}

	/**
	 * Queries the database table for all rows and returns an array containing
	 * 'text' column
	 * 
	 * @return data[] array that holds all the data found in the 'text' column
	 */
	public String[] getThemes() {
		String[] data = { "theme 1", "theme 2", "theme 3" };
		String[] columns = { "nomTheme" };
		try {
			openDataBaseEnLecture();
			Cursor cur = ludorganizerDB.query(THEME_TABLE, columns, null, null,
					null, null, "nomTheme");
			cur.moveToFirst();
			data = new String[cur.getCount()];
			for (int i = 0; i < cur.getCount(); i++) {
				data[i] = cur.getString(0);
				cur.moveToNext();
			}
			cur.close();
			close();
		} catch (Exception ex) {
			Log.e("DatabaseHandler.getCategoryJoke", "" + ex.getMessage());
		}

		return data;
	}

	public void addTheme(String nouveauTheme) {
		try {
			openDataBaseEnEcriture();
			ContentValues values = new ContentValues();
			values.put("nomTheme", nouveauTheme);
			ludorganizerDB.insert(THEME_TABLE, null, values);
		} catch (Exception ex) {
			Log.e("DatabaseHandler.getCategoryJoke", "" + ex.getMessage());
		}
	}

	/**
	 * Method of SQLiteOpenHelper that we don't currently need
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	/**
	 * Method of SQLiteOpenHelper that we don't currently need
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
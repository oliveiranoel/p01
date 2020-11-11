package io.vsia.p01.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.vsia.p01.model.Highscore;

/**
 * @author noel.oliveira
 * @since 20.4.2018
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TWENTYONE";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ENTRY ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TIME INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Generiert einen neuen Eintrag in der DB
     * @param highscore
     */
    public void createNewEntry(Highscore highscore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TIME", highscore.getTime());

        db.insert("ENTRY", null, values);
        db.close();
    }

    /**
     * löscht alle Einträge in der Datenbank
     */
    public void deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("ENTRY", null, null);
    }

    /**
     * Zeigt alle Einträge der DB an
     * Sortierung erfolgt nach Zeit. Kürzeste --> Längste
     * @return
     */
    public List<Highscore> showAllEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Highscore> returnedVal = new ArrayList<>();

        String query = "SELECT * FROM ENTRY ORDER BY TIME ASC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Highscore highscore = new Highscore();
                highscore.setTime(cursor.getLong(cursor.getColumnIndex("TIME")));
                returnedVal.add(highscore);
            } while (cursor.moveToNext());
        }
        return returnedVal;
    }
}

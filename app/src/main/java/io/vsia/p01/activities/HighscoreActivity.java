package io.vsia.p01.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import io.vsia.p01.R;
import io.vsia.p01.business.DatabaseHelper;
import io.vsia.p01.model.Highscore;

public class HighscoreActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView = findViewById(R.id.listView_highscore);
        List<Highscore> entries = databaseHelper.showAllEntries();

        // lädt die History, sofern Einträge in der History vorhanden sind
        // ansonsten erscheint eine Fehlermeldung
        if (!entries.isEmpty()) {
            ArrayAdapter<Highscore> listAdapater = new ArrayAdapter<Highscore>(this, android.R.layout.simple_list_item_1, entries);
            listView.setAdapter(listAdapater);
        } else {
            String toastText = "Keine Einträge in der Liste vorhanden";
            Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onDelete(View view) {
        databaseHelper.deleteAllEntries();
        listView.setAdapter(null);
    }
}

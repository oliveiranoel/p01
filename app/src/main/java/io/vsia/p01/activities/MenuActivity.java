package io.vsia.p01.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import io.vsia.p01.R;
import io.vsia.p01.business.DatabaseHelper;
import io.vsia.p01.model.Highscore;

/**
 * @author noel.oliveira
 * @since 20.4.2018
 */
public class MenuActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    Button btn_single;
    Button btn_multi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        btn_single = findViewById(R.id.btn_singlePlayer);
        btn_multi = findViewById(R.id.btn_multiPlayer);
    }

    public void onSinglePlayer(View view) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra("mode", 1);

        startActivity(intent);
    }

    public void onHighscore(View view) {
        List<Highscore> entries = databaseHelper.showAllEntries();

        if (entries.isEmpty()) {
            String toastText = "Keine Einträge in der Liste vorhanden";
            Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent intent = new Intent(this, HighscoreActivity.class);
            startActivity(intent);
        }
    }

    public void onMultiplayer(View view) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra("mode", 2);

        startActivity(intent);
    }

    public void onExit(View view) {
        finish();
    }

    public void onManual(View view) {
        Intent intent = new Intent(this, ManuelActivity.class);
        startActivity(intent);
    }
}

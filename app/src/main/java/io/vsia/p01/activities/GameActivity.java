package io.vsia.p01.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Random;

import io.vsia.p01.R;
import io.vsia.p01.business.DatabaseHelper;
import io.vsia.p01.model.Highscore;

/**
 * @author noel.oliveira
 * @since 20.4.2018
 */
public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager = null;
    private Sensor acclerometer = null;

    // KOMPONENTEN
    Chronometer chronometer1;
    Chronometer chronometer2;
    ToggleButton toggleButton1;
    ToggleButton toggleButton2;
    ToggleButton toggleButton3;
    ToggleButton toggleButton4;
    ToggleButton toggleButton5;
    ToggleButton toggleButton6;
    Button btn_dismiss;
    TextView textView1;
    TextView textView2;
    TextView textView_player1;
    TextView textView_player2;
    TextView textView_winner;
    Button btn_restart;
    ProgressBar progressBar;
    ProgressBar progressBar1;
    ProgressBar progressBar2;

    // INITIALISIERUNG OBJEKTE
    Highscore highscore = new Highscore();
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    // HILFSVARIABLEN
    int summe1 = 0;
    int summe2 = 0;
    boolean chron1 = false;
    boolean chron2 = false;
    int counter;
    int counter2;
    long elapsedTime = 0;
    long elapsedTime1 = 0;
    int mode;
    int playerTurn = 1;
    private float accValueX;
    private float accValueY;
    private float accValueZ;

    // PLAYERS
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;

    String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 1);
        Log.i(TAG, "Intent Mode: " + mode);

        if (mode == 1) {
            Log.i(TAG, "PORTRAIT MODE ACTIVATED");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        } else if (mode == 2) {
            // initialisiert Komponenten, die nur im Mulitplayer Mode benötigt werden
            Log.i(TAG, "LANDSCAPE MODE ACITVATED");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            chronometer2 = findViewById(R.id.chronometer1);
            textView2 = findViewById(R.id.textView_summe2);
            textView_player1 = findViewById(R.id.textView_pl1);
            textView_player2 = findViewById(R.id.textView_pl2);
            progressBar1 = findViewById(R.id.progressBar_pl1);
            progressBar2 = findViewById(R.id.progressBar_pl2);
            textView_winner = findViewById(R.id.textView_winner);
        }

        chronometer1 = findViewById(R.id.chronometer);
        toggleButton1 = findViewById(R.id.toggleButton1);
        toggleButton2 = findViewById(R.id.toggleButton2);
        toggleButton3 = findViewById(R.id.toggleButton3);
        toggleButton4 = findViewById(R.id.toggleButton4);
        toggleButton5 = findViewById(R.id.toggleButton5);
        toggleButton6 = findViewById(R.id.toggleButton6);
        btn_dismiss = findViewById(R.id.btn_dismiss);
        btn_restart = findViewById(R.id.btn_restart);
        textView1 = findViewById(R.id.textView_summe);
        progressBar = findViewById(R.id.progressBar);

        createRandNumber();

        enableAndUpdateButtons();

        toggleButton1.setEnabled(true);
        toggleButton2.setEnabled(true);
        toggleButton3.setEnabled(true);
        toggleButton4.setEnabled(true);
        toggleButton5.setEnabled(true);
        toggleButton6.setEnabled(true);

        toggleButton1.setChecked(false);
        toggleButton2.setChecked(false);
        toggleButton3.setChecked(false);
        toggleButton4.setChecked(false);
        toggleButton5.setChecked(false);
        toggleButton6.setChecked(false);

        btn_restart.setEnabled(false);

        sensorCheck();
    }

    /**
     * überprüft alle Sensoren und speichert denn Acclerometer sensor in die
     * Klassenvariable acclerometer
     */
    private void sensorCheck() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sensorInfo = new StringBuilder();

        for (Sensor sensor : sensors) {
            sensorInfo.append(sensor.getName() + "\n");

            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    acclerometer = sensor;
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (acclerometer != null) {
            sensorManager.registerListener(this, acclerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //2 = Z - Achse
        //1 = Y - Achse
        //0 = X - Achse
        if (event.sensor == acclerometer) {
            accValueZ = event.values[2];
            accValueX = event.values[0];
            accValueY = event.values[1];
            float accValue = accValueX + accValueY + accValueZ;
            if (accValue > 25) {
                dismiss();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Generiert 6 Zufallszahlen und schreibt diese jeweils in die ToggleButtons
     */
    public void createRandNumber() {
        Random random = new Random();

        int random1 = random.nextInt(10) + 1;
        toggleButton1.setTextOff(String.valueOf(random1));
        toggleButton1.setChecked(false);

        int random2 = random.nextInt(10) + 1;
        toggleButton2.setTextOff(String.valueOf(random2));
        toggleButton2.setChecked(false);

        int random3 = random.nextInt(10) + 1;
        toggleButton3.setTextOff(String.valueOf(random3));
        toggleButton3.setChecked(false);

        int random4 = random.nextInt(10) + 1;
        toggleButton4.setTextOff(String.valueOf(random4));
        toggleButton4.setChecked(false);

        int random5 = random.nextInt(10) + 1;
        toggleButton5.setTextOff(String.valueOf(random5));
        toggleButton5.setChecked(false);

        int random6 = random.nextInt(10) + 1;
        toggleButton6.setTextOff(String.valueOf(random6));
        toggleButton6.setChecked(false);
    }

    /**
     * Wird ausgeführt bei jedem klick auf irgend ein Button.
     * Überprüft ob die Summe = p21 ist --> Runde gewonnen
     * Überprüft ob die Summe > p21 ist --> Runde verloren
     * Überprüft ob 10 Runden gewonnen worden sind --> Game gewonnen
     *
     * @param view
     */
    public void onGame(View view) {
        if (mode == 1) {
            // lässt das Chronometer beim ersten Klick auf irgend ein Button starten (SinglePlayer)
            if (!chron1) {
                chronometer1.setBase(SystemClock.elapsedRealtime());
                chronometer1.start();
                chron1 = true;
            }
        } else if (mode == 2) {
            if (playerTurn == PLAYER1) {
                if (!chron1) {
                    textView_player1.setTextColor(Color.RED);
                    chronometer1.setBase(SystemClock.elapsedRealtime() - elapsedTime);
                    chronometer1.start();
                    chron1 = true;
                    Log.d(TAG, "chron1");
                }
            } else if (playerTurn == PLAYER2) {
                if (!chron2) {
                    chronometer2.setBase(SystemClock.elapsedRealtime() - elapsedTime1);
                    chronometer2.start();
                    chron2 = true;
                    Log.d(TAG, "chron2");
                }
            }
        }

        //überprüft welcher Button geklickt wurde
        boolean on = ((ToggleButton) view).isChecked();

        // deaktiviert den geklickten Button
        if (on) {
            view.setEnabled(false);
        }

        if (mode == 1) {
            // rechnet die Summe aus und gibt diese am Bildschirm aus
            summe1 += Integer.parseInt(((ToggleButton) view).getTextOff().toString());
            textView1.setText(String.valueOf(summe1));
        } else if (mode == 2) {
            if (playerTurn == PLAYER1) {
                summe1 += Integer.parseInt(((ToggleButton) view).getTextOff().toString());
                textView1.setText(String.valueOf(summe1));

            } else if (playerTurn == PLAYER2) {
                summe2 += Integer.parseInt(((ToggleButton) view).getTextOff().toString());
                textView2.setText(String.valueOf(summe2));
            }
        }

        // Wenn eine Runde gewonnen/verloren wurde, dann aktiviere alle Buttons
        // und setzte Summe wieder auf 0
        // bei Summe = p21 wird der counter und die progressbar erhöht
        if (mode == 1) {

            if (summe1 == 21 || summe1 > 21) {
                if (summe1 == 21) {
                    counter++;
                    progressBar.setProgress(counter * 10);
                    Log.i(TAG, "WON Round");
                }

                enableAndUpdateButtons();
                createRandNumber();

                summe1 = 0;
                textView1.setText(String.valueOf(summe1));
            }

        } else if (mode == 2) {

            if (playerTurn == PLAYER1) {
                if (summe1 == 21 || summe1 > 21) {
                    if (summe1 == 21) {
                        counter++;
                        progressBar1.setProgress(counter * 10);
                    }

                    elapsedTime = SystemClock.elapsedRealtime() - chronometer1.getBase();
                    chronometer1.stop();
                    chron1 = false;

                    textView_player2.setTextColor(Color.RED);
                    textView1.setText(String.valueOf(summe1));
                    playerTurn = PLAYER2;
                    summe1 = 0;

                    chronometer2.setBase(SystemClock.elapsedRealtime() - elapsedTime1);
                    chronometer2.start();

                    enableAndUpdateButtons();
                    createRandNumber();

                    Log.i(TAG, "WON Round");
                }
            } else if (playerTurn == PLAYER2) {
                if (summe2 == 21 || summe2 > 21) {
                    if (summe2 == 21) {
                        counter2++;
                        progressBar2.setProgress(counter2 * 10);
                    }

                    elapsedTime1 = SystemClock.elapsedRealtime() - chronometer2.getBase();
                    chronometer2.stop();
                    chron2 = false;

                    textView_player1.setTextColor(Color.RED);
                    textView2.setText(String.valueOf(summe2));
                    playerTurn = PLAYER1;
                    summe2 = 0;

                    chronometer1.setBase(SystemClock.elapsedRealtime() - elapsedTime);
                    chronometer1.start();

                    enableAndUpdateButtons();
                    createRandNumber();

                    Log.i(TAG, "WON Round");
                }
            }


        }

        if (mode == 2) {
            //Momentaner Gewinner anzeigen
            int prg1 = progressBar1.getProgress();
            int prg2 = progressBar2.getProgress();
            if (prg1 > prg2) {
                textView_winner.setText("Player 1 is Winning");
            } else if (prg1 == prg2) {
                textView_winner.setText("undecided");
            } else {
                textView_winner.setText("Player 2 is Winning");
            }
        }

        // SPIEL BEENDET WENN
        // counter = 0 --> Spiel ist gewonnen und wird beendet
        if (counter == 10) {
            if (mode == 1) {
                chronometer1.stop();
                elapsedTime = SystemClock.elapsedRealtime() - chronometer1.getBase();
                elapsedTime = elapsedTime / 1000;
                highscore.setTime(elapsedTime);
                databaseHelper.createNewEntry(highscore);

                disableToggleButtons();

                summe1 = 0;
                textView1.setText("WON GAME");
                btn_restart.setEnabled(true);
                btn_dismiss.setEnabled(false);
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                Log.i(TAG, "WON GAME");
            } else if (mode == 2) {
                if (playerTurn == PLAYER1) {
                    chronometer2.stop();
                    elapsedTime1 = SystemClock.elapsedRealtime() - chronometer2.getBase();
                    highscore.setTime(elapsedTime1 / 1000);
                    databaseHelper.createNewEntry(highscore);

                    summe2 = 0;
                    textView_winner.setText("Player 2 WON GAME");
                    progressBar2.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                } else if (playerTurn == PLAYER2) {
                    chronometer1.stop();
                    elapsedTime = SystemClock.elapsedRealtime() - chronometer1.getBase();
                    highscore.setTime(elapsedTime / 1000);
                    databaseHelper.createNewEntry(highscore);

                    summe1 = 0;
                    textView_winner.setText("Player 1 WON GAME");
                    progressBar1.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                }
                disableToggleButtons();
                btn_restart.setEnabled(true);
                btn_dismiss.setEnabled(false);
            }
        }

        if (mode == 2) {
            if (playerTurn == PLAYER1) {
                textView_player2.setTextColor(Color.BLACK);
            } else if (playerTurn == PLAYER2) {
                textView_player1.setTextColor(Color.BLACK);
            }
        }

    }


    /**
     * Wenn Dismiss Button geklickt wird, dann werden alle Buttons aktiviert und
     * neue Zufallszahlen werden generiert. Ausserdem wird die Summe auf 0 gesetzt.
     *
     * @param view
     */
    public void onDismiss(View view) {
        dismiss();
    }

    /**
     * Aktiviert und Aktualisiert die Buttons.
     */
    public void enableAndUpdateButtons() {
        toggleButton1.setEnabled(true);
        toggleButton2.setEnabled(true);
        toggleButton3.setEnabled(true);
        toggleButton4.setEnabled(true);
        toggleButton5.setEnabled(true);
        toggleButton6.setEnabled(true);

        toggleButton1.setChecked(true);
        toggleButton2.setChecked(true);
        toggleButton3.setChecked(true);
        toggleButton4.setChecked(true);
        toggleButton5.setChecked(true);
        toggleButton6.setChecked(true);
    }

    /**
     * Bei Restart des Games wird die View neu geladen
     *
     * @param view
     */
    public void onRestart(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        btn_restart.setEnabled(false);
    }

    /**
     * Deaktiviert alle ToggleButtons
     */
    public void disableToggleButtons() {
        toggleButton1.setEnabled(false);
        toggleButton2.setEnabled(false);
        toggleButton3.setEnabled(false);
        toggleButton4.setEnabled(false);
        toggleButton5.setEnabled(false);
        toggleButton6.setEnabled(false);
    }

    /**
     * ganze Dismiss Funktion inkl. der Überprüfung, ob mode 1 oder mode 2 aktiviert sind
     */
    public void dismiss() {
        if (counter != 10) {
            if (mode == 1) {
                enableAndUpdateButtons();

                createRandNumber();

                summe1 = 0;
                textView1.setText(String.valueOf(summe1));
            } else if (mode == 2) {
                if (playerTurn == PLAYER1) {
                    enableAndUpdateButtons();

                    createRandNumber();

                    playerTurn = PLAYER2;
                    textView_player1.setTextColor(Color.BLACK);
                    textView_player2.setTextColor(Color.RED);
                    elapsedTime = SystemClock.elapsedRealtime() - chronometer1.getBase();
                    chronometer1.stop();

                    chronometer2.setBase(SystemClock.elapsedRealtime() - elapsedTime1);
                    chronometer2.start();
                    summe1 = 0;
                    textView1.setText(String.valueOf(summe1));
                } else if (playerTurn == PLAYER2) {
                    enableAndUpdateButtons();

                    createRandNumber();

                    playerTurn = PLAYER1;
                    textView_player1.setTextColor(Color.BLACK);
                    textView_player1.setTextColor(Color.RED);
                    elapsedTime1 = SystemClock.elapsedRealtime() - chronometer2.getBase();
                    chronometer2.stop();

                    chronometer1.setBase(SystemClock.elapsedRealtime() - elapsedTime);
                    chronometer1.start();
                    summe2 = 0;
                    textView2.setText(String.valueOf(summe2));
                }
            }
        } else {
            textView1.setText("GAME ALREADY WON");
            Log.i(TAG, "GAME ALREADY WON");
        }
    }
}

package fr.pampitux.lupasql.sqlserver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.pampitux.lupasql.R;
import fr.pampitux.lupasql.sqlserver.classes.ConnectSQLSRV;

public class SpashScreen extends AppCompatActivity {

    private final int SPLASH_TIME = 5000; //time of the loading
    ProgressBar progressBar;
    TextView tv;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        progressBar = findViewById(R.id.progressBar);

        tv = findViewById(R.id.lupa);

        Handler handler = new Handler();
        /*
        Check the data saved
        if true the Mainactivity is open
        if false the Identity activity is open
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress < 100) {
                    ++progress;
                    android.os.SystemClock.sleep(25);
                    handler.post(() -> progressBar.setProgress(progress));
                }
                handler.post(() -> {
                    if (ConnectSQLSRV.checkInformation(Identity.getSharedPrefs(Identity.SERIAL, getApplicationContext()),
                            Identity.getSharedPrefs(Identity.NOM, getApplicationContext()),
                            Identity.getSharedPrefs(Identity.PRENOM, getApplicationContext()),
                            Identity.getSharedPrefs(Identity.MATRICULE, getApplicationContext()))) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent2 = new Intent(getApplicationContext(), Identity.class);
                        startActivity(intent2);
                        finish();
                    }
                });
            }
        }).start();

    }
}
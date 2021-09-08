package fr.pampitux.lupasql.sqlserver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.pampitux.lupasql.R;
import fr.pampitux.lupasql.sqlserver.classes.ConnectSQLSRV;

public class Identity extends AppCompatActivity {
    private TextView tv1, tv2, tv3, tv4;
    private EditText et1, et2, et3, et4;
    SharedPreferences sharedPreferences;

    public static final String SERIAL = "serial";
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String MATRICULE = "matricule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);

        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getPreferences(MODE_PRIVATE);

                setSharedPrefs(SERIAL, et1.getText().toString(), getApplicationContext());
                setSharedPrefs(NOM, et2.getText().toString(), getApplicationContext());
                setSharedPrefs(PRENOM, et3.getText().toString(), getApplicationContext());
                setSharedPrefs(MATRICULE, et4.getText().toString(), getApplicationContext());

                if (ConnectSQLSRV.checkInformation(et1.getText().toString(),
                        et2.getText().toString(),
                        et3.getText().toString(),
                        et4.getText().toString())) {
                    confirm();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    annulation();
                }
            }
        });
    }

    /**
     * Method display a confirmation Toas
     */
    public void confirm() {
        Toast.makeText(this, "Donnée sauvegardé avec succès", Toast.LENGTH_LONG).show();
    }

    /**
     * Method display an error Toast
     */
    public void annulation() {
        Toast.makeText(this, "Il y a une erreur de saisie dans vos données", Toast.LENGTH_LONG).show();
    }

    /**
     * Method to save data with SharedPréférences
     *
     * @param key     type de la valeur
     * @param value   lieu où récupérer la valeur
     * @param context le context/l'endroit
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static void setSharedPrefs(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Method to load the data
     *
     * @param key     type de la valeur
     * @param context le context/l'endroit
     * @return retourne la valeur qui a été sauvegardé récédemment
     */
    public static String getSharedPrefs(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
package fr.pampitux.lupasql.sqlserver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.Statement;

import fr.pampitux.lupasql.R;
import fr.pampitux.lupasql.sqlserver.classes.ConnectSQLSRV;

public class MainActivity extends AppCompatActivity {
    private EditText et1, et2;
    private Button btn1, btn2, btn_info;
    private String psw_checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn1 = findViewById(R.id.btn);
        btn2 = findViewById(R.id.btn2);
        btn_info = findViewById(R.id.btn_info);
        et1 = findViewById(R.id.et_psw1);
        et2 = findViewById(R.id.et_psw2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connect_DB = ConnectSQLSRV.connect();
                try {
                    if (checkPasswords(et1.getText().toString(), et2.getText().toString())) {
                        psw_checked = et1.getText().toString();
                        if (checkString(psw_checked)) {
                            if (connect_DB != null) {
                                String query = "UPDATE agent SET password_agent='" + psw_checked + "'WHERE matricule=" + Identity.getSharedPrefs(Identity.MATRICULE, getApplicationContext());
                                Statement st = connect_DB.createStatement();
                                st.executeUpdate(query);
                                conf();
                            }
                        }
                    } else {
                        annulation();
                    }
                } catch (
                        Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(psw_checked, "mdp copié avec succès");
                clipboard.setPrimaryClip(clip);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Pampitux"));
                startActivity(intent);
                finish();
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), BoutonInfo.class);
                startActivity(it);
            }
        });

    }

    public Boolean checkPasswords(String st1, String st2) {
        if (st1.equals(st2)) {
            return true;
        } else {
            return false;
        }
    }

    public void annulation() {
        Toast.makeText(this, "Il y a une erreur de saisie dans vos données", Toast.LENGTH_SHORT).show();
    }

    public void textShort() {
        Toast.makeText(this, "Votre mot de passe ne respecte pas les critères de sécurités", Toast.LENGTH_SHORT).show();
    }

    public void conf() {
        Toast.makeText(this, "MDP reset avec succès", Toast.LENGTH_LONG).show();
    }

    /**
     * Method to check the security password
     * (12 char minimum, one Uppercase, one lowercase, one number and one special char)
     *
     * @param string the string to check
     * @return true if the criteria are good
     */
    public Boolean checkString(String string) {
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
        char currentCharacter;
        int somme = 0;
        int lower_case = 0;
        int upper_case = 0;
        int number_case = 0;
        int special_case = 0;

        for (int i = 0; i < string.length(); i++) {
            currentCharacter = string.charAt(i);
            if (Character.isDigit(currentCharacter)) {
                number_case += 1;
            } else if (Character.isUpperCase(currentCharacter)) {
                upper_case += 1;
            } else if (Character.isLowerCase(currentCharacter)) {
                lower_case += 1;
            } else if (specialChars.contains(String.valueOf(currentCharacter))) {
                special_case += 1;
            }
            somme += 1;
        }
        if (somme < 12 || number_case < 1 || lower_case < 1 || upper_case < 1 || special_case < 1) {
            textShort();
            return false;
        }
        return true;
    }
}

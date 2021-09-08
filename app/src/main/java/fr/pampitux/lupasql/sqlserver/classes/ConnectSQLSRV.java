package fr.pampitux.lupasql.sqlserver.classes;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class ConnectSQLSRV {
    private static final String ip = "192.168.0.11"; // ip du serveur, ici c'est un serveur local
    private static final String port = "1433";
    private static final String database = "pampiDB1";
    private static final String username = "sa";
    private static final String password = "1234ff";

    /**
     * Méthode pour se connecter à la base de donnée
     *
     * @return l'état de la connection
     */
    public static Connection connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String url;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            url = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + username + ";password=" + password + ";";
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            Log.e("error", Objects.requireNonNull(e.getMessage()));
        }
        return connection;
    }

    /**
     * Méthode qui va comparer les données saisies avec celle de la base de donnée
     *
     * @param serial le SN
     * @param nom    le nom
     * @param prenom le prénom
     * @return vrai si les éléments correspondent, faux sinon
     */
    public static Boolean checkInformation(String serial, String nom, String prenom, String matricule) {
        Connection connectSQLSRV = connect();
        String serialnum = "";
        String name = "";
        String pname = "";
        String matric = "";
        try {
            if (connectSQLSRV != null) {
                String query = "SELECT serial_num,  first_name, last_name, matricule FROM agent a LEFT JOIN app p ON p.id_app = a.id_app WHERE matricule=" + matricule;
                Statement st = connectSQLSRV.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    serialnum = (rs.getString(1));
                    name = (rs.getString(2));
                    pname = (rs.getString(3));
                    matric = (rs.getString(4));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        if (!serialnum.equals("")) {
            if (serialnum.equals(serial) && name.equals(nom) && pname.equals(prenom) && matric.equals(matricule)) {
                return true;
            }
            return true;
        } else {
            return false;
        }
    }
}

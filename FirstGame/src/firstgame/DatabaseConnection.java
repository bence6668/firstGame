package firstgame;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class DatabaseConnection {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private boolean isConnected = true;
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static DatabaseConnection instance;
    private Connection connection;
    private Statement statement;
    private final String LINK = "jdbc:mysql://localhost:3306/multilanguage";
    private final String UNAME = "root";
    private final String PASSW = "";
//</editor-fold>
    //Constructor

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(LINK, UNAME, PASSW);
            statement = connection.createStatement();

        } catch (SQLException ex) {
            isConnected = false;
        }
    }

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(char[] password, byte[] salt) {

        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);

        try {

            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();

            return hash;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(e);
        }

        return null;

    }

    public static boolean checkPassword(char[] password, byte[] salt, byte[] expectedHash) {

        if (password == null || salt == null) {
            return false;
        }

        byte[] pwdHash = hashPassword(password, salt);

        for (int i = 0; i < expectedHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) {
                return false;
            }
        }

        return true;
    }

    public static void testPWHash() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = getNextSalt();
        byte[] hashPassword = hashPassword("test".toCharArray(), salt);
        boolean b = checkPassword("test".toCharArray(), salt, hashPassword);
        System.out.println(b);
    }

    public boolean addUser(String name, String surname, String email, String username, String password) {
        byte[] salt = getNextSalt();
        byte[] hash = hashPassword(password.toCharArray(), salt);

        String query = "INSERT INTO user (name, surname, email, username, password, salt, guthaben) VALUES "
                + "(?, ?, ?, ?, ?, ?, 1000)";

        try {

            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, name);
            pst.setString(2, surname);
            pst.setString(3, email);
            pst.setString(4, username);
            pst.setBytes(5, hash);
            pst.setBytes(6, salt);

            pst.execute();

            pst.close();

        } catch (SQLException sqle) {
            System.out.println("---------------------------");
            System.out.println("SQLEXCEPTION: " + sqle);
            return false;
        } catch (Exception e) {
            System.out.println("---------------------------");
            System.out.println("EXCEPTION: " + e);
            return false;
        }
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM user WHERE username LIKE ?";
        byte[] salt = null;
        byte[] hash = null;

        try {

            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                salt = rs.getBytes("salt");
                hash = rs.getBytes("password");
            }

            pst.close();
            rs.close();

            return checkPassword(password.toCharArray(), salt, hash);

        } catch (SQLException sqle) {
            System.out.println("---------------------------");
            System.out.println("SQLEXCEPTION: " + sqle);
            return false;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.out.println("---------------------------");
            System.out.println("EXCEPTION: " + e);
            return false;
        }
    }

    public int getUID(String username) {
        String query = "SELECT * FROM user WHERE username LIKE ?";

        try {

            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("userID");
            }

            pst.close();
            rs.close();

        } catch (SQLException sqle) {
            System.out.println("---------------------------");
            System.out.println("SQLEXCEPTION: " + sqle);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.out.println("---------------------------");
            System.out.println("EXCEPTION: " + e);
        }
        return 0;
    }

    public int getBalance(String username) {
        int id = getUID(username);
        String query = "SELECT * FROM user WHERE userID LIKE ?";

        try {

            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("guthaben");
            }

            pst.close();
            rs.close();

        } catch (SQLException sqle) {
            System.out.println("---------------------------");
            System.out.println("SQLEXCEPTION: " + sqle);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.out.println("---------------------------");
            System.out.println("EXCEPTION: " + e);
        }
        return 0;
    }

    public void updateBalance(int balance, String username) {
        int id = getUID(username);
        String sql = "UPDATE `user` SET `guthaben` = ? WHERE `userID`= ?";
        PreparedStatement pst;
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, balance);
            pst.setInt(2, id);
            pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //<editor-fold defaultstate="collapsed" desc="getter-methods">
    public static DatabaseConnection getInstance() {
        try {
            if (instance == null) {
                instance = new DatabaseConnection();
            }
        } catch (Exception ex) {

        }

        return instance;
    }

    public boolean getIsConnected() {
        return isConnected;
    }
//</editor-fold>
}

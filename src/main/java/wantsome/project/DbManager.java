package wantsome.project;

import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
    private static String dbFile = "C:\\Users\\Marius\\Desktop\\teme_marius_closca\\src\\main\\resources\\proiect\\budget_tracker.db";


    public static void setDbFile(String newDbFile) {
        dbFile = newDbFile;
        System.out.println("Using custom SQLite db file: " + new File(dbFile).getAbsolutePath());
    }


    public static Connection getConnection() throws SQLException {

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setDateStringFormat("yyyy-MM-dd");

        return DriverManager.getConnection("jdbc:sqlite:" + dbFile, config.toProperties());
    }
}

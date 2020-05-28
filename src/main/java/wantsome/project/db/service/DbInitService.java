package wantsome.project.db.service;

import wantsome.project.DbManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitService {

    public static void createMissingTables() {
        String sqlCategories = "create table if not exists categories (" +
                "    id integer primary key autoincrement," +
                "    description text not null," +
                "    type text check (type in ('INCOME', 'EXPENSE')) not null" +
                ")";

        String sqlTransactions = "create table if not exists transactions (" +
                "    id integer primary key autoincrement," +
                "    category_id references categories(id) not null," +
                "    date datetime not null," +
                "    details text," +
                "    amount real not null" +
                ")";

        try (Connection conn = DbManager.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sqlCategories);
            st.execute(sqlTransactions);
        } catch (SQLException e) {
            System.err.println("Error creating missing tables: " + e.getMessage());
        }
    }

    public static void deleteAllTables() {
        try (Connection conn = DbManager.getConnection();
             Statement st = conn.createStatement()) {
            st.execute("drop table if exists transactions");
            st.execute("drop table if exists categories");
        } catch (SQLException e) {
            System.err.println("Error deleting tables: " + e.getMessage());
        }
    }

}

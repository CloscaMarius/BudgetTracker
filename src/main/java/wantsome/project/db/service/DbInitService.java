package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.CategoryDto;
import wantsome.project.db.dto.Type;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitService {

    public static void createMissingTables() {
        String sqlCategories = "create table if not exists categories (" +
                "    id integer primary key autoincrement," +
                "    description text unique not null," +
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

    public static void createTablesAndInitialData() {
        createMissingTables();
        insertInitialData();
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

    private static void insertInitialData() {
        CategoryDao catDao = new CategoryDao();
        if (catDao.getAll().isEmpty()) { //add only if empty db
            catDao.insert(new CategoryDto("Rent", Type.EXPENSE));
            catDao.insert(new CategoryDto("Salary", Type.INCOME));
            catDao.insert(new CategoryDto("Bills", Type.EXPENSE));
            catDao.insert(new CategoryDto("Sales", Type.INCOME));
            catDao.insert(new CategoryDto("Food", Type.EXPENSE));
            catDao.insert(new CategoryDto("Clothes", Type.EXPENSE));
        }
    }

}

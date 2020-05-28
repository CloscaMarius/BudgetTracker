package wantsome.project.db.service;

import wantsome.project.DbManager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class CsvUtils {

    public void insertTransactionsFromCsv() {
        String sql = "INSERT INTO TRANSACTIONS(CATEGORY_ID, DATE, DETAILS, AMOUNT) VALUES(?,?,?,?)";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            File file = new File("src/main/resources/public/Transactions.csv");

            try (Scanner scanner = new Scanner(file)) {
                scanner.useDelimiter(",");
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",\\s*");

                    p.setLong(1, Long.parseLong(parts[0]));
                    p.setDate(2, Date.valueOf(parts[1]));
                    p.setString(3, parts[2]);
                    p.setDouble(4, Double.parseDouble(parts[3]));
                    p.execute();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@insertTransactionsFromCsv): " + e.getLocalizedMessage());
        }
    }

    public void insertCategoriesFromCsv() {
        String sql = "INSERT INTO CATEGORIES(DESCRIPTION, TYPE) VALUES(?,?)";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            File file = new File("src/main/resources/public/Categories.csv");

            try (Scanner scanner = new Scanner(file)) {
                scanner.useDelimiter(",");
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",\\s*");

                    p.setString(1, parts[0]);
                    p.setString(2, parts[1]);
                    p.execute();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@insertCategoriesFromCsv): " + e.getLocalizedMessage());
        }
    }

}

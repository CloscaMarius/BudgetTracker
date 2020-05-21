package wantsome.project.db.service;


import wantsome.project.DbManager;
import wantsome.project.db.dto.TransactionsDto;
import wantsome.project.db.dto.Type;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static wantsome.project.db.service.CategoriesTable.FLD_TYPE;
import static wantsome.project.db.service.TransactionsTable.*;

public class TransactionsDao {

    public List<TransactionsDto> getAllByDateAsc() {

        String sql = "SELECT * FROM " + NAME +
                " ORDER BY " + FLD_DATE + " ASC";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                results.add(getItem(r));
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllByDateAsc): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getAllByDateDesc() {

        String sql = "SELECT * FROM " + NAME +
                " ORDER BY " + FLD_DATE + " DESC";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                results.add(getItem(r));
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllByDateDesc): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getAllByAmountAsc() {

        String sql = "SELECT * FROM " + NAME +
                " ORDER BY " + FLD_AMOUNT + " ASC";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                results.add(getItem(r));
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllByAmountAsc): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getAllByAmountDesc() {

        String sql = "SELECT * FROM " + NAME +
                " ORDER BY " + FLD_AMOUNT + " DESC";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                results.add(getItem(r));
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllByAmountDesc): " + e.getLocalizedMessage());
        }
        return results;
    }

    public Optional<TransactionsDto> getById(long id) {
        String sql = "SELECT * FROM " + NAME + " WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    TransactionsDto item = getItem(r);
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getById): " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public List<TransactionsDto> getByDate(String date) {
        String sql = "SELECT * FROM " + NAME + " WHERE " + FLD_DATE + " = ?";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(date));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByDate): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getByMonth(String month) {
        String sql = "SELECT * FROM" + NAME + "WHERE strftime('%m-%Y'," + FLD_DATE + ") = ?";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, month);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByDate): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getByDate() {
        List<TransactionsDto> results = getAllByDateDesc();
        return results;
    }

    public List<TransactionsDto> getByDateInterval(String dateMin, String dateMax) {
        String sql = "SELECT * FROM " + NAME + " WHERE " + "date >= ?" +
                " and date <= ?";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(dateMin));
            p.setDate(2, Date.valueOf(dateMax));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByDate): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getByAmount(double amount) {
        String sql = "SELECT * FROM " + NAME + " WHERE " + FLD_AMOUNT + " = ?";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDouble(1, amount);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByAmount): " + e.getLocalizedMessage());
        }
        return results;
    }

    private TransactionsDto getItem(ResultSet r) throws SQLException {

        TransactionsDto newItem = new TransactionsDto();
        newItem.setId(r.getLong(FLD_ID));
        newItem.setCategory_id(r.getLong(FLD_CAT_ID));
        newItem.setDate(r.getDate(FLD_DATE));
        newItem.setDetails(r.getString(FLD_DETAILS));
        newItem.setAmount(r.getDouble(FLD_AMOUNT));
        return newItem;
    }

    public void insert(TransactionsDto item) {
        String sql = "INSERT INTO " + NAME + "(" + FLD_CAT_ID + ", " + FLD_DATE + ", "
                + FLD_DETAILS + ", " + FLD_AMOUNT + ")" + " VALUES(?,?,?,?)";


        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, item.getCategory_id());
            p.setDate(2, item.getDate());
            p.setString(3, item.getDetails());
            p.setDouble(4, item.getAmount());
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@insertTransactions): " + e.getLocalizedMessage());
        }
    }

    public void insertTransactionsFromCsv() {
        String sql = "INSERT INTO " + NAME + "(" + FLD_CAT_ID + ", "
                + FLD_DATE + ", " + FLD_DETAILS + ", " + FLD_AMOUNT + ")" +
                " VALUES(?,?,?,?)";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            File file = new File("C:\\Users\\Marius\\Desktop\\teme_marius_closca\\src\\main\\java\\teme\\proiect\\Budget_Tracker\\Transactions.csv");

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

    public void updateDate(long id, String date) {
        String sql = "UPDATE " + NAME + " SET " + FLD_DATE + " = ? WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(date));
            p.setLong(2, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@updateDate): " + e.getLocalizedMessage());
        }
    }

    public void updateDetails(long id, String details) {
        String sql = "UPDATE " + NAME + " SET " + FLD_DETAILS + " = ? WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, details);
            p.setLong(2, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@updateDetails): " + e.getLocalizedMessage());
        }
    }

    public void updateAmount(long id, double amount) {
        String sql = "UPDATE " + NAME + " SET " + FLD_AMOUNT + " = ? WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDouble(1, amount);
            p.setLong(2, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@updateAmount): " + e.getLocalizedMessage());
        }
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM " + NAME + " WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@deleteById): " + e.getLocalizedMessage());
        }
    }

    public void deleteByCat_id(long cat_id) {
        String sql = "DELETE FROM " + NAME + " WHERE " + FLD_CAT_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, cat_id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@deleteByCat_id): " + e.getLocalizedMessage());
        }
    }

    public void deleteByDate(String date) {
        String sql = "DELETE FROM " + NAME + " WHERE " + FLD_DATE + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(date));
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@deleteByDate): " + e.getLocalizedMessage());
        }
    }

    public void deleteByAmount(double amount) {
        String sql = "DELETE FROM " + NAME + " WHERE " + FLD_AMOUNT + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDouble(1, amount);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@deleteByDouble): " + e.getLocalizedMessage());
        }
    }

    public List<TransactionsDto> getIncomeByDateInterval(String dateMin, String dateMax) {
        String sql = "SELECT * FROM " + NAME + " JOIN " + CategoriesTable.NAME + " ON " +
                NAME + "." + FLD_CAT_ID + " = " + CategoriesTable.NAME + "." + CategoriesTable.FLD_ID +
                " WHERE date >= ?" + " AND date <= ?" +
                " AND " + CategoriesTable.NAME + "." + FLD_TYPE + " = ?" +
                " ORDER BY " + FLD_AMOUNT + " DESC";

        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(dateMin));
            p.setDate(2, Date.valueOf(dateMax));
            p.setString(3, String.valueOf(Type.INCOME));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getIncomeByDateInterval): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getAllIncome() {
        String sql = "SELECT * FROM " + NAME + " JOIN " + CategoriesTable.NAME + " ON " +
                NAME + "." + FLD_CAT_ID + " = " + CategoriesTable.NAME + "." + CategoriesTable.FLD_ID +
                " WHERE " + CategoriesTable.NAME + "." + FLD_TYPE + " = ? " +
                " ORDER BY " + FLD_AMOUNT + " DESC";
        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, String.valueOf(Type.INCOME));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllIncome): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getExpensesByDateInterval(String dateMin, String dateMax) {
        String sql = "SELECT * FROM " + NAME + " JOIN " + CategoriesTable.NAME + " ON " +
                NAME + "." + FLD_CAT_ID + " = " + CategoriesTable.NAME + "." + CategoriesTable.FLD_ID +
                " WHERE date >= ?" + " AND date <= ?" +
                " AND " + CategoriesTable.NAME + "." + FLD_TYPE + " = ?" +
                " ORDER BY " + FLD_AMOUNT + " DESC";

        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(dateMin));
            p.setDate(2, Date.valueOf(dateMax));
            p.setString(3, String.valueOf(Type.EXPENSE));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getExpensesByDateInterval): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionsDto> getAllExpenses() {
        String sql = "SELECT * FROM " + NAME + " JOIN " + CategoriesTable.NAME + " ON " +
                NAME + "." + FLD_CAT_ID + " = " + CategoriesTable.NAME + "." + CategoriesTable.FLD_ID +
                " WHERE " + CategoriesTable.NAME + "." + FLD_TYPE + " = ? " +
                " ORDER BY " + FLD_AMOUNT + " DESC";

        List<TransactionsDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, String.valueOf(Type.EXPENSE));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllExpenses): " + e.getLocalizedMessage());
        }
        return results;
    }

    public double incomeByDateInterval(String dateMin, String dateMax) {
        double totalIncome = 0;

        totalIncome = getIncomeByDateInterval(dateMin, dateMax)
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble(df.format(totalIncome));
    }

    public double allIncome() {
        double totalIncome = 0;

        totalIncome = getAllIncome()
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");


        return Double.parseDouble(df.format(totalIncome));

    }

    public double expensesByDateInterval(String dateMin, String dateMax) {
        double totalExpenses = 0;

        totalExpenses = getExpensesByDateInterval(dateMin, dateMax)
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");


        return Double.parseDouble(df.format(totalExpenses));


    }

    public double allExpenses() {
        double totalExpenses = 0;

        totalExpenses = getAllExpenses()
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");


        return Double.parseDouble(df.format(totalExpenses));

    }

    public double balanceByDateInterval(String dateMin, String dateMax) {
        double balance = 0;

        balance = incomeByDateInterval(dateMin, dateMax) - expensesByDateInterval(dateMin, dateMax);

        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble(df.format(balance));
    }

    public double balance() {
        double balance = 0;

        balance = allIncome() - allExpenses();


        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble(df.format(balance));


    }

}

package wantsome.project.db.service;


import wantsome.project.DbManager;
import wantsome.project.db.dto.TransactionDto;
import wantsome.project.db.dto.TransactionFullDto;
import wantsome.project.db.dto.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDao {

    public List<TransactionFullDto> getAllFull() {

        String sql = "SELECT T.*, " +
                "C.DESCRIPTION AS CATEGORY_DESCRIPTION, " +
                "C.TYPE AS CATEGORY_TYPE " +
                "FROM TRANSACTIONS T " +
                "JOIN CATEGORIES C ON T.CATEGORY_ID = C.ID " +
                "ORDER BY DATE DESC";

        List<TransactionFullDto> transactions = new ArrayList<>();

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet r = ps.executeQuery()) {

            while (r.next()) {

                transactions.add(
                        new TransactionFullDto(
                                r.getLong("ID"),
                                r.getLong("CATEGORY_ID"),
                                r.getString("CATEGORY_DESCRIPTION"),
                                Type.valueOf(r.getString("CATEGORY_TYPE")),
                                r.getDate("DATE"),
                                r.getString("DETAILS"),
                                r.getDouble("AMOUNT")));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all transactions: " + e.getMessage());
        }

        return transactions;
    }

    public List<TransactionDto> getAll() {

        String sql = "SELECT * FROM TRANSACTIONS";
        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                results.add(
                        new TransactionDto(
                                r.getLong("ID"),
                                r.getLong("CATEGORY_ID"),
                                r.getDate("DATE"),
                                r.getString("DETAILS"),
                                r.getDouble("AMOUNT")));
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAll): " + e.getLocalizedMessage());
        }
        return results;
    }

    public Optional<TransactionDto> getById(long id) {
        String sql = "SELECT * FROM TRANSACTIONS WHERE ID = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    TransactionDto item = new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT"));
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getById): " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public List<TransactionDto> getByDate(String date) {
        String sql = "SELECT * FROM TRANSACTIONS WHERE DATE = ?";
        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(date));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT")));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByDate): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionDto> getByDateInterval(Date dateMin, Date dateMax) {
        String sql = "SELECT * FROM TRANSACTIONS WHERE DATE >= ? AND DATE <= ?";
        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, dateMin);
            p.setDate(2, dateMax);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT")));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByDate): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionDto> getByAmount(double amount) {
        String sql = "SELECT * FROM TRANSACTIONS WHERE AMOUNT = ?";
        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDouble(1, amount);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT")));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByAmount): " + e.getLocalizedMessage());
        }
        return results;
    }

    public void insert(TransactionDto item) {
        String sql = "INSERT INTO TRANSACTIONS(CATEGORY_ID, DATE, DETAILS, AMOUNT) VALUES(?,?,?,?)";


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

    public void update(TransactionDto item) {

        String sql = "UPDATE TRANSACTIONS " +
                "SET CATEGORY_ID = ?, " +
                "DATE = ?, " +
                "DETAILS = ?, " +
                "AMOUNT = ? " +
                "WHERE ID = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, item.getCategory_id());
            ps.setDate(2, item.getDate());
            ps.setString(3, item.getDetails());
            ps.setDouble(4, item.getAmount());
            ps.setLong(5, item.getId());

            //execute it (no results to read after this one)
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating transaction " + item + " : " + e.getMessage());
        }
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM TRANSACTIONS WHERE ID = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@deleteById): " + e.getLocalizedMessage());
        }
    }

    public List<TransactionDto> getIncomeByDateInterval(Date dateMin, Date dateMax) {
        String sql = "SELECT * FROM TRANSACTIONS JOIN CATEGORIES ON TRANSACTIONS.CATEGORY_ID = CATEGORIES.ID" +
                " WHERE DATE >= ? AND DATE <= ?" +
                " AND CATEGORIES.TYPE = ?" +
                " ORDER BY AMOUNT DESC";

        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, dateMin);
            p.setDate(2, dateMax);
            p.setString(3, String.valueOf(Type.INCOME));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT")));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getIncomeByDateInterval): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionDto> getAllIncome() {
        String sql = "SELECT * FROM TRANSACTIONS JOIN CATEGORIES ON TRANSACTIONS.CATEGORY_ID = CATEGORIES.ID" +
                " WHERE CATEGORIES.TYPE = ? " +
                " ORDER BY AMOUNT DESC";
        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, String.valueOf(Type.INCOME));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT")));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllIncome): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionDto> getExpensesByDateInterval(Date dateMin, Date dateMax) {
        String sql = "SELECT * FROM TRANSACTIONS JOIN CATEGORIES ON TRANSACTIONS.CATEGORY_ID = CATEGORIES.ID" +
                " WHERE DATE >= ? AND DATE <= ?" +
                " AND CATEGORIES.TYPE = ?" +
                " ORDER BY AMOUNT DESC";

        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, dateMin);
            p.setDate(2, dateMax);
            p.setString(3, String.valueOf(Type.EXPENSE));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT")));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getExpensesByDateInterval): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<TransactionDto> getAllExpenses() {
        String sql = "SELECT * FROM TRANSACTIONS JOIN CATEGORIES ON TRANSACTIONS.CATEGORY_ID = CATEGORIES.ID" +
                " WHERE CATEGORIES.TYPE = ? " +
                " ORDER BY AMOUNT DESC";

        List<TransactionDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, String.valueOf(Type.EXPENSE));
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(new TransactionDto(
                            r.getLong("ID"),
                            r.getLong("CATEGORY_ID"),
                            r.getDate("DATE"),
                            r.getString("DETAILS"),
                            r.getDouble("AMOUNT")));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAllExpenses): " + e.getLocalizedMessage());
        }
        return results;
    }


}

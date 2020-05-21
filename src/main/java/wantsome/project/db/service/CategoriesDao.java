package wantsome.project.db.service;


import wantsome.project.DbManager;
import wantsome.project.db.dto.CategoriesDto;
import wantsome.project.db.dto.Type;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static wantsome.project.db.service.CategoriesTable.*;

public class CategoriesDao {

    public List<CategoriesDto> getAll() {

        String sql = "SELECT * FROM " + NAME +
                " ORDER BY " + FLD_TYPE;
        List<CategoriesDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                results.add(getItem(r));
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAll): " + e.getLocalizedMessage());
        }
        return results;
    }

    public Optional<CategoriesDto> getById(long id) {
        String sql = "SELECT * FROM " + NAME + " WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    CategoriesDto item = getItem(r);
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getById): " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public List<CategoriesDto> getByType(Type type) {
        String sql = "SELECT * FROM " + NAME + " WHERE " + FLD_TYPE + " = ?";
        List<CategoriesDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, type.name());
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByType): " + e.getLocalizedMessage());
        }
        return results;
    }

    public List<CategoriesDto> getByDescription(String description) {
        String sql = "SELECT * FROM " + NAME + " WHERE " + FLD_DESC + " = ?";
        List<CategoriesDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, description.toLowerCase().trim());
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    results.add(getItem(r));
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getByDescription): " + e.getLocalizedMessage());
        }
        return results;
    }

    public void insert(CategoriesDto item) {
        String sql = "INSERT INTO " + NAME + "(" + FLD_DESC + ", "
                + FLD_TYPE + ")" + " VALUES(?,?)";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, item.getDescription());
            p.setString(2, item.getType().name());
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@insertCategories): " + e.getLocalizedMessage());
        }
    }

    public void insertCategoriesFromCsv() {
        String sql = "INSERT INTO " + NAME + "(" + FLD_DESC + ", "
                + FLD_TYPE + ")" +
                " VALUES(?,?)";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            File file = new File("C:\\Users\\Marius\\Desktop\\teme_marius_closca\\src\\main\\java\\teme\\proiect\\Budget_Tracker\\Categories.csv");

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
            System.err.println("An error occurred(@insertTransactionsFromCsv): " + e.getLocalizedMessage());
        }
    }

    public void updateDescription(long id, String description) {
        String sql = "UPDATE " + NAME + " SET " + FLD_DESC + " = ? WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, description);
            p.setLong(2, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@updateDescription): " + e.getLocalizedMessage());
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM " + NAME + " WHERE " + FLD_ID + " = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@delete): " + e.getLocalizedMessage());
        }
    }

    private CategoriesDto getItem(ResultSet r) throws SQLException {

        CategoriesDto newItem = new CategoriesDto();
        newItem.setId(r.getLong(FLD_ID));
        newItem.setDescription(r.getString(FLD_DESC));
        newItem.setType(Type.valueOf(r.getString(FLD_TYPE)));
        return newItem;
    }

}

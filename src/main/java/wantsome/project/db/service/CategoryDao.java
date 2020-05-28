package wantsome.project.db.service;


import wantsome.project.DbManager;
import wantsome.project.db.dto.CategoryDto;
import wantsome.project.db.dto.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDao {

    public List<CategoryDto> getAll() {

        String sql = "SELECT * FROM CATEGORIES ORDER BY TYPE";
        List<CategoryDto> results = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                results.add(
                        new CategoryDto(
                                r.getLong("ID"),
                                r.getString("DESCRIPTION"),
                                Type.valueOf(r.getString("TYPE"))));
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getAll): " + e.getLocalizedMessage());
        }
        return results;
    }

    public Optional<CategoryDto> getById(long id) {
        String sql = "SELECT * FROM CATEGORIES WHERE ID = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    CategoryDto item = new CategoryDto(
                            r.getLong("ID"),
                            r.getString("DESCRIPTION"),
                            Type.valueOf(r.getString("TYPE")));
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred(@getById): " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public void insert(CategoryDto item) {
        String sql = "INSERT INTO CATEGORIES(DESCRIPTION, TYPE) VALUES(?,?)";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, item.getDescription());
            p.setString(2, item.getType().name());
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@insertCategories): " + e.getLocalizedMessage());
        }
    }

    public void update(CategoryDto item) {

        String sql = "UPDATE CATEGORIES " +
                "SET DESCRIPTION = ?, " +
                "TYPE = ? " +
                "WHERE ID = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getDescription());
            ps.setString(2, item.getType().name());
            ps.setLong(3, item.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating category " + item + " : " + e.getMessage());
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM CATEGORIES WHERE ID = ?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.execute();
        } catch (SQLException e) {
            System.err.println("An error occurred(@delete): " + e.getLocalizedMessage());
        }
    }
}

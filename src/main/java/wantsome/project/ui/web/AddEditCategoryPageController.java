package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.CategoryDto;
import wantsome.project.db.dto.Type;
import wantsome.project.db.service.CategoryDao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static wantsome.project.ui.web.SparkUtil.render;

/**
 * Handles the user interaction for adding or updating a category.
 */
public class AddEditCategoryPageController {

    private static final CategoryDao catDao = new CategoryDao();

    public static String showAddForm(Request req, Response res) {
        return renderAddUpdateForm("", "", Type.EXPENSE.name(), "");
    }

    public static String showUpdateForm(Request req, Response res) {
        String id = req.params("id");
        try {
            Optional<CategoryDto> optCat = catDao.getById(Integer.parseInt(id));
            if (optCat.isPresent()) {
                CategoryDto cat = optCat.get();
                return renderAddUpdateForm(String.valueOf(cat.getId()),
                        cat.getDescription(),
                        cat.getType().name(),
                        "");
            }
        } catch (Exception e) {
            System.err.println("Error loading category " + id + ": " + e.getMessage());
        }
        return "Error: category " + id + " not found!";
    }

    private static String renderAddUpdateForm(String id, String description, String type, String errorMessage) {
        Map<String, Object> model = new HashMap<>();
        model.put("prevId", id);
        model.put("prevDescription", description);
        model.put("prevType", type);
        model.put("errorMsg", errorMessage);
        model.put("isUpdate", id != null && !id.isEmpty());
        return render(model, "add_edit_category.vm");
    }

    public static Object handleAddUpdateRequest(Request req, Response res) {

        String id = req.queryParams("id");
        String description = req.queryParams("description");
        String type = req.queryParams("type");

        try {
            CategoryDto cat = validateAndBuildCategory(id, description, type);

            if (id != null && !id.isEmpty()) {
                catDao.update(cat);
            } else {
                catDao.insert(cat);
            }

            res.redirect("/categories");
            return res;

        } catch (Exception e) {
            return renderAddUpdateForm(id, description, type, e.getMessage());
        }
    }

    private static CategoryDto validateAndBuildCategory(String id, String description, String type) {
        long idValue = id != null && !id.isEmpty() ? Long.parseLong(id) : -1;

        if (description == null || description.isEmpty()) {
            throw new RuntimeException("Description is required!");
        } else if (catDao.getAll().stream().map(i -> i.getDescription()).anyMatch(i -> i.equalsIgnoreCase(description))) {
            throw new RuntimeException("Please input a distinct description!");
        }

        if (type == null || type.isEmpty()) {
            throw new RuntimeException("Priority is required!");
        }
        Type typeValue;
        try {
            typeValue = Type.valueOf(type);
        } catch (Exception e) {
            throw new RuntimeException("Invalid priority value: '" + type +
                    "', must be one of: " + Arrays.toString(Type.values()));
        }
        return new CategoryDto(idValue, description, typeValue);
    }
}

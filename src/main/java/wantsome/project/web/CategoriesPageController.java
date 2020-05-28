package wantsome.project.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.CategoryDto;
import wantsome.project.db.dto.Type;
import wantsome.project.db.service.CategoryDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static wantsome.project.web.SparkUtil.render;

public class CategoriesPageController {

    public enum SortedBy {
        ID_ASC,
        ID_DESC
    }

    private static final CategoryDao catDao = new CategoryDao();

    public static String showCategoriesPage(Request req, Response res) {

        SortedBy sortedBy = getSortedByFromParamOrSes(req);

        Type type = getTypeFromParamOrSes(req);

        List<CategoryDto> categories = getCategoriesToDisplay(catDao.getAll(), type, sortedBy);


        Map<String, Object> model = new HashMap<>();
        model.put("categories", categories);
        model.put("sortedBy", sortedBy);
        model.put("type", type);


        return render(model, "categories.vm");
    }

    public static Object handleDeleteRequest(Request req, Response res) {
        String id = req.params("id");
        try {
            catDao.delete(Long.parseLong(id));
        } catch (Exception e) {
            System.out.println("Error deleting category with id '" + id + "': " + e.getMessage());
        }
        res.redirect("/categories");
        return res;
    }

    private static SortedBy getSortedByFromParamOrSes(Request req) {
        String param = req.queryParams("sortedBy");
        if (param != null) {
            req.session().attribute("sortedBy", param);
        } else {
            param = req.session().attribute("sortedBy");
        }
        return param != null ? SortedBy.valueOf(param) : SortedBy.ID_ASC;
    }

    private static Type getTypeFromParamOrSes(Request req) {
        String param = req.queryParams("type");
        if (param != null) {
            req.session().attribute("type", param);
        } else {
            param = req.session().attribute("type");
        }
        return param != null ? Type.valueOf(param) : Type.ALL;
    }

    private static List<CategoryDto> getCategoriesToDisplay(List<CategoryDto> allCategories, Type
            type, SortedBy sortedBy) {

        List<CategoryDto> categories = allCategories;


        if (type == (Type.INCOME)) {
            categories = allCategories.stream()
                    .filter(i -> i.getType() == Type.INCOME)
                    .collect(toList());

        } else if (type == (Type.EXPENSE)) {
            categories = allCategories.stream()
                    .filter(i -> i.getType() == Type.EXPENSE)
                    .collect(toList());
        } else {

        }

        return categories.stream()
                .sorted((n1, n2) -> {
                    if (sortedBy == SortedBy.ID_ASC) {
                        return Long.compare(n1.getId(), n2.getId());
                    } else {
                        return Long.compare(n2.getId(), n1.getId());
                    }
                })
                .collect(toList());

    }


}

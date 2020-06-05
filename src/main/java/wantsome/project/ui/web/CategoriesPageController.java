package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.CategoryDto;
import wantsome.project.db.dto.TransactionDto;
import wantsome.project.db.dto.Type;
import wantsome.project.db.service.CategoryDao;
import wantsome.project.db.service.TransactionDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static wantsome.project.ui.web.SparkUtil.render;

public class CategoriesPageController {

    public enum SortedBy {
        ID_ASC,
        ID_DESC,
        DESCR_ASC,
        DESCR_DESC
    }

    private static final CategoryDao catDao = new CategoryDao();
    private static final TransactionDao transDao = new TransactionDao();

    public static String showCategoriesPage(Request req, Response res) {

        SortedBy sortedBy = getSortedByFromParamOrSes(req);

        Type type = getTypeFromParamOrSes(req);


        List<CategoryDto> categories = getCategoriesToDisplay(catDao.getAll(), type, sortedBy);

        List<TransactionDto> transactions = transDao.getAll();

        List<Long> transCatId = transDao.getAll()
                .stream()
                .map(i -> i.getCategory_id())
                .collect(Collectors.toList());

        /*List<Long> transDto = transDao.getAll().stream().map(TransactionDto::getCategory_id).collect(toList());
        List<Long> catDto = catDao.getAll().stream().map(CategoryDto::getId).collect(toList());

        boolean canBeDeleted = transDao.getAll().stream().map(i->i.getCategory_id()).anyMatch(t->t.equals(getId));
*/


        Map<String, Object> model = new HashMap<>();
        model.put("categories", categories);
        model.put("transactions", transactions);
        model.put("transCatId", transCatId);
        model.put("sortedBy", sortedBy);
        model.put("type", type);
        /*model.put("canBeDeleted", canBeDeleted);*/


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
                    } else if (sortedBy == SortedBy.ID_DESC) {
                        return Long.compare(n2.getId(), n1.getId());
                    } else if (sortedBy == SortedBy.DESCR_ASC) {
                        return n1.getDescription().compareTo(n2.getDescription());
                    } else {
                        return n2.getDescription().compareTo(n1.getDescription());
                    }
                })
                .collect(toList());

    }


}

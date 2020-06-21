package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.TransactionFullDto;
import wantsome.project.db.dto.Type;
import wantsome.project.db.service.TransactionDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static wantsome.project.ui.web.SparkUtil.render;

public class TransactionsPageController {

    public enum SortBy {
        DATE_DESC,
        DATE_ASC,
        AMOUNT_DESC,
        AMOUNT_ASC,
        CATEGORY_DESC,
        CATEGORY_ASC,
    }

    private static final TransactionDao transacDao = new TransactionDao();

    public static String showTransactionsPage(Request req, Response res) {

        List<TransactionFullDto> allTransactions = transacDao.getAllFull();

        double balance = TransactionStats.balance();

        Type type = getTypeFromParamOrSes(req);

        SortBy sortBy = getSortByFromParamOrSes(req);

        String category = getCategoryFromParamOrSes(req);

        List<TransactionFullDto> transactions = getTransactionsToDisplay(allTransactions, sortBy, type, category);

        List<String> allDistinctTransactions = transacDao.getAllFull()
                .stream().map(n -> n.getCategory_description()).distinct().collect(Collectors.toList());

        boolean isNegative = balance < 0;

        Map<String, Object> model = new HashMap<>();
        model.put("transactions", transactions);
        model.put("allDistinctTransactions", allDistinctTransactions);
        model.put("category", category);
        model.put("balance", balance);
        model.put("isNegative", isNegative);
        model.put("sortBy", sortBy);
        model.put("type", type);
        return render(model, "/transactions.vm");
    }

    private static List<TransactionFullDto> getTransactionsToDisplay
            (List<TransactionFullDto> allTransactions, SortBy sortBy, Type type, String category) {
        List<TransactionFullDto> transactions = allTransactions;

        if (allTransactions.stream().anyMatch(i -> i.getCategory_description().equalsIgnoreCase(category))) {
            transactions = allTransactions.stream()
                    .filter(i -> i.getCategory_description().equalsIgnoreCase(category))
                    .collect(toList());
        } else if (type == (Type.INCOME)) {
            transactions = allTransactions.stream()
                    .filter(i -> i.getCategory_type() == Type.INCOME)
                    .collect(toList());

        } else if (type == (Type.EXPENSE)) {
            transactions = allTransactions.stream()
                    .filter(i -> i.getCategory_type() == Type.EXPENSE)
                    .collect(toList());
        }


        return transactions.stream()
                .sorted((n1, n2) -> {
                    if (sortBy == SortBy.DATE_DESC) {
                        return n2.getDate().compareTo(n1.getDate());
                    } else if (sortBy == SortBy.DATE_ASC) {
                        return n1.getDate().compareTo(n2.getDate());
                    } else if (sortBy == SortBy.AMOUNT_DESC) {
                        return Double.compare(n2.getAmount(), n1.getAmount());
                    } else if (sortBy == SortBy.AMOUNT_ASC) {
                        return Double.compare(n1.getAmount(), n2.getAmount());
                    } else if (sortBy == SortBy.CATEGORY_DESC) {
                        return n2.getCategory_description().compareTo(n1.getCategory_description());
                    } else {
                        return n1.getCategory_description().compareTo(n2.getCategory_description());

                    }
                })
                .collect(toList());

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

    private static SortBy getSortByFromParamOrSes(Request req) {
        String param = req.queryParams("sortBy");
        if (param != null) {
            req.session().attribute("sortBy", param);
        } else {
            param = req.session().attribute("sortBy");
        }
        return param != null ? SortBy.valueOf(param) : SortBy.DATE_DESC;
    }

    private static String getCategoryFromParamOrSes(Request req) {
        String param = req.queryParams("category");
        if (param != null) {
            req.session().attribute("category", param);
        } else {
            param = req.session().attribute("category");
        }
        return param != null ? param : "all";
    }

    public static Object handleDeleteRequest(Request req, Response res) {
        String id = req.params("id");
        try {
            transacDao.deleteById(Long.parseLong(id));
        } catch (Exception e) {
            System.out.println("Error deleting transaction with id '" + id + "': " + e.getMessage());
        }
        res.redirect("/main");
        return res;
    }

}



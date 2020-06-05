package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.TransactionFullDto;
import wantsome.project.db.service.TransactionDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static wantsome.project.ui.web.SparkUtil.render;

public class TransactionsPageController {

    public enum SortBy {
        DATE_DESC,
        DATE_ASC,
        AMOUNT_DESC,
        AMOUNT_ASC,
    }

    private static final TransactionDao transacDao = new TransactionDao();

    public static String showTransactionsPage(Request req, Response res) {

        List<TransactionFullDto> allTransactions = transacDao.getAllFull();

        double balance = TransactionStats.balance();

        String type = getTypeFromParamOrSes(req);

        SortBy sortBy = getSortByFromParamOrSes(req);

        List<TransactionFullDto> transactions = getTransactionsToDisplay(allTransactions, sortBy);

        Map<String, Object> model = new HashMap<>();
        model.put("transactions", transactions);
        model.put("balance", balance);
        model.put("sortBy", sortBy);
        model.put("type", type);
        return render(model, "/transactions.vm");
    }

    private static List<TransactionFullDto> getTransactionsToDisplay(List<TransactionFullDto> allTransactions, SortBy sortBy) {
        List<TransactionFullDto> transactions = allTransactions;

        return transactions.stream()
                .sorted((n1, n2) -> {
                    if (sortBy == SortBy.DATE_DESC) {
                        return n2.getDate().compareTo(n1.getDate());
                    } else if (sortBy == SortBy.DATE_ASC) {
                        return n1.getDate().compareTo(n2.getDate());
                    } else if (sortBy == SortBy.AMOUNT_DESC) {
                        return Double.compare(n2.getAmount(), n1.getAmount());
                    } else {
                        return Double.compare(n1.getAmount(), n2.getAmount());
                    }
                })
                .collect(toList());

    }

    private static String getTypeFromParamOrSes(Request req) {
        String param = req.queryParams("type");
        if (param != null) {
            req.session().attribute("type", param);
        } else {
            param = req.session().attribute("type");
        }
        return param;
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



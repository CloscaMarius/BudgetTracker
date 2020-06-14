package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.TransactionFullDto;
import wantsome.project.db.dto.Type;
import wantsome.project.db.service.TransactionDao;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static wantsome.project.ui.web.SparkUtil.render;

public class ReportsPageController {

    public enum Sorted {
        DATE_DESC,
        DATE_ASC,
        AMOUNT_DESC,
        AMOUNT_ASC,
    }

    private static final TransactionDao transacDao = new TransactionDao();

    public static String showReportsPage(Request req, Response res) {

        List<TransactionFullDto> allTransactions = transacDao.getAllFull();

        Type type = getTypeFromParamOrSes(req);

        Sorted sorted = getSortedFromParamOrSes(req);

        String date1 = getDate1FromParamOrSes(req);
        String date2 = getDate2FromParamOrSes(req);

        double balance = getAmountByType(allTransactions, Type.ALL, sorted, date1, date2);
        double income = getAmountByType(allTransactions, Type.INCOME, sorted, date1, date2);
        double expense = getAmountByType(allTransactions, Type.EXPENSE, sorted, date1, date2);

        List<TransactionFullDto> transactions = getTransactionsToDisplay(allTransactions, type, sorted, date1, date2);

        Map<String, Double> incomeCategories = getGroupedCategories(Type.INCOME, date1, date2);

        Map<String, Double> expenseCategories = getGroupedCategories(Type.EXPENSE, date1, date2);

        Map<String, Object> model = new HashMap<>();
        model.put("transactions", transactions);
        model.put("balance", balance);
        model.put("sorted", sorted);
        model.put("category_type", type);
        model.put("date1", date1);
        model.put("date2", date2);
        model.put("income", income);
        model.put("expense", expense);
        model.put("incomeCategories", incomeCategories);
        model.put("expenseCategories", expenseCategories);
        return render(model, "/reports.vm");
    }

    private static Map<String, Double> getGroupedCategories(Type type, String date1, String date2) {

        List<TransactionFullDto> allTransactions = transacDao.getAllFull();


        if ((date1 != null && !date1.isEmpty()) && (date2 != null && !date2.isEmpty())) {

            return allTransactions.stream()
                    .filter(i -> i.getCategory_type() == type)
                    .filter(i -> i.getDate().equals(Date.valueOf(date1)) ||
                            i.getDate().after(Date.valueOf(date1)) &&
                                    i.getDate().before(Date.valueOf(date2)) ||
                            i.getDate().equals(Date.valueOf(date2)))
                    .collect(groupingBy(n -> n.getCategory_description(), summingDouble(n -> n.getAmount())));


        } else if ((date1 != null && !date1.isEmpty())) {

            return allTransactions.stream()
                    .filter(i -> i.getCategory_type() == type)
                    .filter(i -> i.getDate().equals(Date.valueOf(date1)) ||
                            i.getDate().after(Date.valueOf(date1)))
                    .collect(groupingBy(n -> n.getCategory_description(), summingDouble(n -> n.getAmount())));

        } else if ((date2 != null && !date2.isEmpty())) {

            return allTransactions.stream()
                    .filter(i -> i.getCategory_type() == type)
                    .filter(i -> i.getDate().before(Date.valueOf(date2)) ||
                            i.getDate().equals(Date.valueOf(date2)))
                    .collect(groupingBy(n -> n.getCategory_description(), summingDouble(n -> n.getAmount())));
        } else {
            return allTransactions.stream()
                    .filter(i -> i.getCategory_type() == type)
                    .collect(groupingBy(n -> n.getCategory_description(), summingDouble(n -> n.getAmount())));
        }
    }

    private static double getAmountByType(List<TransactionFullDto> allTransactions, Type type, Sorted sorted, String date1, String date2) {
        double amount = 0;

        if ((date1 != null && !date1.isEmpty()) && (date2 != null && !date2.isEmpty()) && (type == Type.INCOME)) {

            amount = TransactionStats.incomeByDateInterval(Date.valueOf(date1), Date.valueOf(date2));

        } else if ((date1 != null && !date1.isEmpty()) && (date2 != null && !date2.isEmpty()) && (type == Type.EXPENSE)) {

            amount = TransactionStats.expensesByDateInterval(Date.valueOf(date1), Date.valueOf(date2));

        } else if ((date1 != null && !date1.isEmpty()) && (date2 != null && !date2.isEmpty()) && (type == Type.ALL)) {

            amount = TransactionStats.balanceByDateInterval(Date.valueOf(date1), Date.valueOf(date2));


        } else if ((type == Type.EXPENSE)) {

            amount = getTransactionsToDisplay(allTransactions, type, sorted, date1, date2)
                    .stream()
                    .filter(i -> i.getCategory_type() == Type.EXPENSE)
                    .map(i -> i.getAmount())
                    .reduce((double) 0, (a, b) -> a + b);

        } else if ((type == Type.INCOME)) {
            amount = getTransactionsToDisplay(allTransactions, type, sorted, date1, date2)
                    .stream()
                    .filter(i -> i.getCategory_type() == Type.INCOME)
                    .map(i -> i.getAmount())
                    .reduce((double) 0, (a, b) -> a + b);
        } else if ((type == Type.ALL)) {
            amount = getAmountByType(allTransactions, Type.INCOME, sorted, date1, date2) -
                    getAmountByType(allTransactions, Type.EXPENSE, sorted, date1, date2);

        }

        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(amount));

    }

    private static List<TransactionFullDto> getTransactionsToDisplay
            (List<TransactionFullDto> allTransactions, Type type, Sorted sorted, String date1, String date2) {
        List<TransactionFullDto> transactions = allTransactions;


        if ((date1 != null && !date1.isEmpty()) && (date2 != null && !date2.isEmpty())) {
            transactions = allTransactions.stream()
                    .filter(i -> i.getDate().equals(Date.valueOf(date1)) ||
                            i.getDate().after(Date.valueOf(date1)) &&
                                    i.getDate().before(Date.valueOf(date2)) ||
                            i.getDate().equals(Date.valueOf(date2)))
                    .collect(toList());
        } else if ((date1 != null && !date1.isEmpty())) {
            transactions = allTransactions.stream()
                    .filter(i -> i.getDate().equals(Date.valueOf(date1)) ||
                            i.getDate().after(Date.valueOf(date1)))
                    .collect(toList());
        } else if (date2 != null && !date2.isEmpty()) {
            transactions = allTransactions.stream()
                    .filter(i -> i.getDate().before(Date.valueOf(date2)) ||
                            i.getDate().equals(Date.valueOf(date2)))
                    .collect(toList());
        }

        if (type == (Type.INCOME)) {
            transactions = transactions.stream()
                    .filter(i -> i.getCategory_type() == Type.INCOME)
                    .collect(toList());

        } else if (type == (Type.EXPENSE)) {
            transactions = transactions.stream()
                    .filter(i -> i.getCategory_type() == Type.EXPENSE)
                    .collect(toList());
        }


        return transactions.stream()
                .sorted((n1, n2) -> {
                    if (sorted == Sorted.DATE_DESC) {
                        return n2.getDate().compareTo(n1.getDate());
                    } else if (sorted == Sorted.DATE_ASC) {
                        return n1.getDate().compareTo(n2.getDate());
                    } else if (sorted == Sorted.AMOUNT_DESC) {
                        return Double.compare(n2.getAmount(), n1.getAmount());
                    } else {
                        return Double.compare(n1.getAmount(), n2.getAmount());
                    }
                })
                .collect(toList());

    }

    private static Type getTypeFromParamOrSes(Request req) {
        String param = req.queryParams("category_type");
        if (param != null) {
            req.session().attribute("category_type", param);
        } else {
            param = req.session().attribute("category_type");
        }
        return param != null ? Type.valueOf(param) : Type.ALL;
    }

    private static Sorted getSortedFromParamOrSes(Request req) {
        String param = req.queryParams("sorted");
        if (param != null) {
            req.session().attribute("sorted", param);
        } else {
            param = req.session().attribute("sorted");
        }
        return param != null ? Sorted.valueOf(param) : Sorted.DATE_DESC;
    }

    private static String getDate1FromParamOrSes(Request req) {
        String param = req.queryParams("date1");
        if (param != null) {
            req.session().attribute("date1", param);
        } else {
            param = req.session().attribute("date1");
        }
        return param;

    }

    private static String getDate2FromParamOrSes(Request req) {
        String param = req.queryParams("date2");
        if (param != null) {
            req.session().attribute("date2", param);
        } else {
            param = req.session().attribute("date2");
        }
        return param;
    }

}



package wantsome.project.ui.web;

import spark.Request;
import spark.Response;
import wantsome.project.db.dto.TransactionDto;
import wantsome.project.db.service.CategoryDao;
import wantsome.project.db.service.TransactionDao;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static wantsome.project.ui.web.SparkUtil.render;

/**
 * Handles the user interaction for adding or updating a transaction.
 */
public class AddEditTransactionPageController {

    private static final TransactionDao transactionDao = new TransactionDao();
    private static final CategoryDao categDao = new CategoryDao();

    public static String showAddForm(Request req, Response res) {
        return renderAddUpdateForm("", "", "", "", 0.0, "");
    }

    public static String showUpdateForm(Request req, Response res) {
        String id = req.params("id");

            Optional<TransactionDto> optTransaction = transactionDao.getById(Integer.parseInt(id));
            if (optTransaction.isPresent()) {
                TransactionDto transaction = optTransaction.get();
                return renderAddUpdateForm(
                        String.valueOf(transaction.getId()),
                        String.valueOf(transaction.getCategory_id()),
                        transaction.getDate().toString(),
                        transaction.getDetails() != null ? transaction.getDetails().toString() : "",
                        transaction.getAmount(),
                        "");
            }
        throw new RuntimeException("Transaction " + id + " not found!");
    }

    private static String renderAddUpdateForm(String id, String prevCategoryId,
                                              String date, String details, double amount,
                                              String errorMessage) {
        Map<String, Object> model = new HashMap<>();
        model.put("prevId", id);
        model.put("prevCategoryId", prevCategoryId);
        model.put("prevDate", date);
        model.put("prevDetails", details);
        model.put("prevAmount", amount);
        model.put("errorMsg", errorMessage);
        model.put("isUpdate", id != null && !id.isEmpty());
        model.put("categories", categDao.getAll());
        return render(model, "add_edit_transaction.vm");
    }

    public static Object handleAddUpdateRequest(Request req, Response res) {
        //read form values (posted as params)
        String id = req.queryParams("id");
        String categoryId = req.queryParams("categoryId");
        String date = req.queryParams("date");
        String details = req.queryParams("details");
        String amount = req.queryParams("amount");

        boolean isUpdateCase = id != null && !id.isEmpty();

        try {
            TransactionDto transaction = validateAndBuildTransaction(id, categoryId, date, details, Double.parseDouble(amount));

            if (isUpdateCase) {
                transactionDao.update(transaction);
            } else {
                transactionDao.insert(transaction);
            }

            res.redirect("/main");
            return res;

        } catch (Exception e) {
            return renderAddUpdateForm(id, categoryId, date, details, Double.parseDouble(amount), e.getMessage());
        }
    }

    private static TransactionDto validateAndBuildTransaction(String id, String categoryId,
                                                              String date, String details, double amount) {

        long idValue = id != null && !id.isEmpty() ? Long.parseLong(id) : -1;

        long categIdValue = Long.parseLong(categoryId);

        Date dateValue;
        if (date != null && !date.isEmpty()) {
            try {
                dateValue = Date.valueOf(date);
            } catch (Exception e) {
                throw new RuntimeException("Invalid due date value: '" + date +
                        "', must be a date in format: yyyy-[m]m-[d]d");
            }
        } else {
            throw new RuntimeException("Date is required!");
        }

        if (amount == 0.0) {
            throw new RuntimeException("Amount is required!");
        }


        return new TransactionDto(idValue, categIdValue, dateValue, details, amount);
    }
}

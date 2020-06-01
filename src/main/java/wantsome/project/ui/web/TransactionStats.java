package wantsome.project.ui.web;

import wantsome.project.db.service.TransactionDao;

import java.sql.Date;
import java.text.DecimalFormat;

public class TransactionStats {

    private static final TransactionDao transacDao = new TransactionDao();

    public static double incomeByDateInterval(Date dateMin, Date dateMax) {

        double totalIncome = 0;

        totalIncome = transacDao.getIncomeByDateInterval(dateMin, dateMax)
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble(df.format(totalIncome));
    }

    public static double allIncome() {
        double totalIncome = 0;

        totalIncome = transacDao.getAllIncome()
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");


        return Double.parseDouble(df.format(totalIncome));

    }

    public static double expensesByDateInterval(Date dateMin, Date dateMax) {
        double totalExpenses = 0;

        totalExpenses = transacDao.getExpensesByDateInterval(dateMin, dateMax)
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");


        return Double.parseDouble(df.format(totalExpenses));


    }

    public static double allExpenses() {
        double totalExpenses = 0;

        totalExpenses = transacDao.getAllExpenses()
                .stream()
                .map(i -> i.getAmount())
                .reduce((double) 0, (a, b) -> a + b);

        DecimalFormat df = new DecimalFormat("#.##");


        return Double.parseDouble(df.format(totalExpenses));

    }

    public static double balanceByDateInterval(Date dateMin, Date dateMax) {
        double balance = 0;

        balance = incomeByDateInterval(dateMin, dateMax) - expensesByDateInterval(dateMin, dateMax);

        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble(df.format(balance));
    }

    public static double balance() {
        double balance = 0;

        balance = allIncome() - allExpenses();


        DecimalFormat df = new DecimalFormat("#.##");

        return Double.parseDouble(df.format(balance));


    }

}

package wantsome.project.db.dto;

import java.sql.Date;
import java.util.Objects;

public class TransactionsDto {
    private long id;
    private long category_id;
    private Date date;
    private String details;
    private double amount;


    //Date utilDate = new SimpleDateFormat("yyyy-MM-dd");
    // because PreparedStatement#setDate(..) expects a java.sql.Date argument
    //java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

    public TransactionsDto() {
    }

    public TransactionsDto(long category_id, String date, String details, double amount) {
        this(-1, category_id, date, details, amount);
    }

    public TransactionsDto(long id, long category_id, String date, String details, double amount) {

        this.id = id;
        this.category_id = category_id;
        this.date = Date.valueOf(date);
        this.details = details;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionsDto that = (TransactionsDto) o;
        return id == that.id &&
                category_id == that.category_id &&
                Double.compare(that.amount, amount) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category_id, date, details, amount);
    }

    @Override
    public String toString() {
        return "\nTransactions{" +
                "id=" + id +
                ", category_id=" + category_id +
                ", date=" + date +
                ", details='" + details + '\'' +
                ", amount=" + amount +
                '}';
    }
}

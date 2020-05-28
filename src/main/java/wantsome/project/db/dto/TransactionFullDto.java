package wantsome.project.db.dto;

import java.sql.Date;
import java.util.Objects;

public class TransactionFullDto {
    private long id;
    private long category_id;
    private String category_description;
    private Type category_type;
    private Date date;
    private String details;
    private double amount;


    public TransactionFullDto(long category_id, String category_description, Type category_type, Date date, String details, double amount) {
        this(-1, category_id, category_description, category_type, date, details, amount);
    }

    public TransactionFullDto(long id, long category_id, String category_description, Type category_type, Date date, String details, double amount) {
        this.id = id;
        this.category_id = category_id;
        this.category_description = category_description;
        this.category_type = category_type;
        this.date = date;
        this.details = details;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public String getCategory_description() {
        return category_description;
    }

    public Type getCategory_type() {
        return category_type;
    }

    public Date getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public double getAmount() {
        return amount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public void setCategory_type(Type category_type) {
        this.category_type = category_type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionFullDto that = (TransactionFullDto) o;
        return id == that.id &&
                category_id == that.category_id &&
                Double.compare(that.amount, amount) == 0 &&
                Objects.equals(category_description, that.category_description) &&
                category_type == that.category_type &&
                Objects.equals(date, that.date) &&
                Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category_id, category_description, category_type, date, details, amount);
    }

    @Override
    public String toString() {
        return "TransactionsFullDto{" +
                "id=" + id +
                ", category_id=" + category_id +
                ", category_description='" + category_description + '\'' +
                ", category_type=" + category_type +
                ", date=" + date +
                ", details='" + details + '\'' +
                ", amount=" + amount +
                '}';
    }
}

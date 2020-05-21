package wantsome.project.db.dto;

import java.util.Objects;

public class CategoriesDto {

    private long id;
    private String description;
    private Type type;

    public CategoriesDto() {
    }


    public CategoriesDto(String description, Type type) {
        this(-1, description, type);
    }

    public CategoriesDto(long id, String description, Type type) {
        this.id = id;
        this.description = description;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriesDto that = (CategoriesDto) o;
        return id == that.id &&
                Objects.equals(description, that.description) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, type);
    }

    @Override
    public String toString() {
        return "\nCategories{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}

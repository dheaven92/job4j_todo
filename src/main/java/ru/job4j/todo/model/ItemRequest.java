package ru.job4j.todo.model;

import java.util.List;

public class ItemRequest {

    private String description;

    private List<Integer> categoryIds;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public String toString() {
        return "ItemRequest{"
                + "description='" + description + '\''
                + ", categoryIds=" + categoryIds
                + '}';
    }
}

package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {

    List<Item> findAllItems();

    Item createItem(Item item, List<Integer> categoryIds);

    Item updateItem(int id);

    User createUser(User user);

    User findUserByEmail(String email);

    List<Category> findAllCategories();
}

package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.List;

public interface Store {

    List<Item> findAllItems();

    Item saveItem(Item item);
}

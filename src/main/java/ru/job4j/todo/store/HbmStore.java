package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import java.util.List;

public class HbmStore implements Store {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();

    private final SessionFactory sessionFactory = new MetadataSources(registry)
            .buildMetadata()
            .buildSessionFactory();

    @Override
    public List<Item> findAllItems() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            List items = session.createQuery(
                    "from ru.job4j.todo.model.Item item order by item.created desc "
            ).list();
            session.getTransaction().commit();
            return items;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Item saveItem(Item item) {
        try {
            return item.getId() != null
                    ? updateItem(item)
                    : createItem(item);
        } catch (Exception e) {
            throw new IllegalStateException("Could not create a record in DB", e);
        }
    }

    private Item createItem(Item item) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            int id = (int) session.save(item);
            item.setId(id);
            session.getTransaction().commit();
            session.close();
            return item;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private Item updateItem(Item item) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
            session.close();
            return item;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}

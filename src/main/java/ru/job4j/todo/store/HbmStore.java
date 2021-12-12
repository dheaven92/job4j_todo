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

    private static final class HbmStoreHolder {
        private static final Store INSTANCE = new HbmStore();
    }

    public static Store instanceOf() {
        return HbmStoreHolder.INSTANCE;
    }

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
    public Item createItem(String description) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Item item = new Item(description);
            int id = (int) session.save(item);
            item.setId(id);
            session.getTransaction().commit();
            session.close();
            return item;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new IllegalStateException("Could not create a record in DB", e);
        } finally {
            session.close();
        }
    }

    @Override
    public Item updateItem(int id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Item item = session.get(Item.class, id);
            if (item == null) {
                throw new IllegalStateException("Could not find a record in DB");
            }
            item.setDone(!item.isDone());
            session.update(item);
            session.getTransaction().commit();
            session.close();
            return item;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new IllegalStateException("Could not update a record in DB", e);
        } finally {
            session.close();
        }
    }
}

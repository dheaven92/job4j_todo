package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

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
        return executeTransaction(session ->
                session.createQuery("from ru.job4j.todo.model.Item item order by item.created desc ").list());
    }

    @Override
    public Item createItem(Item item) {
        int id = (int) executeTransaction(session -> session.save(item));
        item.setId(id);
        return item;
    }

    @Override
    public Item updateItem(int id) {
        Item item = executeTransaction(session -> session.get(Item.class, id));
        if (item == null) {
            throw new IllegalStateException("Could not find a record in DB");
        }
        item.setDone(!item.isDone());
        item.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        executeTransaction(session -> {
            Query query = session.createQuery(
                    "UPDATE Item SET "
                            + "done = :done, "
                            + "updated = :updated "
                            + "WHERE id = :id"
            );
            query.setParameter("id", item.getId());
            query.setParameter("done", item.isDone());
            query.setParameter("updated", item.getUpdated());
            return query.executeUpdate() > 0;
        });
        return item;
    }

    @Override
    public User createUser(User user) {
        int id = (int) executeTransaction(session -> session.save(user));
        user.setId(id);
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return executeTransaction(session -> {
           Query<User> query = session.createQuery("from User user where email = :email");
           query.setParameter("email", email);
           return query.stream()
                   .findFirst()
                   .orElse(null);
        });
    }

    private <T> T executeTransaction(final Function<Session, T> command) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}

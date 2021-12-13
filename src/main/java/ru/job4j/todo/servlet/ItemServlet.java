package ru.job4j.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class ItemServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ItemServlet.class.getName());

    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Item> items = HbmStore.instanceOf().findAllItems();
        String json = objectMapper.writeValueAsString(items);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Item item = objectMapper.readValue(req.getReader().readLine(), Item.class);
            Item itemInDb = HbmStore.instanceOf().createItem(item.getDescription());
            String json = objectMapper.writeValueAsString(itemInDb);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Could not save an item", e);
            resp.setContentType("application/json; charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Item item = objectMapper.readValue(req.getReader().readLine(), Item.class);
            Item itemInDb = HbmStore.instanceOf().updateItem(item.getId());
            String json = objectMapper.writeValueAsString(itemInDb);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Could not update an item", e);
            resp.setContentType("application/json; charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}

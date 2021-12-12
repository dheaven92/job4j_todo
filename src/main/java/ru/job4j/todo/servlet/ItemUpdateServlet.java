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

public class ItemUpdateServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ItemUpdateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
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

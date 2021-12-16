package ru.job4j.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Category> items = HbmStore.instanceOf().findAllCategories();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(items);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(json);
    }
}

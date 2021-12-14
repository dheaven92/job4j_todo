package ru.job4j.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("registration.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(req.getReader().readLine(), User.class);
        User userWithSameEmail = HbmStore.instanceOf().findUserByEmail(user.getEmail());
        if (userWithSameEmail != null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("Email занят другим пользователем");
        } else {
            User createdUser = HbmStore.instanceOf().createUser(user);
            HttpSession session = req.getSession();
            session.setAttribute("user", createdUser);
            String json = objectMapper.writeValueAsString(createdUser);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(json);
        }
    }
}

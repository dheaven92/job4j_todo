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

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(req.getReader().readLine(), User.class);
        User userInDb = HbmStore.instanceOf().findUserByEmail(user.getEmail());
        if (userInDb != null && user.getPassword().equals(userInDb.getPassword())) {
            HttpSession session = req.getSession();
            session.setAttribute("user", userInDb);
            String json = objectMapper.writeValueAsString(userInDb);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(json);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("Неверные email или пароль");
        }
    }
}

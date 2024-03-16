package step.learning.servlets;

import com.google.inject.Singleton;
import step.learning.dall.dao.ShareDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//@WebServlet ("/share")
@Singleton
public class ShareServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ShareDao shareDao = new ShareDao();
        req.setAttribute("share", shareDao.getShare());
        req.setAttribute("page-body", "share");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }
}

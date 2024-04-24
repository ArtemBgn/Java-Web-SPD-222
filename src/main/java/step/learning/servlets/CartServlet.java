package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dall.dao.CartDao;
import step.learning.dall.dao.ProductDao;
import step.learning.models.CartPageModel;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CartServlet extends HttpServlet {
    private final ProductDao productDao;
    private final CartDao cartDao;
    @Inject
    public CartServlet(ProductDao productDao, CartDao cartDao) {
        this.productDao = productDao;
        this.cartDao = cartDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //String token = req.getParameter("token");
        ServletContext context = getServletContext();
        String userId = (String) context.getAttribute("user-id");
        //String userId = (String) req.getAttribute("user-id");
        //.getParameter("user-id"); //req.getAttribute("user-id").toString();
        //"03db0c5e-f874-4b99-ba35-24be6d4c1411";// = (String)req.getAttribute("data-user-id");// .getParameter("user_id");
        //запит даних
        ////////CartDao cartDao = new CartDao();
        //передача їх до представлення
        ////////req.setAttribute("cart", cartDao.getCart()); //роль контроллера запитати дані та перетворити їх для представлення
        req.setAttribute("skip-container", "true");
        req.setAttribute("model", new CartPageModel(
                productDao.getList(0, 10),
                cartDao.getCart(userId)
                ) );
        req.setAttribute("page-body", "cart");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }
}

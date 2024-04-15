package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dall.dao.CartDao;
import step.learning.dall.dao.ProductDao;
import step.learning.models.CartPageModel;

import javax.servlet.ServletException;
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
        //запит даних
        ////////CartDao cartDao = new CartDao();
        //передача їх до представлення
        ////////req.setAttribute("cart", cartDao.getCart()); //роль контроллера запитати дан та перетворити їх для представлення
        req.setAttribute("skip-container", "true");
        req.setAttribute("model", new CartPageModel(
                productDao.getList(0, 10),
                cartDao.getCart()
                ) );
        req.setAttribute("page-body", "cart");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }
}

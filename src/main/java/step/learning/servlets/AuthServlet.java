package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dall.dao.UserDao;
import step.learning.dall.dto.User;
import step.learning.services.kdf.KdfService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Singleton
public class AuthServlet extends HttpServlet {
    // авторизація по токенам
    private final KdfService kdfService;
    private final UserDao userDao;
    @Inject
    public AuthServlet(KdfService kdfService, UserDao userDao) {
        this.kdfService = kdfService;
        this.userDao = userDao;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doDelete(req, resp);
        String token = req.getParameter("token");
        if( token == null || token.isEmpty() ) {
            sendRest(resp, "error", "Property 'token' required", null);
            return ;
        }
        else if(!userDao.exitUser(token)) {
            sendRest(resp, "error", "Oops!.. something went wrong...", token);
            return ;
        }
        else {
            ServletContext context = getServletContext();
            context.removeAttribute("user-id");
            sendRest(resp, "success", "Logout has been completed!", null );
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if( token == null || token.isEmpty() ) {
            sendRest(resp, "error", "Property 'token' required", null);
            return ;
        }
        /*
        Робота з токенами зазвичай робиться в окремому "журналі" видачі токенів
        CREATE TABLE Tokens(
        token_id CHAR(36) PRIMARY KEY,
        user_id CHAR(36) NOT NULL,
        token_created DATETIME DEFAULT CURRENT_TIMESTAMP,
        token_expires DATETIME
        )ENGINE=INNODB, DEFAULT CHARSET=utf8mb4
         */
        User user = userDao.getUserByToken(token);
        if(user == null)
        {
            sendRest(resp, "error", "Token invalid or expired", null);
        }
        else {
            JsonObject rest = new JsonObject();
            JsonObject meta = new JsonObject();
            meta.addProperty("service", "auth");
            meta.addProperty("status", "success");
            meta.addProperty("message", "Authorized");
            meta.addProperty("time", Instant.now().getEpochSecond());
            rest.add("meta", meta);
            Gson gson = new GsonBuilder().serializeNulls().create();
            rest.add("data", gson.toJsonTree(user));
            resp.getWriter().print( gson.toJson(rest) );

            String userid = user.getId().toString();
            ServletContext context = getServletContext();
            context.setAttribute("user-id", userid);
            /*
            JsonObject data = null;
            if(token != null)
            {
                data = new JsonObject();
                data.addProperty("token", token);
            }
            */
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if( email == null || email.isEmpty() ) {
            sendRest(resp, "error", "Property 'email' required", null);
            return ;
        }
        String password = req.getParameter("password");
        if( password == null || password.isEmpty()) {
            sendRest(resp, "error", "Property 'password' required", null);
            return ;
        }
        User user = userDao.getUserByEmail(email);
        if( user == null ) {
            sendRest(resp, "error", "Credentials rejected", null);
            return ;
        }
        if(!user.getDerivedKey().equals(kdfService.derivedKey(password, user.getSalt()))) {
            sendRest(resp, "error", "Credentials rejected", null);
            return ;
        }
        // генеруємо токен доступу користувача
        String token = userDao.generateToken(user);
        sendRest(resp, "success", "Credentials confirmed", token );

    }
    private void sendRest(HttpServletResponse resp, String status, String message, String token) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();
        meta.addProperty("service", "auth");
        meta.addProperty("status", status);
        meta.addProperty("message", message);
        meta.addProperty("time", Instant.now().getEpochSecond());
        rest.add("meta", meta);

        JsonObject data = null;
        if(token != null)
        {
            data = new JsonObject();
            data.addProperty("token", token);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        rest.add("data", data);
        resp.getWriter().print( gson.toJson(rest) );
    }
}

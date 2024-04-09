package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dall.dao.CartDao;
import step.learning.dall.dao.UserDao;
import step.learning.dall.dto.User;
import step.learning.services.form.FormParseResult;
import step.learning.services.form.FormParseService;
import step.learning.services.kdf.KdfService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SignupServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final UserDao userDao;
    private final KdfService kdfService;
    @Inject
    public SignupServlet(FormParseService formParseService, UserDao userDao, KdfService kdfService) {
        this.formParseService = formParseService;
        this.userDao = userDao;
        this.kdfService = kdfService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // userDao.installTable();
        req.setAttribute("page-body", "signup");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult parseResult = formParseService.parse(req);
        Map<String, String> fields = parseResult.getFields();
        Map<String, FileItem> files = parseResult.getFiles();
        String userName = fields.get("name-user");
        if(userName == null || userName.isEmpty())
        {
            sendRest(resp, "error", "Poperty 'user-name' required", null);
            return;
        }
        String userEmail = fields.get("email-user");
        if(userEmail == null || userEmail.isEmpty())
        {
            sendRest(resp, "error", "Poperty 'user-email' required", null);
            return;
        }
        String userPassword = fields.get("password-user");
        if(userPassword == null || userPassword.isEmpty())
        {
            sendRest(resp, "error", "Poperty 'user-password' required", null);
            return;
        }

        // Реєструємо користувача
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userName);
        user.setEmail(userEmail);
        user.setSalt(kdfService.derivedKey(UUID.randomUUID().toString(), ""));
        user.setDerivedKey(kdfService.derivedKey(userPassword, user.getSalt()));


        FileItem avatar = files.get("avatar-user");
        if(avatar != null)
        {
            // не обов'язкове поле, але якщо є, то проходить перевірку
            String path = req.getServletContext().getRealPath("/") +
                    "img" + File.separator + "avatar" + File.separator;
            // визначаємо тип файлу (розширення)
            int dotPosition = avatar.getName().lastIndexOf('.');
            if(dotPosition < 0) {
                sendRest(resp, "error", "Avatar file must have extension", null);
                return;
            }
            String  ext = avatar.getName().substring(dotPosition);
            // формуємо нове ім'я
            String savedName;
            File savedFile;
            do {
                savedName = UUID.randomUUID() + ext;
                savedFile = new File(path, savedName);
            } while (savedFile.exists());

            try
            {
                avatar.write(savedFile);
                user.setAvatar(savedName);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        // reg user
        if(userDao.registerUser(user)) {
            sendRest(resp, "success", "User registered", user.getId().toString());
        }
        else {
            sendRest(resp, "error", "Internal error, look", null);
        }
    }
    private void sendRest(HttpServletResponse resp, String status, String message, String id) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();
            meta.addProperty("service", "signup");
            meta.addProperty("status", status);
            meta.addProperty("message", message);
            meta.addProperty("time", Instant.now().getEpochSecond());
        rest.add("meta", meta);

        JsonObject data = null;
        if(id != null)
        {
            data = new JsonObject();
            data.addProperty("id", id);
        }
        rest.add("data", data);

        Gson gson = new GsonBuilder().serializeNulls().create();
        resp.getWriter().print( gson.toJson(rest) );
    }
}
/*
REST {
meta: {
    service: "signup",
    status: "succes",
    message: "User Created",
    time: 16154846
},
data: {
    id: '.....'
}

Щодо файлової системи

Процеси, що відповідають за веб-застосунок, виконуються веб-сервером.
Тому "домашня директорія" належить йому.
new File(".").getAbsolutePath() --- C:\xampp\tomcat\bin\.
Для того щоб встановити розміщення ресурсів застосунку виконується
звернення до контексту (ServletContext)
req.getServletContext().getRealPath("/") --- C:\Users\Lector\source\repos\Java-Web-SPD111\target\Java-Web-SPD111\
Але також звертаємо увагу на те, що посилання іде на deploy-зрозміщення
(\target\...)
Розуміємо, що файли, які є у проєкті, та файли, які "напрацьовані"
проєктом під час роботи - різні ресурси з різним розміщенням.
 */
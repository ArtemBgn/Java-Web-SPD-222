package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton // при використанні Guiсe необхідна аннотація @Singleton
public class CharsetFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /*
        При ініціалізації фільтру передається конфігурація, через неї
        можна дізнатися про файли шляхи та інші параметри оточення.
        Зберігаємо для можливості подальшого доступу
         */
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*
        chain - ланцюг фільтрів, яким проходить запит (і відповідь у звротньому напрямку)
        завдання фільтру - викликати перехід до наступного шару або зупинити роботу
         */
        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name()); // прямий хід - до
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name()); // виклику chain

        filterChain.doFilter(servletRequest, servletResponse);

        // код, описаний після chain буде виконуватися на зворотньому шляху
    }

    @Override
    public void destroy() {
        this.filterConfig = null; // аналог delete - зменшення кількості посилань на об'єкт
    }
}
/*
Фільтри(сервлетні фільтри) - концепція Middleware у JSP
утворення каскаду обробників запиту, які передають (або не передають)
процес один іншому. Фільтри спрацбовують раніше за сервлети, тому вони
не залежать від методу запиту.
Прямий хід - проходження запиту до JSP (до преставлення)
Зворотній хід - поверняння згенерованого HTML до сервера (Tomcat)
 */
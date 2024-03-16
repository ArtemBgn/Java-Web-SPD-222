package step.learning.ioc;

import com.google.inject.AbstractModule;
import step.learning.services.db.DbService;
import step.learning.services.db.MySqlDbService;
import step.learning.services.form.FormParseService;
import step.learning.services.form.HybridFormParser;
import step.learning.services.hash.HashService;
import step.learning.services.hash.Md5HashService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        //конфігурація служб
        // "буде запит на HashService -- повернути об'єкт Md5HasService"
        bind(HashService.class).to(Md5HashService.class);   // повний аналог ASP: Service.AddSingleton<Hash, Md5>
        bind(DbService.class).to(MySqlDbService.class);
        bind(FormParseService.class).to(HybridFormParser.class);
    }
}
/*
Інверсія управління (Inversion of Control, IoC) - архітекрурний підхід (стиль)
згідно з яким питання життєвого циклу обєктів передаються(вирішуються) окремим
спеціальним модулем(контейнером залежностей, інжектором, інвертором).

Життєвий цикл об'єкту - CRUD, у простішому випадку мова іде про створення
об'єктів:
    чи створювати нвий / чи залишати раніше сворений.

    Також на модуль IoC покладається задача (Resolve) заповнення об'єктів -
    впровадження (Inject) у них залежностей - посилань на інші об'єкти,
    що їх створює IoC.

    Без IoC                     з IoC
    class Klass {               class Klass {
    service = new Service()     @Inject Service service
    ...                         ...
    }                           }
    k1 = new Klass()             k1 = Injector.Resolve(Klass)
    k2= new Klass()              k2 = Injector.Resolve(Klass)
    у k1 та k2 різні service     однакові service
 */
/*
    Впровадження на базі Google Guice
    (Spring - аналог)
    - підключаємо до проекту
        <!-- https://mvnrepository.com/artifact/com.google.inject/guice -->
        <dependency>
            <groupId>com.google.inject</groupId>
            <artifactId>guice</artifactId>
            <version>6.0.0</version>
        </dependency>
    - створюєм клас - "слухач" створення контексту (розгортання застосунку)
        (див. IocContextListener)
    - створюємо класи конфігуратори:
        = ServiceModule - клас для налаштування служб за DIP (з SOLID)
            згідно з яким залежності слід ствоорювати не від класі а від інтерфейсів.
            Але оскільки об'єктів з інтерфейсом створити не можна, необхідно встановити зв'язок (bind)
            між інтерфейсом реалізацією (класом)
        = RouterModule - для маршрутизації сервлетів.
            створюємо інструкції маршрутизації
            замінюємо сервлетні анотації (@WebServlet) на
    - змінюємо налаштування сервера (див web.xml)
 */
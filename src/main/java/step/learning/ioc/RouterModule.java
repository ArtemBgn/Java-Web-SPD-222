package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/").with( HomeServlet.class );
        serve("/cart").with( CartServlet.class ) ;
        serve("/signup").with( SignupServlet.class ) ;
        serve("/share").with( ShareServlet.class ) ;
        serve("/addprod").with( AddprodServlet.class ) ;
    }
}

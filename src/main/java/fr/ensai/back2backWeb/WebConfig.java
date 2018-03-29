package fr.ensai.back2backWeb;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import javax.servlet.annotation.WebServlet;

/**
 * 
 */
public class WebConfig {

    @SuppressWarnings("serial")
	@WebServlet(urlPatterns = "/*", asyncSupported = true)
   // @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }

}

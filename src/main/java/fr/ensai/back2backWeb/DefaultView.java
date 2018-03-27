package fr.ensai.back2backWeb;

import java.io.File;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DefaultView extends Composite implements View {
	
    public DefaultView() {
    	// Find the application directory
    	String basepath = VaadinService.getCurrent()
    	                  .getBaseDirectory().getAbsolutePath();

    	// Image as a file resource
    	FileResource resource = new FileResource(new File(basepath +
    	                        "/WEB-INF/images/rspark.png"));

    	// Show the image in the application
    	Image image = new Image("", resource);
    	image.setWidth("500px");
    	image.setHeight("250px");
    	Label lab = new Label("Welcome to Back2Back Testing App!!!");
    	lab.addStyleName("lab");
    	VerticalLayout layout = new VerticalLayout(lab, image);
    	
        setCompositionRoot(layout);
        
    }
    
}

package fr.ensai.back2backWeb;

import java.io.File;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Composite;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DefaultView extends Composite implements View {
	
    public DefaultView() {
    	// Find the application directory
    	String basepath = VaadinService.getCurrent()
    	                  .getBaseDirectory().getAbsolutePath();

    	// Image as a file resource
    	FileResource resourceR = new FileResource(new File(basepath + "/WEB-INF/images/renjin.svg"));
    	Image imageR = new Image("", resourceR);
    	FileResource resourceSp = new FileResource(new File(basepath + "/WEB-INF/images/sparkml.png"));
    	Image imageSp = new Image("", resourceSp);
    	FileResource resourceW = new FileResource(new File(basepath + "/WEB-INF/images/weka.png"));
    	Image imageW = new Image("", resourceW);
    	String width = "475px";
    	String height = "250px";
    	imageR.setWidth(width);
    	imageR.setHeight(height);
    	imageSp.setWidth(width);
    	imageSp.setHeight(height);
    	imageW.setWidth(width);
    	imageW.setHeight(height);
    	
    	GridLayout imagesLayout  = new GridLayout(3, 1);
    	imagesLayout.addComponents(imageR, imageSp, imageW);
    	Panel images = new Panel();
    	images.setContent(imagesLayout);
    	
    	VerticalLayout main = new VerticalLayout();
    	main.addComponent(images);
    	
    	Panel mainMain = new Panel();
    	mainMain.setContent(main);
    	
    	Label back2back = new Label("Back To Back Testing");
    	back2back.addStyleName("lab_back2back");
    	
    	main.addComponent(back2back);
    	main.setComponentAlignment(back2back, Alignment.MIDDLE_CENTER);
    	
    	Label description = new Label(
    		    "<p>This web application allows to perform back-to-back testing for machine learning algorithms </br>" +
    		    "Three easy steps to launch model framework comparison :" + 
    		    "<ul>"+
    		    "  <li>Upload data and set up some parameters</li>"+
    		    "  <li>Choose the model (Decision Tree / Random Forest)</li>"+
    		    "  <li>Launch the comparison</li>"+
    		    "</ul> </p>",
    		    ContentMode.HTML);
    	main.addComponent(description);
    	
    	// Show the image in the application
    	
    		
    	Label lab = new Label("Welcome to Back2Back Testing App!!!");
    	lab.addStyleName("lab");
    	//VerticalLayout layout = new VerticalLayout(lab, image);
    	
        setCompositionRoot(mainMain);
        
    }
    
}

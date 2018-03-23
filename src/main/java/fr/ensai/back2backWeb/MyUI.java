package fr.ensai.back2backWeb;

import javax.servlet.annotation.WebServlet;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.apache.log4j.Logger;

import application.Data;
import application.DecisionTree;
import application.RDecisionTree;
import application.SparkDecisionTree;
import application.WekaDecisionTree;

import java.util.ArrayList;
import org.apache.log4j.Level;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */

@PushStateNavigation
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
    	
    	
    	 Label title = new Label("Main Menu - Data settings");
         title.addStyleName(ValoTheme.MENU_TITLE);

         Button dTreeBtn = new Button("Decision Tree", e ->  getNavigator().navigateTo("decisonTree"));
         
         
         dTreeBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
         Button rfBtn = new Button("Random Forest", e -> getNavigator().navigateTo("randomForest"));
         rfBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);

         CssLayout menu = new CssLayout(title, dTreeBtn, rfBtn);
         menu.setWidth("250px");
         
         
         menu.addStyleName(ValoTheme.MENU_ROOT);

         CssLayout viewContainer = new CssLayout();

         HorizontalLayout mainLayout = new HorizontalLayout(menu, viewContainer);
         mainLayout.setSizeFull();
         setContent(mainLayout);

         Navigator navigator = new Navigator(this, viewContainer);
         navigator.addView("", DefaultView.class);
         navigator.addView("decisonTree", DecisionTreeView.class);
         navigator.addView("randomForest", RandomForestView.class);
         
         
}
}

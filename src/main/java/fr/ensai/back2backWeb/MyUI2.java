//package fr.ensai.back2backWeb;
//
//import javax.servlet.annotation.WebServlet;
//
//import com.vaadin.annotations.Theme;
//import com.vaadin.annotations.VaadinServletConfiguration;
//import com.vaadin.server.VaadinRequest;
//import com.vaadin.server.VaadinServlet;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.UI;
//import com.vaadin.ui.VerticalLayout;
//
//
//import org.apache.log4j.Logger;
//
//import application.Data;
//import application.DecisionTree;
//import application.RDecisionTree;
//import application.SparkDecisionTree;
//import application.WekaDecisionTree;
//
//import java.util.ArrayList;
//import org.apache.log4j.Level;
//
///**
// * This UI is the application entry point. A UI may either represent a browser window 
// * (or tab) or some part of an HTML page where a Vaadin application is embedded.
// * <p>
// * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
// * overridden to add component to the user interface and initialize non-component functionality.
// */
//@Theme("mytheme")
//public class MyUI2 extends UI {
//
//    @Override
//    protected void init(VaadinRequest vaadinRequest) {
//        final VerticalLayout layout = new VerticalLayout();
//        
//        
//        
//        
//        
//        
//        
//        
//        
//     // logger configuration
//     		Logger.getLogger("org").setLevel(Level.OFF);
//     		Logger.getLogger("akka").setLevel(Level.OFF);
//
//     		// ----------------------------------------------------------------- //
//     		// Data parameters 
//     		// ----------------------------------------------------------------- //
//     		String path = "data/testreg2.csv";
//     		String header = "true";
//     		String targetName = "y";
//     		String hasRowNames = "false";
//     		String sep = ",";
//     		String dec = ".";
//     		boolean classif = false;
//     		ArrayList<String> catNames = new ArrayList<String>();
//     		catNames.add("cat");
//     		catNames.add("cat2");
//
//     		// ----------------------------------------------------------------- //
//     		// Model parameters  (a Decision Tree in our case)
//     		// ----------------------------------------------------------------- //
//
//     		int maxDepth = 10;
//     		int minSplit = 15;
//     		double cp = 0.1;
//     		boolean gini = false;
//     		int minPerLeaf = 10;
//     		int maxBins = 50;
//
//     		// ----------------------------------------------------------------- //
//     		// Set Data parameters & Model parameters
//     		// ----------------------------------------------------------------- //
//     		Data d =  new Data();
//     		d.setPath(path);
//     		d.setHeader(header);
//     		d.setTargetName(targetName);
//     		d.setHasRowNames(hasRowNames);
//     		d.setSep(sep);
//     		d.setDec(dec);
//     		d.setClassif(classif);
//     		d.setNumClasses(3); // for classif
//     		d.setCatFeaturesNames(catNames);
//
//     		application.DecisionTree dt = new DecisionTree();
//     		dt.setMaxDepth(maxDepth);
//     		dt.setMinSplit(minSplit);
//     		dt.setCp(cp);
//     		dt.setGini(gini);
//     		dt.setMinPerLeaf(minPerLeaf);
//     		dt.setMaxBins(maxBins);
//
//     		// ----------------------------------------------------------------- //
//     		// Set propTrain & nbIter for the comparison 
//     		// ----------------------------------------------------------------- //
//     		double propTrain = 0.8;
//     		int nbIter = 1;
//
//     	
//     			try {
//					SparkDecisionTree sdt = new SparkDecisionTree(d, dt, propTrain);
//					WekaDecisionTree wdt = new WekaDecisionTree(d, dt, propTrain);
//	        		RDecisionTree rdt = new RDecisionTree(d, dt, propTrain);
//	        		
//	        		double sr = sdt.aggregateEval(nbIter, propTrain);
//	        		double wr =  wdt.aggregateEval(nbIter, propTrain);
//	        		double rr = rdt.aggregateEval(nbIter, propTrain);
//	        		
//	        		layout.addComponent(new Label("Spark : **********  " + sr + "************"));
//	        		layout.addComponent(new Label("Weka : ******** " + wr + "************"));
//	        		layout.addComponent(new Label("Renjin :  " + rr));
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//        		
//        		
//				
//        		
////        		layout.addComponent(new Label("Spark :  "));
////        		layout.addComponent(new Label("Weka :  "));
////        		layout.addComponent(new Label("Renjin :  " ));
//			
//     		
//     		
//    		
//        
//        final TextField name = new TextField();
//        name.setCaption("Type your name here:");
//
//        Button button = new Button("Click Me");
//        button.addClickListener(e -> {
//            layout.addComponent(new Label("Thanks " + name.getValue() 
//                    + ", it works!"));
//            
//            
//            
//        });
//        
//        layout.addComponents(name, button);
//        
//        setContent(layout);
//    }
//
//    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
//    @VaadinServletConfiguration(ui = MyUI2.class, productionMode = false)
//    public static class MyUIServlet extends VaadinServlet {
//    }
//}

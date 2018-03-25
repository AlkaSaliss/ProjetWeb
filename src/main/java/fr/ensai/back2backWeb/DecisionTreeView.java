package fr.ensai.back2backWeb;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import application.DecisionTree;
import application.RDecisionTree;
//import application.SparkDecisionTree;
import application.WekaDecisionTree;

@SuppressWarnings("serial")
public class DecisionTreeView extends Composite implements View {

	FormLayout dtForm = new FormLayout();
	DecisionTree dtree = new DecisionTree();

	// ntree params
	Slider minPerLeaf = new Slider("Min. number of instances in each leaf", 1, 2500);
	Slider minSplit = new Slider("Min. number of instances to split a node", 1, 5000);
	Slider cp = new Slider("Complexity paramter", 0, 10);
	Slider propTrain = new Slider("Training set proportion", 0, 1);
	Slider nbIter = new Slider("Iteration number for evaluation", 1, 1000);
	Slider maxDepth = new Slider("Maximum depth", 1, 100);
	Slider maxBins = new Slider("Maximum number of bins", 1, 100);
	Slider seed = new Slider("Seed", 1, 100);

	RadioButtonGroup<String> gini = new RadioButtonGroup<>("Gini");
	Button submit = new Button("Submit");
	Button reset = new Button("Restore default");
	
	VerticalLayout popupContent = new VerticalLayout();
	PopupView popup = new PopupView(null, popupContent);
	
	VerticalLayout main = new VerticalLayout(); // layout containing the form panel and the result panel
	

	public DecisionTreeView() {

		cp.setResolution(2); // two digits after decimals
		gini.setItems("True", "False");
		gini.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		propTrain.setResolution(2);

		cp.setValue(0.01);
		maxDepth.setValue((double) 30);
		maxBins.setValue((double) 2);
		minSplit.setValue((double) 20);
		minPerLeaf.setValue((double) 15);
		gini.setSelectedItem("True");
		nbIter.setValue((double) 1);
		propTrain.setValue(0.75);

		cp.setDescription("Default : 0.01");
		maxDepth.setDescription("Default : 30");
		maxBins.setDescription("Default : 2");
		minSplit.setDescription("Default : 20");
		minPerLeaf.setDescription("Default : 15");
		nbIter.setDescription("Default : 1");
		propTrain.setDescription("Default : 0.75");;

		dtForm.addComponents(propTrain, maxDepth, minSplit, minPerLeaf, maxBins, cp, gini, nbIter);
		dtForm.setCaption("Decsion Tree Parameters");

		submit.setStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addClickListener(e -> {

			dtree.setMaxDepth(maxDepth.getValue().intValue());
			dtree.setMinSplit(minSplit.getValue().intValue());
			dtree.setCp(cp.getValue());
			dtree.setGini(gini.getValue().equalsIgnoreCase("true"));
			dtree.setMinPerLeaf(minPerLeaf.getValue().intValue());
			dtree.setMaxBins(maxBins.getValue().intValue());

			((MyUI) getUI()).dtree = dtree;

			
			popupContent.removeAllComponents();
			popupContent.addComponent(new Label("Here, set parameters: "));
			popupContent.addComponent(new Label("MaxBins: " + ((MyUI) getUI()).dtree.getMaxBins()));
			popupContent.addComponent(new Label("Max Depth: " + ((MyUI) getUI()).dtree.getMaxDepth()));
			popupContent.addComponent(new Label("Cp: " + ((MyUI) getUI()).dtree.getCp()));
			popupContent.addComponent(new Label("MinPerLeaf: " + ((MyUI) getUI()).dtree.getMinPerLeaf()));
			popupContent.addComponent(new Label("MinSplit: " + ((MyUI) getUI()).dtree.getMinSplit()));
			popup.setPopupVisible(true);
			
			Button stay = new Button("Stay here", e2 -> {
				popup.setPopupVisible(false);
			}) ;
			Button btnDtree = new Button("Launch comparison",  e2 -> {
				try {
					main.addComponent(getComparisonPanel());
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			CssLayout choiceBtn = new CssLayout(btnDtree, stay);
			//VerticalLayout choice = new VerticalLayout(msg1, msg2, choiceBtn);
			//String tmp = ((MyUI) getUI()).data.isClassif()  ? "Classification":"Regression";
			//popupContent.addComponent(new Label("Model type: " + tmp ));
			
			popupContent.addComponents(choiceBtn);
			popup.setPopupVisible(true);

		});

		reset.setStyleName(ValoTheme.BUTTON_DANGER);
		reset.addClickListener(e -> {
			cp.setValue(0.01);
			maxDepth.setValue((double) 30);
			maxBins.setValue((double) 2);
			minSplit.setValue((double) 20);
			minPerLeaf.setValue((double) 15);
			gini.setSelectedItem("True");
			propTrain.setValue(0.75);
			nbIter.setValue((double) 1);
		});
		
		

		HorizontalLayout btns = new HorizontalLayout(submit, reset);
		dtForm.addComponent(btns);
		dtForm.addComponent(popup);
		
		Panel formPanel = new Panel("Decision Tree parameters");
		dtForm.setMargin(true);
		formPanel.setContent(dtForm);
		main.addComponent(formPanel);
		
		Panel mainMain = new Panel();
		mainMain.setContent(main);
		mainMain.setSizeFull();;
		//mainMain.setHeight("1500px");
//		setWidthUndefined();
//		setHeightUndefined();
		setCompositionRoot(mainMain);
	}
	
	//public Hashtable<String, Double> launchComparison() throws Exception {
	public Panel getComparisonPanel() throws Exception {
		Panel p = new Panel("Comparison Results");
		
		//SparkDecisionTree sdt = new SparkDecisionTree(((MyUI)getUI()).data, ((MyUI)getUI()).dtree, propTrain.getValue());
		WekaDecisionTree wdt = new WekaDecisionTree(((MyUI)getUI()).data, ((MyUI)getUI()).dtree, propTrain.getValue());
		RDecisionTree rdt = new RDecisionTree(((MyUI)getUI()).data, ((MyUI)getUI()).dtree, propTrain.getValue());
		
		// double resSpark = sdt.aggregateEval(nbIter.getValue().intValue(), propTrain.getValue());
		 double resWeka = wdt.aggregateEval(nbIter.getValue().intValue(), propTrain.getValue());
		 double resR = rdt.aggregateEval(nbIter.getValue().intValue(), propTrain.getValue());
		 
		 VerticalLayout resultLayout = new VerticalLayout(new Label("R : " + resR), new Label("Weka : " + resWeka), new Label("Spark : " + 00000000));
		 p.setContent(resultLayout);
		 
		 return p;
		 
		 
	} 

}

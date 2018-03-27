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
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import application.RRandomForest;
import application.RandomForest;
import application.SparkRandomForest;
import application.WekaRandomForest;

@SuppressWarnings("serial")
public class RandomForestView extends Composite implements View {

	FormLayout rfForm = new FormLayout();

	// Random forest params
	Slider propTrain = new Slider("Training set proportion");
	Slider nbIter = new Slider("Iteration number for evaluation", 1, 1000);
	Slider ntrees = new Slider("Number of trees", 1, 1000);
	//Slider mtry = new Slider("Percentage of features to select", 0, 1);
	Slider mtry = new Slider("Percentage of features to select");
	Slider sampsize = new Slider("Percentage of individuals to select ");
	Slider maxDepth = new Slider("Maximum depth for each tree", 1, 30);
	Slider maxBins = new Slider("Maximum number of bins", 1, 100);
	Slider seed = new Slider("Seed", 1, 100);
	//RadioButtonGroup<String> featureSubsetStrategy = new RadioButtonGroup<>("Feature subset strategy");
	//RadioButtonGroup<String> gini = new RadioButtonGroup<>("Gini");
	Button submit = new Button("Submit");
	Button reset = new Button("Restore default");
	
	RandomForest rf = new RandomForest();
	
	VerticalLayout popupContent = new VerticalLayout();
	PopupView popup = new PopupView(null, popupContent);
	
	VerticalLayout main = new VerticalLayout(); // layout containing the form panel and the result panel

	public RandomForestView() {

		mtry.setResolution(2);
		sampsize.setResolution(2);
	//	gini.setItems("True", "False");
	//	featureSubsetStrategy.setItems("auto", "Not auto");
	//	gini.setSelectedItem("True");
	//	featureSubsetStrategy.setSelectedItem("auto");
		//gini.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

		mtry.setValue((double) 0.75);
		mtry.setMin(0.2);
		mtry.setMax(1.0);
		ntrees.setValue((double) 500);
		sampsize.setMin(0.2);
		sampsize.setMax(1);
		sampsize.setValue(0.7);
		propTrain.setMin(0.1);
		propTrain.setMax(1);
		propTrain.setValue(0.75);
		propTrain.setResolution(2);
		nbIter.setValue((double) 1);
		
		mtry.setDescription("Default: depend on algorithm)");
		ntrees.setDescription("Default: 500");
		sampsize.setDescription("Default: 0.7");
		propTrain.setDescription("Default: 0.75");
		nbIter.setDescription("Default: 1");
		

		//rfForm.addComponents(propTrain, ntrees, mtry, sampsize, maxDepth, maxBins, seed, featureSubsetStrategy, gini, nbIter);
		rfForm.addComponents(propTrain, ntrees, mtry, sampsize, maxDepth, maxBins, seed, nbIter);

		rfForm.setCaption("Random Forest Parameters");
		rfForm.addStyleName("rfForm");

		submit.setStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addClickListener(e -> {
			rf.setNtrees(ntrees.getValue().intValue());
			rf.setMtry(mtry.getValue());
			rf.setSampsize(sampsize.getValue());
			rf.setMaxDepth(maxDepth.getValue().intValue());
			rf.setMaxBins(maxBins.getValue().intValue());
			rf.setSeed(seed.getValue().intValue());
			//rf.setFeatureSubsetStrategy(featureSubsetStrategy.getValue());
			rf.setFeatureSubsetStrategy("auto");

			//rf.setGini(gini.getValue().equalsIgnoreCase("true"));
			rf.setGini(((MyUI) getUI()).data.isClassif());
			
			((MyUI) getUI()).rf = rf;
			
			popupContent.removeAllComponents();
			popupContent.addComponent(new Label("Here, set parameters: "));
			popupContent.addComponent(new Label("MaxBins: " + ((MyUI) getUI()).rf.getMaxBins()));
			popupContent.addComponent(new Label("Max Depth: " + ((MyUI) getUI()).rf.getMaxDepth()));
			popupContent.addComponent(new Label("Number of trees" + ((MyUI) getUI()).rf.getNtrees()));
			popupContent.addComponent(new Label("Percentage of features at each iteration" + ((MyUI) getUI()).rf.getMtry()));
			popupContent.addComponent(new Label("Percentage of instances at each iteration: " + ((MyUI) getUI()).rf.getSampsize()));
			popupContent.addComponent(new Label("Feature subset strat.: " + ((MyUI) getUI()).rf.getFeatureSubsetStrategy()));
			popupContent.addComponent(new Label("Gini as impurity criterion: " + ((MyUI) getUI()).rf.isGini()));
			popup.setPopupVisible(true);
			
			Button stay = new Button("Stay here", e2 -> {
				popup.setPopupVisible(false);
			}) ;
			Button btnRF = new Button("Launch comparison",  e2 -> {
				try {
					//main.addComponent(getComparisonPanel());
					main.addComponent(getComparisonPanel());
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			CssLayout choiceBtn = new CssLayout(btnRF, stay);
			popupContent.addComponents(choiceBtn);
			popup.setPopupVisible(true);
		});

		reset.setStyleName(ValoTheme.BUTTON_DANGER);
		reset.addClickListener(e -> {
			mtry.setValue((double) 0.75);
			ntrees.setValue((double) 500);
			sampsize.setValue(0.7);
			propTrain.setValue(0.75);
			nbIter.setValue((double) 1);

		});
		
		

		HorizontalLayout btns = new HorizontalLayout(submit, reset);
		rfForm.addComponent(btns);

		rfForm.addComponent(popup);
		Panel formPanel = new Panel("Random Forest parameters");
		formPanel.setSizeFull();
		rfForm.setMargin(true);

		formPanel.setContent(rfForm);
		main.addComponent(formPanel);
		
		Panel mainMain  = new Panel();
		mainMain.setContent(main);
		//mainMain.setSizeFull();
		mainMain.setHeight("800px");

		setCompositionRoot(mainMain);
	}
	
	
	public Panel getComparisonPanel() throws Exception {
		Panel p = new Panel("Comparison Results");
		
		SparkRandomForest sparkRf = new SparkRandomForest(((MyUI)getUI()).data, ((MyUI)getUI()).rf, propTrain.getValue().doubleValue());		
		WekaRandomForest wRf = new WekaRandomForest(((MyUI)getUI()).data, ((MyUI)getUI()).rf, propTrain.getValue().doubleValue());
		RRandomForest rRf = new RRandomForest(((MyUI)getUI()).data, ((MyUI)getUI()).rf, propTrain.getValue().doubleValue());
		
		 double resSpark = sparkRf.aggregateEval(nbIter.getValue().intValue(), propTrain.getValue());
		 double resWeka = wRf.aggregateEval(nbIter.getValue().intValue(), propTrain.getValue());
		 double resR = rRf.aggregateEval(nbIter.getValue().intValue(), propTrain.getValue());
		 
		 VerticalLayout resultLayout = new VerticalLayout(new Label("R : " + resR), new Label("Weka : " + resWeka), new Label("Spark : " + resSpark));
		 p.setContent(resultLayout);
		 
		 return p;
		 
		 
	} 
}

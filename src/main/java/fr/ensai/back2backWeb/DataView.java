package fr.ensai.back2backWeb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import application.Data;

@SuppressWarnings("serial")
public class DataView extends Composite implements View {

	public String fileName;
	public FormLayout dataForm = new FormLayout();
	public Button submit = new Button("Submit");
	public Button reset = new Button("Restore default");

	public RadioButtonGroup<String> hasRowname = new RadioButtonGroup<>("File has row names column");
	public RadioButtonGroup<String> header = new RadioButtonGroup<>("File has header");
	public final TextField targetName = new TextField();
	public final TextField sep = new TextField();
	public final TextField dec = new TextField();
	public final TextField catFeatures = new TextField();
	public RadioButtonGroup<String> classif = new RadioButtonGroup<>("Are you performing a classification ?");
	public Data data = new Data();
	
	VerticalLayout popupContent = new VerticalLayout();
	PopupView popup = new PopupView(null, popupContent);

	public DataView() {

		Upload upload = new Upload("Upload CSV File", new Upload.Receiver() {
			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				try {
					/*
					 * Here, we'll stored the uploaded file as a temporary file. No doubt there's a
					 * way to use a ByteArrayOutputStream, a reader around it, use ProgressListener
					 * (and a progress bar) and a separate reader thread to populate a container
					 * *during* the update.
					 * 
					 * This is quick and easy example, though.
					 */
					// tempFile = File.createTempFile("temp", ".csv");
					// tempFile = File.createTempFile("/home/alka/Documents/wine", ".csv");
					File file = new File("data/" + filename);
					// File tmp = File.
					fileName = filename;
					// return new FileOutputStream(tempFile);
					return new FileOutputStream(file);

				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		});

		header.setItems("True", "False");
		header.setValue("True");
		header.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

		targetName.setCaption("Target variable name");
		targetName.setDescription("The name is case sensitive");
		targetName.setRequiredIndicatorVisible(true);

		hasRowname.setItems("True", "False");
		hasRowname.setValue("False");
		hasRowname.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

		sep.setCaption("CSV Field separator");
		sep.setDescription("e.g : ';', ',', '|', etc");
		sep.setValue(",");
		sep.setRequiredIndicatorVisible(true);

		dec.setCaption("Decimal separator");
		dec.setDescription("e.g : '.', ',', etc");
		dec.setValue(".");
		dec.setRequiredIndicatorVisible(true);

		catFeatures.setCaption("Categorical features");
		catFeatures.setDescription("separated by comma");
		catFeatures.setPlaceholder("var1,var2,...,varn");
		catFeatures.setWidth("");

		classif.setItems("True", "False");
		classif.setValue("True");
		classif.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

		HorizontalLayout btns = new HorizontalLayout(submit, reset);

		dataForm.addComponents(upload, header, targetName, hasRowname, sep, dec, catFeatures, classif, btns);
		dataForm.addComponent(popup);
		dataForm.setMargin(true);
		dataForm.setSizeFull();
		Panel main = new Panel("Data parameters");
		main.setContent(dataForm);

		submit.setStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addClickListener(e -> {

			if (targetName == null || sep == null || dec == null || targetName.isEmpty() || sep.isEmpty()
					|| dec.isEmpty()) {
				Notification.show("Please fill all required fields!!!", Notification.Type.ERROR_MESSAGE);
			} else if (fileName == null || fileName.isEmpty()) {
				Notification.show("Please upload a valid csv file!!!", Notification.Type.ERROR_MESSAGE);
			} else {
				//Notification.show(fileName + " uploaded!!", Notification.Type.HUMANIZED_MESSAGE);

				data.setPath("data/" + fileName);
				data.setHeader(header.getValue().toLowerCase());
				data.setTargetName(targetName.getValue());
				data.setHasRowNames(hasRowname.getValue().toLowerCase());
				data.setSep(sep.getValue());
				data.setDec(dec.getValue());
				data.setClassif(classif.getValue().equalsIgnoreCase("true") ? true : false);

				if (!(catFeatures == null || catFeatures.isEmpty())) {
					List<String> tmp = Arrays.asList(catFeatures.getValue().split(","));
					tmp = tmp.stream().map(x -> x.trim()).collect(Collectors.toList());
					data.setCatFeaturesNames((ArrayList<String>) tmp);
				}

			//save the data object in the main UI class
			((MyUI)getUI()).data = data;
			
			//Notification.show(((MyUI)getUI()).data.getCatFeaturesNames().toString(), Notification.Type.HUMANIZED_MESSAGE);
			popupContent.removeAllComponents();
			popupContent.addComponent(new Label("***Recap of data params***"));
			popupContent.addComponent(new Label("File: " + ((MyUI) getUI()).data.getPath()));
			popupContent.addComponent(new Label("Target variable: " + ((MyUI) getUI()).data.getTargetName()));
			popupContent.addComponent(new Label("File has header: " + ((MyUI) getUI()).data.getHeader()));
			popupContent.addComponent(new Label("File has row names: " + ((MyUI) getUI()).data.getHasRowNames()));
			popupContent.addComponent(new Label("Categorical Features: " + ((MyUI) getUI()).data.getCatFeaturesNames().toString()));
			popupContent.addComponent(new Label("File separator: " + ((MyUI) getUI()).data.getSep()));
			popupContent.addComponent(new Label("Decimal delimitor: " + ((MyUI) getUI()).data.getDec()));
			
			Button stay = new Button("Stay here", e2 -> {
				popup.setPopupVisible(false);
			}) ;
			Button btnDtree = new Button("Decision Tree",  e2 -> {
				((MyUI) getUI()).getNavigator().navigateTo("decisonTree");
			});
			Button btnRf = new Button("Random Forest",  e2 -> {
				((MyUI) getUI()).getNavigator().navigateTo("randomForest");
			});
			Label msg1 = new Label("Data succesfully uploaded and parameters well set");
			Label msg2 = new Label("Continue to ...");
			
			CssLayout choiceBtn = new CssLayout(btnDtree, btnRf, stay);
			//VerticalLayout choice = new VerticalLayout(msg1, msg2, choiceBtn);
			//String tmp = ((MyUI) getUI()).data.isClassif()  ? "Classification":"Regression";
			//popupContent.addComponent(new Label("Model type: " + tmp ));
			
			popupContent.addComponents(msg1, msg2, choiceBtn);
			popup.setPopupVisible(true);
			
			}

		});

		reset.setStyleName(ValoTheme.BUTTON_DANGER);
		reset.addClickListener(e -> {
			((MyUI) getUI()).data = null;
			header.setValue("True");
			targetName.clear();
			hasRowname.setValue("False");
			sep.setValue(",");
			dec.setValue(".");
			catFeatures.clear();
			classif.setValue("True");

			Path p = Paths.get("data/" + fileName);

			try {
				Files.deleteIfExists(p);
				fileName = null;
			} catch (IOException e1) {

				Notification.show(e1.getMessage());
			}

		});

		main.setSizeFull();
		setCompositionRoot(main);
	}
	
	
	
	
//	 @Override
//	    public void attach() {
//	            ((MyUI)this.getUI()).data = data;
//	    }
}

// package fr.ensai.back2backWeb;
//
// import com.vaadin.navigator.View;
// import com.vaadin.ui.Composite;
// import com.vaadin.ui.Panel;
//
//
// @SuppressWarnings("serial")
// public class DataView extends Composite implements View {
//
//
// //public DataForm form = new DataForm(this);
//
// public DataView() {
// Panel main = new Panel("Data parameters");
// //main.setContent(form);
// setCompositionRoot(main);
// }
//
//
// }

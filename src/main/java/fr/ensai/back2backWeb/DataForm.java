//package fr.ensai.back2backWeb;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import com.vaadin.ui.Button;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Notification;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.RadioButtonGroup;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.Upload;
//import com.vaadin.ui.themes.ValoTheme;
//
//import application.Data;
//
//@SuppressWarnings("serial")
//public class DataForm extends FormLayout {
//	
//	
//	Data data;
//	MyUI ui;
//	
//	public String fileName;
// 	public FormLayout dataForm = new FormLayout();
// 	Button submit = new Button("Submit");
// 	Button reset = new Button("Reset");
//	RadioButtonGroup<String> header = new RadioButtonGroup<>("file has header");
//	final TextField targetName = new TextField();
//	final TextField sep = new TextField();
//	final TextField dec = new TextField();
//	final TextField catFeatures = new TextField();
//	RadioButtonGroup<String> classif = new RadioButtonGroup<>("Are you performing a classification ?");
//	
//        
//	public DataForm(MyUI ui) {
//		this.ui = ui;
//		
//    	Upload upload = new Upload("Upload CSV File", new Upload.Receiver() {
//    	      @Override
//    	      public OutputStream receiveUpload(String filename, String mimeType) {
//    	        try {
//    	          /* Here, we'll stored the uploaded file as a temporary file. No doubt there's
//    	            a way to use a ByteArrayOutputStream, a reader around it, use ProgressListener (and
//    	            a progress bar) and a separate reader thread to populate a container *during*
//    	            the update.
//
//    	            This is quick and easy example, though.
//    	            */
//    	         // tempFile = File.createTempFile("temp", ".csv");
//    	         // tempFile = File.createTempFile("/home/alka/Documents/wine", ".csv");
//    	        	File file = new File("data/"+filename);
//    	          //File tmp = File.
//    	         fileName = filename;
//    	         // return new FileOutputStream(tempFile);
//    	          return new FileOutputStream(file);
//    	          
//    	          
//    	        } catch (IOException e) {
//    	          e.printStackTrace();
//    	          return null;
//    	        }
//    	      }
//    	    });
//    	     
//    	
//     
//        
//        header.setItems("True", "False");
//        header.setValue("True");
//        header.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
//        
//        
//        targetName.setCaption("Target variable name");
//        targetName.setDescription("The name is case sensitive");
//        
//        RadioButtonGroup<String> hasRowname = new RadioButtonGroup<>("file has row names column");
//        hasRowname.setItems("True", "False");
//        hasRowname.setValue("False");
//        hasRowname.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
//        
//        
//        sep.setCaption("Field separator");
//        sep.setDescription("e.g : ';', ',', '|', etc");
//        sep.setValue(",");
//        
//        
//        dec.setCaption("Decimal separator");
//        dec.setDescription("e.g : '.', ',', etc");
//        dec.setValue(";");
//        
//        
//        catFeatures.setCaption("Categorical features");
//        catFeatures.setDescription("separated by comma");
//        catFeatures.setPlaceholder("var1,var2,...,varn");
//        catFeatures.setWidth("");
//        
//        
//        classif.setItems("True", "False");
//        classif.setValue("True");
//        classif.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
//        
//      
//        HorizontalLayout btns = new HorizontalLayout(submit, reset);
//       
//        dataForm.addComponents(upload, header, targetName, hasRowname, sep, dec, catFeatures, classif, btns);
//        dataForm.setMargin(true);
//        Panel main = new Panel("Data parameters");
//        main.setContent(dataForm);
//        
//        submit.setStyleName(ValoTheme.BUTTON_PRIMARY);
//        submit.addClickListener(e -> {
//        	if (fileName != null) {
//        		Notification.show(fileName, " uploaded!!", Notification.Type.HUMANIZED_MESSAGE);
//
//        	}
//        	else {
//        		Notification.show( "No file uploaded!!", Notification.Type.HUMANIZED_MESSAGE);
//
//        	}
//        	
//        });
//        
//        reset.setStyleName(ValoTheme.BUTTON_DANGER);
//        reset.addClickListener(e -> {
//       	
//            header.setValue("True");
//            targetName.setValue("");            
//            hasRowname.setValue("False");
//            sep.setValue(",");
//            dec.setValue(";");            
//            catFeatures.setValue("");       
//            classif.setValue("True");
//          
//            	Path p = Paths.get("data/"+fileName);
//            	
//                try {
//					Files.deleteIfExists(p);
//					fileName = null;
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					Notification.show(e1.getMessage());
//				}
//            
//        });
//        
//        
//    }
//
//}

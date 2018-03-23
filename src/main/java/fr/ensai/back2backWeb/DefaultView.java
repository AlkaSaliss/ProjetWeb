package fr.ensai.back2backWeb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.marcus.PivotTable;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


public class DefaultView extends Composite implements View {
	   
	
	public String fileName;
    public DefaultView() {
    	


        
    	
    	Upload upload = new Upload("Upload CSV File", new Upload.Receiver() {
    	      @Override
    	      public OutputStream receiveUpload(String filename, String mimeType) {
    	        try {
    	          /* Here, we'll stored the uploaded file as a temporary file. No doubt there's
    	            a way to use a ByteArrayOutputStream, a reader around it, use ProgressListener (and
    	            a progress bar) and a separate reader thread to populate a container *during*
    	            the update.

    	            This is quick and easy example, though.
    	            */
    	         // tempFile = File.createTempFile("temp", ".csv");
    	         // tempFile = File.createTempFile("/home/alka/Documents/wine", ".csv");
    	        	File file = new File("data/"+filename);
    	          //File tmp = File.
    	         fileName = filename;
    	         // return new FileOutputStream(tempFile);
    	          return new FileOutputStream(file);
    	          
    	          
    	        } catch (IOException e) {
    	          e.printStackTrace();
    	          return null;
    	        }
    	      }
    	    });
    	     
     
        RadioButtonGroup<String> header = new RadioButtonGroup<>("file has header");
        header.setItems("True", "False");
        header.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        
        final TextField targetName = new TextField();
        targetName.setCaption("Target variable name");
        targetName.setDescription("The name is case sensitive");
        
        RadioButtonGroup<String> hasRowname = new RadioButtonGroup<>("file has row names column");
        hasRowname.setItems("True", "False");
        hasRowname.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        
        final TextField sep = new TextField();
        sep.setCaption("Field separator");
        sep.setDescription("e.g : ';', ',', '|', etc");
        
        final TextField dec = new TextField();
        dec.setCaption("Decimal separator");
        dec.setDescription("e.g : '.', ',', etc");
        
        final TextField catFeatures = new TextField();
        catFeatures.setCaption("Categorical features");
        catFeatures.setDescription("separated by semi-column");
        
        RadioButtonGroup<String> classif = new RadioButtonGroup<>("file has row names column");
        classif.setItems("True", "False");
        classif.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        
       Button btn = new Button("click me", e -> {
    	   Notification.show(fileName, " uploaded!!", Notification.Type.HUMANIZED_MESSAGE);
       } ) ;
        VerticalLayout layout = new VerticalLayout(upload, header, targetName, hasRowname, sep, dec, catFeatures, classif, btn);
        
       // setCompositionRoot(new Label("This is the default view"));
        setCompositionRoot(layout);
    }
}

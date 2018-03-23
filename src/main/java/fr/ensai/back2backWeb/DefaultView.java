package fr.ensai.back2backWeb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.marcus.PivotTable;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Alejandro Duarte
 */
public class DefaultView extends Composite implements View {

//	ByteArrayOutputStream tempCSV;
	//PivotTable pivotTable;
	
//	private ByteArrayOutputStream tempCSV;
//    private PivotTable pivotTable;
	
	protected File tempFile;
	  protected Tab table;
    public DefaultView() {
    	
//        Upload upload = new Upload();
//        upload.setCaption("Click to upload");
//        upload.setImmediateMode(false);
        
        
//        final VerticalLayout layout = new VerticalLayout();
//        layout.setSpacing(true);
//        layout.setMargin(true);
//        //setContent(layout);

       
        
//        Upload upload = new Upload("Upload CSV",
//                new Upload.Receiver() {
//                    @Override
//                    public OutputStream receiveUpload(String name, String mime) {
//                         tempCSV = new ByteArrayOutputStream();
//                        return tempCSV;
//                    }
//                }
//        );
//        upload.setImmediateMode(true);
//
//        upload.addFinishedListener(new Upload.FinishedListener() {
//            @Override
//            public void uploadFinished(Upload.FinishedEvent finishedEvent) {
//                pivotTable.setData(tempCSV.toString());
//            }
//        });
//
//        pivotTable = new PivotTable();
        
    	
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
    	          tempFile = File.createTempFile("temp", ".csv");
    	          return new FileOutputStream(tempFile);
    	        } catch (IOException e) {
    	          e.printStackTrace();
    	          return null;
    	        }
    	      }
    	    });
    	
    	upload.addFinishedListener(new Upload.FinishedListener() {
    	      @Override
    	      public void uploadFinished(Upload.FinishedEvent finishedEvent) {
    	        try {
    	          /* Let's build a container from the CSV File */
    	          FileReader reader = new FileReader(tempFile);
    	          IndexedContainer indexedContainer = buildContainerFromCSV(reader);
    	          reader.close();
    	          tempFile.delete();

    	          /* Finally, let's update the table with the container */
    	          table.setCaption(finishedEvent.getFilename());
    	          table.setContainerDataSource(indexedContainer);
    	          table.setVisible(true);
    	        } catch (IOException e) {
    	          e.printStackTrace();
    	        }
    	      }
    	    });
    	

//        layout.addComponents(upload, pivotTable);
        
//        final TextField path = new TextField();
//        path.setCaption("Path to the csv file");
//        path.setDescription("e.g. /data/iris.csv");
        
     
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
        
        
        VerticalLayout layout = new VerticalLayout(upload, pivotTable, header, targetName, hasRowname, sep, dec, catFeatures, classif);
        
       // setCompositionRoot(new Label("This is the default view"));
        setCompositionRoot(layout);
    }
}

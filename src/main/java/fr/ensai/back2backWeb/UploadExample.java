//package fr.ensai.back2backWeb;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import com.vaadin.book.examples.BookExampleBundle;
//import com.vaadin.terminal.FileResource;
//import com.vaadin.terminal.StreamResource;
//import com.vaadin.terminal.StreamResource.StreamSource;
//import com.vaadin.ui.CustomComponent;
//import com.vaadin.ui.Embedded;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.Upload;
//import com.vaadin.ui.Upload.Receiver;
//import com.vaadin.ui.Upload.SucceededEvent;
//import com.vaadin.ui.Upload.SucceededListener;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window.Notification;
//
//public class UploadExample extends CustomComponent implements BookExampleBundle {
//    private static final long serialVersionUID = -4292553844521293140L;
//
//    public void init (String context) {
//        VerticalLayout layout = new VerticalLayout();
//        
//        if ("basic".equals(context))
//            basic(layout);
//        else
//            layout.addComponent(new Label("Invalid context: " + context));
//        
//        setCompositionRoot(layout);
//    }
//    
//    void basic(VerticalLayout layout) {
//        // BEGIN-EXAMPLE: component.upload.basic
//        // Create the upload with a caption and set receiver later
//        Upload upload = new Upload("Upload Image Here", null);
//        upload.setButtonCaption("Start Upload");
//        
//        // Put the upload component in a panel
//        Panel panel = new Panel("Cool Image Storage");
//        panel.addComponent(upload);
//        
//        // Show uploaded file in this placeholder
//        final Embedded image = new Embedded("Uploaded Image");
//        image.setVisible(false);
//        panel.addComponent(image);
//
//        // Implement both receiver that saves upload in a file and
//        // listener for successful upload
//        class ImageUploader implements Receiver, SucceededListener {
//            private static final long serialVersionUID = -1276759102490466761L;
//
//            public File file;
//            
//            public OutputStream receiveUpload(String filename, String mimeType) {
//                // Create upload stream
//                FileOutputStream fos = null; // Output stream to write to
//                try {
//                    // Open the file for writing.
//                    file = new File("/tmp/uploads/" + filename);
//                    fos = new FileOutputStream(file);
//                } catch (final java.io.FileNotFoundException e) {
//                    getWindow().showNotification(
//                            "Could not open file<br/>", e.getMessage(),
//                            Notification.TYPE_ERROR_MESSAGE);
//                    return null;
//                }
//                return fos; // Return the output stream to write to
//            }
//
//            public void uploadSucceeded(SucceededEvent event) {
//                // Show the uploaded file in the image viewer
//                image.setVisible(true);
//                image.setSource(new FileResource(file, getApplication()));
//            }
//        };
//        final ImageUploader uploader = new ImageUploader(); 
//        upload.setReceiver(uploader);
//        upload.addListener(uploader);
//        // END-EXAMPLE: component.upload.basic
//
//        // Create uploads directory
//        File uploads = new File("/tmp/uploads");
//        if (!uploads.exists() && !uploads.mkdir())
//            layout.addComponent(new Label("ERROR: Could not create upload dir"));
//
//        ((VerticalLayout) panel.getContent()).setSpacing(true);
//        panel.setWidth("-1");
//        layout.addComponent(panel);
//    }
//
//    void memorybuffer(VerticalLayout layout) {
//        // BEGIN-EXAMPLE: component.upload.memorybuffer
//        // Create the upload with a caption and set receiver later
//        Upload upload = new Upload("Upload the image here", null);
//        
//        // Put the upload component in a panel
//        Panel panel = new Panel("Cool Image Storage");
//        panel.addComponent(upload);
//        
//        // Show uploaded file in this placeholder
//        final Embedded image = new Embedded("Uploaded Image");
//        panel.addComponent(image);
//
//        // Put upload in this memory buffer that grows automatically
//        final ByteArrayOutputStream os =
//            new ByteArrayOutputStream(10240);
//        
//        // Implement receiver that stores in the memory buffer
//        class ImageReceiver implements Receiver {
//            private static final long serialVersionUID = -1276759102490466761L;
//
//            public String filename; // The original filename
//            
//            public OutputStream receiveUpload(String filename, String mimeType) {
//                this.filename = filename;
//                os.reset(); // If re-uploading
//                return os;
//            }
//        };
//        final ImageReceiver receiver = new ImageReceiver(); 
//        upload.setReceiver(receiver);
//
//        // Handle success in upload
//        upload.addListener(new SucceededListener() {
//            private static final long serialVersionUID = 6053253347529760665L;
//
//            public void uploadSucceeded(SucceededEvent event) {
//                image.setCaption("Uploaded Image " + receiver.filename +
//                        " has length " + os.toByteArray().length);
//                
//                // Display the image in the feedback component
//                StreamSource source = new StreamSource() {
//                    private static final long serialVersionUID = -4905654404647215809L;
//
//                    public InputStream getStream() {
//                        return new ByteArrayInputStream(os.toByteArray());
//                    }
//                };
//                
//                if (image.getSource() == null)
//                    image.setSource(new StreamResource(source,
//                            receiver.filename, getApplication()));
//                else { // Replace picture
//                    StreamResource resource = (StreamResource) image.getSource();
//                    resource.setStreamSource(source);
//                    resource.setFilename(receiver.filename);
//                }
//
//                image.requestRepaint();
//            }
//        });
//        // END-EXAMPLE: component.upload.memorybuffer
//
//        ((VerticalLayout) panel.getContent()).setSpacing(true);
//        layout.addComponent(panel);
//    }
//}

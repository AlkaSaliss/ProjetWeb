package fr.ensai.back2backWeb;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;


public class DataView extends Composite implements View {

    public DataView() {
        setCompositionRoot(new Label("This is the Data view"));
    }
}

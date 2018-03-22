package fr.ensai.back2backWeb;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

/**
 * @author Alejandro Duarte
 */
public class RandomForestView extends Composite implements View {

    public RandomForestView() {
        setCompositionRoot(new Label("This is Random forest view"));
    }
}

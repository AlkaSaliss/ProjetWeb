package fr.ensai.back2backWeb;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

/**
 * @author Alejandro Duarte
 */
public class DecisionTreeView extends Composite implements View {

    public DecisionTreeView() {
        setCompositionRoot(new Label("This Decision tree view"));
    }
}

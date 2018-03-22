package application;
import org.renjin.sexp.DoubleArrayVector;

public class RDecisionTree extends RModel {

	// Attributes
	private DecisionTree dtree = new DecisionTree();
	private boolean classif; // to specify whether its classification or regression
	
	// Constructors
	public RDecisionTree() {
		super();
	}
	
	public RDecisionTree(Data d, application.DecisionTree dt, double propTrain) throws Exception {
		this.setCompleteData(d);
		this.split(propTrain); // initial split
		this.dtree = dt;
	}
	
	// Methods
	public void setCompleteData(Data d) throws Exception {
		super.setCompleteData(d);
		this.classif = d.isClassif();
	}
	
	public void fit() throws Exception {
		String splitCriteria=this.dtree.isGini()?"gini":"information";
		String options  = String.join(",", "control=rpart.control(maxdepth="+this.dtree.getMaxDepth(), "minsplit = " + this.dtree.getMinSplit(), "cp=" + this.dtree.getCp() + ")", "parms = list(split= '" + splitCriteria + "')");
		if(this.classif) {
			options += ",method = '"+"class'";
		}
		else {
			options += ",method = '"+"anova'";
		}
		this.getEngine().eval("library(rpart)"); 
		this.getEngine().eval("dtree <- rpart(formula, " + ",data=train, " + options + ")");
	}

	public double eval() throws Exception {
		if(this.classif) {
		super.getEngine().eval("pred <- predict(dtree, test, type=\"class\" )");
		super.getEngine().eval("confMat <- table(test[,targetName], pred)");
		super.getEngine().eval("accuracy <- sum(diag(confMat))/sum(confMat)");
		return  ((DoubleArrayVector) super.getEngine().eval("accuracy")).asReal();
		}
		else {
			super.getEngine().eval("pred <- predict(dtree, test)");
			super.getEngine().eval("mse=sum((test[,targetName]-pred)**2)/nrow(test)");
			return  ((DoubleArrayVector) super.getEngine().eval("mse")).asReal();
		}
	}
}

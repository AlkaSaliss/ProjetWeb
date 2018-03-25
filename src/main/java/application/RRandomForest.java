package application;
import org.renjin.sexp.DoubleArrayVector;

public class RRandomForest extends RModel {
	
	// Attributes
	private RandomForest rf = new RandomForest();
	private boolean classif; // to specify whether it is classification or regression

	// Constructor
	public RRandomForest() throws Exception {
		super();
	}
	
	public RRandomForest(Data data, RandomForest rf2, double propTrain) throws Exception {
		this.setCompleteData(data);
		this.split(propTrain); // initial split
		this.rf = rf2;
	}

	// Methods
	public void setCompleteData(Data d) throws Exception {
		super.setCompleteData(d);
		this.classif = d.isClassif();
	}

	public void fit() throws Exception {
		if (this.rf.getMtry()==0) { this.getEngine().eval("mtry <- floor(sqrt(ncol(data)-1))"); }
		else { this.getEngine().eval("mtry <- floor((ncol(data)-1)*" + this.rf.getMtry() + ")");}
		String options  = String.join(",", "mtry=mtry ", "ntree = " + this.rf.getNtrees(), "sampsize=floor(nrow(train)* " + this.rf.getSampsize() + ")");
		if (this.classif) {
			options += ",type ='classification'";
		}
		else {
			options += ",type ='regression'";
		}
		this.getEngine().eval("library(randomForest)"); 
		this.getEngine().eval("rf <- randomForest(formula, " + ",data=train, " + options + ")");
	}

	public double eval() throws Exception {
		if (this.classif) {
		this.getEngine().eval("pred <- predict(rf, test, type=\"class\" )");
		this.getEngine().eval("confMat <- table(test[,targetName], pred)");
		this.getEngine().eval("accuracy <- sum(diag(confMat))/sum(confMat)");
		return  ((DoubleArrayVector) this.getEngine().eval("accuracy")).asReal();
		}
		else {
			super.getEngine().eval("pred <- predict(rf, test)");
			super.getEngine().eval("mse=sum((test[,targetName]-pred)**2)/nrow(test)");
			return  ((DoubleArrayVector) super.getEngine().eval("mse")).asReal();
		}
	}
}

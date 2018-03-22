package application;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.M5P;

public class WekaDecisionTree  extends WekaModel{

	// Attributes
	private DecisionTree dtree=new DecisionTree(); 
	private J48 classifier= new J48();
	private M5P regTree= new M5P();
	private boolean classif; 
	
	// Constructors
	public WekaDecisionTree() {
		super();
	}
	
	public WekaDecisionTree(Data d, application.DecisionTree dt, double propTrain) throws Exception {
		super();
		this.setCompleteData(d);
		this.split(propTrain); //split initial 
		this.dtree = dt;
	}
	
	// Methods
	public void setCompleteData(Data d) throws Exception {
		super.setCompleteData(d);
		this.classif = d.isClassif();
	}
	
	public void fit() throws Exception {
		if(this.classif) {
			this.classifier.setMinNumObj(this.dtree.getMinPerLeaf());
			this.classifier.buildClassifier(this.getTrain());
		}
		else {
			this.regTree.setMinNumInstances(this.dtree.getMinPerLeaf());
			this.regTree.buildClassifier(this.getTrain());
		}
	}

	public double eval() throws Exception {
		Evaluation eval = new Evaluation(this.getTrain());
		if(this.classif) {
		eval.evaluateModel(this.classifier, this.getTest());
		return 1-eval.errorRate();
		}
		else {
			eval.evaluateModel(this.regTree, this.getTest());
			return Math.pow(eval.rootMeanSquaredError(),2);
		}
	}
}

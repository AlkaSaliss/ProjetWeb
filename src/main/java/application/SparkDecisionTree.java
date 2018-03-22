package application;
import java.io.Serializable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import scala.Tuple2;

public class SparkDecisionTree extends SparkModel implements Serializable{
	
	// Attributes 
	private static final long serialVersionUID = 1L;
	private String impurity = "gini";
	private DecisionTreeModel model;
	private application.DecisionTree dt;
	private boolean classif;
	private int numClasses;
	
	// Constructor
	public SparkDecisionTree(Data d, application.DecisionTree dt, double propTrain) throws Exception {
		this.setCompleteData(d);
		this.split(propTrain); //split initial 
		this.dt = dt;
		if(this.dt.isGini()) {
			this.impurity = "gini";
		}
		else {
			this.impurity = "variance";
		}
	}
	
	// Methods
	public void setCompleteData(Data d) {
		super.setCompleteData(d);
		this.classif = d.isClassif();
		this.numClasses = d.getNumClasses();
	}

	public void fit() {
		if(this.classif) {
			this.model = org.apache.spark.mllib.tree.DecisionTree.trainClassifier(this.getTrain(), this.numClasses,
					  this.getCategoricalFeaturesInfo(), this.impurity, this.dt.getMaxDepth(), this.dt.getMaxBins());
		}
		else {
			this.model = org.apache.spark.mllib.tree.DecisionTree.trainRegressor(this.getTrain(),
					  this.getCategoricalFeaturesInfo(), this.impurity, this.dt.getMaxDepth(), this.dt.getMaxBins());
		}
	}

	public double eval() {
		if(this.classif) {
			JavaPairRDD<Double, Double> predictionAndLabel = this.getTest().mapToPair(p -> new Tuple2<>(this.model.predict(p.features()), p.label()));
			Double testErr = predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) this.getTest().count();
			return 1-testErr;
		}
		else {
			JavaPairRDD<Double, Double> predictionAndLabel =  this.getTest().mapToPair(p -> new Tuple2<>(this.model.predict(p.features()), p.label()));
			double testMSE = predictionAndLabel.mapToDouble(pl -> {double diff = pl._1() - pl._2(); return diff * diff;}).mean();
			return testMSE;
		}
	}
}

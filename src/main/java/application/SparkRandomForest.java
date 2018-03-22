package application;
import java.io.Serializable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import application.Data;
import application.SparkModel;
import scala.Tuple2;

public class SparkRandomForest  extends SparkModel  implements Serializable{
	
	// Attributes
	private static final long serialVersionUID = 1L;
	private String impurity = "gini";
	private RandomForestModel model;
	private application.RandomForest rf;
	private boolean classif;
	private int numClasses;
	
	// Constructor
	public SparkRandomForest(Data d, application.RandomForest rf, double propTrain) throws Exception {
		this.setCompleteData(d);
		this.split(propTrain); // initial split
		this.rf = rf;
		if(this.rf.isGini()) {
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
			this.model = RandomForest.trainClassifier(this.getTrain(), this.numClasses,
					  this.getCategoricalFeaturesInfo(), this.rf.getNtrees(), this.rf.getFeatureSubsetStrategy(), this.impurity, this.rf.getMaxDepth(), this.rf.getMaxBins(),
					  this.rf.getSeed());
		}
		else {
			this.model = RandomForest.trainRegressor(this.getTrain(),
					  this.getCategoricalFeaturesInfo(), this.rf.getNtrees(), this.rf.getFeatureSubsetStrategy(), this.impurity, this.rf.getMaxDepth(), this.rf.getMaxBins(),
					  this.rf.getSeed());
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

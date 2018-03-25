package application;
import org.apache.log4j.Logger;

import application.Data;
import application.DecisionTree;
import application.RDecisionTree;
import application.SparkDecisionTree;
import application.WekaDecisionTree;

import java.util.ArrayList;
import org.apache.log4j.Level;

public class Launch {

	public static void launchComparison(Data d, DecisionTree dt, double propTrain, int nbIter) throws Exception {
		SparkDecisionTree sdt = new SparkDecisionTree(d, dt, propTrain);
		WekaDecisionTree wdt = new WekaDecisionTree(d, dt, propTrain);
		RDecisionTree rdt = new RDecisionTree(d, dt, propTrain);
		System.out.println("Launch a comparison for DT models");
		System.out.println("-----------------------------------");
		System.out.println("SparkML: " + sdt.aggregateEval(nbIter, propTrain));
		System.out.println("Weka: " + wdt.aggregateEval(nbIter, propTrain));
		System.out.println("R: " + rdt.aggregateEval(nbIter, propTrain));
	} // → this method could (and should be) ameliorated in order to be more general
	  // it is just given for a DT comparison

	public static void main(String[] args) throws Exception {
		// logger configuration
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);

		// ----------------------------------------------------------------- //
		// Data parameters 
		// ----------------------------------------------------------------- //
		String path = "data/testreg2.csv";
		String header = "true";
		String targetName = "y";
		String hasRowNames = "false";
		String sep = ",";
		String dec = ".";
		boolean classif = false;
		ArrayList<String> catNames = new ArrayList<String>();
		catNames.add("cat");
		catNames.add("cat2");

		// ----------------------------------------------------------------- //
		// Model parameters  (a Decision Tree in our case)
		// ----------------------------------------------------------------- //

		int maxDepth = 10;
		int minSplit = 15;
		double cp = 0.1;
		boolean gini = false;
		int minPerLeaf = 10;
		int maxBins = 50;

		// ----------------------------------------------------------------- //
		// Set Data parameters & Model parameters
		// ----------------------------------------------------------------- //
		Data d =  new Data();
		d.setPath(path);
		d.setHeader(header);
		d.setTargetName(targetName);
		d.setHasRowNames(hasRowNames);
		d.setSep(sep);
		d.setDec(dec);
		d.setClassif(classif);
		d.setNumClasses(3); // for classif
		d.setCatFeaturesNames(catNames);

		application.DecisionTree dt = new DecisionTree();
		dt.setMaxDepth(maxDepth);
		dt.setMinSplit(minSplit);
		dt.setCp(cp);
		dt.setGini(gini);
		dt.setMinPerLeaf(minPerLeaf);
		dt.setMaxBins(maxBins);

		// ----------------------------------------------------------------- //
		// Set propTrain & nbIter for the comparison 
		// ----------------------------------------------------------------- //
		double propTrain = 0.8;
		int nbIter = 100;

		// ----------------------------------------------------------------- //
		// Set propTrain & nbIter for the comparison 
		// ----------------------------------------------------------------- //
		launchComparison(d, dt, propTrain, nbIter);
	}
}
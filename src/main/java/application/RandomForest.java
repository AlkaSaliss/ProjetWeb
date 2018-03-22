package application;
import java.io.Serializable;

public class RandomForest implements Serializable{
	
	// Attributes
	private static final long serialVersionUID = 1L;
	private int ntrees; // Number of trees to aggregate
	private double mtry; // Percentage of features to select for each tree
	private double sampsize; // Percentage of individuals to take for each tree
	private int maxDepth; // Maximum depth for each tree (needed for Spark)
	private int maxBins; // Needed for Spark
	private int seed; // Needed for Spark
	private String featureSubsetStrategy = "auto"; // Type of algorithm (needed for Spark)
	private boolean gini;
	
	// Constructor
	public RandomForest() {
		this.ntrees = 500;
		this.mtry = 0;
		this.sampsize = 0.7;
	}

	// Getters & Setters
	public int getNtrees() {
		return ntrees;
	}

	public void setNtrees(int ntrees) {
		this.ntrees = ntrees;
	}

	public double getMtry() {
		return mtry;
	}

	public void setMtry(double mtry) {
		this.mtry = mtry;
	}

	public double getSampsize() {
		return sampsize;
	}

	public void setSampsize(double sampsize) {
		this.sampsize = sampsize;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getMaxBins() {
		return maxBins;
	}

	public void setMaxBins(int maxBins) {
		this.maxBins = maxBins;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public String getFeatureSubsetStrategy() {
		return featureSubsetStrategy;
	}

	public void setFeatureSubsetStrategy(String featureSubsetStrategy) {
		this.featureSubsetStrategy = featureSubsetStrategy;
	}

	public boolean isGini() {
		return gini;
	}

	public void setGini(boolean gini) {
		this.gini = gini;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

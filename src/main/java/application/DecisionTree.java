package application;
import java.io.Serializable;

public class DecisionTree implements Serializable{
	// Attributes
	private static final long serialVersionUID = 1L;
	private int maxDepth; // Maximum depth of the tree
	private int minSplit; // Minimum number of instances  required for splitting
	private double cp ; // Complexity criteria
	private  boolean gini; // Criteria used for splitting
	private int minPerLeaf; // Minimum number of instances in each leaf
	private int maxBins; // Needed for SparkML
	
	// Constructor
	public DecisionTree() {
		this.maxDepth = 30;
		this.minSplit = 20;
		this.cp = 0.01d;
		this.gini = true;
		this.minPerLeaf=15;
		this.maxBins = 2;
	}

	// Getters & Setters
	
	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getMinSplit() {
		return minSplit;
	}

	public void setMinSplit(int minSplit) {
		this.minSplit = minSplit;
	}

	public double getCp() {
		return cp;
	}

	public void setCp(double cp) {
		this.cp = cp;
	}

	public boolean isGini() {
		return gini;
	}

	public void setGini(boolean gini) {
		this.gini = gini;
	}

	public int getMinPerLeaf() {
		return minPerLeaf;
	}

	public void setMinPerLeaf(int minPerLeaf) {
		this.minPerLeaf = minPerLeaf;
	}

	public int getMaxBins() {
		return maxBins;
	}

	public void setMaxBins(int maxBins) {
		this.maxBins = maxBins;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

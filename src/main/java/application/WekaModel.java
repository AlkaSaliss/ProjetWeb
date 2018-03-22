package application;
import weka.core.Instances;

public abstract class WekaModel implements Model {

	// Attributes 
	private Instances completeDataSet;
	private Instances train;
	private Instances test;
	
	// Constructor
	public WekaModel() {
		
	}

	// Methods
	public void setCompleteData(Data d) throws Exception {
			this.completeDataSet = d.readWeka();
	}

	public void split(double propTrain) {
		this.completeDataSet.randomize(new java.util.Random(0));
		int trainSize = (int) Math.round(this.completeDataSet.numInstances() * propTrain);
		int testSize = this.completeDataSet.numInstances() - trainSize;
		this.train = new Instances(this.completeDataSet, 0, trainSize);
		this.test = new Instances(this.completeDataSet, trainSize, testSize);
	}

	// Getters & Setters

	public Instances getCompleteDataSet() {
		return completeDataSet;
	}



	public void setCompleteDataSet(Instances completeDataSet) {
		this.completeDataSet = completeDataSet;
	}



	public Instances getTrain() {
		return train;
	}



	public void setTrain(Instances train) {
		this.train = train;
	}



	public Instances getTest() {
		return test;
	}



	public void setTest(Instances test) {
		this.test = test;
	}
}

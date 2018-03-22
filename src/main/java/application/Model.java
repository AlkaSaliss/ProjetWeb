package application;

public interface Model {

	public abstract void setCompleteData(Data d) throws Exception;
	public abstract void split(double propTrain) throws Exception;
	public abstract void fit() throws Exception;
	public abstract double eval() throws Exception;
	public default double aggregateEval(int iterations, double propTrain) throws Exception {
		double sum_eval = 0;
		for(int i=0; i< iterations; i++) {
			this.split(propTrain);
			this.fit();
			sum_eval += this.eval();
		} 	
		return sum_eval/iterations;
	}
}
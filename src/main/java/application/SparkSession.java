package application;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkSession{

	  // Attributes
	  private static SparkSession sess = new SparkSession();
	  private JavaSparkContext sc;

	   

	  // Private constructor
	  private SparkSession(){
		SparkConf sconf = new SparkConf().setAppName("conf").setMaster("local");
	    	sc = new JavaSparkContext(sconf);
	  }

	 // Methods
	  public static SparkSession getInstance(){
	    if(sess == null){
		sess = new SparkSession();
		}      
	    return sess;
	  }

	  public JavaSparkContext getContext(){
		  return this.sc;
	  }
}
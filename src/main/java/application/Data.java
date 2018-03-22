package application;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.ArrayUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import java.io.File;
import javax.script.ScriptException;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptContext;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;

public class Data implements Serializable {
	// Attributes
	private static final long serialVersionUID = 1L; // for Serializable
	private String path;
	private String header = "true";
	private String targetName;
	private String hasRowNames = "false";
	private String sep = ","; 
	private String dec = "."; 
	int numClasses;	
	private boolean classif = true;
	private ArrayList<String> catFeaturesNames = new ArrayList<String>();
	private Map<Integer, Integer> categoricalFeaturesInfo = Collections.synchronizedMap(new HashMap<Integer, Integer>()); 

	// Constructors
	public Data() {}
	
	public Data(String path, String header, String targetName, String hasRowNames, String sep, String dec, boolean classif, ArrayList<String> catFeaturesNames) {
		this.path = path;
		this.targetName = targetName;
		this.header = header;
		this.hasRowNames = hasRowNames;
		this.classif = classif;
		this.sep = sep;
		this.dec = dec;
		this.classif = classif;
		this.catFeaturesNames = catFeaturesNames;
		}
	
	// Data opening (and attribute managing) methods

	
	// Spark opening
	public JavaRDD<LabeledPoint> readSpark(){
		SQLContext sqlcontext = new SQLContext(SparkSession.getInstance().getContext());
		JavaRDD<Row> test = sqlcontext.read()
				.format("com.databricks.spark.csv")
				.option("inferSchema", "true")
				.option("header", this.header)
				.load(this.path).javaRDD();
		Map<String, Double> catmap = new HashMap<>();
		ArrayList<Integer> catFeaturesIndex = new ArrayList<Integer>();
		Row firstRow = test.first(); //only need the first row to get the index of each categorical columns → no need to do it for each row in the map
		HashMap<Integer, HashMap<String, Double>> stringToDouble = new HashMap<>(); //map of map → {1 (→ indexcol) : {"a" (→ modality):1, "b":2}}
		for(int i=0; i<this.catFeaturesNames.size(); i++) {
			Integer catindex = firstRow.fieldIndex(this.catFeaturesNames.get(i));
			catFeaturesIndex.add(catindex);
			this.categoricalFeaturesInfo.put(catindex-1, 0); //map initialization with all index columns associated to 0 
			stringToDouble.put(catindex, new HashMap<String, Double>());
		}
		long n_row = test.count();
		for(int l=0; l<n_row; l++) {
			Row ligne = test.take(l+1).get(l);
			for (Iterator<Integer> iter = catFeaturesIndex.iterator(); iter.hasNext(); ) {
			    Integer ind = iter.next();
			    String modality = ligne.getString(ind);
			    if(stringToDouble.get(ind).containsKey(modality)==false) {
			    	Integer previous = this.categoricalFeaturesInfo.get(ind-1);
			    	this.categoricalFeaturesInfo.put(ind-1, previous+1);
			    	stringToDouble.get(ind).put(modality, (double)stringToDouble.get(ind).size());
			    	}
			    }
			}
		JavaRDD<LabeledPoint> labeldata = test
			    .map((Row line) -> {
			    	int rowsize =  line.length();
			    	int indextarget = line.fieldIndex(this.targetName);
			    	Double finalLabel;
			    	if(this.classif) {
			    		String target = line.getAs(this.targetName);
			    		Double Catlabel;
				    	if(catmap.containsKey(target)) {
				    		Catlabel = catmap.get(target);
				    	}
				    	else {
				    		catmap.put(target, (double)catmap.size());
				    		Catlabel = catmap.get(target);
				    	}
				    	finalLabel = Catlabel;
			    	}
			    	else {
			    		finalLabel = line.getDouble(indextarget);
			    	}
			    	double[] global;
			    	if(this.catFeaturesNames.isEmpty() == false) {
			    		double[] colcat = new double[stringToDouble.size()];
				    	int k=0;
				    	for (Entry<Integer, HashMap<String, Double>> entry : stringToDouble.entrySet()) {
				    	    Integer key = entry.getKey(); //key : column index
				    	    HashMap<String, Double> value = entry.getValue(); // value : map which associates each modality to a double
				    	    String modality = line.getString(key);
				    	    if(value.containsKey(modality)) {
				    	    	colcat[k] = value.get(modality);
				    	    }
				    	    else {
				    	    	value.put(modality, (double)value.size());
				    	    	int previous_number_modality = this.categoricalFeaturesInfo.get(key);
				    	    	this.categoricalFeaturesInfo.put(key, previous_number_modality+1);
				    	    	colcat[k] = value.get(modality);
				    	    }
				    	    k++;
				    	}
				    	int n = (rowsize-stringToDouble.size())-1;
				    	int j = 0;
				    	int start=0;
				    	if(this.hasRowNames.equals("true")) {
				    		start=1;
				    		n--;
				    	}
				    	double[] col = new double[n];
				    	for(int i = start; i<n+1; i++) {
				    		if(i != indextarget & stringToDouble.containsKey(i)==false) {
				    			col[j] = (double)line.getDouble(i);
				    			j++;
				    		}
				    	}
				    	global = (double[])ArrayUtils.addAll(col, colcat);
			    	}
			    	else {
			    		int n = rowsize -1;
			    		int j = 0;
				    	
				    	int start=0;
				    	if(this.hasRowNames.equals("true")) {
				    		start=1;
				    		n--;
				    	}
				    	global = new double[n];
				    	for(int i = start; i<n+1; i++) {
				    		if(i != indextarget) {
				    			global[j] = (double)line.getDouble(i);
				    			j++;
				    		}
				    	}
			    	}
			    	LabeledPoint labelpt = new LabeledPoint(finalLabel, Vectors.dense(global));
			    	return labelpt;
			    });
		return labeldata;
	}

	// Renjin opening
	public void readR(RenjinScriptEngine engine) throws ScriptException {
		 // if the input file has rowname (we suppose it is the 1st column)
		 // → then we pass 1 for the row.names param in the R read.csv function
		 // → else we pass NULL 
		String rownames = this.hasRowNames.equalsIgnoreCase("true") ? "1" : "NULL";
		
		// Reading the data with R
		engine.eval("data <- read.csv(\"" + this.path + "\", header=" + this.header.toUpperCase() + ", sep=" + this.sep + ", row.names=" + rownames + ")");
		
		// Storing the target variable in R
		engine.eval("targetName <- \"" + this.targetName + "\"");
		// if we are doing classification, we convert (coerce) the target variable to a factor one
		if (this.classif) {
			engine.eval("data[, targetName] <- as.factor(data[, targetName])");
		}
		
		//Creating the needed formula (for most R models)
		engine.eval("formula <- as.formula(paste(targetName, \"~ .\"))");
		
		// Convert/coerce all categorical variables as factors in R		
		if (this.catFeaturesNames.size()>0){
			// Add quotes for each categorical variable name in the catFeaturesNames
			ArrayList<String> tmp = new ArrayList<String>();
			for (String s:this.catFeaturesNames) {
				tmp.add("\""  + s+ "\"");
			}
			String factors = String.join(",", tmp);
			
			// Converting list of categorical features as R vector
			factors = "c(" + factors + ")";
			engine.eval("cat_vars <- " + factors);
			engine.eval("for (v in cat_vars) {data[, v] = as.factor(data[, v])}");
		}	
	}
	
	// Weka opening
	public Instances readWeka() throws Exception {
		File f = new File(this.path);
		CSVLoader cnv = new CSVLoader();
		cnv.setSource(f);
		Instances data = cnv.getDataSet();
		
		// Retrieve the target column index and set this column as the model dependent variable
		int targetIndex = data.attribute(this.targetName).index(); // target variable index
		data.setClassIndex(targetIndex);
		
		// If dataset has a rownames column (we assume it is the 1st column) → we remove it
		if (this.hasRowNames.equalsIgnoreCase("true")) {
			String[] options = new String[2];
			options[0] = "-R";                                    // "range"
			options[1] = "1";                                     // first attribute
			Remove remove = new Remove();                         // new instance of filter
			remove.setOptions(options);                           // set options
			remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
			data = Filter.useFilter(data, remove);   // apply filter
		}
		
		// Convert target variable to Weka nominal one if we are performing classification
		if (this.classif & data.attribute(targetIndex).type()==0 ) {// 0 corresponds to numeric attribute type in Weka
			// → data.attribute(targetIndex).type()==0 to avoid the error generated when trying to apply numericToNominal on a string attribute
			NumericToNominal convert= new NumericToNominal();
	        String[] options= new String[2];
	        options[0]="-R";
	        options[1]= String.valueOf(targetIndex+1) ;
	        convert.setOptions(options);
	        convert.setInputFormat(data);
	        data = Filter.useFilter(data, convert);
		}
		
		// Convert/coerce categorical variables to Weka Nominal if necessary
		if (this.catFeaturesNames.size()>0){
			for (String s:this.catFeaturesNames) {
				int var_index = data.attribute(s).index(); // retrieve the categorical variable index
				if(data.attribute(var_index).type()==0 ){
					NumericToNominal convert= new NumericToNominal();
					String[] options= new String[2];
					options[0]="-R";
					options[1]= String.valueOf(var_index+1) ;
					convert.setOptions(options);
					convert.setInputFormat(data);
					data = Filter.useFilter(data, convert);
				}
			}
		}
		return data;
	}

	// getters & setters
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getHasRowNames() {
		return hasRowNames;
	}

	public void setHasRowNames(String hasRowNames) {
		this.hasRowNames = hasRowNames;
	}

	public String getSep() {
		return sep;
	}

	public void setSep(String sep) {
		this.sep = sep;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public int getNumClasses() {
		return numClasses;
	}

	public void setNumClasses(int numClasses) {
		this.numClasses = numClasses;
	}

	public boolean isClassif() {
		return classif;
	}

	public void setClassif(boolean classif) {
		this.classif = classif;
	}

	public ArrayList<String> getCatFeaturesNames() {
		return catFeaturesNames;
	}

	public void setCatFeaturesNames(ArrayList<String> catFeaturesNames) {
		this.catFeaturesNames = catFeaturesNames;
	}

	public Map<Integer, Integer> getCategoricalFeaturesInfo() {
		return categoricalFeaturesInfo;
	}

	public void setCategoricalFeaturesInfo(Map<Integer, Integer> categoricalFeaturesInfo) {
		this.categoricalFeaturesInfo = categoricalFeaturesInfo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

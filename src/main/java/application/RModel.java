package application;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;

public abstract class RModel implements Model {
	
	// Attributes
	private RenjinScriptEngine engine;
	
	// Constructor
	public RModel() {
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	    // create a Renjin engine →
	    RenjinScriptEngine engine = factory.getScriptEngine();
		this.engine = engine;
	}
	
	// Methods
	@Override  
	public void setCompleteData(Data d) throws Exception {
		d.readR(this.engine);
	}
	
	@Override
	public void split(double propTrain) throws Exception {
		//engine.eval("set.seed(123)");
		this.engine.eval("trainIndex <- sample(1:nrow(data), size=round(nrow(data)*" + propTrain + "), replace=F)");
		this.engine.eval("train <- data[trainIndex, ]");
		this.engine.eval("test <- data[-trainIndex, ]");
	}

	public RenjinScriptEngine getEngine() {
		return engine;
	}

	public void setEngine(RenjinScriptEngine engine) {
		this.engine = engine;
	}
}
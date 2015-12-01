package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.interfaces.AObject;

public class ASignum extends AFunction{

	public ASignum(AObject[] args){
		super(args);
	}

	@Override
	public double getResult(double x) {
		return (x<0.0)?-1.0:((x>0.0)?1.0:0.0);
	}

	@Override
	public String getStringRepresentation() {
		return "sign(" + getInner().getStringRepresentation() + ")";
	}

}

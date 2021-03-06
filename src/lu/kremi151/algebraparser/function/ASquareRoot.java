package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsLength=1,argsMinimum=1)
public class ASquareRoot extends AFunction{

	public ASquareRoot(AObject[] args){
		super(args);
	}

	@Override
	public double getResult(double x) throws AlgebraException {
		double innerResult = getInner().getResult(x);
		if(innerResult >= 0.0){
			return Math.sqrt(innerResult);
		}
		throw new AlgebraException("The inner contant of a square root has to be >= 0");
	}

	@Override
	public String getStringRepresentation() {
		return "sqrt(" + getInner().getStringRepresentation() + ")";
	}

}

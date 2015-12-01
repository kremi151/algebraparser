package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsLength=2,argsMinimum=2)
public class ARoot extends AFunction{
	
	private AObject base = null;

	public ARoot(AObject[] args){
		super(args);
		base = args[1];
	}

	@Override
	public double getResult(double x) throws AlgebraException {
		double innerResult = getInner().getResult(x);
		double baseResult = base.getResult(x);
		try{
			return Math.pow(innerResult, 1.0/baseResult);
		}catch(Throwable t){
			throw new AlgebraException("Invalid mathematical operation");
		}
	}

	@Override
	public String getStringRepresentation() {
		return "root[" + base.getStringRepresentation() + "](" + getInner().getStringRepresentation() + ")";
	}

}

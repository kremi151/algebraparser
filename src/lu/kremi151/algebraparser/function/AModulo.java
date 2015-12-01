package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsLength=2,argsMinimum=2)
public class AModulo extends AFunction{

	AObject what;
	
	public AModulo(AObject[] args) {
		super(args);
		what = args[1];
	}

	@Override
	public double getResult(double x) throws AlgebraException {
		return getInner().getResult(x) % what.getResult(x);
	}

	@Override
	public String getStringRepresentation() {
		return "(" + getInner().getStringRepresentation() + " % " + what.getStringRepresentation() + ")";
	}

}

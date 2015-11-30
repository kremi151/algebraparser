package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsLength=2,argsMinimum=1)
public class ALogarithm extends AFunction
{

	public ALogarithm(AObject inner){
		super(inner);
	}

	@Override
	public double getResult(double x) throws AlgebraException
	{
		return Math.log(getInner().getResult(x));
	}

	@Override
	public String getStringRepresentation()
	{
		return "log(" + getInner().getStringRepresentation() + ")";
	}

}
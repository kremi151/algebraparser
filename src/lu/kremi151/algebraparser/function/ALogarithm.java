package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsLength=2,argsMinimum=1)
public class ALogarithm extends AFunction
{
	
	private AObject base = null;

	public ALogarithm(AObject[] args){
		super(args);
		
		if(args.length == 2){
			base = args[1];
		}
	}

	@Override
	public double getResult(double x) throws AlgebraException
	{
		if(base == null){
			return Math.log(getInner().getResult(x));
		}else{
			return Math.log(getInner().getResult(x)) / Math.log(base.getResult(x));
		}
	}

	@Override
	public String getStringRepresentation()
	{
		if(base == null){
			return "log[e](" + getInner().getStringRepresentation() + ")";
		}else{
			return "log[" + base.getStringRepresentation() + "](" + getInner().getStringRepresentation() + ")";
		}
	}

}
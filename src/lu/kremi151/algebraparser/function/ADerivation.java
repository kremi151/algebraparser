package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsMinimum=1, argsLength=2)
public class ADerivation extends AFunction{
	
	double pr = 0.0001;

	public ADerivation(AObject[] args) throws AlgebraException {
		super(args);
		if(args.length == 2){
			if(!args[1].isConstant()){
				throw new AlgebraException("The precision argument has to be constant");
			}
			pr = args[1].getResult(0);
		}
	}

	@Override
	public double getResult(double x) throws AlgebraException {
		x += 0.5 * pr;
		return dx(x, pr);
	}

	@Override
	public String getStringRepresentation() {
		return "dx(" + getInner().getStringRepresentation() + ")";
	}
	
	@Override
	public boolean isExact(){
		return false;
	}
	
	private double dx(double x, double a) throws AlgebraException{
		return (getInner().getResult(x) - getInner().getResult(x + a)) / (-a);
	}

}

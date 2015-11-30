package lu.kremi151.algebraparser.function;

import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

public class ACrossfoot extends AFunction{

	public ACrossfoot(AObject inner) {
		super(inner);
	}

	@Override
	public double getResult(double x) throws AlgebraException {
		double sum = 0.0;
		int res = (int)Math.floor(getInner().getResult(x));
		
		while(res > 0){
			int n = res % 10;
			res /= 10;
			sum += n;
		}
		return sum;
	}

	@Override
	public String getStringRepresentation() {
		return "cft(" + getInner().getStringRepresentation() + ")";
	}

}

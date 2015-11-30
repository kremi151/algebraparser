package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AComplexable;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.special.Complex;

public abstract class ABasicObject implements AObject, AComplexable{

	@Override
	public Complex getComplexResult(Complex c) throws AlgebraException {
		if(c.getImaginary() == 0){
			return getComplexResult(c.getReal());
		}else{
			return new Complex(Double.NaN, 0);
		}
	}

	@Override
	public Complex getComplexResult(double r) throws AlgebraException {
		return Complex.fromNumber(getResult(r));
	}

}

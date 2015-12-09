package lu.kremi151.algebraparser.interfaces;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.special.Complex;

public interface AComplexable {

	public Complex getComplexResult(Complex c) throws AlgebraException;
	
}

package lu.kremi151.algebraparser.interfaces;

import lu.kremi151.algebraparser.exception.AlgebraException;

public interface APolynomiable {

	public int getDegree() throws AlgebraException;
	public double getConstantTermAt(int power) throws AlgebraException;
	public boolean isPolynome() throws AlgebraException;
	
}

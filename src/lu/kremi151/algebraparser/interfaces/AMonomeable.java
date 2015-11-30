package lu.kremi151.algebraparser.interfaces;

import lu.kremi151.algebraparser.exception.AlgebraException;

public interface AMonomeable {

	public int getDegree() throws AlgebraException;
	public double getConstantTerm() throws AlgebraException;
	public boolean isMonome() throws AlgebraException;
	
}

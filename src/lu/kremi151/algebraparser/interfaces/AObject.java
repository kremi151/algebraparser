package lu.kremi151.algebraparser.interfaces;

import lu.kremi151.algebraparser.exception.AlgebraException;

public interface AObject{
	public double getResult(double x) throws AlgebraException;
	public String getStringRepresentation();
	public boolean isConstant();
}
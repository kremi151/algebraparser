package lu.kremi151.algebraparser.interfaces;

import lu.kremi151.algebraparser.exception.AlgebraException;


public interface AObjectSimplifiable extends AObject{
	public AObject simplify() throws AlgebraException;
}
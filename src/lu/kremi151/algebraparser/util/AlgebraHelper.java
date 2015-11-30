package lu.kremi151.algebraparser.util;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AMonomeable;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.interfaces.APolynomiable;

public class AlgebraHelper {

	public static boolean isNaturalNumber(double d){
		return (d - (int)d) == 0.0;
	}
	
	public static boolean isPolynome(AObject obj) throws AlgebraException{
		return obj instanceof APolynomiable && ((APolynomiable)obj).isPolynome();
	}
	
	public static boolean isMonome(AObject obj) throws AlgebraException{
		return obj instanceof AMonomeable && ((AMonomeable)obj).isMonome();
	}
	
	public static double calculate(double d1, double d2, AOperation op){
		switch(op){
		case PLUS:
			return d1+d2;
		case MINUS:
			return d1-d2;
		case MULTI:
			return d1*d2;
		case DIVIDE:
			return d1/d2;
		case POW:
			return Math.pow(d1,d2);
		}
		throw new NullPointerException();
	}
	
}

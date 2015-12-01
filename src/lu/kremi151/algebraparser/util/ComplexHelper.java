package lu.kremi151.algebraparser.util;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AComplexable;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.special.Complex;

public class ComplexHelper {
	
	private static final int ITERATION_MAXIMUM = 4;

	public static Complex calculateComplexOperation(Complex c1, Complex c2, AOperation op) throws AlgebraException{
		switch(op){
		case PLUS:
			return c1.add(c2);
		case MINUS:
			return c1.substract(c2);
		case MULTI:
			return c1.multiply(c2);
		case DIVIDE:
			return c1.divide(c2);
		case POW:
			if(c2.getImaginary() != 0.0){
				throw new AlgebraException("The power operator ( ^ ) can only be applied with a real number as power");
			}
			int i = (int)c2.getReal();
			if(c2.getReal() - (double)i == 0){
				return c1.power(i);
			}else{
				double d = c2.getReal();
				double dif = (d - (int)d);
				int it = 0;
				while(dif != 0.0 && it <= ITERATION_MAXIMUM){
					d *= 10;
					dif = (d - (int)d);
					it++;
				}
				int num = (int)d;
				Complex c3 = c1.power(num);
				return c3.root(Math.pow(10, it));
			}
		}
		return null;
	}
	
	public static Complex calculateComplexValue(AObject obj, Complex c) throws AlgebraException{
		if(obj instanceof AComplexable){
			return ((AComplexable)obj).getComplexResult(c);
		}else if(c.isRealNumber()){
			return Complex.fromNumber(obj.getResult(c.getReal()));
		}else{
			return Complex.fromNumber(Double.NaN);
		}
	}
}

package lu.kremi151.algebraparser.special;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AComplexable;
import lu.kremi151.algebraparser.interfaces.AObject;

public class Complex implements AObject, AComplexable
{
	
	public static boolean DEFAULT_STRING_REPRESENTATION_CIS = false;
	public static int DEFAULT_STRING_REPRESENTATION_PRECISION = 3;

	private double r;
	private double i;

	public Complex(double r, double i){
		this.r = r;
		this.i = i;
	}

	public double getReal(){
		return r;
	}

	public double getImaginary(){
		return i;
	}
	
	public boolean isRealNumber(){
		return i == 0.0;
	}

	public String toString(int precision, boolean cis){
		double pr = Math.pow(10.0, precision);
		if(!cis){
			double r = Math.round(this.r * pr) / pr;
			double i = Math.round(this.i * pr) / pr;
			if(r==0.0&&i==0.0)return "0.0";
			StringBuilder sb = new StringBuilder();
			if(r != 0.0){
				sb.append("" + r);
			}
			if(i > 0.0){
				if(r != 0.0){
					sb.append("+" + i + "i");
				}else{
					sb.append(i + "i");
				}
			}else if(i < 0.0){
				sb.append(i + "i");
			}
			return sb.toString();
		}else{
			double module = Math.round(getCisModule() * pr) / pr;
			double angle = Math.round(getCisAngle() * pr) / pr;
			return module + "*cis(" + angle + ")";
		}
	}

	public String toString(boolean cis){
		return toString(DEFAULT_STRING_REPRESENTATION_PRECISION, cis);
	}

	public String toString(int precision){
		return toString(precision, DEFAULT_STRING_REPRESENTATION_CIS);
	}

	@Override
	public String toString()
	{
		return toString(DEFAULT_STRING_REPRESENTATION_PRECISION, DEFAULT_STRING_REPRESENTATION_CIS);
	}

	public Complex add(Complex c){
		double r = this.r + c.r;
		double i = this.i + c.i;
		return new Complex(r,i);
	}

	public Complex substract(Complex c){
		double r = this.r - c.r;
		double i = this.i - c.i;
		return new Complex(r,i);
	}

	public Complex multiply(Complex c){
		double r = (this.r * c.r) - (this.i * c.i);
		double i = (this.r * c.i) + (this.i * c.r);
		return new Complex(r,i);
	}

	public Complex divide(Complex c){
		Complex t = new Complex(c.r, -c.i);
		double dn = (c.r * c.r) + (c.i * c.i);
		Complex nm = this.multiply(t);
		//Complex dn = c.multiply(t);
		return new Complex(nm.r / dn, nm.i / dn);
		//return new Complex(nm.r / dn.r, nm.i / dn.r);
	}

	public Complex power(int pow){
		if(pow > 0){
			Complex r = this;
			for(int i = pow ; i > 1 ; i--){
				r = r.multiply(this);
			}
			return r;
		}else if(pow < 0){
			Complex d = power(-pow);
			Complex r = new Complex(1,0).divide(d);
			return r;
		}else{
			return new Complex(1,0);
		}
	}

	public double getCisModule(){
		return Math.sqrt((r*r) + (i*i));
	}

	public double getCisAngle(){
		if(r != 0.0){
			return Math.atan(i/r);
		}else if(i > 0){
			return Math.PI * 0.5;
		}else{
			return Math.PI * 1.5;
		}
	}

	public Complex root(double root){
		double m = Math.pow(getCisModule(), 1.0/(double)root);
		double a = getCisAngle() / (double)root;
		double r = m * Math.cos(a);
		double i = m * Math.sin(a);
		return new Complex(r, i);
	}
	
	@Override
	public double getResult(double x)
	{
		if(getImaginary() != 0)return Double.NaN;
		return getReal();
	}

	@Override
	public String getStringRepresentation()
	{
		return "[" + toString() + "]";
	}

	@Override
	public boolean isConstant()
	{
		return false;
	}

	@Override
	public Complex getComplexResult(Complex c) throws AlgebraException {
		return this;
	}

	@Override
	public boolean isExact(){
		return true;
	}

	public static Complex fromCis(double module, double angle){
		return new Complex(module * Math.cos(angle), module * Math.sin(angle));
	}

	public static Complex fromNumber(Number number){
		return new Complex(number.doubleValue(), 0);
	}

}
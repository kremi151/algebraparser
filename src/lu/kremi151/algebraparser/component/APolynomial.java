package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.interfaces.AMonomeable;
import lu.kremi151.algebraparser.interfaces.APolynomiable;
import lu.kremi151.algebraparser.util.Debug;

public class APolynomial extends ABasicObject implements AMonomeable, APolynomiable{
	
	private double[] factors;
	private int highestDegree;
	
	public APolynomial(double[] factors){
		this.factors = factors;
		highestDegree = factors.length - 1;
		if(Debug.isInDebugMode()){
			boolean foundNonZero = false;
			for(int i = 0 ; i < factors.length ; i++){
				if(factors[i] != 0.0){
					foundNonZero = true;
					break;
				}
			}
			if(!foundNonZero){
				throw new RuntimeException("Something probably went wrong constructiong this polynomial");
			}
		}
	}

	@Override
	public double getResult(double x) {
		int length = factors.length;
		double r = 0.0;
		for(int i = 0 ; i < length ; i++){
			if(i == 0){
				r += factors[0];
			}else if(i == 1){
				r += factors[1] * x;
			}else{
				r += factors[i] * Math.pow(x, i);
			}
		}
		return r;
	}

	@Override
	public String getStringRepresentation() {
		int length = factors.length;
		StringBuilder sb = new StringBuilder();
		boolean cut = true;
		for(int i = length - 1 ; i >= 0 ; i--){
			double f = factors[i];
			if(f == 0.0)continue;
			if(!cut){
				sb.append(" ");
				if(f >= 0.0){
					sb.append("+");
				}
			}else{
				cut = false;
			}
			if(i == 0){
				sb.append(""+f);
			}else{
				sb.append(f + "x^" + i);
			}
		}
		return "("+sb.toString()+")";
	}

	@Override
	public boolean isConstant() {
		int length = factors.length;
		for(int i = 0 ; i < length ; i++){
			if(factors[i] != 0.0)return false;
		}
		return true;
	}

	@Override
	public int getDegree() {
		return highestDegree;
	}

	@Override
	public double getConstantTermAt(int power) {
		if(power < 0 || power >= factors.length){
			throw new RuntimeException("Constant term index out of bounds: " + power);
		}
		return factors[power];
	}

	@Override
	public boolean isPolynome() {
		return true;
	}

	@Override
	public double getConstantTerm() {
		if(isMonome()){
			for(int i = 0 ; i <= highestDegree ; i++){
				if(factors[i] != 0.0){
					return factors[i];
				}
			}
		}
		return 0;
	}

	@Override
	public boolean isMonome() {
		int nonNullConstants = 0;
		for(int i = 0 ; i <= highestDegree ; i++){
			if(factors[i] != 0.0){
				nonNullConstants++;
			}
		}
		return nonNullConstants <= 1;
	}

}

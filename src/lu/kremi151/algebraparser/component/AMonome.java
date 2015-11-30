package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AMonomeable;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.interfaces.AObjectSimplifiable;

public class AMonome extends ABasicObject implements AMonomeable, AObjectSimplifiable{
	
	double constant;
	int degree;
	
	public AMonome(double constant, int degree){
		this.constant = constant;
		this.degree = degree;
	}

	@Override
	public int getDegree() {
		return degree;
	}

	@Override
	public double getConstantTerm() {
		return constant;
	}

	@Override
	public boolean isMonome() {
		return true;
	}

	@Override
	public double getResult(double x) {
		return Math.pow(x, degree) * constant;
	}

	@Override
	public String getStringRepresentation() {
		return "(" + constant + "x^" + degree + ")";
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public AObject simplify() throws AlgebraException {
		if(constant == 1.0 && degree == 1){
			return new AVariable();
		}else if(constant == 0){
			return new AConstant(0);
		}else if(degree == 0){
			return new AConstant(constant);
		}
		return this;
	}

}

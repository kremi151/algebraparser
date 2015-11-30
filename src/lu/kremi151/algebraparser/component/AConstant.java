package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.interfaces.AMonomeable;

public class AConstant extends ABasicObject implements AMonomeable {

	double v;

	public AConstant(double v) {
		this.v = v;
	}

	@Override
	public double getResult(double x) {
		return v;
	}

	@Override
	public String getStringRepresentation() {
		return "" + v;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public int getDegree() {
		return 0;
	}

	@Override
	public double getConstantTerm() {
		return v;
	}

	@Override
	public boolean isMonome() {
		return true;
	}

}
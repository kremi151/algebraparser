package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AMonomeable;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.util.AlgebraHelper;

public class ANegative extends ABasicObject implements AMonomeable {

	AObject obj;

	public ANegative(AObject obj) {
		this.obj = obj;
	}

	@Override
	public double getResult(double x) throws AlgebraException {
		return -obj.getResult(x);
	}

	@Override
	public String getStringRepresentation() {
		return "-(" + obj.getStringRepresentation() + ")";
	}

	@Override
	public boolean isConstant() {
		return obj.isConstant();
	}

	@Override
	public int getDegree() throws AlgebraException {
		if(isMonome()){
			return ((AMonomeable)obj).getDegree();
		}
		return 0;
	}

	@Override
	public double getConstantTerm() throws AlgebraException {
		if(isMonome()){
			return (-1.0) * ((AMonomeable)obj).getConstantTerm();
		}
		return 0;
	}

	@Override
	public boolean isMonome() throws AlgebraException {
		return AlgebraHelper.isMonome(obj);
	}

}
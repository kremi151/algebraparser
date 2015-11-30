package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.interfaces.AMonomeable;
import lu.kremi151.algebraparser.interfaces.AObject;


public class AVariable implements AObject, AMonomeable
{

	@Override
	public double getResult(double x)
	{
		return x;
	}

	@Override
	public String getStringRepresentation()
	{
		return "x";
	}

	@Override
	public boolean isConstant()
	{
		return false;
	}

	@Override
	public int getDegree() {
		return 1;
	}

	@Override
	public double getConstantTerm() {
		return 1;
	}

	@Override
	public boolean isMonome() {
		return true;
	}
	
}
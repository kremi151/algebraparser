package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;

public class AAbsolute extends ABasicObject
{
	
	AObject base;
	
	public AAbsolute(AObject base){
		this.base = base;
	}

	@Override
	public double getResult(double x) throws AlgebraException
	{
		double tmp = base.getResult(x);
		return (tmp<0)?(-tmp):tmp;
	}

	@Override
	public String getStringRepresentation()
	{
		return "|" + base.getStringRepresentation() + "|";
	}

	@Override
	public boolean isConstant()
	{
		return base.isConstant();
	}
	
	@Override
	public boolean isExact(){
		return base.isExact();
	}
	
}
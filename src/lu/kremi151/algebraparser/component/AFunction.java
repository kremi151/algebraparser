package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsLength=1,argsMinimum=1)
public abstract class AFunction implements AObject{

	AObject inner;

	public AFunction(AObject inner){
		this.inner = inner;
	}

	public final AObject getInner(){
		return inner;
	}

	@Override
	public boolean isConstant()
	{
		return inner.isConstant();
	}

}
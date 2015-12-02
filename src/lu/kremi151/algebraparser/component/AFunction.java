package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.interfaces.AObject;

@FunctionMeta(argsLength=1,argsMinimum=1)
public abstract class AFunction extends ABasicObject{

	AObject[] args;

	public AFunction(AObject[] args){
		this.args = args;
	}

	public final AObject[] getArguments(){
		return args;
	}
	
	public AObject getInner(){
		return args[0];
	}

	@Override
	public boolean isConstant()
	{
		return getInner().isConstant();
	}

	@Override
	public boolean isExact(){
		for(int i = 0 ; i < args.length ; i++)if(!args[i].isExact())return false;
		return true;
	}

}
package lu.kremi151.algebraparser.util;

import java.util.Iterator;

public class StringIterator implements Iterator<String>
{
	
	private String[] obj;
	private int idx = -1;
	
	public StringIterator(String[] obj){
		this.obj = obj;
	}

	@Override
	public boolean hasNext()
	{
		return idx < obj.length - 1;
	}

	@Override
	public String next()
	{
		return obj[++idx];
	}
	
	public String preview(){
		return obj[idx+1];
	}

	@Override
	public void remove(){}
	
	public StringIterator clone(){
		StringIterator newi = new StringIterator(obj);
		newi.idx = this.idx;
		return newi;
	}
	
}
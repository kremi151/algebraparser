package lu.kremi151.algebraparser.util;

public enum AOperation{
	PLUS("+"),
	MINUS("-"),
	MULTI("*"),
	DIVIDE("/"),
	POW("^");
	
	private String symbol;
	
	AOperation(String symbol){this.symbol = symbol;}
	
	public String getSymbol(){return symbol;}
}
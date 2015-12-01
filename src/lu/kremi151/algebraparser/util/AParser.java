package lu.kremi151.algebraparser.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

import lu.kremi151.algebraparser.annotation.FunctionMeta;
import lu.kremi151.algebraparser.component.AAbsolute;
import lu.kremi151.algebraparser.component.AConstant;
import lu.kremi151.algebraparser.component.AFunction;
import lu.kremi151.algebraparser.component.ANegative;
import lu.kremi151.algebraparser.component.AStatement;
import lu.kremi151.algebraparser.component.AVariable;
import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.function.ACosine;
import lu.kremi151.algebraparser.function.ACrossfoot;
import lu.kremi151.algebraparser.function.AExponential;
import lu.kremi151.algebraparser.function.ALogarithm;
import lu.kremi151.algebraparser.function.AModulo;
import lu.kremi151.algebraparser.function.ARoot;
import lu.kremi151.algebraparser.function.ASignum;
import lu.kremi151.algebraparser.function.ASine;
import lu.kremi151.algebraparser.function.ASquareRoot;
import lu.kremi151.algebraparser.function.ATangent;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.interfaces.AObjectSimplifiable;
import lu.kremi151.algebraparser.special.Complex;

public class AParser{
	
	private HashMap<String, Class<? extends AFunction>> functions;
	private HashMap<String, Double> constants;
	
	private AParser(){
		functions = new HashMap<String, Class<? extends AFunction>>();
		constants = new HashMap<String, Double>();
	}
	
	public static AParser createCleanParser(){
		return new AParser();
	}
	
	public static AParser createBasicParser(){
		return new AParser()
				.registerFunction("sin", ASine.class)
				.registerFunction("cos", ACosine.class)
				.registerFunction("tan", ATangent.class)
				.registerFunction("log", ALogarithm.class)
				.registerFunction("exp", AExponential.class)
				.registerFunction("sign", ASignum.class)
				.registerFunction("sqrt", ASquareRoot.class)
				.registerFunction("cft", ACrossfoot.class)
				.registerFunction("root", ARoot.class)
				.registerFunction("mod", AModulo.class)
				.registerConstant("pi", Math.PI)
				.registerConstant("cdeg", Math.PI / 180.0)
				.registerConstant("e", Math.exp(1));
	}
	
	public static boolean isNumeric(String text){
		int l = text.length();
		for(int i = 0 ; i < l ; i++){
			char c = text.charAt(i);
			boolean d = Character.isDigit(c);
			boolean p = c == '.';
			boolean s = c == '-' || c == '+';
			boolean allowPoint = true;
			if(p){
				if(!allowPoint){
					return false;
				}else{
					allowPoint = false;
					continue;
				}
			}else if(s && i != 0){
				return false;
			}else if(!d &! s){
				return false;
			}
			
		}
		return true;
	}
	
	public static boolean isImaginary(String text){
		int l = text.length();
		boolean found = false;
		for(int i = 0 ; i < l ; i++){
			char c = text.charAt(i);
			if(c == 'i'){
				if(!found){
					found = true;
					continue;
				}
			}else if(!Character.isDigit(c)){
				return false;
			}
			if(found){
				return false;
			}
		}
		return found;
	}

	public static boolean isSimpleArgument(String text){
		StringIterator si = createSimpleIterator(text);
		while(si.hasNext()){
			String next = si.next();
			if(!next.equalsIgnoreCase("x") &! isNumeric(next) &! isImaginary(text)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean isComplexArgument(String text){
		StringIterator si = createSimpleIterator(text);
		while(si.hasNext()){
			String next = si.next();
			if(next.equalsIgnoreCase("x") || (! isNumeric(next) &! isImaginary(next))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 Creates a String iterator instance for a general expression including following symbols: +, -, /, *, (, ), |
	 */
	public static StringIterator createIterator(String statement){
		return new StringIterator(statement.split("(?=[+-/*()|^;])|(?<=[+-/*()|^;])"));
	}

	/**
	 Creates a String iterator instance for a simple expression including only numbers and variables called x
	 */
	public static StringIterator createSimpleIterator(String statement){
		return new StringIterator(statement.split("(?=[x])|(?<=[x])"));
	}
	
	/**
	 Creates a String iterator instance for a complex expression including only numbers and the imaginary number i
	 */
	public static StringIterator createComplexIterator(String statement){
		return new StringIterator(statement.split("(?=[i])|(?<=[i])"));
	}
	
	public AObject parseAlgebra(String text) throws AlgebraException{
		return parseAlgebra(text, false);
	}
	
	public AObject parseAlgebra(String text, boolean simplifyResult) throws AlgebraException{
		StringIterator si = createIterator(text);
		if(simplifyResult){
			return simplify(parseAlgebra(si, AParseMode.DEFAULT));
		}else{
			return parseAlgebra(si, AParseMode.DEFAULT);
		}
	}

	private AObject parseAlgebra(StringIterator si) throws AlgebraException{
		return parseAlgebra(si, AParseMode.DEFAULT);
	}

	private AObject parseAlgebra(StringIterator si, AParseMode mode) throws AlgebraException{
		AObject res = null;
		AOperation op = null;

		String obj = null;

		String arg = null;

		while(si.hasNext()){
			obj = formatArgument(si.next());
			if(obj.length() == 0)continue;
			if(obj.equalsIgnoreCase("+")){
				res = getIntermediateResult(res, op, arg);
				op = AOperation.PLUS;
				arg = null;
				Debug.println("+ res: " + res);
			}else if(obj.equalsIgnoreCase("-")){
				res = getIntermediateResult(res, op, arg);
				op = AOperation.MINUS;
				arg = null;
				Debug.println("- res: " + res);
			}else if(obj.equalsIgnoreCase("*")){
				res = getIntermediateResult(res, op, arg);
				op = AOperation.MULTI;
				arg = null;
				Debug.println("* res: " + res);
			}else if(obj.equalsIgnoreCase("/")){
				res = getIntermediateResult(res, op, arg);
				op = AOperation.DIVIDE;
				arg = null;
				Debug.println("/ res: " + res);
			}else if(obj.equalsIgnoreCase("^")){
				res = getIntermediateResult(res, op, arg);
				op = AOperation.POW;
				arg = null;
				Debug.println("^ res: " + res);
			}else if(obj.equalsIgnoreCase("(")){
				AObject tmp1 = parseAlgebra(si);
				if(res == null){
					res = tmp1;
				}else{
					AObject tmp = res;
					res = new AStatement(tmp, tmp1, op);
					tmp = null;
				}
				tmp1 = null;
			}else if(obj.equalsIgnoreCase(")")){
				break;
			}else if(obj.equalsIgnoreCase("|")){
				if(mode != AParseMode.ABS){
					AObject tmp1 = parseAlgebra(si, AParseMode.ABS);
					if(res == null){
						res = tmp1;
					}else{
						AObject tmp = res;
						res = new AStatement(tmp, tmp1, op);
						tmp = null;
					}
					tmp1 = null;
				}else{
					break;
				}
			}else if(obj.equalsIgnoreCase(";")){
				if(mode == AParseMode.FUNCTION)break;
				throw new AlgebraException("Using argument delimiter \";\" is completeley non-sense in a non-function context");
			}else if(isSimpleArgument(obj)){
				arg = obj;
				Debug.println("setting arg to " + arg);
			}else if(isComplexArgument(obj)){
				arg = obj;
				Debug.println("setting complex arg to " + arg);
			}else{
				try{
					if(hasFunction(obj)){
						si.next();
						AObject[] args = parseFunctionArguments(si);
						AFunction tmp = createFunctionObject(obj, args);
						res = getIntermediateResult(res, op, tmp);
						tmp = null;
					}else if(hasConstant(obj)){
						res = getIntermediateResult(res, op, new AConstant(getConstant(obj)));
					}else{
						throw(new AlgebraException("This parser has no registred function or constant called \"" + obj.toLowerCase() + "\""));
					}
				}catch(Throwable t){
					t.printStackTrace();
				}
			}
			
		}

		if(arg != null){
			res = getIntermediateResult(res, op, arg);
		}

		switch(mode){
			case ABS:
				return new AAbsolute(res);
			default:
				return res;
		}
	}
	
	private AObject[] parseFunctionArguments(StringIterator si) throws AlgebraException{
		StringIterator tmp = si.clone();
		int argCount = 0;
		while(true){
			String n = tmp.next();
			if(n.equalsIgnoreCase(";")){
				argCount++;
			}else if(n.equalsIgnoreCase(")")){
				break;
			}else if(argCount == 0){
				argCount = 1;
			}
		}
		tmp = null;
		if(argCount == 0)throw new AlgebraException("A function must contain at least one argument");
		AObject[] args = new AObject[argCount];
		for(int i = 0 ; i < argCount ; i++){
			args[i] = parseAlgebra(si, AParseMode.FUNCTION);
		}
		return args;
	}
	
	private AObject getIntermediateResult(AObject currentResult, AOperation currentOperation, AObject newArg) throws AlgebraException{
		Debug.println("psa: " + currentResult + " " + currentOperation + " " +newArg);
		if(currentResult == null){
			if(newArg != null){
				if(currentOperation == null || currentOperation == AOperation.PLUS){
					currentResult = newArg;
				}else if(currentOperation == AOperation.MINUS){
					currentResult = new ANegative(newArg);
				}else{
					throw(new AlgebraException("Missing Argument: ? " + currentOperation.getSymbol() + " " + newArg.getStringRepresentation()));
				}
			}else{
				//TODO
			}
		}else{
			if(newArg != null){
				currentResult = new AStatement(currentResult, newArg, currentOperation);
			}else{
				//nope
			}
		}
		Debug.println("gir: " + currentResult);
		return currentResult;
	}

	private AObject getIntermediateResult(AObject currentResult, AOperation currentOperation, String previousArgument) throws AlgebraException{
		if(previousArgument != null && previousArgument.length() > 0){
			return getIntermediateResult(currentResult, currentOperation, parseSimpleArgument(previousArgument));
		}else{
			return currentResult;
		}
	}

	private String formatArgument(String argument){
		if(argument != null)return argument.replace(" ", "");
		return null;
	}

	public AObject parseSimpleArgument(String phrase) throws AlgebraException{
		Debug.println("parse simple argument \"" + phrase + "\"");
		AObject res = null;
		StringIterator si = createSimpleIterator(phrase);
		String arg = null;
		boolean imaginary = phrase.endsWith("i");
		while(si.hasNext()){
			String obj = si.next();
			if(!imaginary){
				if(obj.equalsIgnoreCase("x")){
					if(res == null){
						if(arg != null && arg.length() > 0){
							res = new AStatement(new AConstant(Double.parseDouble(arg)), new AVariable(), AOperation.MULTI);
						}else{
							res = new AVariable();
						}
					}else{
						/**AObject tmp = res;
						 res = new AStatement(tmp, new AVariable(), AOperation.MULTI);
						 tmp = null;**/
						if(arg != null && arg.length() > 0){
							AObject tmp = new AStatement(new AConstant(Double.parseDouble(arg)), new AVariable(), AOperation.MULTI);
							res = new AStatement(res, tmp, AOperation.MULTI);
							tmp = null;
						}else{
							res = new AStatement(res, new AVariable(), AOperation.MULTI);
						}
					}
					arg = null;
				}else if(obj != null && obj.length() > 0){
					arg = obj;
				}
			}else{
				if(obj.equalsIgnoreCase("x")){
					throw new AlgebraException("Imaginary numbers cannot be used in combination with variables");
				}else if(obj != null && obj.length() > 0){
					arg = obj;
				}
			}
			
		}

		if(arg != null &! imaginary){
			if(res == null){
				res = new AConstant(Double.parseDouble(arg));
			}else{
				AObject tmp = res;
				res = new AStatement(tmp, new AConstant(Double.parseDouble(arg)), AOperation.MULTI);
				tmp = null;
			}
		}else if(arg != null && imaginary){
			if(res == null){
				int argl = arg.length();
				if(argl > 1){
					res = new Complex(0, Double.parseDouble(arg.substring(0, arg.length()-1)));
				}else{
					res = new Complex(0, 1.0);
				}
			}else{
				throw new AlgebraException("Complex numbers have to be given in the following format: a+bi\nThe + operator can be replaced by any other operator");
			}
		}

		return res;
	}
	
	public AObject parseComplexArgument(String phrase){
		double r = 0;
		double i = 0;
		Iterator<String> it = createComplexIterator(phrase);
		while(it.hasNext()){
			String s = it.next();
			if(s.equalsIgnoreCase("i")){
				i = r;
				r = 0;
			}else{
				r = Double.parseDouble(s);
			}
		}
		if(i == 0){
			return new AConstant(r);
		}else{
			return new Complex(r, i);
		}
	}
	
	public static AObject simplify(AObject obj) throws AlgebraException{
		return simplify(obj, 8);
	}
	
	public static AObject simplify(AObject obj, int maxIterations) throws AlgebraException{
		int iterations = 0;
		
		boolean updated = false;
		
		do{
			updated = false;
			iterations++;
			
			if(obj instanceof AObjectSimplifiable){
				AObject res = ((AObjectSimplifiable)obj).simplify();
				if(res != obj){
					obj = res;
					updated = true;
				}
			}
			
		}while(updated && iterations <= maxIterations);
		
		Debug.println("Simplified with " + iterations + " iterations");
		
		return obj;
	}
	
	public AParser registerFunction(String name, Class<? extends AFunction> class_){
		String cname = name.toLowerCase();
		if(functions.containsKey(cname)){
			throw(new RuntimeException("This parser already contains a function called \"" + cname + "\""));
		}else if(hasConstant(name)){
			throw(new RuntimeException("This parser already contains a constant called \"" + cname + "\""));
		}else{
			functions.put(cname, class_);
		}
		return this;
	}
	
	public AParser registerConstant(String name, double value){
		String cname = name.toUpperCase();
		if(constants.containsKey(cname)){
			throw(new RuntimeException("This parser already contains a constant called \"" + cname + "\""));
		}else if(hasFunction(name)){
			throw(new RuntimeException("This parser already contains a function called \"" + cname + "\""));
		}else{
			constants.put(cname, value);
		}
		return this;
	}
	
	public boolean hasFunction(String name){
		String cname = name.toLowerCase();
		return functions.containsKey(cname);
	}
	
	public boolean hasConstant(String name){
		String cname = name.toUpperCase();
		return constants.containsKey(cname);
	}
	
	private AFunction createFunctionObject(String name, AObject[] args) throws NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IllegalAccessException, InstantiationException, AlgebraException{
		Class<? extends AFunction> cl = functions.get(name.toLowerCase());
		
		FunctionMeta fm = cl.getAnnotation(FunctionMeta.class);
		if(fm != null){
			if(args.length < fm.argsMinimum() || args.length > fm.argsLength()){
				throw new AlgebraException("The function \"" + name + "\" works only with " + fm.argsMinimum() + " - " + fm.argsLength() + " arguments");
			}
		}
		
		AFunction f = cl.getConstructor(AObject[].class).newInstance(new Object[]{args});
		return f;
	}
	
	private double getConstant(String name){
		return constants.get(name.toUpperCase());
	}
	
}
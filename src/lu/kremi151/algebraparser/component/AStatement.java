package lu.kremi151.algebraparser.component;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AMonomeable;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.interfaces.AObjectSimplifiable;
import lu.kremi151.algebraparser.interfaces.APolynomiable;
import lu.kremi151.algebraparser.special.Complex;
import lu.kremi151.algebraparser.util.AOperation;
import lu.kremi151.algebraparser.util.AParser;
import lu.kremi151.algebraparser.util.AlgebraHelper;
import lu.kremi151.algebraparser.util.ComplexHelper;
import lu.kremi151.algebraparser.util.Debug;


public class AStatement implements AObjectSimplifiable, AMonomeable, APolynomiable
{

	AObject stA;
	AObject stB;
	AOperation op;
	
	public AStatement(AObject stA, AObject stB, AOperation op){
		this.stA = stA;
		this.stB = stB;
		this.op = op;
	}
	
	public double getResult(double x) throws AlgebraException{
		if(op==null){
			Debug.println("op is null -> " + stA.getResult(x) + " ? " + stB.getResult(x));
		}
		switch(op){
			case PLUS:
				return stA.getResult(x) + stB.getResult(x);
			case MINUS:
				return stA.getResult(x) - stB.getResult(x);
			case MULTI:
				return stA.getResult(x) * stB.getResult(x);
			case DIVIDE:
				return stA.getResult(x) / stB.getResult(x);
			case POW:
				return Math.pow(stA.getResult(x), stB.getResult(x));
		}
		return -1;
	}
	
	@Override
	public AObject simplify() throws AlgebraException{
		AObject o1 = AParser.simplify(stA);
		AObject o2 = AParser.simplify(stB);
		
		boolean rootChanged = (o1 == stA) && (o2 == stB);
		
		if(isConstant()){
			Debug.println("Statement + " + getStringRepresentation() + " is constant");
			return new AConstant(getResult(0));
		}else if((o1.isConstant() && o2 instanceof AMonomeable || o2.isConstant() && o1 instanceof AMonomeable) && op == AOperation.MULTI){
			Debug.println("Statement + " + getStringRepresentation() + " has a monome and a constant");
			AMonomeable as = (o2 instanceof AMonomeable)?(AMonomeable)o2:(AMonomeable)o1;
			if(as.isMonome()){
				double ct = (o1.isConstant())?o1.getResult(0):o2.getResult(0);
				return new AMonome(ct * as.getConstantTerm(), as.getDegree());
			}
		}else if(o2.isConstant() && o1 instanceof AMonomeable && op == AOperation.POW){
			Debug.println("Statement + " + getStringRepresentation() + " has a monome and a constant (power operation)");
			AMonomeable as = (AMonomeable)o1;
			if(as.isMonome()){
				double pw = o2.getResult(0);
				if(AlgebraHelper.isNaturalNumber(pw)){
					return new AMonome(Math.pow(as.getConstantTerm(), pw), as.getDegree() * (int)pw);
				}
			}
		}else if(AlgebraHelper.isMonome(o1) && AlgebraHelper.isMonome(o2)){
			Debug.println("Statement + " + getStringRepresentation() + " has two monomes");
			AMonomeable as1 = (AMonomeable)o1;
			AMonomeable as2 = (AMonomeable)o2;
			if(op == AOperation.PLUS || op == AOperation.MINUS){
				if(as1.getDegree() == as2.getDegree()){
					if(op == AOperation.PLUS){
						return new AMonome(as1.getConstantTerm() + as2.getConstantTerm(), as1.getDegree());
					}else{
						return new AMonome(as1.getConstantTerm() - as2.getConstantTerm(), as1.getDegree());
					}
				}else{
					boolean negation = op == AOperation.MINUS;
					int highestDegree = Math.max(as1.getDegree(), as2.getDegree());
					double[] factors = new double[highestDegree + 1];
					for(int i = 0 ; i < factors.length ; i++){
						if(i == as1.getDegree()){
							factors[i] = as1.getConstantTerm();
						}else if(i == as2.getDegree()){
							factors[i] = as2.getConstantTerm() * (negation?(-1):1);
						}else{
							factors[i] = 0.0;
						}
					}
					return new APolynomial(factors);
				}
			}else{
				switch(op){
				case MULTI:
					return new AMonome(as1.getConstantTerm() * as2.getConstantTerm(), as1.getDegree() + as2.getDegree());
				case DIVIDE:
					return new AMonome(as1.getConstantTerm() / as2.getConstantTerm(), as1.getDegree() - as2.getDegree());
				default:
					break;
				}
			}
		}else if(AlgebraHelper.isPolynome(o1) && AlgebraHelper.isPolynome(o2)){
			Debug.println("Statement + " + getStringRepresentation() + " has two polynomes");
			if(op == AOperation.PLUS || op == AOperation.MINUS){//TODO: Multi & Divide
				APolynomiable ap1 = (APolynomiable) o1;
				APolynomiable ap2 = (APolynomiable) o2;
				int highestDegree = Math.max(ap1.getDegree(), ap2.getDegree());
				double[] factors = new double[highestDegree + 1];
				for(int i = 0 ; i < factors.length ; i++){
					double f1 = ap1.getConstantTermAt(i);
					double f2 = ap2.getConstantTermAt(i);
					factors[i] = AlgebraHelper.calculate(f1, f2, op);
				}
				return new APolynomial(factors); 
			}
		}else if((op != AOperation.POW && op != AOperation.DIVIDE) && ((AlgebraHelper.isPolynome(o1) && AlgebraHelper.isMonome(o2)) || (AlgebraHelper.isPolynome(o2) && AlgebraHelper.isMonome(o1)))){
			Debug.println("Statement + " + getStringRepresentation() + " has one polynome and one monome \\{^;/}");
			AMonomeable mon = AlgebraHelper.isMonome(o1)?(AMonomeable)o1:(AMonomeable)o2;
			APolynomiable poly = AlgebraHelper.isPolynome(o1)?(APolynomiable)o1:(APolynomiable)o2;
			
			int polyHD = poly.getDegree();
			int monHD = mon.getDegree();
			int highestDegree = Math.max(monHD, polyHD);
			
			if(op == AOperation.MULTI)highestDegree = highestDegree + mon.getDegree();
			
			double[] factors = new double[highestDegree + 1];
			
			for(int i = 0 ; i <= polyHD ; i++){
				factors[i] = poly.getConstantTermAt(i);
			}
			
			if(op == AOperation.PLUS || op == AOperation.MINUS){
				int i = mon.getDegree();
				double newFact = AlgebraHelper.calculate(factors[i], mon.getConstantTerm(), op);
				factors[i] = newFact;
			}else if(op == AOperation.MULTI){
				double[] oldfactors = factors;
				factors = new double[oldfactors.length];
				for(int i = 0 ; i <= polyHD ; i++){
					int newDeg = i + mon.getDegree();
					double newFact = oldfactors[i] * mon.getConstantTerm();
					factors[newDeg] = newFact;
				}
			}
			return new APolynomial(factors); 
		}else if(o1.isConstant() && o2 instanceof Complex){
			Debug.println("Statement + " + getStringRepresentation() + " has one constant and one complex number");
			Complex c = (Complex)o2;
			return ComplexHelper.calculateComplexOperation(c, Complex.fromNumber(o1.getResult(0)), op);
		}else if(o2.isConstant() && o1 instanceof Complex){
			Debug.println("Statement + " + getStringRepresentation() + " has one constant and one complex number");
			Complex c = (Complex)o1;
			return ComplexHelper.calculateComplexOperation(c, Complex.fromNumber(o2.getResult(0)), op);
		}else if(o1 instanceof Complex && o2 instanceof Complex){
			Debug.println("Statement + " + getStringRepresentation() + " has two complex numbers");
			Complex c1 = (Complex)o1;
			Complex c2 = (Complex)o2;
			return ComplexHelper.calculateComplexOperation(c1, c2, op);
		}
		
		if(rootChanged){
			return new AStatement(o1, o2, op);
		}else{
			return this;
		}
	}
	
	@Override
	public boolean isConstant()
	{
		return stA.isConstant() && stB.isConstant();
	}

	@Override
	public double getConstantTerm() throws AlgebraException{
		if(AlgebraHelper.isMonome(stA) && AlgebraHelper.isMonome(stB)){
			AMonomeable m1 = (AMonomeable) stA;
			AMonomeable m2 = (AMonomeable) stB;
			if(m1.getDegree() == m2.getDegree())return AlgebraHelper.calculate(m1.getConstantTerm(), m2.getConstantTerm(), op);
		}
		return (stA.isConstant()&&stB.isConstant())?(getResult(0)):((stA.isConstant()?stA.getResult(0):(stB.isConstant()?stB.getResult(0):0)));
	}

	@Override
	public boolean isMonome() throws AlgebraException{
		if(op != AOperation.MULTI && op != AOperation.DIVIDE && op != AOperation.POW)return false;
		if(stA.isConstant() && stB.isConstant()){
			return true;
		}else if(stA.isConstant()){
			if(stB.getClass() == AVariable.class){
				return true;
			}else if(stB.getClass() == AStatement.class){
				AStatement s = (AStatement)stB;
				if(s.stA.getClass() == AVariable.class && s.op == AOperation.POW && s.stB.isConstant()){
					return AlgebraHelper.isNaturalNumber(s.stB.getResult(0));
				}else{
					return false;
				}
			}
		}else if(stB.isConstant()){
			if(stA.getClass() == AVariable.class){
				return true;
			}else if(stA.getClass() == AStatement.class){
				AStatement s = (AStatement)stA;
				if(s.stA.getClass() == AVariable.class && s.op == AOperation.POW && s.stB.isConstant()){
					return AlgebraHelper.isNaturalNumber(s.stB.getResult(0));
				}else{
					return false;
				}
			}
		}else if(AlgebraHelper.isMonome(stA) && AlgebraHelper.isMonome(stB)){
			AMonomeable m1 = (AMonomeable) stA;
			AMonomeable m2 = (AMonomeable) stB;
			if(m1.getDegree() == m2.getDegree())return true;
		}
		return false;
	}
	
	@Override
	public String getStringRepresentation()
	{
		return "(" + stA.getStringRepresentation() + op.getSymbol() + stB.getStringRepresentation() + ")";
	}

	@Override
	public String toString()
	{
		return getStringRepresentation();
	}

	@Override
	public int getDegree() throws AlgebraException {
		if(isPolynome()){
			try {
				APolynomiable p = (APolynomiable) AParser.simplify(this);
				if(p != this)return p.getDegree();
			} catch (AlgebraException e) {
				e.printStackTrace();
			}
		}else if(stA.isConstant() && stB.isConstant()){
			return 0;
		}else if(stA.isConstant()){
			if(stB.getClass() == AVariable.class){
				return 1;
			}else if(stB.getClass() == AStatement.class){
				AStatement s = (AStatement)stB;
				if(s.stA.getClass() == AVariable.class && s.op == AOperation.POW && s.stB.isConstant()){
					double d = s.stB.getResult(0);
					if(AlgebraHelper.isNaturalNumber(s.stB.getResult(0))){
						return (int)d;
					}
				}
			}
		}else if(stB.isConstant()){
			if(stA.getClass() == AVariable.class){
				return 1;
			}else if(stA.getClass() == AStatement.class){
				AStatement s = (AStatement)stA;
				if(s.stA.getClass() == AVariable.class && s.op == AOperation.POW && s.stB.isConstant()){
					double d = s.stB.getResult(0);
					if(AlgebraHelper.isNaturalNumber(s.stB.getResult(0))){
						return (int)d;
					}
				}
			}
		}else if(AlgebraHelper.isMonome(stA) && AlgebraHelper.isMonome(stB)){
			AMonomeable m1 = (AMonomeable) stA;
			AMonomeable m2 = (AMonomeable) stB;
			if(m1.getDegree() == m2.getDegree()){
				if(op == AOperation.PLUS || op == AOperation.MINUS){
					return m1.getDegree();
				}else if(op == AOperation.MULTI){
					return m1.getDegree() + m2.getDegree();
				}else if(op == AOperation.DIVIDE){
					return m1.getDegree() - m2.getDegree();
				}
			}
		}
		return 0;
	}

	@Override
	public double getConstantTermAt(int power) {
		try{
			if(AlgebraHelper.isPolynome(stA) && AlgebraHelper.isPolynome(stB)){
				AObject s = AParser.simplify(this);
				if(AlgebraHelper.isPolynome(s)){
					APolynomiable p = (APolynomiable) s;
					return p.getConstantTermAt(power);
				}
			}
		}catch(Throwable t){
			t.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean isPolynome() throws AlgebraException {
		return (AlgebraHelper.isPolynome(stA) && AlgebraHelper.isPolynome(stB)) || (AlgebraHelper.isPolynome(stA) && AlgebraHelper.isMonome(stB)) || (AlgebraHelper.isPolynome(stB) && AlgebraHelper.isMonome(stA)) || (AlgebraHelper.isMonome(stA) && AlgebraHelper.isMonome(stB));
	}
	
}
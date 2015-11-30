package lu.kremi151.algebraparser;

import java.util.Scanner;

import lu.kremi151.algebraparser.exception.AlgebraException;
import lu.kremi151.algebraparser.interfaces.AObject;
import lu.kremi151.algebraparser.util.AParser;

public class Main
{
	private static final boolean debug = false;
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		boolean running = true;
		
		String inline;
		AObject st;
		
		while(running){
			try{
				System.out.print("Enter a statement: ");

				inline = input.nextLine();
				
				AParser parser = AParser.createBasicParser();
				
				st = parser.parseAlgebra(inline, false);
				System.out.println("parsed: " + st.getStringRepresentation());
				st = AParser.simplify(st);
				System.out.println("simplified: " + st.getStringRepresentation());

				while(true){
					System.out.println("Enter a variable:");
					System.out.println("(\"exit\" to leave ; \"nf\" for new statement ; \"print\" to print current expression)");
					String line = input.nextLine();

					if(line.equalsIgnoreCase("exit")){
						running = false;
						break;
					}else if(line.equalsIgnoreCase("nf")){
						break;
					}else if(line.equalsIgnoreCase("print")){
						System.out.println(st.getStringRepresentation());
					}else if(!AParser.isNumeric(line)){
						System.out.println("Did you mean something different?");
					}else{
						double x = Double.parseDouble(line);
						System.out.println(st.getResult(x));
					}
				}
			}catch(AlgebraException e){
				e.printStackTrace();
			}
		}

		input.close();
	}
	
	public static void debug(String line){
		if(debug)System.out.println("(debug) " + line);
	}
	
}
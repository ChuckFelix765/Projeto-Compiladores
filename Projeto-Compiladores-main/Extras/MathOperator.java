//MathOperator.java
import java.text.CharacterIterator;

public class MathOperator extends AFD {
	
	@Override
	public Token evaluate (CharacterIterator code){
		switch (code.current()) {
			case '+':
				code.next();
				return new Token("PLUS","+");
			case '-':
				code.next();
				return new Token("MINUS","-");
			case '*':
				code.next();
				return new Token("MULT","*");
			case '/':
				code.next();
				return new Token("DIVI","/");
			case '%':
				code.next();
				return new Token("REST","%");
			case '(':
				code.next();
				return new Token("APAR","(");
			case ')':
				code.next();
				return new Token("FPAR",")");

		default:
			return null;
		}
	}
}
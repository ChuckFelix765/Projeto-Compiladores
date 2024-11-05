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
			case '{':
				code.next();
				return new Token("ACHA","{");
			case '}':
				code.next();
				return new Token("FCHA","}");
			case '>':
				code.next();
				return new Token("MAIOQ", ">");
			case '<':
				code.next();
				return new Token("MENQ", "<");
			case '=':
				code.next();
				if(code.current() == '='){
					code.next();
					return new Token("DIGL", "==");
				} else{
					return new Token("IGUL","=");
				}
			case ':':
				code.next();
				return new Token("DOSP",":");
			case ',':
				code.next();
				return new Token("VIRG",",");
			case ';':
				code.next();
				return new Token("PVIR",";");

		default:
			return null;
		}
	}
}
//Number.java
import java.text.CharacterIterator;

public class Number extends AFD {
	boolean ponto = false;
	@Override
	public Token evaluate(CharacterIterator code) {
		if(Character.isDigit(code.current())) {
			String number = readNumber(code);
			if (endNumber(code) && ponto == false) {
				return new Token("INT", number);
			}else if(endNumber(code) && ponto == true){
				ponto = false;
				return new Token("FLT", number);
			}
		}
		return null;
	}
	private String readNumber(CharacterIterator code) {
		String number="";
		while (Character.isDigit(code.current())){
			number += code.current();
			code.next();
		}
		if(code.current()=='.'){
			ponto = true;
			number += ".";
			code.next();
			while (Character.isDigit(code.current())){
				number += code.current();
				code.next();
			}
		}
		return number;
	}

	private boolean endNumber(CharacterIterator code){
		return code.current() == ' ' ||
		code.current() == '+' ||
		code.current() == '-' ||
		code.current() == '*' ||
		code.current() == '/' ||
		code.current() == '%' ||
		code.current() == '\n' ||
		code.current() == '(' ||
		code.current() == ')' ||
		code.current() == CharacterIterator.DONE;
	}
}
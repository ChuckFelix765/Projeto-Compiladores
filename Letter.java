//Letter.java
import java.text.CharacterIterator;

public class Letter extends AFD {
	@Override
	public Token evaluate(CharacterIterator code) {
		if(Character.isLetter(code.current())) {
			String letter = readLetter(code);

			if (endLetter(code)) {
				return new Token("VAR", letter);
			}
		}
		return null;
	}
	private String readLetter(CharacterIterator code) {
		String letter="";
		while (Character.isLetter(code.current())){
			letter += code.current();
			code.next();
		}
		return letter;
	}

	private boolean endLetter(CharacterIterator code){
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
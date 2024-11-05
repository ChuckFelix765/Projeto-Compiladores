//Letter.java
import java.text.CharacterIterator;
import java.util.ArrayList;


public class Letter extends AFD {

	@Override
	public Token evaluate(CharacterIterator code) {
		//palavras reservadas
		ArrayList<String> reservadas = new ArrayList<>();
		reservadas.add("if");
		reservadas.add("else");
		reservadas.add("elif");
		reservadas.add("for");
		reservadas.add("while");
		reservadas.add("int");
		reservadas.add("float");
		reservadas.add("String");
		reservadas.add("char");
		reservadas.add("return");
		reservadas.add("break");
		reservadas.add("continue");
		reservadas.add("boolean");
		reservadas.add("print");

		if(code.current() == ('\"')){
			String palavra = "";
			code.next();
			while(code.current() != '\"'){
				palavra += code.current();
				code.next();
			}
			code.next();
			return new Token("STRG", palavra);
		}else if(code.current() == ('\'')){
			String palavra = "";
			code.next();
			while(code.current() != '\''){
				palavra += code.current();
				code.next();
			}
			code.next();
			return new Token("CHAR", palavra);
		}

		if(Character.isLetter(code.current())) {
			String letter = readLetter(code);

			if (reservadas.contains(letter)){
				return new Token("RES", letter);
			}

			else if (endLetter(code)) {
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

		/*if(code.current()=='\''){
			cha = true;
			letter += "\'";
			code.next();

			while(Character.isLetter(code.current())){
				letter += code.current();
				code.next();
			}
		}else if(code.current()=='\"'){
			str = true;
			letter += "\"";
			code.next();

			while(Character.isLetter(code.current())){
				letter += code.current();
				code.next();
			}
		}*/
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
		code.current() == '>' ||
		code.current() == '<' ||
		code.current() == ':' ||
		code.current() == ',' ||
		code.current() == CharacterIterator.DONE;
	}
}

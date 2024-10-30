//Number.java
import java.text.CharacterIterator;

public class Number extends AFD {
	//Váriavel para verificar se possui algum ponto para se tornar do tipo float.
	boolean ponto = false;
	@Override

	//Verifica se o número é do tipo int ou float chamando a classe Token.
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

	//Verifica os digitos do número.
	private String readNumber(CharacterIterator code) {
		String number="";
		while (Character.isDigit(code.current())){
			number += code.current();
			code.next();
		}

		//Para caso haja algum ponto, ele insere o ponto no número e continua a ler os digitos do número.
		if(code.current()=='.'){
			ponto = true;
			number += ".";
			code.next();

			//Verifica os números do digito
			while (Character.isDigit(code.current())){
				number += code.current();
				code.next();
			}
		}
		return number;
	}

	//Para caso finalize com algum destes caracteres, ele aceita como uma finalização também.
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

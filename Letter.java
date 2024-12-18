//Letter.java
import java.text.CharacterIterator;
import java.util.ArrayList;


public class Letter extends AFD {

	@Override
	public Token evaluate(CharacterIterator code) {
		//palavras reservadas
		ArrayList<String> reservadas = new ArrayList<>();
		reservadas.add("si"); //if
		reservadas.add("sino"); //else
		reservadas.add("enton"); //elif
		reservadas.add("para"); //for
		reservadas.add("mientras"); //whiles
		reservadas.add("entero"); //int
		reservadas.add("flotante"); //float
		reservadas.add("palabra"); // String
		reservadas.add("letra"); //char
		reservadas.add("retorna"); //return
		reservadas.add("rompe"); //break
		reservadas.add("continua"); //continue
		reservadas.add("mugrilo"); //boolean
		reservadas.add("verdad"); //true
		reservadas.add("paraguai"); //false
		reservadas.add("escriba"); //input
		reservadas.add("muestrame"); //print
		reservadas.add("importe"); //include
		reservadas.add("funcion"); //function
		reservadas.add("cfuncion"); // chama funcao


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
		}else if(code.current()=='#'){
			String comentario = "";
			code.next();
			while(code.current()!='#'){
				comentario += code.current();
				code.next();
			}
			code.next();
			return new Token("COMENT", comentario);
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
		code.current() == ';' ||
		code.current() == CharacterIterator.DONE;
	}
}

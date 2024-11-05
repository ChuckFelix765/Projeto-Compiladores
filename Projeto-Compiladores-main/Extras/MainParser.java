
//Main.java
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainParser{
	public static void main(String[] args) throws IOException {
		List<TokenP> tokens = new ArrayList<>();
        tokens.add(new TokenP("reservada_if","if"));
        tokens.add(new TokenP("id","soma"));
        tokens.add(new TokenP("operador_condicional",">"));
        tokens.add(new TokenP("num","5"));
        tokens.add(new TokenP("reservada_then","then"));
        tokens.add(new TokenP("id","soma"));
        tokens.add(new TokenP("operador_atribuicao","="));
        tokens.add(new TokenP("num","3"));
        tokens.add(new TokenP("reservada_else","else"));
        tokens.add(new TokenP("id","soma"));
        tokens.add(new TokenP("operador_atribuicao","="));
        tokens.add(new TokenP("num","2"));
        tokens.add(new TokenP("EOF","$"));
        Parser parser = new Parser(tokens);
        parser.main();
        //List<Token> tokens = null;
		/*String data = "4.0 + 2";
		Lexer lexer = new Lexer(data);
		tokens = lexer.getTokens();
		for(Token token : tokens) {
			System.out.println(token);
		}*/
	}
}
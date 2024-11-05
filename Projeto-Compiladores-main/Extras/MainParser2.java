
//Main.java
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainParser2{
	public static void main(String[] args) throws IOException {
		List<TokenP> tokens = new ArrayList<>();
        tokens.add(new TokenP("reservada_while","while"));
        tokens.add(new TokenP("id","x"));
        tokens.add(new TokenP("operador_condicional",">"));
        tokens.add(new TokenP("num","5"));
        tokens.add(new TokenP("reservada_dp",":"));
        tokens.add(new TokenP("id","x"));
        tokens.add(new TokenP("operador_atribuicao","="));
        tokens.add(new TokenP("num","1"));
        tokens.add(new TokenP("EOF","$"));
        Parser2 parser = new Parser2(tokens);
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
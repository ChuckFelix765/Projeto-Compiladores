//Main.java
import java.io.IOException;
import java.util.List;

public class Main{
	public static void main(String[] args) throws IOException {
		List<Token> tokens = null;
		String data = "4.0 + 2";
		Lexer lexer = new Lexer(data);
		tokens = lexer.getTokens();
		for(Token token : tokens) {
			System.out.println(token);
		}
	}
}
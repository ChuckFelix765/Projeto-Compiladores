//Main.java
import java.io.IOException;
import java.util.List;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Main{
	public static void main(String[] args) throws IOException {
		try{
			File arq = new File("teste.txt");
			Scanner ler = new Scanner(arq);
			List<Token> tokens = null;
			String data = ler.nextLine();
			while (ler.hasNextLine()) {
				data += ler.nextLine();
				data += '\n';
			}
			ler.close();
			Lexer lexer = new Lexer(data);
			tokens = lexer.getTokens();
			for(Token token : tokens){
				System.out.println(token);
			}
			Parser parser = new Parser(tokens);
        	parser.main();
		}catch(FileNotFoundException e){
			System.out.println("Arquivo nao encontrado");
      		e.printStackTrace();
		}
	}
}
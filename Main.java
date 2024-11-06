//Main.java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;  // Import the File class
import java.util.List;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Main{
	public static void main(String[] args) throws IOException {
		try{
			File arq = new File("teste.txt");
			Scanner ler = new Scanner(arq);
			Scanner input = new Scanner(System.in);
			List<Token> tokens = null;
			String data = "";
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
//Main.java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;  // Import the File class
import java.util.List;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Main{

	public static void main(String[] args) throws IOException {

		try{
			File arq = new File("Entrada.txt");
			FileWriter arqt = new FileWriter("Token.txt");
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
			// System.out.print("Deseja observar os Tokens?\n1 - Sim\n2 - Nao\n\nDigite o que deseja: ");
			// int a = input.nextInt();
			//if(a == 1){
				for(Token token : tokens){
					arqt.write(token.toString()+"\n");
				}
			//}
			arqt.close();


			System.out.println("Selecione a liguagem para qual deseja traduzir:\n 1 - Python \n 2 - C");
			int selecao = input.nextInt();

			input.close();
			switch (selecao) {
				case 1 -> {	System.out.println("Python");
                        	ParserPy parser = new ParserPy(tokens);
                        	parser.main();
						}	
				case 2 -> {
							System.out.println("C");
							Parser parser = new Parser(tokens);
							parser.main();
                        }
				default -> {
							System.out.println("Selecao invalida");
							return;
							//throw new AssertionError();
                        }
					//throw new AssertionError();
			}
		}catch(FileNotFoundException e){
			System.out.println("Arquivo nao encontrado");
      		e.printStackTrace();
		}
	}
}

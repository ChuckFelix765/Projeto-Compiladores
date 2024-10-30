//Token.java
public class TokenP{
	public String tipo;
	public String lexema;

	public TokenP(String tipo, String lexema){
		this.lexema = lexema;
		this.tipo = tipo;
	}

	public String getLexema(){
		return lexema;
	}

	public String getTipo(){
		return tipo;
	}

	@Override
	public String toString(){
		return "< " + tipo + ", " + lexema + " >";
	}

}
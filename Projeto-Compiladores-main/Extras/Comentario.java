//Comentário.java
import java.text.CharacterIterator;

public class Comentario extends AFD {
	
	@Override
	public Token evaluate (CharacterIterator code){
        if(code.current() == '#'){
            code.next();
            while(code.current() != '#'){
                code.next();
            }
            code.next();
            return new Token("COMT","#");
        }
        return null;
	}
}
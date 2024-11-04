import java.util.List;

public class Parser {
    List<Token> tokens;
    Token token;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }
    public Token nextToken(){
        return (tokens.size() > 0) ? tokens.remove(0) : null; //Operador ternário
    }
    private void erro(String regra){
        System.out.println("Regra: " + regra);
        System.out.println("token inválido: " + token.getLexema());
        System.exit(0); //encerra o parser
    }
    
    public void main(){
        token = nextToken();
        if(verifica()){
            if(token.getTipo() == "EOF"){
                System.out.println("Sintaticamente correto");
            }else{
                erro("EOF");
            }
        }else{
            erro("fim do verifica");
        }
    }

    public boolean verifica(){
        if(token.getLexema().equals("if")){
            if(iff()) return true;
        }else if(token.getLexema().equals("while")){
            if(whilee()) return true;
        }else if(token.getLexema().equals("for")){
            if(fore()) return true;
        }else if(token.getLexema().equals("else")){
            if(elsee()) return true;
        }else if(token.getLexema().equals("elif")){
            if(elseif()) return true;
        }else if(token.getTipo().equals("VAR")){
            if(atrib()) return true;
        }else if(token.getLexema().equals("$")){
            return true;
        }
        erro("verifica");
        return false;
    }

    public boolean init(){
        if(bloco() || exis()){
            return true;
        }
        erro("init");
        return false;
    }

    public boolean exis(){
        if(init()){
            return true;
        }
        return true;
    }

    public boolean bloco(){
        while(!token.getLexema().equals("}")){
            verifica();
        }
        return true;
    }

    public boolean iff(){
        if(matchL("if") && condicao() && matchL("{") && bloco() && matchL("}")){
            return true;
        // if(matchL("if") && condicao() && matchL("{") && (expressao() || iff()) && matchL("}")||verifica()){
        //     if(verifica()){
        //         return true;
        //     }
        }
        erro("iff");
        return false;
    }

    public boolean elseif(){
        if(matchL("elif") && condicao() && matchL("{") && bloco() && matchL("}")){
            return true;
        }
        erro("elsee");
        return false;
    }

    public boolean elsee(){
        if(matchL("else") && matchL("{") && bloco() && matchL("}")){
            return true;
        }
        erro("elsee");
        return false;
    }

    public boolean whilee(){
        if(matchL("while") && condicao() && matchL("{") && bloco() && matchL("}")){
            return true;
        }
        erro("whilee");
        return false;
    }

    public boolean fore(){
        if(matchL("for") && condicao() && matchL(";") && condicao() && matchL(";") && incr()){
            ;
        }return true;
        //for(id = 0; i< 10; i++)
    }

    public boolean incr(){
        if((matchT("VAR")||matchT("INT")||matchT("FLT")) && operador() && operador()){
            return true;
        }
        erro("incr");
        return false;
    }

    public boolean atrib(){
        if(matchT("VAR") && matchL("=") && (matchT("VAR")||matchT("INT")||matchT("FLT"))){
            return true;
        }
        erro("atrib");
        return false;
    }

    public boolean condicao(){
        if(matchL("(") && valor() && operador() && valor() && matchL(")")){
            return true;
        }
        erro("condicao");
        return false;
    }

    public boolean valor(){
        if(matchT("INT") || matchT("FLT") || matchT("VAR")){
            return true;
        }
        erro("valor");
        return false;
    }

    public boolean expressao(){
        if(matchT("VAR") && matchL("=") && (matchT("INT") || matchT("FLT"))){
            return true;
        }
        erro("expressao");
        return false;
    }

    public boolean operador(){
        if(matchL("+") || matchL("-") || matchL("*") || matchL("/") || matchL("%") || matchL("==") || matchL("<") || matchL(">")){
            //Falta %, (, ), :;
            return true;
        }
        erro("operador");
        return false;
    }

    private boolean matchL(String lexema){
        if(token.getLexema().equals(lexema)){
            token = nextToken();
            return true;
        }
        return false;
    }

    private boolean matchT(String tipo){
        if(token.getTipo().equals(tipo)){
            token = nextToken();
            return true;
        }
        return false;
    }

    /*private boolean matchL(String lexema, String newcode){
        if(token.getLexema().equals(lexema)){
            print(newcode);
            token = nextToken();
            return true;
        }
        erro(lexema);
        return false;
    }

    private boolean matchT(String tipo, String newcode){
        if(token.getTipo().equals(tipo)){
            print(newcode);
            token = nextToken();
            return true;
        }
        erro(tipo);
        return false;
    }*/
    
    public void print(String code){
        System.out.println(code);
    }
}
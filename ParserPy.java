import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ParserPy {
    String tabs = "";
    List<Token> tokens;
    Token token;
    StringBuilder tradutor = new StringBuilder();

    public ParserPy(List<Token> tokens){
        this.tokens = tokens;
    }
    public Token nextToken(){
        return (tokens.size() > 0) ? tokens.remove(0) : null; //Operador ternário
    }

    private void erro(String regra){
        System.out.println("Regra: " + regra);
        System.out.println("token invalido: " + token.getLexema());
        System.exit(0); //encerra o parser
    }
    
    public void main(){
        //print("#include <stdio.h>\n\nint main(){\n");
        token = nextToken();
        
        while(!token.getTipo().equals("EOF")){
            verifica();
            if(token.getTipo().equals("EOF")){
                //tradutor.append("\n}");
                System.out.println("\nSintaticamente correto");
                try(FileWriter writer = new FileWriter("Code.py")){
                    writer.write(tradutor.toString());
                }catch (IOException e){
                    System.out.println(e);
                }
                System.out.print(tradutor.toString());
            }
        }
    }

    public boolean verifica(){
        if(matchT("COMT")){
            return true;
        }else if(token.getLexema().equals("importe")){
            if(cabeca()) return true;
        }else if(token.getLexema().equals("funcion")){ //if
            if(funcion()) return true;
        }else if(token.getLexema().equals("si")){ //if
            if(iff()) return true;
        }else if(token.getLexema().equals("mientras")){ //while
            if(whilee()) return true;
        }else if(token.getLexema().equals("para")){ //for
            if(fore()) return true;
        }else if(token.getLexema().equals("sino")){ //else
            if(elsee()) return true;
        }else if(token.getLexema().equals("enton")){ //elif
            if(elseif()) return true;
        }else if(token.getLexema().equals("escriba")){ //input
            if(inputt()) return true;
        }else if(token.getLexema().equals("muestrame")){//print
            if(printt()) return true;
        }else if(token.getLexema().equals("entero")){ //int
            if(dec_int()) return true;
        }else if(token.getLexema().equals("flotante")){ //float
            if(dec_float()) return true;
        }else if(token.getLexema().equals("palabra")){// string
            if(dec_string()) return true;
        }else if(token.getLexema().equals("letra")){ //char
            if(dec_char()) return true;
        }else if(token.getLexema().equals("retorna")){ // return
            if(retorna()) return true;
        }
            /* 
            else if(token.getTipo().equals("VAR")){
                if(atrib()) return true;
            }else if(token.getTipo().equals("RES")){
                if(dete()) return true;
            }
            */

        erro("verifica");
        return false;
    }

    public boolean cabeca(){
        if(matchL("importe") && matchL("<") 
        && matchL("spanIO") && matchL(">")){
            return true;
        }
        erro("cabeca");
        return false;
    }

    public boolean funcion(){
        if(matchL("funcion", tabs + "def ") && tipo() 
        && matchT("VAR",token.getLexema()) && matchL("(") 
        && matchL(")") && matchL("{",tabs + ":\n") 
        && bloco() && matchL("}","\n\n")){
            return true;
        }
        erro("funcion");
        return false;
    }

    public boolean tipo(){
        if(matchL("entero",tabs + "int ") || matchL("flotante",tabs + "float ")
        || matchL("palabra",tabs + "string ") || matchL("letra",tabs + "char ")){
            return true;
        }
        erro("tipo");
        return false;
    }

    public boolean tipos(){
        if(matchL("entero","%d") || matchL("flotante","%f")
        || matchL("palabra","%s") || matchL("letra","%c")){
            return true;
        }
        erro("tipos");
        return false;
    }
    public boolean tipos_input(){
        if(matchL("entero") || matchL("flotante")
        || matchL("palabra") || matchL("letra")){
            return true;
        }
        erro("tipos");
        return false;
    }

    public boolean bloco(){
        while(!token.getLexema().equals("}")){
            tabs = tabs + "\t";
            verifica();
        }
        tabs = tabs.replaceFirst("\t", "");

        return true;
    }

    public boolean iff(){
        //if x > 0: 
        //  a = 0
        if(matchL("si",tabs + "if ") && matchL("("," ") 
        && condicao() && matchL(")",":\n") 
        && matchL("{", tabs) && bloco() 
        && matchL("}","\n")){
            return true;
        }
        erro("iff");
        return false;
    }

    public boolean elseif(){
        if(matchL("enton",tabs + "elif ") && matchL("(") 
        && condicao() && matchL(")") 
        && matchL("{",":\n" + tabs) && bloco() 
        && matchL("}","\n")){
            return true;
        }
        erro("elsif");
        return false;
    }

    public boolean elsee(){
        if(matchL("sino",tabs + "else ") && matchL("{",":\n") 
        && bloco() && matchL("}","\n")){
            return true;
        }
        erro("elsee");
        return false;
    }

    public boolean whilee(){
        if(matchL("mientras",tabs + "while ") && matchL("(") 
        && condicao() && matchL(")",":\n") 
        && matchL("{", tabs) && bloco() 
        && matchL("}","\n")){
            return true;
        }
        erro("whilee");
        return false;
    }

    public boolean fore(){
        if(matchL("para",tabs + "for ") && matchL("(","(") 
        && atrib() && matchL(";") 
        && condicao() && matchL(";") 
        && incr() && matchL(")",")") 
        && matchL("{","{\n") && bloco() 
        && matchL("}","\n}")){
            return true;
        }
        erro("fore");
        return false;
    }

    public boolean inputt(){
        StringBuilder entra = new StringBuilder();

        if(matchL("escriba", tabs)){
            entra.append("input()");
             if(matchL("(") ){
                if(tipos_input()){
                    entra.insert(0, token.getLexema() + " = ");
                    if(matchT("VAR")){
                        if(matchL(")", entra + " \n")){
                            tabs = tabs.replaceFirst("\t", "");

                            return true;
                        }
                    }
                }
            }
            
        }
        erro("inputt");
        return false;
    }

    public boolean printt(){
        if(matchL("muestrame", tabs + "print") && matchL("(", "(\"") 
        && (matchT("STRG", token.getLexema() + "\"") || (tipos() 
        && matchT("VAR", "\\n\" %" + token.getLexema()))) && matchL(")", ")\n")){
            return true;
        }
        erro("print");
        return false;
    }

    public boolean retorna(){
        if(matchL("retorna", tabs + "return ") && matchL("verdad", "true\n") 
        || matchL("paraguai", "false\n")){
            return true;
        }
        erro("return");
        return false;
    }

//dec -> declaracao int | float | String | char
    public boolean dec_int(){
        if((matchL("entero", tabs + "int ") && matchT("VAR", token.getLexema()) && (matchL("=", " = ") && matchT("INT", token.getLexema() + "\n")))){
            return true;
        }return false;
    }
    public boolean dec_float(){
        if(matchL("flotante", tabs + "float ") && matchT("VAR", token.getLexema()) && (matchL("=", " = ") && matchT("FLT", token.getLexema() + "\n"))){
            return true;
        }return false;
    }

    public boolean dec_string(){
        if(matchL("palabra", tabs + "str ") && matchT("VAR", token.getLexema()) 
        && matchL("=", " = ") && matchT("STRG", "\"" + token.getLexema() + "\"\n")){
            return true;
        }return false;
    }

    public boolean dec_char(){
        if((matchL("letra", tabs + "char ") && matchT("VAR", token.getLexema()) && matchL("=", " = ") && matchT("CHAR", "\'" + token.getLexema() + "\'\n"))){
            return true;
        }return false;
    }

    public boolean incr(){
        if((matchT("VAR")||matchT("INT")||matchT("FLT")) && operador() && operador()){
            return true;
        }
        erro("incr");
        return false;
    }

    public boolean dete(){
        if(matchT("RES","int ") && matchT("VAR"," "+token.getLexema()+";\n")){
            return true;
        }
        erro("dete");
        return false;
    }

    public boolean atrib(){
        if(matchT("VAR",token.getLexema()) && operador() 
        && (matchT("VAR",token.getLexema()+";")||matchT("INT",token.getLexema()+";")
        ||matchT("FLT",token.getLexema()+";")||matchT("STRG",token.getLexema()+";")
        ||matchT("CHAR",token.getLexema()+";")||expressao())){
            return true;
        }
        erro("atrib");
        return false;
    }

    public boolean condicao(){
        if(valor() && operador() && valor()){
            return true;
        }
        erro("condicao");
        return false;
    }

    public boolean valor(){
        if(matchT("INT",token.getLexema()) || matchT("FLT",token.getLexema()) 
        || matchT("VAR",token.getLexema())){
            return true;
        }
        erro("valor");
        return false;
    }

    public boolean expressao(){
        if((matchT("INT",token.getLexema()) || matchT("FLT",token.getLexema())) 
        && operador() && (matchT("INT",token.getLexema()) 
        || matchT("FLT",token.getLexema()))){
            return true;
        }
        erro("expressao");
        return false;
    }

    public boolean operador(){
        if(matchL("+"," + ") || matchL("-"," - ") 
        || matchL("*"," * ") || matchL("/"," / ")
        || matchL("%"," % ") || matchL("=="," == ") 
        || matchL("<"," < ") || matchL(">"," > ") 
        || matchL("="," = ")){
            //Falta  (, ), :;
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

    private boolean matchL(String lexema, String newcode){
        if(token.getLexema().equals(lexema)){
            print(newcode);
            token = nextToken();
            return true;
        }
        //erro("mathcL"+lexema);
        return false;
    }

    private boolean matchT(String tipo, String newcode){
        if(token.getTipo().equals(tipo)){
            print(newcode);
            token = nextToken();
            return true;
        }
        //erro("matchT"+tipo);
        return false;
    }
    
    public void print(String code){
        //System.out.print(code);
        tradutor.append(code);
    }
}
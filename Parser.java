import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Parser {
    List<Token> tokens;
    Token token;
    StringBuilder tradutor = new StringBuilder();

    public Parser(List<Token> tokens){
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
        token = nextToken();
        
        while(!token.getTipo().equals("EOF")){
            verifica();
            if(token.getTipo().equals("EOF")){
                System.out.println("\nCodigo Aceito\n");
                try(FileWriter writer = new FileWriter("Code.c")){
                    writer.write(tradutor.toString());
                }catch (IOException e){
                    System.out.println(e);
                }
            }
        }
    }

    public boolean verifica(){
        if(token.getLexema().equals("importe")){
            if(cabeca()) return true;
        }else if(token.getLexema().equals("funcion")){ //if
            if(funcion()) return true;
        }else if(token.getLexema().equals("cfuncion")){ //if
            if(cfuncion()) return true;
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
        }else if(token.getLexema().equals("retorna")){
            if(retorna()) return true;
        }else if(token.getTipo().equals("RES")){ //int
            if(dec_int()) return true;
        }else if(token.getTipo().equals("VAR")){
            if(dete()) return true;
        }else if(token.getTipo().equals("COMENT")){
            if(comentei()) return true;
        }
            /* 
            else if(token.getTipo().equals("VAR")){
                if(atrib()) return true;
            }else if(token.getTipo().equals("RES")){
                if(dete()) return true;
            }

            else if(token.getLexema().equals("flotante")){ //float
            if(dec_int()) return true;
        }else if(token.getLexema().equals("palabra")){// string
            if(dec_string()) return true;
        }else if(token.getLexema().equals("letra")){ //char
            if(dec_char()) return true;
        }
            */

        erro("verifica");
        return false;
    }


    public boolean comentei(){
        if(matchT("COMENT", "/*" + token.getLexema() + "*/\n") ){
    
            return true;
        }
        erro("Comentario");
        return false;
    }

    public boolean cabeca(){
        if(matchL("importe","#include ") && matchL("<","<") 
        && matchL("spanIO","stdio.h") && matchL(">",">\n\n")){
            return true;
        }
        erro("Importe de biblioteca");
        return false;
    }

    public boolean funcion(){
        if(matchL("funcion") && tipo() 
        && matchT("VAR",token.getLexema()) && matchL("(","(") 
        && matchL(")",")") && matchL("{","{\n") 
        && bloco() && matchL("}","\n}\n")){
            return true;
        }
        erro("Declaracao de funcao");
        return false;
    }
    public boolean cfuncion(){
        if(matchL("cfuncion") && matchT("VAR",token.getLexema()) 
        && matchL("(","(") && valor() 
        && matchL(")",");")){
            return true;
        }
        erro("Chamada de Funcao");
        return false;
    }

    public boolean tipo(){
        if(matchL("entero","int ") || matchL("flotante","float ")
        || matchL("letra","char ") || (matchL("palabra","char ") && palav())){
            return true;
        }
        erro("Determinar tipo de funcao");
        return false;
    }

    public boolean tipos(){
        if(matchL("entero","%d") || matchL("flotante","%f")
        || matchL("palabra","%s") || matchL("letra","%c")){
            return true;
        }
        erro("Determinar tipo de input/output");
        return false;
    }

    public boolean bloco(){
        while(!token.getLexema().equals("}")){
            verifica();
        }
        return true;
    }

    public boolean iff(){
        if(matchL("si","if ") && matchL("(","(") 
        && condicao() && matchL(")",")") 
        && matchL("{","{\n") && bloco() 
        && matchL("}","\n}")){
            return true;
        }
        erro("Erro no if");
        return false;
    }

    public boolean elseif(){
        if(matchL("enton","else if ") && matchL("(","(") 
        && condicao() && matchL(")",")") 
        && matchL("{","{\n") && bloco() 
        && matchL("}","\n}")){
            return true;
        }
        erro("Erro no else if");
        return false;
    }

    public boolean elsee(){
        if(matchL("sino","else ") && matchL("{","{\n") 
        && bloco() && matchL("}","\n}")){
            return true;
        }
        erro("Erro no else");
        return false;
    }

    public boolean whilee(){
        if(matchL("mientras","while ") && matchL("(","(") 
        && condicao() && matchL(")",")") 
        && matchL("{","{\n") && bloco() 
        && matchL("}","\n}")){
            return true;
        }
        erro("Erro no while");
        return false;
    }

    public boolean fore(){
        if(matchL("para","for ") && matchL("(","(") 
        && matchT("VAR",token.getLexema()) && atrib() 
        && matchL(";",";") && condicao() 
        && matchL(";",";") && incr() 
        && matchL(")",")") && matchL("{","{\n") 
        && bloco() && matchL("}","\n}")){
            return true;
        }
        erro("Erro no for");
        return false;
    }

    public boolean inputt(){
        if(matchL("escriba", "scanf") && matchL("(", "(\"") 
        && tipos() && matchT("VAR", "\", &"+token.getLexema()) 
        && matchL(")", ");\n")){
            return true;
        }
        erro("Erro no input");
        return false;
    }

    public boolean printt(){
        if(matchL("muestrame", "printf") && matchL("(", "(\"") 
        && (matchT("STRG", token.getLexema() + "\"") || (tipos() 
        && matchT("VAR", "\\n\"," + token.getLexema()))) && matchL(")", ");\n")){
            return true;
        }
        erro("Erro no print");
        return false;
    }

    public boolean retorna(){
        if(matchL("retorna", "return ") && matchL("verdad", "true;\n") 
        || matchL("paraguai", "false;\n") || valor()){
            return true;
        }
        erro("Erro no return");
        return false;
    }
//dec -> declaracao int | float | String | char

    public boolean dete(){
        if(matchT("VAR",token.getLexema()) && atrib()){
            print(";\n");
            return true;
        }
        return true;
    }

    public boolean palav(){
        if(matchT("VAR",token.getLexema()+"[255]") && dec_string()){
            print(";\n");
            return true;
        }
        return true;
    }

    public boolean atrib(){
        if(matchL("=", "=") && e()){
            return true;
        }
        return true;
    }

    public boolean dec_int(){
        if(tipo() && dete()){
            return true;
        }return false;
    }

    public boolean dec_string(){
        if(matchL("=", " = ") && matchT("STRG", "\"" + token.getLexema() + "\"")){
            return true;
        }return true;
    }

    public boolean incr(){
        if(matchT("VAR",token.getLexema()) && operador() && operador()){
            return true;
        }
        erro("Erro na incrementacao");
        return false;
    }

    public boolean condicao(){
        if(valor() && operador() && valor()){
            return true;
        }
        erro("Erro na condicao");
        return false;
    }

    public boolean valor(){
        if(matchT("INT",token.getLexema()) || matchT("FLT",token.getLexema()) 
        || matchT("VAR",token.getLexema())){
            return true;
        }
        erro("Erro no valor");
        return false;
    }

    public boolean e(){
        if(matchT("CHAR","\'"+token.getLexema()+"\'") || t() && el()){
            return true;
        }
        erro("Erro na expr1");
        return false;
    }

    public boolean el(){
        if((matchL("+","+") || matchL("-","-")) && t() && el()){
            return true;
        }
        return true;
    }

    public boolean t(){
        if(f() && tl()){
            return true;
        }
        erro("Erro na expr3");
        return false;
    }

    public boolean tl(){
        if((matchL("*","*") || matchL("/","/")) && f() && tl()){
            return true;
        }
        return true;
    }

    public boolean f(){
        if(matchL("(","(") && e() && matchL(")",")") || valor()){
            return true;
        }
        erro("Erro na expr5");
        return false;
    }

    public boolean operador(){
        if(matchL("+","+") || matchL("-","-") 
        || matchL("*","*") || matchL("/","/")
        || matchL("%","%") || matchL("==","==") 
        || matchL("<","<") || matchL(">",">") 
        || matchL("=","=") || matchL("<=","<=")
        || matchL(">=",">=")){
            return true;
        }
        erro("Operador invalido");
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
        return false;
    }

    private boolean matchT(String tipo, String newcode){
        if(token.getTipo().equals(tipo)){
            print(newcode);
            token = nextToken();
            return true;
        }
        return false;
    }
    
    public void print(String code){
        tradutor.append(code);
    }
}
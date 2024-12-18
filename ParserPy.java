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
        token = nextToken();
        
        while(!token.getTipo().equals("EOF")){
            verifica();
            if(token.getTipo().equals("EOF")){
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
        }else if(token.getLexema().equals("funcion")){ //funcao
            if(funcion()) return true;
        }else if(token.getLexema().equals("cfuncion")){
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
        }else if(token.getTipo().equals("COMENT")){
            if(comentei()) return true;
        }else if(token.getTipo().equals("VAR")){
            if(dete()) return true;
        }

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
    public boolean parametro(){
        if((matchL("entero") || matchL("flotante") 
        || matchL("palabra")) && matchT("VAR", token.getLexema())){
            if(matchT("VIRG", ", ")){
                parametro();
            }
            return true;

        }else if(token.getLexema().equals(")")){
            return true;
        }
        erro("parametros");
        return false;
    }


    public boolean funcion(){
        if(matchL("funcion", tabs + "def ") 
        && tipos_input() 
        && matchT("VAR",token.getLexema()) 
        && matchL("(", "(") 
        && parametro()
        && matchL(")", ")") 
        && matchL("{",":\n") 
        && bloco() && matchL("}","\n\n")){
            return true;
        }
        erro("funcion");
        return false;
    }

    public boolean cfuncion(){
        if(matchL( "cfuncion", tabs) && matchT("VAR",token.getLexema()) 
        && matchL("(","(") && valor() 
        && matchL(")",")\n")){
            return true;
        }
        erro("Chamada de Funcao");
        return false;
    }

    public boolean tipo(){
        if(matchL("entero",tabs + "int ") || matchL("flotante",tabs + "float ")
        || matchL("palabra",tabs + "str ") || matchL("letra",tabs + "char ")){
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
        && matchL("{") && bloco() 
        && matchL("}","\n")){
            return true;
        }
        erro("iff");
        return false;
    }

    public boolean elseif(){
        if(matchL("enton",tabs + "elif ") && matchL("(") 
        && condicao() && matchL(")") 
        && matchL("{",":\n") && bloco() 
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
        && ( matchL("paraguai", "False") || matchL("verdad", "True") || condicao()) && matchL(")",":\n") 
        && matchL("{") && bloco() 
        && matchL("}","\n")){
            return true;
        }
        erro("whilee");
        return false;
    }

    // for i in range(0,)
    //para(i = 0 ; i<10 ; i++)
    public boolean fore(){
        if(matchL("para",tabs + "for ") && matchL("(")) {
            if(matchT("VAR",token.getLexema()) && matchL("=")){
                String valor_inicio = token.getLexema(); 
                if(matchT("INT") && matchL(";") && matchT("VAR") && matchL("<") || matchL(">")){
                    String valor_final = token.getLexema();
                    if(matchT("INT") && matchL(";")){
                        if(matchT("VAR") && matchL("+") && matchL("+") && matchL(")") && matchL("{", " in range(" + valor_inicio +", " + valor_final + "):\n") && bloco() && matchL("}","\n")){
                            return true;
                        }
                    }
                }
            }
        }
        erro("fore");
        return false;
    }

    public boolean inputt(){
        StringBuilder entra = new StringBuilder();

        if(matchL("escriba", tabs)){
            entra.append("(input())");
             if(matchL("(") ){
                String tipo = token.getLexema();
                if(tipos_input()){
                    if(tipo.equals("entero")){
                        entra.insert(0, "int");
                    }else if(tipo.equals("flotante")){
                        entra.insert(0, "float");
                    }else if(tipo.equals("palabra")){
                        entra.insert(0, "str");
                    }else if(tipo.equals("letra")){
                        entra.insert(0, "char");
                    }
                    entra.insert(0, token.getLexema() + " = ");
                    if(matchT("VAR")){
                        if(matchL(")", entra + " \n")){
                            if(tabs.length()>0){
                                tabs = tabs.replaceFirst("\t", "");
                            }//VAR = input()

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
        && matchT("VAR", "\" %" + token.getLexema()))) && matchL(")", ")\n")){
            if(tabs.length()>0){
                tabs = tabs.replaceFirst("\t", "");
            }
            return true;
        }
        erro("print");
        return false;
    }

    public boolean retorna(){
        if(matchL("retorna", tabs + "return ") && matchL("verdad", "True\n") 
        || matchL("paraguai", "False\n")){
            if(tabs.length()>0){
                tabs = tabs.replaceFirst("\t", "");
            }
            return true;
        }
        erro("return");
        return false;
    }

//dec -> declaracao int | float | String | char
public boolean definir(){
    if(matchL("=", " = ") && matchT("INT","int(" + token.getLexema() + ")\n") 
    || matchL("FLT", "float(" + token.getLexema() + ") \n") 
    || matchL("STRG", "str(\"" + token.getLexema() + "\")\n") 
    || matchL("CHAR", "\'" +token.getLexema() + "\'\n")){
        
        return true;
    }
    return false;
}

public boolean definir(String sem){
    if(sem.equals("entero")){
        print(" = int()\n");
        return true;
    }else if(sem.equals("flotante")){
        print(" = float()\n");
        return true;
    }else if(sem.equals("palabra")){
        print(" = str()\n");
        return true;
    }else if(sem.equals("letra")){
        print(" = char()\n");
        return true;
    }
        
    return false;
}



public boolean dec_int(){
    String tipo = token.getLexema();
    if(matchL("entero", tabs) && matchT("VAR", token.getLexema()) && definir() || definir(tipo)){
        if(tabs.length()>0){
            tabs = tabs.replaceFirst("\t", "");
        }return true;
        
    }return false;
}
public boolean dec_float(){
    String tipo = token.getLexema();
    if(matchL("flotante", tabs) && matchT("VAR", token.getLexema()) && definir()  || definir(tipo)){
        if(tabs.length()>0){
            tabs = tabs.replaceFirst("\t", "");
        }
        return true;
    }return false;
}

public boolean dec_string(){
    String tipo = token.getLexema();
    StringBuilder decl_str = new StringBuilder();
    if(matchL("palabra", tabs) && matchT("VAR", token.getLexema()) && definir() || definir(tipo)) {
        if(tabs.length()>0){
        tabs = tabs.replaceFirst("\t", "");
        }
        return true;
    }
    return false;

}
public boolean dec_char(){
    String tipo = token.getLexema();
    if((matchL("letra", tabs ) && matchT("VAR", token.getLexema()) && definir() || definir(tipo))){
        if(tabs.length()>0){
            tabs = tabs.replaceFirst("\t", "");
        }
        return true;
    }return false;
}

public boolean comentei(){
    if(matchT("COMENT", tabs+ "'''\n" + token.getLexema() + "\n'''\n") ){

        return true;
    }

    erro("Comentario");
    return false;
}

    public boolean incr(){
        if((matchT("VAR")||matchT("INT")||matchT("FLT")) && operador() && operador()){
            return true;
        }
        erro("incr");
        return false;
    }

    public boolean dete(){
        if(matchT("VAR", tabs + token.getLexema()) && atrib()){
            print("\n");
            if(tabs.length()>0){
                tabs = tabs.replaceFirst("\t", "");
            }
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
            if(matchT("VIRG", ", ")){
                valor();
            }
            return true;
        }else if(token.getLexema().equals(")")){
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
        if(matchL("+","+") || matchL("-","-") 
        || matchL("*","*") || matchL("/","/")
        || matchL("%","%") || matchL("==","==") 
        || matchL("<","<") || matchL(">",">") 
        || matchL("=","=") || matchL("<=","<=")
        || matchL(">=",">=")){
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
package utilidades;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Types;
import bdados.Conexao;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Palavra 
{
    private String Palavra;
    public String getPalavra()
    {
    	return Palavra;
    }

    public void setPalavra(String Palavra)
    {
    	this.Palavra = Palavra;
    }

 

////////////////////////SUBSTITUI STRINGS DENTRO DE ARQUIVOS JAVA   ///////////////////////////////////	
    public void parsingPalavra()
    {
//        System.out.println("----------");
//        System.out.println(this.getPalavra());
//        System.out.println("----------");

        this.setPalavra(removeCaracteresInvalidos(this.Palavra));
        this.setPalavra(separaCamelCase(this.Palavra));

//        System.out.println(this.getPalavra());
//        System.out.println("----------");
    }



        private String removeCaracteresInvalidos(String palavra)
        {

            palavra = palavra.replace("_", " ");
            palavra = palavra.replace(".", " ");
            palavra = palavra.replace("*", " ");
            palavra = palavra.replace("(", " ");
            palavra = palavra.replace(")", " ");
            palavra = palavra.replace(";", " ");
            palavra = palavra.replace("{", " ");
            palavra = palavra.replace("}", " ");
            palavra = palavra.replace("\"", " ");
            palavra = palavra.replace("=", " ");
            palavra = palavra.replace(",", " ");
            palavra = palavra.replace("@", " ");
            palavra = palavra.replace("/", " ");
            palavra = palavra.replace("\"", " ");
            palavra = palavra.replace("\'", " ");
            palavra = palavra.replace("+", " ");
            palavra = palavra.replace("&", " ");
            palavra = palavra.replace("-", " ");
            palavra = palavra.replace("!", " ");
            palavra = palavra.replace("\\", " ");
            palavra = palavra.replace("%", " ");
            palavra = palavra.replace(":", " ");
            palavra = palavra.replace("^", " ");
            palavra = palavra.replace(">", " ");
            palavra = palavra.replace("<", " ");
            palavra = palavra.replace("|", " ");
            palavra = palavra.replace("?", " ");
            palavra = palavra.replace("[", " ");
            palavra = palavra.replace("]", " ");
            palavra = palavra.replace("~", " ");
            palavra = palavra.replace("$", " ");
            palavra = palavra.replace("#", " ");
            palavra = palavra.replace("0", " ");
            palavra = palavra.replace("1", " ");
            palavra = palavra.replace("2", " ");
            palavra = palavra.replace("3", " ");
            palavra = palavra.replace("4", " ");
            palavra = palavra.replace("5", " ");
            palavra = palavra.replace("6", " ");
            palavra = palavra.replace("7", " ");
            palavra = palavra.replace("8", " ");
            palavra = palavra.replace("9", " ");

//            palavra=palavra.replace('\n', ' ');
//            palavra=palavra.replace('\r', ' ');
//            palavra=palavra.replace('\t', ' ');
//            palavra = palavra.replaceAll("( )+", " ");
            return palavra;
        }


        private String separaCamelCase(String palavra)
        {
            Inflector palavra1 = new Inflector();

            String [] palavraDividida  = palavra.split("[ ]");

            int i=0;
            String subPalavra ="";
            while(i<=palavraDividida.length-1)  // Percorre cada token do vetor de strings
            {
                subPalavra = subPalavra + " " + (palavra1.underscore(palavraDividida[i]));
                i++;
            }
            palavra = subPalavra;
            return palavra;
        }
}

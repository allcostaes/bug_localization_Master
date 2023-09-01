package arquivos;

import utilidades.Palavra;
import utilidades.Inflector;
import pacote.Metodo_Bruto;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;


public class Arquivo_Java 
{
    private String Nome_Completo;
    private Palavra Palavra;
    private Metodo_Bruto metodo;
    private Integer CF_CODIGO;

    public Arquivo_Java()
    {
        this.Palavra = new Palavra();
        this.metodo = new Metodo_Bruto();
    }

    public void setNome_Completo(String Nome_Completo)
    {
        this.Nome_Completo = Nome_Completo;
    }

    public void setPalavra(Palavra Palavra)
    {
        this.Palavra = Palavra;
    }

    public void setCF_CODIGO(Integer CF_CODIGO)
    {
        this.CF_CODIGO = CF_CODIGO;
    }


    private void gravaMetodos(JavaClass classeJava)
    {

        JavaMethod metodosDaClasse[] = classeJava.getMethods();
        for (JavaMethod method : metodosDaClasse)
        {

            String classePacote[] = (classeJava.getFullyQualifiedName().toString()).split("\\.");
            String Classe = classePacote[(classePacote.length) - 1];

            metodo.setPACOTE(classeJava.getPackageName());
            metodo.setCLASSE(Classe);
            metodo.setMETODO(method.getName());

            // Monta uma string com as partes do comentario do tipo @algumacoisa   ==  tags
            DocletTag[] tags = method.getTags();

            StringBuilder Implementacao = new StringBuilder();


            Implementacao.append(method.getComment());
            Implementacao.append("\n ");
            for (int k = 0; k <= tags.length - 1; k++)
            {
                Implementacao.append(tags[k].getName());
                Implementacao.append(" ");
                Implementacao.append(tags[k].getValue());
                Implementacao.append(" \n ");
            }

            Implementacao.append(method.getName());
            Implementacao.append("\n \n\n ");
            Implementacao.append(method.getSourceCode());
            Palavra.setPalavra(Implementacao.toString());

            Palavra.parsingPalavra();

            // Processa o nome da classe quebrando-o em pedaços menores e gerando os fragmentos do nome da classe
            String classeQuebrada = Classe;
            Inflector palavra2 = new Inflector();
            classeQuebrada = palavra2.underscore(classeQuebrada);
            classeQuebrada = classeQuebrada.replaceAll("[\\d\\$_]"," ");
            palavra2 = null;


            StringBuilder implementacaoFinal = new StringBuilder();
            implementacaoFinal.append(classeJava.getPackageName());
            implementacaoFinal.append("\n ");
            implementacaoFinal.append(Classe);
            implementacaoFinal.append("\n ");
            implementacaoFinal.append(method.getName());
            implementacaoFinal.append("\n ");
            implementacaoFinal.append((Palavra.getPalavra()).replaceAll("_"," "));
            implementacaoFinal.append(" ");
            implementacaoFinal.append(classeQuebrada);
            metodo.setIMPLEMENTACAO(implementacaoFinal.toString());

            Implementacao=null;
            implementacaoFinal=null;
            //    ---    //
            String[] Nome_Separado = null;
            Nome_Separado = separaNome_Completo(this.Nome_Completo);
            //   ---    //

            metodo.setDIRETORIO_ARQUIVO(Nome_Separado[0].replace("\\", "/"));
            metodo.setNOME_ARQUIVO(Nome_Separado[1]);

            metodo.setCF_CODIGO(this.CF_CODIGO);
            //Grava o metodo no banco de dados
            metodo.persisteMetodo();

        }
    }

    public void listaClasses()
    {

        String aFileName = this.Nome_Completo;
        try
        {
            JavaDocBuilder builder = new JavaDocBuilder();
            builder.addSource(new FileReader(aFileName));
            JavaClass classes[] = builder.getClasses();
            for (JavaClass classeJava : classes)
            {
                gravaMetodos(classeJava);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Separa o nome completo de um arquivo em duas partes: o nome do diretorio onde o arquivo se encontra e o nome simples do mesmo  **/
    private String[] separaNome_Completo(String Nome_Completo) {
        String[] temporario;
        String temporario1 = "";
        Nome_Completo = Nome_Completo.replace("\\", "/");
        temporario = Nome_Completo.split("\\/");

        for (int i = 0; i <= (temporario.length - 2); i++) {
            temporario1 = temporario1 + temporario[i] + "\\";
        }

        String[] Nome_Separado = {temporario1, temporario[(temporario.length - 1)]};
        return Nome_Separado;
    }

}

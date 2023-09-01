package arquivos;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.*;

import bdados.Conexao;
import javax.swing.JOptionPane;

public class Corpus {

    static Conexao Conexao;
    static Statement Comando;
    static SnowballStemmer stemmer;
    private String pesquisa[];
    private Integer BG_CODIGO;

    public Corpus() {
        Class stemClass;
        try {
            stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer");
            this.stemmer = (SnowballStemmer) stemClass.newInstance();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Conexao = new Conexao();
        try {
            Comando = Conexao.conexao.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void setPesquisa(String pesquisa)
    {
        this.pesquisa = pesquisa.split(" ");
        for (int i=0;i<=(this.getPesquisa().length-1);i++)
        {
            stemmer.setCurrent(this.getPesquisa()[i]);
            stemmer.stem();
            this.getPesquisa()[i] = stemmer.getCurrent();
        }
    }

    private String[] getPesquisa()
    {
        return this.pesquisa;
    }

    public void setBG_CODIGO(Integer BG_CODIGO)
    {
        this.BG_CODIGO = BG_CODIGO;
    }

    private Integer getBG_CODIGO()
    {
        return this.BG_CODIGO;
    }




    // Abordagem 1 - Testa todo o código fonte
    public void constroiCorpusCodigoFonte()
    {
        ResultSet Resultado;
        try {
            String Selecao = "";
            Selecao = "SELECT  \"MB\".\"MB_CODIGO\" , \"MB\".\"MB_IMPLEMENTACAO\", \"MB\".\"MB_PACOTE\", \"MB\".\"MB_CLASSE\", \"MB\".\"MB_METODO\"";
            Selecao = Selecao + " FROM \"BUGS\"  \"BG\", \"METODO_BRUTO\"  \"MB\" WHERE ";
            Selecao = Selecao + " (\"BG\".\"BG_CODIGO\"=" + this.getBG_CODIGO() + ") " ;
            Selecao = Selecao + " AND (\"BG\".\"CF_CODIGO\" = \"MB\".\"CF_CODIGO\") ";
            //System.out.println(Selecao);
            Comando.execute(Selecao);
            Resultado = Comando.getResultSet();
            while (Resultado.next())
            {
                String nomeArquivo = "c:\\analise\\corpus\\";
                String Pacote, Classe, Metodo;

                stemmer.setCurrent(Resultado.getString(3).toLowerCase());
                stemmer.stem();
                Pacote = stemmer.getCurrent();

                stemmer.setCurrent(Resultado.getString(4).toLowerCase());
                stemmer.stem();
                Classe = stemmer.getCurrent();

                stemmer.setCurrent((Resultado.getString(5)).toLowerCase());
                stemmer.stem();
                Metodo = stemmer.getCurrent();

                nomeArquivo = nomeArquivo + Integer.toString(Resultado.getInt(1));
                String conteudoArquivo = Resultado.getString(2);
                persisteDocumentoCorpus(nomeArquivo, conteudoArquivo);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }

    // Abordagem 2 - Testa somente o rastro oriundo dos rastros de execução
    public void constroiCorpusRastro()
    {
        ResultSet Resultado;
        try
        {
            String Selecao = "";
            Selecao = " SELECT DISTINCT ON (\"MC\".\"MB_CODIGO\") \"MC\".\"MB_CODIGO\", \"MB\".\"MB_IMPLEMENTACAO\" " +
                      " FROM \"BUGS\"  \"BG\",\"METODO_CARACTERISTICA\"  \"MC\", \"METODO_BRUTO\"  \"MB\"  "+
                      " WHERE (\"BG\".\"BG_CODIGO\"=" + this.getBG_CODIGO() + ") "+
                      " AND (\"BG\".\"CF_CODIGO\" = \"MB\".\"CF_CODIGO\") AND (\"MB\".\"CF_CODIGO\" = \"MC\".\"CF_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" = \"MB\".\"MB_CODIGO\") AND (\"BG\".\"BG_CODIGO\" = \"MC\".\"BG_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" <> 0) ";

            //System.out.println(Selecao);
            Comando.execute(Selecao);
            Resultado = Comando.getResultSet();
            while (Resultado.next())
            {
                String nomeArquivo = "c:\\analise\\corpus\\";
                nomeArquivo = nomeArquivo + Integer.toString(Resultado.getInt(1));
                String conteudoArquivo = Resultado.getString(2);
                persisteDocumentoCorpus(nomeArquivo, conteudoArquivo);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        retornaNumDocsRastroSemGrep();
        retornaNumDocsRastroComGrep();
        //JOptionPane.showMessageDialog(null,  "Anote o consumo de memória para K=300.",  "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }


    // Abordagem 3 - Testa somente o rastro oriundo dos rastros de execução com aplicação de grep com termos vindos da consulta
    public void constroiCorpusRastroComGrep()
    {
        ResultSet Resultado;
        try
        {
            String Selecao = "";
            Selecao = " SELECT DISTINCT ON (\"MC\".\"MB_CODIGO\") \"MC\".\"MB_CODIGO\", \"MB\".\"MB_IMPLEMENTACAO\" " +
                      " FROM \"BUGS\"  \"BG\",\"METODO_CARACTERISTICA\"  \"MC\", \"METODO_BRUTO\"  \"MB\"  "+
                      " WHERE (\"BG\".\"BG_CODIGO\"=" + this.getBG_CODIGO() + ") "+
                      " AND (\"BG\".\"CF_CODIGO\" = \"MB\".\"CF_CODIGO\") AND (\"MB\".\"CF_CODIGO\" = \"MC\".\"CF_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" = \"MB\".\"MB_CODIGO\") AND (\"BG\".\"BG_CODIGO\" = \"MC\".\"BG_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" <> 0) "+
                      " AND (";

            for (int i=0;i<=(this.getPesquisa().length-1);i++)
            {
                if ((this.getPesquisa()[i].trim()).isEmpty()==false)
                {
                    if(i!=(this.getPesquisa().length-1))
                    {
                        Selecao = Selecao + " (\"MB\".\"MB_IMPLEMENTACAO\" ilike '%" + this.getPesquisa()[i] + "%') OR ";
                    }
                    else
                    {
                        Selecao = Selecao + " (\"MB\".\"MB_IMPLEMENTACAO\" ilike '%" + this.getPesquisa()[i] + "%') ";
                    }
                }
            }

            Selecao = Selecao +")";

            //System.out.println(Selecao);
            Comando.execute(Selecao);
            Resultado = Comando.getResultSet();
            while (Resultado.next())
            {
                String nomeArquivo = "c:\\analise\\corpus\\";
                nomeArquivo = nomeArquivo + Integer.toString(Resultado.getInt(1));
                String conteudoArquivo = Resultado.getString(2);
                persisteDocumentoCorpus(nomeArquivo, conteudoArquivo);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        retornaNumDocsRastroSemGrep();
        retornaNumDocsRastroComGrep();
        //JOptionPane.showMessageDialog(null,  "Anote o consumo de memória para K=300.",  "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }



    private static void persisteDocumentoCorpus(String nomeArquivo, String conteudoArquivo) {
        try {
            FileWriter fw = new FileWriter(nomeArquivo, true);
            fw.write(conteudoArquivo + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }















public void retornaNumDocsRastroSemGrep()
    {
        ResultSet Resultado1;
        Integer NUMDOCS=0;
        try
        {
            String Selecao = "";
            Selecao = " SELECT COUNT(*) FROM (SELECT DISTINCT ON (\"MC\".\"MB_CODIGO\")  \"MC\".\"MB_CODIGO\" " +
                      " FROM \"BUGS\"  \"BG\",\"METODO_CARACTERISTICA\"  \"MC\", \"METODO_BRUTO\"  \"MB\"  "+
                      " WHERE (\"BG\".\"BG_CODIGO\"=" + this.getBG_CODIGO() + ") "+
                      " AND (\"BG\".\"CF_CODIGO\" = \"MB\".\"CF_CODIGO\") AND (\"MB\".\"CF_CODIGO\" = \"MC\".\"CF_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" = \"MB\".\"MB_CODIGO\") AND (\"BG\".\"BG_CODIGO\" = \"MC\".\"BG_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" <> 0)) AS NUMDOCS ";
            System.out.println(Selecao);
            Comando.execute(Selecao);
            Resultado1 = Comando.getResultSet();
            while (Resultado1.next())
            {
                 NUMDOCS = Resultado1.getInt(1);
            }
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        //JOptionPane.showMessageDialog(null,  "BUG:  "+ this.getBG_CODIGO() +"\nDocumentos SEM grep: " + NUMDOCS + ".",  "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void retornaNumDocsRastroComGrep()
    {
        ResultSet Resultado2;
        Integer NUMDOCS=0;
        try
        {
            String Selecao = "";
            Selecao = " SELECT COUNT(*) FROM (SELECT DISTINCT ON (\"MC\".\"MB_CODIGO\")   \"MC\".\"MB_CODIGO\" " +
                      " FROM \"BUGS\"  \"BG\",\"METODO_CARACTERISTICA\"  \"MC\", \"METODO_BRUTO\"  \"MB\"  "+
                      " WHERE (\"BG\".\"BG_CODIGO\"=" + this.getBG_CODIGO() + ") "+
                      " AND (\"BG\".\"CF_CODIGO\" = \"MB\".\"CF_CODIGO\") AND (\"MB\".\"CF_CODIGO\" = \"MC\".\"CF_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" = \"MB\".\"MB_CODIGO\") AND (\"BG\".\"BG_CODIGO\" = \"MC\".\"BG_CODIGO\") "+
                      " AND (\"MC\".\"MB_CODIGO\" <> 0) "+
                      " AND (";

            for (int i=0;i<=(this.getPesquisa().length-1);i++)
            {
                if ((this.getPesquisa()[i].trim()).isEmpty()==false)
                {
                    if(i!=(this.getPesquisa().length-1))
                    {
                        Selecao = Selecao + " (\"MB\".\"MB_IMPLEMENTACAO\" ilike '%" + this.getPesquisa()[i] + "%') OR ";
                    }
                    else
                    {
                        Selecao = Selecao + " (\"MB\".\"MB_IMPLEMENTACAO\" ilike '%" + this.getPesquisa()[i] + "%') ";
                    }
                }
            }

            Selecao = Selecao +")  )  AS NUMDOCS ";
            System.out.println(Selecao);
            Comando.execute(Selecao);
            Resultado2 = Comando.getResultSet();
            while (Resultado2.next())
            {
                 NUMDOCS = Resultado2.getInt(1);
            }
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        //JOptionPane.showMessageDialog(null,  "BUG:  "+ this.getBG_CODIGO() +"\nDocumentos COM grep: " + NUMDOCS + ".",  "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }


}

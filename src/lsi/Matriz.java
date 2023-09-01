package lsi;

import utilidades.Inflector;
import lsi.Matriz;
import arquivos.Arquivo;
import java.io.BufferedReader;
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
import java.sql.PreparedStatement;
import org.openide.util.Exceptions;

public class Matriz
{

    private Integer[] Documentos;
    private short valorMaximo;
    private String[] Termos;
    private short[][] matTermDoc;
    private int nLinhas, nColunas;
    private Conexao Conexao;
    static Statement Comando;
    ResultSet Resultado;
    static SnowballStemmer stemmer;
    private PreparedStatement comandoPreparado1;

    Matriz()
    {
         nLinhas = 0;
         nColunas = 0;
        Conexao = new Conexao();
        try {
            Comando = Conexao.conexao.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        try
        {
           this.comandoPreparado1 = this.Conexao.conexao.prepareStatement("SELECT \"MB_METODO\",\"MB_CLASSE\" FROM \"METODO_BRUTO\"  WHERE \"MB_CODIGO\"=?");
        } catch (SQLException ex)
        {
           Exceptions.printStackTrace(ex);
        }

    }

    private void setnLinhas(Integer nLinhas)
    {
        this.nLinhas = nLinhas;
    }
    private void setnColunas(Integer nColunas)
    {
        this.nColunas = nColunas;
    }
    private void setvalorMaximo(short valorMaximo)
    {
        this.valorMaximo = valorMaximo;
    }


    private Integer getnLinhas()
    {
        return this.nLinhas;
    }
    private Integer getnColunas()
    {
        return this.nColunas;
    }

    private short getvalorMaximo()
    {
        return this.valorMaximo;
    }


    private void getDimensoes()
    {
        // Le as dimensoes da matriz a partir do arquivo
        try
        {
            FileReader fileReader = new FileReader("c:\\analise\\matriz\\dimensoes_matriz1.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linhaDimensoes = null;
            while ((linhaDimensoes = bufferedReader.readLine()) != null)
            {
                String dimensoes[] = linhaDimensoes.split(" ");
                this.setnLinhas(Integer.parseInt(dimensoes[0].trim()));
                this.setnColunas(Integer.parseInt(dimensoes[1].trim()));
                this.setvalorMaximo((short) (2 * Short.parseShort(dimensoes[2].trim())));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void lerMatriz()
    {
        this.getDimensoes();
        // Gera a matriz e preenche a mesma lendo a partir do arquivo
        this.Documentos =  new Integer[this.getnColunas()];
        this.Termos = new String[this.getnLinhas()];
        this.matTermDoc = new short[this.getnLinhas()][this.getnColunas()];
        try
        {
            FileReader fileReader = new FileReader("c:\\analise\\matriz\\matriz1.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linhaMatriz =null;
            int numeroLinha = 0;
            String linha[];
            while ((linhaMatriz = bufferedReader.readLine()) != null)
            {
                linha = linhaMatriz.split(",");
                for (int numeroColuna = 0; numeroColuna <= this.getnColunas(); numeroColuna++)
                {
                        if ((numeroLinha==0) && (numeroColuna!=0))
                        {
                               this.Documentos[numeroColuna-1] = Integer.parseInt(linha[numeroColuna].replaceAll("\"", ""));
                        }

                        if((numeroLinha!=0) && (numeroColuna==0))
                        {
                                this.Termos[numeroLinha-1]=linha[numeroColuna];
                        }

                        if((numeroLinha!=0) && (numeroColuna!=0))
                        {
                                this.matTermDoc[numeroLinha-1][numeroColuna-1] = Short.parseShort(linha[numeroColuna]);
                        }
                }
                numeroLinha++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    public void atribuiPesoMatriz(Integer PESOS[])
    {
        System.out.println("Valor Máximo multiplicado por 2: " + this.getvalorMaximo());
        Integer MB_CODIGO;
        String metodoQuebrado[] = null;
        String classeQuebrada[] = null;
        Inflector palavra1 = new Inflector();
        String temporario="";

        String Metodo="", Classe="";

        //Percorre cada um dos documentos do corpus armazenados no vetor Documentos
        for (int numeroDocumento = 0; numeroDocumento <= this.Documentos.length-1; numeroDocumento++)
        {
            // Recupera o nome do metodo e classe do banco de dados
            MB_CODIGO = this.Documentos[numeroDocumento];
            try
            {
                this.comandoPreparado1.setInt(1, MB_CODIGO);
                this.comandoPreparado1.executeQuery();
                this.comandoPreparado1.clearParameters( );

                Resultado = comandoPreparado1.getResultSet();
                while (Resultado.next())
                {
                    Metodo = Resultado.getString(1);
                    stemmer.setCurrent(Metodo);
                    stemmer.stem();
                    Metodo = (stemmer.getCurrent()).toLowerCase();
                    Classe = Resultado.getString(2);
                    stemmer.setCurrent(Classe);
                    stemmer.stem();
                    Classe = (stemmer.getCurrent()).toLowerCase();

                    temporario="";
                    temporario = Resultado.getString(1).replaceAll("\\$", " ");
                    temporario = palavra1.underscore(temporario);
                    temporario = temporario.replaceAll("_", " ");
                    metodoQuebrado = temporario.split(" ");

                    temporario="";
                    temporario = Resultado.getString(2).replaceAll("\\$", " ");
                    temporario = palavra1.underscore(temporario);
                    temporario = temporario.replaceAll("_", " ");
                    classeQuebrada = temporario.split(" ");
                }
            } catch (SQLException ex)
            {
                Exceptions.printStackTrace(ex);
            }

            // Aplicando o processo de stemming no vetor de caracteres do nome do metodo para achar os fragmentos do metodo de na forma stemizada.
            if (metodoQuebrado.length > 1) {
                for (int j = 0; j <= metodoQuebrado.length - 1; j++)
                {
                    stemmer.setCurrent(metodoQuebrado[j]);
                    stemmer.stem();
                    metodoQuebrado[j] = (stemmer.getCurrent()).toLowerCase();
                }
            }

            // Aplicando o processo de stemming no vetor de caracteres do nome da classe para achar os fragmentos da classe de na forma stemizada.
            if (classeQuebrada.length > 1)
            {
                for (int j = 0; j <= classeQuebrada.length - 1; j++)
                {
                    stemmer.setCurrent(classeQuebrada[j]);
                    stemmer.stem();
                    classeQuebrada[j] = (stemmer.getCurrent()).toLowerCase();
                }
            }



            for(int termosMatriz=0; termosMatriz<=this.Termos.length-1; termosMatriz++)
            {
                String termoMatriz = ((Termos[termosMatriz].replaceAll("\"","")).trim()).toLowerCase();
                if(PESOS[0]==1)
                {
                    if (Metodo.equalsIgnoreCase(termoMatriz))
                    {
                        matTermDoc[termosMatriz][numeroDocumento] = (short) (matTermDoc[termosMatriz][numeroDocumento] + this.getvalorMaximo());
                    }
                }

                if(PESOS[1]==1)
                {
                    if (metodoQuebrado.length > 1)
                    {
                        for (int k = 0; k <= metodoQuebrado.length - 1; k++)
                        {
                            if (metodoQuebrado[k].equalsIgnoreCase(termoMatriz))
                            {
                                    matTermDoc[termosMatriz][numeroDocumento] = (short) (matTermDoc[termosMatriz][numeroDocumento] + this.getvalorMaximo());
                            }
                        }
                    }
                }

                if(PESOS[2]==1)
                {
                    if (Classe.equalsIgnoreCase(termoMatriz))
                    {
                        matTermDoc[termosMatriz][numeroDocumento] = (short) (matTermDoc[termosMatriz][numeroDocumento] + this.getvalorMaximo());
                    }
                }

                if(PESOS[3]==1)
                {
                    if (classeQuebrada.length > 1)
                    {
                        for (int k = 0; k <= classeQuebrada.length - 1; k++)
                        {
                            if (classeQuebrada[k].equalsIgnoreCase(termoMatriz))
                            {
                                    matTermDoc[termosMatriz][numeroDocumento] = (short) (matTermDoc[termosMatriz][numeroDocumento] + this.getvalorMaximo());
                            }
                        }
                    }
                }

            }

        }
    }

    public void persisteMatrizComPeso() throws IOException
    {
        //Apaga a matriz anterior com resultados antigos
        Arquivo matrizComPeso = new Arquivo("c:\\analise\\matriz\\matriz2.csv");
        matrizComPeso.deletarArquivo();

        //Cria um arquivo para armazena a matriz com pesos
        FileWriter fw = null;
        fw = new FileWriter("c:\\analise\\matriz\\matriz2.csv", true);

        // Gera a primeira linha com o numero de cada documento do corpus
        
        StringBuilder Linha0= new StringBuilder();
        Linha0.append("\"\",");
        for (int numeroDocumento = 0; numeroDocumento <= this.Documentos.length-1; numeroDocumento++)
        {
            if(numeroDocumento!=this.Documentos.length-1)
            {
                Linha0.append("\"");
                Linha0.append(this.Documentos[numeroDocumento]);
                Linha0.append("\",");
            }else
            {
                Linha0.append("\"");
                Linha0.append(this.Documentos[numeroDocumento]);
                Linha0.append("\"");
            }
        }
        Linha0.append("\n");
        // Grava a primeira linha no arquivo matriz2
        try
        {
            fw.write(Linha0.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Linha0=null;

        //Grava o restante da matriz2
        //String linhaArquivo = "";
        
        for(int termosMatriz=0; termosMatriz<=this.Termos.length-1; termosMatriz++)
        {
            StringBuilder linhaArquivo = new StringBuilder();
            linhaArquivo.append(Termos[termosMatriz]);
            linhaArquivo.append(",");

            for (int numeroDocumento = 0; numeroDocumento <= this.Documentos.length-1; numeroDocumento++)
            {
                if(numeroDocumento!=this.Documentos.length-1)
                {
                    linhaArquivo.append(matTermDoc[termosMatriz][numeroDocumento]);
                    linhaArquivo.append(",");
                }else
                {
                    linhaArquivo.append(matTermDoc[termosMatriz][numeroDocumento]);
                }
            }
            linhaArquivo.append("\n");
            try
            {
                fw.write(linhaArquivo.toString());
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            linhaArquivo = null;
        }
        fw.close();
        fw=null;
    }
}


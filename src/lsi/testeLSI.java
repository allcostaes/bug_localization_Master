package lsi;

import bugs.bugCorrigido;
import arquivos.Diretorio;
import arquivos.Corpus;
import arquivos.Arquivo;
import java.sql.SQLException;
import java.sql.Statement;
import bdados.Conexao;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;

public class testeLSI
{
    private Conexao Conexao;
    private Statement Comando1, Comando2;
    private PreparedStatement comandoPreparado1;
    private PreparedStatement comandoPreparado2;
    private PreparedStatement comandoPreparado3;
    private PreparedStatement comandoPreparado4;
    private ResultSet Resultado1, Resultado2;
    private Integer TL_CODIGO;
    private Integer BG_CODIGO;
    private Integer TL_VALORK;
    private Integer TL_PESOMETODO;
    private Integer TL_PESOFRAGMETODO;
    private Integer TL_PESOCLASSE;
    private Integer TL_PESOFRAGCLASSE;
    private Integer TL_STOPWORDS;
    private Integer TL_STOPWORDS_OLD;
    private String TL_PESQUISA;
    private Integer TL_CORPUS;
    private bugCorrigido bugCorrigido;
    private Integer corpusPersistido;
    private Integer stopwordsPersistido;

    public testeLSI()
    {
        Conexao = new Conexao();
        try
        {
            Comando1 = Conexao.conexao.createStatement();
            Comando2 = Conexao.conexao.createStatement();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
           this.comandoPreparado1 = this.Conexao.conexaoCommitFalse.prepareStatement("INSERT INTO \"RESULTADO_TESTELSI\" (\"TL_CODIGO\",\"RL_CORRELACAO\",\"MB_CODIGO\") VALUES (?,?,?)");
        } catch (SQLException ex)
        {
           Exceptions.printStackTrace(ex);
        }

        try
        {
           this.comandoPreparado2 = this.Conexao.conexao.prepareStatement("SELECT \"MB_CODIGO\"  FROM \"BUGS_CORRIGIDOS\"  WHERE \"BG_CODIGO\" = ? ");
        } catch (SQLException ex)
        {
           Exceptions.printStackTrace(ex);
        }

        try
        {
           this.comandoPreparado3 = this.Conexao.conexao.prepareStatement("SELECT \"MB_CODIGO\"  FROM \"RESULTADO_TESTELSI\"  WHERE \"TL_CODIGO\" = ? ORDER BY  \"RL_CORRELACAO\" DESC ");
        } catch (SQLException ex)
        {
           Exceptions.printStackTrace(ex);
        }

        try
        {
           this.comandoPreparado4 = this.Conexao.conexao.prepareStatement("UPDATE \"TESTELSI\" SET \"TL_MEDIAPRECISAO\"=?  WHERE \"TL_CODIGO\" =? ");
        } catch (SQLException ex)
        {
           Exceptions.printStackTrace(ex);
        }

         bugCorrigido = new bugCorrigido();
         
    }

    public void setBG_CODIGO(Integer BG_CODIGO)
    {
        this.BG_CODIGO = BG_CODIGO;
    }

    public void setTL_VALORK(Integer TL_VALORK)
    {
        this.TL_VALORK = TL_VALORK;
    }

    public void setTL_PESOMETODO(Integer TL_PESOMETODO)
    {
        this.TL_PESOMETODO = TL_PESOMETODO;
    }

    public void setTL_PESOFRAGMETODO(Integer TL_PESOFRAGMETODO)
    {
        this.TL_PESOFRAGMETODO = TL_PESOFRAGMETODO;
    }

    public void setTL_PESOCLASSE(Integer TL_PESOCLASSE)
    {
        this.TL_PESOCLASSE = TL_PESOCLASSE;
    }

    public void setTL_PESOFRAGCLASSE(Integer TL_PESOFRAGCLASSE)
    {
        this.TL_PESOFRAGCLASSE = TL_PESOFRAGCLASSE;
    }

    public void setTL_STOPWORDS(Integer TL_STOPWORDS)
    {
        this.TL_STOPWORDS = TL_STOPWORDS;
    }

    public void setTL_STOPWORDS_OLD(Integer TL_STOPWORDS_OLD)
    {
        this.TL_STOPWORDS_OLD = TL_STOPWORDS_OLD;
    }


    public void setTL_PESQUISA(String TL_PESQUISA)
    {
        this.TL_PESQUISA = TL_PESQUISA;
    }


    public void setTL_CODIGO(Integer TL_CODIGO)
    {
        this.TL_CODIGO = TL_CODIGO;
    }

    public void setTL_CORPUS(Integer TL_CORPUS)
    {
        this.TL_CORPUS = TL_CORPUS;
    }


    private void setCorpusPersistido(Integer corpusPersistido)
    {
        this.corpusPersistido=corpusPersistido;
    }

    private void setStopwordsPersistido(Integer stopwordsPersistido)
    {
        this.stopwordsPersistido=stopwordsPersistido;
    }


    private Integer getTL_VALORK()
    {
        return this.TL_VALORK;
    }

    private Integer getTL_PESOMETODO()
    {
        return this.TL_PESOMETODO;
    }

    private Integer getTL_PESOFRAGMETODO()
    {
        return this.TL_PESOFRAGMETODO;
    }

    private Integer getTL_PESOCLASSE()
    {
        return this.TL_PESOCLASSE;
    }

    private Integer getTL_PESOFRAGCLASSE()
    {
        return this.TL_PESOFRAGCLASSE;
    }

    private Integer getTL_STOPWORDS()
    {
        return this.TL_STOPWORDS;
    }

    public Integer getTL_STOPWORDS_OLD()
    {
        return this.TL_STOPWORDS_OLD;
    }

    private String getTL_PESQUISA()
    {
        return this.TL_PESQUISA;
    }

    private Integer getBG_CODIGO()
    {
        return this.BG_CODIGO;
    }

    public Integer getTL_CODIGO()
    {
        return this.TL_CODIGO;
    }

    public Integer getTL_CORPUS()
    {
        return this.TL_CORPUS;
    }

    private Integer getCorpusPersistido()
    {
        return this.corpusPersistido;
    }

    private Integer getStopwordsPersistido()
    {
        return this.stopwordsPersistido;
    }


    public void realizarTesteLSIIndividual()
    {
            //Gerando o resultado utilizando LSI
            Arquivo arquivoWorkspaceR = new Arquivo("C:\\Users\\user\\Documents\\NetBeansProjects\\JRLSI2\\.RData");
            arquivoWorkspaceR.deletarArquivo();
            arquivoWorkspaceR = null;
            //Variável responsável por forçar o primeiro processo de montagem da matriz termos documentos com o primeiro conjunto de stopwords
            this.setCorpusPersistido(0);
            this.setStopwordsPersistido(0);
            this.setTL_STOPWORDS_OLD(-1);

            System.out.println("TESTANDO PARÂMETROS: [K=" + this.getTL_VALORK() + "] "+
                    "[PESOMETODO=" + this.getTL_PESOMETODO() + "]   [PESOFRAGMETODO=" + this.getTL_PESOFRAGMETODO() + "]   " +
                    "[PESOCLASSE=" + this.getTL_PESOCLASSE() + "]   [PESOFRAGCLASSE=" +this.getTL_PESOFRAGCLASSE() + "]   " +
                    "[STOPWORDS=" + this.getTL_STOPWORDS() + "]  [CORPUS=" + this.getTL_CORPUS() + "]");
            cadastrar();
            persisteCorpusStopwords();
            persisteMatriz1();
            testarLSI();
            persisteResultadoLSI();
            calcularPrecisaoRevocacao();
    }



    public void realizarTesteLSITudo()
    {

            //String VALORESK[] = {"100","200","300"};
            //String PESOSMETODO[] = {"0","1"};
            //String PESOSFRAGMETODO[] = {"0","1"};
            //String PESOSCLASSE[] = {"0","1"};
            //String PESOSFRAGCLASSE[] = {"0","1"};
            /*  Variação das STOPWORDS
            1 - Todas - English+Java
            2 - Palavras English
            3 - Palavras Java
             */
            /*  Variação do CORPUS
                1- Todo o código fonte
                2- Código acionado na execução da feature
             * OBSERVAÇÃO: Mantido somente o código fonte acionado por questões
             * do tempo necessário para teste em todo o código fonte
             */
            //Variável responsável por forçar o primeiro processo de montagem da matriz termos documentos com o primeiro conjunto de stopwords
            this.setCorpusPersistido(0);
            this.setStopwordsPersistido(0);
            this.setTL_STOPWORDS_OLD(-1);



            try
            {
                String Selecao = "";
                Selecao = "SELECT * FROM \"COMBINACOES\""+
                "ORDER BY \"CB_STOPWORDS\" ASC, \"CB_VALORK\" DESC";  //ordenação importante para eficiência nos testes
                Comando1.executeQuery(Selecao);
                Resultado1 = Comando1.getResultSet();

                while (Resultado1.next())
                {
                    //Apagando o arquivo temporario que o R gera
                    Arquivo arquivoR = new Arquivo("C:\\Users\\user\\Documents\\NetBeansProjects\\JRLSI2\\.RData");
                    arquivoR.deletarArquivo();
                    arquivoR=null;

                    //JOptionPane.showMessageDialog(null,  "Anote o consumo de memória para K=300.",  "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    this.setTL_VALORK(Resultado1.getInt(2));
                    this.setTL_PESOMETODO(Resultado1.getInt(3));
                    this.setTL_PESOFRAGMETODO(Resultado1.getInt(4));
                    this.setTL_STOPWORDS(Resultado1.getInt(5));
                    this.setTL_CORPUS(Resultado1.getInt(6));
                    this.setTL_PESOCLASSE(Resultado1.getInt(7));
                    this.setTL_PESOFRAGCLASSE(Resultado1.getInt(8));

                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    imprimeHora();
                    System.out.println("TESTANDO PARÂMETROS: [K=" + Resultado1.getInt(2) + "] "+
                    "[PESOMETODO=" + Resultado1.getInt(3) + "]   [PESOFRAGMETODO=" + Resultado1.getInt(4) + "]   " +
                    "[PESOCLASSE=" + Resultado1.getInt(7) + "]   [PESOFRAGCLASSE=" + Resultado1.getInt(8) + "]   " +
                    "[STOPWORDS=" + Resultado1.getInt(5) + "]  [CORPUS=" + Resultado1.getInt(6) + "]");
                    cadastrar();
                    persisteCorpusStopwords();
                    persisteMatriz1();
                    testarLSI();
                    persisteResultadoLSI();
                    calcularPrecisaoRevocacao();
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                }
            } catch (SQLException e)
            {
                System.out.println("SQL Exception");
                e.printStackTrace();
            }
    }
    
    public void imprimeHora()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Current Date Time : " + dateFormat.format(date));
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
    }


    public void cadastrar()
    {
        String Inclusao = "INSERT INTO \"TESTELSI\" (\"BG_CODIGO\",\"TL_VALORK\",\"TL_PESOMETODO\",\"TL_PESOFRAGMETODO\",\"TL_PESOCLASSE\",\"TL_PESOFRAGCLASSE\",\"TL_PESQUISA\",\"TL_STOPWORDS\",\"TL_CORPUS\" ) VALUES " +
             "("+  this.getBG_CODIGO() + "," +this.getTL_VALORK() + "," + this.getTL_PESOMETODO() + "," + this.getTL_PESOFRAGMETODO() + "," +
             this.getTL_PESOCLASSE() + "," + this.getTL_PESOFRAGCLASSE() + ",'" +
             this.getTL_PESQUISA() + "'," + this.getTL_STOPWORDS() + "," + this.getTL_CORPUS() + ") " +
             "RETURNING  \"TL_CODIGO\"";
        //System.out.println(Inclusao);
        try
        {
            Comando2.execute(Inclusao);
            Resultado2 = Comando2.getResultSet();
            Resultado2.next();
            this.setTL_CODIGO(Resultado2.getInt("TL_CODIGO"));
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }


   public void persisteCorpusStopwords()
    {
        if(this.getStopwordsPersistido()==0)
        {
            Stopwords Stopwords = new Stopwords();
            Stopwords.geraStopwords(this.getTL_PESQUISA());
            Stopwords = null;
            this.setStopwordsPersistido(1);
        }
        // Cria o corpus para a pesquisa somente uma vez
        if(this.getCorpusPersistido()==0)
        {
            // Construindo o corpus para a pesquisa
            Diretorio Diretorio = new Diretorio();
            Diretorio.setNome("c:\\analise\\corpus\\");
            Diretorio.apagaTodosArquivos();
            Diretorio=null;

            Corpus corpoTexto = new Corpus();
            corpoTexto.setBG_CODIGO(this.getBG_CODIGO());
            corpoTexto.setPesquisa(this.getTL_PESQUISA());
            switch (this.getTL_CORPUS())
            {
                    case 1:   corpoTexto.constroiCorpusCodigoFonte();  break;
                    case 2:   corpoTexto.constroiCorpusRastro();  break;
                    case 3:   corpoTexto.constroiCorpusRastroComGrep();  break;
            }
            corpoTexto=null;
            this.setCorpusPersistido(1);
        }
    }

    private void persisteMatriz1()
    {
        // Se o conjunto de Stopwords não varia não entra neste laço e econmiza bastante tempo na rotina textmatrix do R que conta a frequencia dos termos nos documentos
        // Inicialmente TL_STOPWORDS_OLD é setado para -1
        String nomeArquivo;
        if(this.getTL_STOPWORDS()!= this.getTL_STOPWORDS_OLD())
        {
                System.out.println("Gerando Matriz 1 - Novo conjunto de Stopwords ");
                System.out.println("Novo Conjunto de Stopwords em teste:" + this.getTL_STOPWORDS());
                //Gerando o resultado utilizando LSI
                Arquivo arquivoMatriz1 = new Arquivo("C:\\analise\\matriz\\matriz1.csv");
                arquivoMatriz1.deletarArquivo();
                arquivoMatriz1=null;

                nomeArquivo = "c:\\analise\\cmdr\\comandos1.r";
                StringBuilder comandos1R = new StringBuilder();
                // Gerando os comandos do R dinamicamente
                try
                {
                    FileWriter fw = new FileWriter(nomeArquivo, false);
                    comandos1R.append("library(\"lsa\")");
                    comandos1R.append("\n");
                    switch (this.getTL_STOPWORDS())  // Importante a ordem das combinações aqui. Observe o ORDER BY STOPWORDS.
                    {
                        case 1:   comandos1R.append("vetor_stopwords = c(t(read.csv(\"c:/analise/stopwords/stopwords_en+jv.txt\",header=F,sep=\" \"))) \n"); break;
                        case 2:   comandos1R.append("vetor_stopwords = c(t(read.csv(\"c:/analise/stopwords/stopwords_en.txt\",header=F,sep=\" \"))) \n"); break;
                        case 3:   comandos1R.append("vetor_stopwords = c(t(read.csv(\"c:/analise/stopwords/stopwords_jv.txt\",header=F,sep=\" \"))) \n"); break;
                    }
                    comandos1R.append("trm = textmatrix(\"C:/analise/corpus\", stemming=TRUE, language=\"english\", minWordLength=3, minDocFreq=1, stopwords=vetor_stopwords, vocabulary=NULL) \n");
                    comandos1R.append("write(c(attributes(trm)$dim, max(trm)),,file=\"C:/analise/matriz/dimensoes_matriz1.csv\") \n");
                    comandos1R.append("write.csv(trm,file=\"C:/analise/matriz/matriz1.csv\")  \n");
                    fw.write(comandos1R.toString());
                    fw.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                    System.exit(-1);
                }
                comandos1R = null;
                // Executando os comandos R preliminares de acordo com a configuracao acima.
                try
                {
                    Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos1.r");
                    p.waitFor();
                    //System.out.println(p.exitValue());
                } catch (Exception err)
                {
                    err.printStackTrace();
                }

        } 
        this.setTL_STOPWORDS_OLD(this.getTL_STOPWORDS());
    }


    public void testarLSI()
    {
        String nomeArquivo;
        // Le a matriz de termos documentos gerada pelo R e atribui pesos a ela conforme parametros do testeLSI
        Matriz matrizTermosDocumentos = new Matriz();
        matrizTermosDocumentos.lerMatriz();

        Integer PESOS[] = { this.getTL_PESOMETODO(), this.getTL_PESOFRAGMETODO(), this.getTL_PESOCLASSE(), this.getTL_PESOFRAGCLASSE()};

        matrizTermosDocumentos.atribuiPesoMatriz(PESOS);

        
        try
        {
            matrizTermosDocumentos.persisteMatrizComPeso();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        matrizTermosDocumentos=null;



        Arquivo arquivoResultado = new Arquivo("C:\\analise\\resultado\\resultado.csv");
        arquivoResultado.deletarArquivo();
        arquivoResultado=null;


        // Gravando a pesquisa no arquivo para uso posterior
        nomeArquivo = "c:\\analise\\pesquisa\\pesquisa.txt";
        try
        {
            FileWriter fw = new FileWriter(nomeArquivo, false);
            fw.write(this.getTL_PESQUISA() + "\n");
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        // Gerando os comandos do R dinamicamente - segunda parte
        nomeArquivo = "c:\\analise\\cmdr\\comandos2.r";
        StringBuilder comandos2R = new StringBuilder();
        try
        {
            FileWriter fw = new FileWriter(nomeArquivo, false);
            comandos2R.append("library(\"lsa\") \n");
            comandos2R.append("trm1 = read.csv(\"c:/analise/matriz/matriz2.csv\",sep=\",\",header=TRUE,row.names=1) \n");

            comandos2R.append("trm1 = as.matrix(trm1) \n");
            comandos2R.append("trm1 = as.textmatrix(trm1) \n");
            comandos2R.append("trm1 = lw_logtf(trm1) \n");

            //Esquemas de peso alternativos tentados sem grande melhorias na precisão dos testes
            //comandos2R.append("trm1 = lw_logtf(trm1)*entropy(trm1) \n");
            //comandos2R.append("trm1 = lw_logtf(trm1) * gw_idf(trm1) \n"); ruim
            //comandos2R.append("trm1 = lw_logtf(trm1)*gw_idf(trm1)*entropy(trm1) \n");
            //comandos2R.append("trm1 = lw_logtf(trm1)*entropy(trm1)* gw_normalisation(trm1) \n");
            //comandos2R.append("trm1 = lw_logtf(trm1) * gw_gfidf(trm1) \n");
            //comandos2R.append("trm1 = gw_normalisation(trm1)\n");  não funciona sozinho
            //comandos2R.append("trm1 = lw_logtf(trm1) * gw_idf(trm1) \n"); ruim
            //comandos2R.append("trm1 =   lw_logtf(trm1)*gw_idf(trm1)  \n");
            //comandos2R.append("trm1 = lw_logtf(trm1) * gw_idf(trm1) * gw_normalisation(trm1)\n");

            comandos2R.append("space = lsa(trm1, dims =");
            comandos2R.append(this.getTL_VALORK());
            comandos2R.append(")");
            comandos2R.append("\n");
            comandos2R.append("trm_red = as.textmatrix(space) \n");
            comandos2R.append("tem = textmatrix(\"C:/analise/pesquisa\", minWordLength=3, vocabulary=rownames(trm1),stemming=TRUE) \n");
            //comandos2R.append("tem = lw_logtf(tem) \n");
            comandos2R.append("tem_red = fold_in(tem, space) \n");
            comandos2R.append("cors <- cor(tem_red[,\"pesquisa.txt\"], trm_red,  method=\"pearson\") \n");
            comandos2R.append("write.table(cors[1,],file=\"C:/analise/resultado/resultado.csv\") \n");
            fw.write(comandos2R.toString());
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        comandos2R = null;
        try
        {
           Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos2.r");
            p.waitFor();
            //System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
        //planilhaResultado planilhaResultado = new planilhaResultado();
        //planilhaResultado.abreDocumento();
    }


    public void persisteResultadoLSI()
    {
        // Le os resultados obtidos no teste LSI do arquivo resultado.csv
        try
        {
            FileReader fileReader = new FileReader("c:\\analise\\resultado\\resultado.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linhaResultado, resultadoLSI[], RL_CORRELACAO;
            Integer contador=1;

            while ((linhaResultado = bufferedReader.readLine()) != null)
            {
                if((contador!=1))  //exclui a primeira linha do arquivo resultado.csv gerado pelo R que contem o caracter x que não interessa
                {
                    resultadoLSI = linhaResultado.split(" ");
                    Integer MB_CODIGO = Integer.parseInt(  (resultadoLSI[0].replaceAll("\"", "")).replaceAll("X","")  );
                    RL_CORRELACAO = resultadoLSI[1];
                    try
                    {
                        this.comandoPreparado1.setInt(1, this.getTL_CODIGO());
                        this.comandoPreparado1.setDouble(2,  Double.parseDouble(RL_CORRELACAO));
                        this.comandoPreparado1.setInt(3, MB_CODIGO);
                        this.comandoPreparado1.execute( );
                        this.comandoPreparado1.clearParameters( );
                    } catch (SQLException ex)
                    {
                        Exceptions.printStackTrace(ex);
                    }
                }
                contador++;
            }
            try
            {
                this.Conexao.conexaoCommitFalse.commit();
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public void calcularPrecisaoRevocacao()
    {
        //Seleciona os metodos corrigidos na correção do bug e armazena dentro de um Arraylist
        ArrayList<Integer> bugsCorrigidos = new ArrayList<Integer>();
        try
        {
            try
            {
                this.comandoPreparado2.setInt(1, this.getBG_CODIGO());
                this.comandoPreparado2.executeQuery();
                this.comandoPreparado2.clearParameters( );
            } catch (SQLException ex)
            {
                Exceptions.printStackTrace(ex);
            }
            Resultado2 = this.comandoPreparado2.getResultSet();
            while (Resultado2.next())
            {
                bugsCorrigidos.add(Resultado2.getInt(1));
            }
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }

        //Seleciona o resultado do teste LSI já rankeando para cálculo de precisão e revocação
        Double Recuperados=0.0, recuperadosRelevantes=0.0;
        Double PR_PRECISAO=0.0,PR_REVOCACAO=0.0,TL_MEDIAPRECISAO=0.0;
        try
        {
            try
            {
                this.comandoPreparado3.setInt(1, this.getTL_CODIGO());
                this.comandoPreparado3.executeQuery();
                this.comandoPreparado3.clearParameters( );
             } catch (SQLException ex)
            {
                Exceptions.printStackTrace(ex);
            }

            Resultado2 = comandoPreparado3.getResultSet();
            while (Resultado2.next())
            {
                Recuperados= Recuperados+1.0;
                for(int i=0; i<=bugsCorrigidos.size()-1; i++)
                {
                    if(Resultado2.getInt(1)==bugsCorrigidos.get(i))
                      {
                          recuperadosRelevantes=recuperadosRelevantes+1.0;
                          PR_PRECISAO=recuperadosRelevantes/Recuperados;
                          TL_MEDIAPRECISAO = TL_MEDIAPRECISAO + PR_PRECISAO;
                          PR_REVOCACAO=recuperadosRelevantes/bugsCorrigidos.size();
                                                                      // COD TESTELSI,               COD METBRUTO,                    POSICAO RANK,                       PRECISAO,               REVOCACAO
                          this.bugCorrigido.persistePrecisaoRevocacao(this.getTL_CODIGO().toString(),bugsCorrigidos.get(i).toString(),String.valueOf(Resultado2.getRow()), PR_PRECISAO.toString(), PR_REVOCACAO.toString());
                      }
                }
            }
         } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        TL_MEDIAPRECISAO = TL_MEDIAPRECISAO/recuperadosRelevantes;

        try
        {
             this.comandoPreparado4.setDouble(1, TL_MEDIAPRECISAO);
             this.comandoPreparado4.setInt(2, this.getTL_CODIGO());
             this.comandoPreparado4.execute();
             this.comandoPreparado4.clearParameters( );
        } catch (SQLException ex)
        {
             Exceptions.printStackTrace(ex);
        }
    }
}

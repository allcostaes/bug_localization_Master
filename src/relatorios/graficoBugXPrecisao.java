package relatorios;

import bdados.Conexao;
import utilidades.Estatistica;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.openide.util.Exceptions;
import java.text.DecimalFormat;

public class graficoBugXPrecisao
{
    private Conexao Conexao;
    private Statement Comando;
    private ResultSet Resultado;
    private ArrayList<String[]> pontosGrafico;
    FileWriter fw;
    private String TL_VALORK;
    private String TL_PESOMETODO;
    private String TL_PESOFRAGMETODO;

    private String TL_PESOCLASSE;
    private String TL_PESOFRAGCLASSE;

    private Integer TL_STOPWORDS;
    private Integer Software;

    public graficoBugXPrecisao()
    {
      Conexao = new Conexao();
        try
        {
            Comando = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      pontosGrafico = new ArrayList<String[]>();

    }


    public void setTL_VALORK(String TL_VALORK)
    {
        this.TL_VALORK = TL_VALORK;
    }
    public void setSoftware(Integer Software)
    {
        this.Software = Software;
    }

    public void setTL_PESOMETODO(String TL_PESOMETODO)
    {
        this.TL_PESOMETODO = TL_PESOMETODO;
    }
    public void setTL_PESOFRAGMETODO(String TL_PESOFRAGMETODO)
    {
        this.TL_PESOFRAGMETODO = TL_PESOFRAGMETODO;
    }
    public void setTL_PESOCLASSE(String TL_PESOCLASSE)
    {
        this.TL_PESOCLASSE = TL_PESOCLASSE;
    }
    public void setTL_PESOFRAGCLASSE(String TL_PESOFRAGCLASSE)
    {
        this.TL_PESOFRAGCLASSE = TL_PESOFRAGCLASSE;
    }

    public void setTL_STOPWORDS(Integer TL_STOPWORDS)
    {
        this.TL_STOPWORDS = TL_STOPWORDS;
    }

        private String getTL_VALORK()
    {
        return this.TL_VALORK;
    }
    private String getTL_PESOMETODO()
    {
        return this.TL_PESOMETODO;
    }
    private String getTL_PESOFRAGMETODO()
    {
        return this.TL_PESOFRAGMETODO;
    }
    private String getTL_PESOCLASSE()
    {
        return this.TL_PESOCLASSE;
    }
    private String getTL_PESOFRAGCLASSE()
    {
        return this.TL_PESOFRAGCLASSE;
    }
    private Integer getTL_STOPWORDS()
    {
        return this.TL_STOPWORDS;
    }

    private Integer getSoftware()
    {
        return this.Software;
    }


/*
 * @Software
 * 1 - ArgoUml
 * 2-Jedit
 */


    public void plotarGraficos()
    {
        String VALORESK[] = {"100","200","300"};
        String PESOSMETODO[] = {"0","1"};
        String PESOSFRAGMETODO[] = {"0","1"};
        String PESOSCLASSE[] = {"0","1"};
        String PESOSFRAGCLASSE[] = {"0","1"};

        /*  Variação das STOPWORDS
        1 - Todas - English+Java
        2 - Palavras English
        3 - Palavras Java
         */
        Integer STOPWORDS[] = {1,2,3};
        /*  Variação do CORPUS
            1- Todo o código fonte
            2- Código acionado na execução da feature
         * OBSERVAÇÃO: Mantido somente o código fonte acionado por questões
         * do tempo necessário para teste em todo o código fonte
         */
        Integer CORPUS[] = {2};

        /*  Variação das MEDIDAS DE CORRELAÇÃO
        1 - pearson
        2 - kendall
        3 - spearman
         */
        Integer MEDIDASCORRELACAO[] = {1};


//        String VALORESK[] = {"300"};
//        String PESOSMETODO[] = {"0"};
//        String PESOSFRAGMETODO[] = {"0"};
//        Integer STOPWORDS[] = {1};
//        Integer CORPUS[] = {1,2};   // 1- Todo o código fonte    2- Código acionado na execução da feature


        String nomeArquivo = "c:\\analise\\cmdr\\comandos4.r";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("jpeg(\"c:/analise/resultado/precisaobug.jpg\",width = 1000, height = 11000, quality = 100)"+"\n");
            this.fw.write("par(mfrow=c(48,3))");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }



        for(int a=0;a<=VALORESK.length-1;a++)
        {
            for(int b=0;b<=PESOSMETODO.length-1;b++)
            {
                for(int c=0;c<=PESOSFRAGMETODO.length-1;c++)
                {
                    for(int d=0; d<=PESOSCLASSE.length-1;d++)
                    {
                        for(int e=0; e<=PESOSFRAGCLASSE.length-1;e++)
                        {
                            for(int f=0;f<=STOPWORDS.length-1;f++)
                            {
                                for(int g=0; g<=CORPUS.length-1;g++)
                                {
                                this.setTL_VALORK(VALORESK[a]);
                                this.setTL_PESOMETODO(PESOSMETODO[b]);
                                this.setTL_PESOFRAGMETODO(PESOSFRAGMETODO[c]);
                                this.setTL_PESOCLASSE(PESOSCLASSE[d]);
                                this.setTL_PESOFRAGCLASSE(PESOSFRAGCLASSE[e]);
                                this.setTL_STOPWORDS(STOPWORDS[f]);
                                selecionaPontos();
                                montaComandosR();
                                 }
                            }
                        }
                    }
                }
            }
        }

        try
        {
            this.fw.write("dev.off()" + "\n");
            this.fw.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        executaComandosR();
        visualizarGrafico();
    }

    public void selecionaPontos()
    {
        //Seleciona os metodos corrigidos na correção do bug e armazena dentro de um Arraylist
        this.pontosGrafico.removeAll(this.pontosGrafico);
        try {
            String Selecao = "";
            Selecao = " SELECT \"BG_CODIGO\" , \"TL_MEDIAPRECISAO\"   FROM \"TESTELSI\" "+
                      " WHERE (\"TL_VALORK\" = " + this.getTL_VALORK() +
                      ")  AND  (\"TL_PESOMETODO\" = " + this.getTL_PESOMETODO() + ") AND (\"TL_PESOFRAGMETODO\" = " + this.getTL_PESOFRAGMETODO() +
                      ")  AND  (\"TL_PESOCLASSE\" = " + this.getTL_PESOCLASSE() + ") AND (\"TL_PESOFRAGCLASSE\" = " + this.getTL_PESOFRAGCLASSE() +
                      ") AND (\"TL_STOPWORDS\" = " + this.getTL_STOPWORDS() + ") ";

             switch (this.getSoftware())
            {
                case 1:       Selecao = Selecao + " AND  \"BG_CODIGO\">=1  AND  \"BG_CODIGO\"<=10 ";  break;  // selecionou argouml
                case 2:       Selecao = Selecao + " AND  \"BG_CODIGO\">=11 AND  \"BG_CODIGO\"<=20 ";  break;  // selecionou jedit
            }

              Selecao = Selecao +" ORDER BY \"BG_CODIGO\"";
//            System.out.println(Selecao);
            Comando.execute(Selecao);
            Resultado = Comando.getResultSet();
            while (Resultado.next())
            {
                pontosGrafico.add(new String[]{String.valueOf(Resultado.getInt(1)),String.valueOf(Resultado.getDouble(2))});
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }


    public void montaComandosR()
    {
      // Gerando os comandos do R dinamicamente - segunda parte
        
        try
        {

            String Pontos[] = new String[2];
            String x="",y="";
            Double mediaPrecisao=0.0;
            double[] vetor = new double[pontosGrafico.size()];

            for(int i=0; i<=pontosGrafico.size()-1;i++)
            {
                Pontos = pontosGrafico.get(i);
                //System.out.println(Pontos[1]);
                mediaPrecisao = mediaPrecisao+Double.valueOf(Pontos[1]);
                vetor[i]=Double.parseDouble(Pontos[1]);
                if (i==0)
                {
                    x = x + Pontos[0];
                    y = y + Pontos[1];
                }else
                {
                    x = x + "," + Pontos[0];
                    y = y + "," + Pontos[1];
                }
            }
            mediaPrecisao = mediaPrecisao/pontosGrafico.size();
             //Instancia a classe utilitária
            Estatistica e = new Estatistica();
             e.setArray(vetor);

            String fmt = "0.000";
            DecimalFormat df = new DecimalFormat (fmt);
            String mediaFormatada = df.format (mediaPrecisao);
            String desvioPadraoFormatado = df.format(e.getDesvioPadrao());
            x = "c("+ x + ")";
            y = "c("+ y + ")";
            this.fw.write("\n\n");
            this.fw.write("x<-" + x + "\n");
            this.fw.write("y<-" + y + "\n");
            this.fw.write("par(pch=22, col=\"red\")" + "\n");
            String stopWords="";

             switch (this.getTL_STOPWORDS())
            {
                case 1:       stopWords="English+Java";  break;
                case 2:       stopWords="English";  break;
                case 3:       stopWords="Java";  break;
            }



            String titulo = "BUG X PRECISÃO\n"+
                    "[Média das precisões: "+ mediaFormatada +"\", \"]  ****  [Desvio Padrão:" +desvioPadraoFormatado+ "]\n"+
                    "[K: "+ this.getTL_VALORK()+"]  [PM: "+this.getTL_PESOMETODO()+"]  [PFM: "+ this.getTL_PESOFRAGMETODO()+"]   "+
                    "[PC: "+this.getTL_PESOCLASSE()+"]  [PFC: "+ this.getTL_PESOFRAGCLASSE()+"]   "+"[STPW:"+ stopWords +"]";


            this.fw.write("heading = paste(\"" + titulo + "\")\n");
                         switch (this.getSoftware())
            {
                case 1:       this.fw.write("plot(x,y,main=heading,xlab=\"Bugs\",ylab=\"Precisão\",ylim=c(0.0,1.0),xlim=c(0," + pontosGrafico.size() + "))"+"\n");  break;
                case 2:       int i = 10+pontosGrafico.size(); this.fw.write("plot(x,y,main=heading,xlab=\"Bugs\",ylab=\"Precisão\",ylim=c(0.0,1.0),xlim=c(10," + i + "))"+"\n");  break;
            }

            
            //this.fw.write("legend(\"bottom\", c(\"Média: "+ mediaFormatada +"\", \"Desvio Padrão:" +e.getDesvioPadrao() +
              //            ",[K: "+ this.getTL_VALORK()+"]  [PM: "+this.getTL_PESOMETODO()+"]  [PFM: "+this.getTL_PESOFRAGMETODO()+"]  [STPW:"+this.getTL_STOPWORDS()+"]"+
                //          "\"),pch = c(1, 19), bg =\"white\")" + "\n");
            this.fw.write("lines(x, y, type=\"c\")"+"\n");
            this.fw.write("\n\n");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private void executaComandosR()
    {
          // Executando os comandos R preliminares de acordo com a configuracao acima.
        try
        {
            Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos4.r");
            p.waitFor();
            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }


    private void visualizarGrafico()
    {
           // Vizualizando o gráfico Precisão versus Revocação em visualizador externo
        try
        {
            Process p = Runtime.getRuntime().exec("xnview c:\\analise\\resultado\\precisaobug.jpg");
        } catch (Exception err) {
            err.printStackTrace();
        }

    }

}

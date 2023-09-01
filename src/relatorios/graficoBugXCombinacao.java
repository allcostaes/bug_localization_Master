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

public class graficoBugXCombinacao
{
    private Conexao Conexao;
    private Statement Comando1;
    private Statement Comando2;
    private ResultSet Resultado1;
    private ResultSet Resultado2;
    private ArrayList<String[]> pontosGrafico;
    private FileWriter fw;
    private short TL_VALORK;
    private short BG_CODIGO;
    private short TL_PESOMETODO;
    private short TL_PESOFRAGMETODO;

    private short TL_PESOCLASSE;
    private short TL_PESOFRAGCLASSE;

    private short TL_STOPWORDS;
    private short Software;
    private int contador;
    public graficoBugXCombinacao()
    {
      Conexao = new Conexao();
        try
        {
            Comando1 = Conexao.conexao.createStatement();
            Comando2 = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      pontosGrafico = new ArrayList<String[]>();
    }

    public void setSoftware(short Software)
    {
        this.Software = Software;
    }
    public void setBG_CODIGO(short BG_CODIGO)
    {
        this.BG_CODIGO = BG_CODIGO;
    }
    public void setTL_VALORK(short TL_VALORK)
    {
        this.TL_VALORK = TL_VALORK;
    }
    public void setTL_PESOMETODO(short TL_PESOMETODO)
    {
        this.TL_PESOMETODO = TL_PESOMETODO;
    }
    public void setTL_PESOFRAGMETODO(short TL_PESOFRAGMETODO)
    {
        this.TL_PESOFRAGMETODO = TL_PESOFRAGMETODO;
    }
    public void setTL_PESOCLASSE(short TL_PESOCLASSE)
    {
        this.TL_PESOCLASSE = TL_PESOCLASSE;
    }
    public void setTL_PESOFRAGCLASSE(short TL_PESOFRAGCLASSE)
    {
        this.TL_PESOFRAGCLASSE = TL_PESOFRAGCLASSE;
    }
    public void setTL_STOPWORDS(short TL_STOPWORDS)
    {
        this.TL_STOPWORDS = TL_STOPWORDS;
    }

    private short getSoftware()
    {
        return this.Software;
    }
    private short getBG_CODIGO()
    {
        return this.BG_CODIGO;
    }
    private short getTL_VALORK()
    {
        return this.TL_VALORK;
    }
    private short getTL_PESOMETODO()
    {
        return this.TL_PESOMETODO;
    }
    private short getTL_PESOFRAGMETODO()
    {
        return this.TL_PESOFRAGMETODO;
    }
    private short getTL_PESOCLASSE()
    {
        return this.TL_PESOCLASSE;
    }
    private short getTL_PESOFRAGCLASSE()
    {
        return this.TL_PESOFRAGCLASSE;
    }
    private short getTL_STOPWORDS()
    {
        return this.TL_STOPWORDS;
    }

/*
 * @Softwares
 * 1 - ArgoUml
 * 2 - Jedit
 */


    public void plotarGraficos()
    {

        String nomeArquivo = "c:\\analise\\cmdr\\comandos4.r";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("jpeg(\"c:/analise/resultado/precisaobug.jpg\",width = 1500, height = 3000, quality = 100)"+"\n");
            this.fw.write("par(mfrow=c(10,1))");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        int bug=0;
        switch (this.getSoftware())
        {
            case 1: bug=1;  break;  // selecionou argouml
            case 2: bug=11;  break;  // selecionou jedit
        }
        for(int bugCodigo=bug;bugCodigo<=bug+9 ;bugCodigo++)
        {
            contador=1;
            this.pontosGrafico.removeAll(this.pontosGrafico);
            geraCombinacoesporBug(bugCodigo);
            montaComandosR();
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




    private void geraCombinacoesporBug(int bugCodigo)
    {
        StringBuilder Selecao = new StringBuilder();

        try
        {
            Selecao.append(" SELECT DISTINCT \"TL_VALORK\",\"TL_PESOMETODO\",\"TL_PESOFRAGMETODO\",\"TL_PESOCLASSE\",\"TL_PESOFRAGCLASSE\", \"TL_STOPWORDS\" ");
            Selecao.append(" FROM \"TESTELSI\"");
            Selecao.append(" ORDER BY \"TL_VALORK\" ASC");
            //Selecao.append(" ORDER BY \"TL_PESOMETODO\" ASC");
            //Selecao.append(" ORDER BY \"TL_PESOFRAGMETODO\" ASC");
            //Selecao.append(" ORDER BY \"TL_STOPWORDS\" ASC");
            //Selecao.append(" ORDER BY \"TL_PESOCLASSE\" ASC");
            //Selecao.append(" ORDER BY \"TL_PESOFRAGCLASSE\" ASC");
            System.out.println(Selecao.toString());
            Comando1.execute(Selecao.toString());
            Resultado1 = Comando1.getResultSet();
            int teste=0;
            while (Resultado1.next())
            {
                teste++;
                System.out.println(teste);
                this.setBG_CODIGO((short) bugCodigo);
                this.setTL_VALORK(Resultado1.getShort(1));
                this.setTL_PESOMETODO(Resultado1.getShort(2));
                this.setTL_PESOFRAGMETODO(Resultado1.getShort(3));
                this.setTL_PESOCLASSE(Resultado1.getShort(4));
                this.setTL_PESOFRAGCLASSE(Resultado1.getShort(5));
                this.setTL_STOPWORDS(Resultado1.getShort(6));
                selecionaPontosXY();


                StringBuilder SelecaoVariaveis = new StringBuilder();
                SelecaoVariaveis.append("| K:");
                SelecaoVariaveis.append(Resultado1.getShort(1));
                SelecaoVariaveis.append("| PM:");
                SelecaoVariaveis.append(Resultado1.getShort(2));
                SelecaoVariaveis.append("| PFM:");
                SelecaoVariaveis.append(Resultado1.getShort(3));
                SelecaoVariaveis.append("| PC:");
                SelecaoVariaveis.append(Resultado1.getShort(4));
                SelecaoVariaveis.append("| PFC:");
                SelecaoVariaveis.append(Resultado1.getShort(5));
                SelecaoVariaveis.append("| STW:");
                SelecaoVariaveis.append(Resultado1.getShort(6));
                System.out.println(SelecaoVariaveis.toString());

            }
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }

    }



    private void geraCombinacoesporBug1(int bugCodigo)
    {
        short VALORESK[] = {100,200,300};
        short PESOSMETODO[] = {0,1};
        short PESOSFRAGMETODO[] = {0,1};
        short PESOSCLASSE[] = {0,1};
        short PESOSFRAGCLASSE[] = {0,1};

        /*  Variação das STOPWORDS
        1 - Todas - English+Java
        2 - Palavras English
        3 - Palavras Java
         */
        short STOPWORDS[] = {1,2,3};
        /*  Variação do CORPUS
            1- Todo o código fonte
            2- Código acionado na execução da feature
         * OBSERVAÇÃO: Mantido somente o código fonte acionado por questões
         * do tempo necessário para teste em todo o código fonte
         */
        short CORPUS[] = {2};

        
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
                                                this.setBG_CODIGO((short) bugCodigo);
                                                this.setTL_VALORK(VALORESK[a]);
                                                this.setTL_PESOMETODO(PESOSMETODO[b]);
                                                this.setTL_PESOFRAGMETODO(PESOSFRAGMETODO[c]);
                                                this.setTL_PESOCLASSE(PESOSCLASSE[d]);
                                                this.setTL_PESOFRAGCLASSE(PESOSFRAGCLASSE[e]);
                                                this.setTL_STOPWORDS(STOPWORDS[f]);
                                                selecionaPontosXY();
                                             }
                                        }
                                    }
                                }
                            }
                        }
                    }

            
    }



    public void selecionaPontosXY()
    {
        StringBuilder Selecao = new StringBuilder();
        
        try
        {
            Selecao.append(" SELECT \"TL_MEDIAPRECISAO\"   FROM \"TESTELSI\" ");
            Selecao.append(" WHERE (\"TL_VALORK\" = ");
            Selecao.append(this.getTL_VALORK());
            Selecao.append(")  AND  (\"TL_PESOMETODO\" = ");
            Selecao.append(this.getTL_PESOMETODO());
            Selecao.append(") AND (\"TL_PESOFRAGMETODO\" = ");
            Selecao.append(this.getTL_PESOFRAGMETODO());
            Selecao.append(")  AND  (\"TL_PESOCLASSE\" = ");
            Selecao.append(this.getTL_PESOCLASSE());
            Selecao.append(") AND (\"TL_PESOFRAGCLASSE\" = ");
            Selecao.append(this.getTL_PESOFRAGCLASSE());
            Selecao.append(") AND (\"TL_STOPWORDS\" = ");
            Selecao.append(this.getTL_STOPWORDS());
            Selecao.append(") AND  (\"BG_CODIGO\"=");
            Selecao.append(this.getBG_CODIGO());
            Selecao.append(")");
            //System.out.println(Selecao.toString());
            Comando2.execute(Selecao.toString());
            Resultado2 = Comando2.getResultSet();
            while (Resultado2.next())
            {
                //pontosGrafico.add(new String[]{eixoX.toString(),String.valueOf(Resultado.getDouble(1))});
                pontosGrafico.add(new String[]{String.valueOf(contador),String.valueOf(Resultado2.getDouble(1))});
            }
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        contador++;

   }


    public void montaComandosR()
    {

        String Pontos[] = new String[2];
        String x="",y="";
        double[] vetor = new double[pontosGrafico.size()];
        try
        {
            for(int i=0; i<=pontosGrafico.size()-1;i++)
            {
                Pontos = pontosGrafico.get(i);
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

            x = "c("+ x + ")";
            y = "c("+ y + ")";
            this.fw.write("\n\n");
            this.fw.write("x<-" + x + "\n");
            this.fw.write("y<-" + y + "\n");
            this.fw.write("par(pch=22, col=\"red\")" + "\n");
            //String titulo = "TITULO";
            //this.fw.write("heading = paste(\"" + titulo + "\")\n");
            switch (this.getSoftware())
            {
                case 1:                                        this.fw.write("plot(x,y,type=\"h\",lty=1,main=heading,xlab=\"Combinações\",ylab=\"Precisão\",ylim=c(0.0,1.0),xlim=c(0," + pontosGrafico.size() + "))"+"\n");  break;
                case 2:       int i = 10+pontosGrafico.size(); this.fw.write("plot(x,y,type=\"h\",lty=1,main=heading,xlab=\"Combinações\",ylab=\"Precisão\",ylim=c(0.0,1.0),xlim=c(10," + i + "))"+"\n");  break;
            }
            //this.fw.write("legend(\"left\", c(\"teste\"))" + "\n");
            this.fw.write("mtext(\"BUG " + this.getBG_CODIGO() + "\", side=4,cex=2)" + "\n");
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
        } catch (Exception err)
        {
            err.printStackTrace();
        }
    }

}

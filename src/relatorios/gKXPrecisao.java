/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorios;

import bdados.Conexao;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.openide.util.Exceptions;

/**
 *
 * @author user
 */
public class gKXPrecisao
{

    private Conexao Conexao;
    private Statement Comando1, Comando2, Comando3;
    private ResultSet Resultado1, Resultado2;
    private FileWriter fw;
    private Integer Software;
    private Integer numerodaCombinacao;
    private int  variak100_200B, variak100_200I, variak100_200R, variak200_300B, variak200_300I, variak200_300R;
    private int contaMenorTrabalhok100, contaMenorTrabalhok200, contaMenorTrabalhok300;


    private void setnumerodaCombinacao(Integer numerodaCombinacao) {
        this.numerodaCombinacao = numerodaCombinacao;
    }

    private Integer getnumerodaCombinacao() {
        return this.numerodaCombinacao;
    }


    public void setSoftware(Integer Software) {
        this.Software = Software;
    }

    private Integer getSoftware() {
        return this.Software;
    }

    public gKXPrecisao() {
        Conexao = new Conexao();
        try {
            Comando1 = Conexao.conexao.createStatement();
            Comando2 = Conexao.conexao.createStatement();
            Comando3 = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        variak100_200B=0;
        variak100_200I=0;
        variak100_200R=0;
        variak200_300B=0;
        variak200_300I=0;
        variak200_300R=0;

        contaMenorTrabalhok100=0;
        contaMenorTrabalhok200=0;
        contaMenorTrabalhok300=0;
    }

    public void plotarGraficos() {

        montarComandosR();

        executaComandosR();
        visualizarGrafico();

    }

    public void montarComandosR()
    {
        String nomeArquivo = "c:/analise/cmdr/comandos_grafico.r";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("jpeg(\"c:/analise/resultado/grafico.jpg\",width = 1500, height = 20000, quality = 100)" + "\n");
            this.fw.write("par(mfrow=c(49,3))" + "\n");


        } catch (IOException e) {
            e.printStackTrace();
        }

        String Selecao = " SELECT DISTINCT \"CB_PESOMETODO\",\"CB_PESOFRAGMETODO\",\"CB_PESOCLASSE\",\"CB_PESOFRAGCLASSE\",\"CB_STOPWORDS\" FROM \"COMBINACOES\"";
        Selecao = Selecao+" ORDER BY \"CB_STOPWORDS\" ";
        try
        {
            Comando1.execute(Selecao);
            Resultado1 = Comando1.getResultSet();
        } catch (SQLException ex)
        {
            Exceptions.printStackTrace(ex);
        }

        try
        {
            this.setnumerodaCombinacao(0);
            while (Resultado1.next())
            {
                this.setnumerodaCombinacao(this.getnumerodaCombinacao()+1);
                try
                {
                    String titulo = "<PM=" + Resultado1.getInt(1) + "><PFM=" + Resultado1.getInt(2)
                    + "><PC=" + Resultado1.getInt(3) + ">\n<PFC=" + Resultado1.getInt(4)
                    + "><STW=" + Resultado1.getInt(5) + ">";
                    this.fw.write("heading = paste(\"" + titulo + "\")\n");
                    this.fw.write("plot(0,0,type=\"b\",lty=1,main=heading,xlab=\"Valor de K\",ylab=\"Precisão\",ylim=c(0.0,1.1),xlim=c(100,340))" + "\n");
                    this.fw.write("legend(\"topright\",c(\"BUG1\",\"BUG2\",\"BUG3\",\"BUG4\",\"BUG5\",\"BUG6\",\"BUG7\",\"BUG8\",\"BUG9\",\"BUG10\"),cex=1.0, col=1:10, pch=1:10,lty=1:10)"+ "\n");
                    String XY,Tertis;
                    XY = obtemXY(Resultado1.getInt(1), Resultado1.getInt(2), Resultado1.getInt(3), Resultado1.getInt(4), Resultado1.getInt(5));
                    this.fw.write(XY + "\n");
                    Tertis = obtemTertisK(Resultado1.getInt(1), Resultado1.getInt(2), Resultado1.getInt(3), Resultado1.getInt(4), Resultado1.getInt(5));
                    this.fw.write(Tertis + "\n");

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }catch (SQLException ex)
        {
            Exceptions.printStackTrace(ex);
        }


        try {
            this.fw.write(obtemPorcentagemVariacaoK());
            this.fw.write("dev.off()" + "\n");
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(contaMenorTrabalhok100);
        System.out.println(contaMenorTrabalhok200);
        System.out.println(contaMenorTrabalhok300);
    }



    private String obtemXY(Integer PM, Integer PFM, Integer PC, Integer PFC, Integer STW)
    {
        String Selecao="";
        String XY="";
        Selecao = " SELECT \"TL\".\"TL_VALORK\",\"TL\".\"TL_MEDIAPRECISAO\" , (SELECT MAX(\"PR\".\"PR_POSICAO\") FROM  \"PREC_REVOCACAO\" \"PR\" WHERE \"PR\".\"TL_CODIGO\"=\"TL\".\"TL_CODIGO\") AS \"MAX_POSICAO\"" +
                  " FROM \"TESTELSI\"  \"TL\"  WHERE  \"TL\".\"TL_PESOMETODO\"=" + PM + " AND  \"TL\".\"TL_PESOFRAGMETODO\"=" + PFM +
                  " AND  \"TL\".\"TL_PESOCLASSE\"=" + PC + " AND  \"TL\".\"TL_PESOFRAGCLASSE\"=" + PFC  + " AND  \"TL\".\"TL_STOPWORDS\"=" + STW;

        System.out.println(Selecao);

        if (this.getSoftware() == 1)
        {
           Selecao = Selecao + " AND \"BG_CODIGO\">=1  AND \"BG_CODIGO\"<=10";
        }

         if (this.getSoftware() == 2)
         {
            Selecao = Selecao + " AND \"BG_CODIGO\">=11  AND \"BG_CODIGO\"<=20";
         }
        Selecao = Selecao + "  ORDER BY \"BG_CODIGO\", \"TL_VALORK\" ";

        String x = "", y = "";
        String Legenda="";
        int unts_k100=0, unts_k200=0, unts_k300=0;
        try
        {
            Comando2.execute(Selecao);
            Resultado2 = Comando2.getResultSet();
            int contador=0,linhatipo=1;
            Double P1=0.0,P2=0.0,P3=0.0;
            while (Resultado2.next())
            {
                contador++;
                if(contador==1)   //contabiliza parametros para k = 100
                {
                    x = x + Resultado2.getInt(1) + ",";
                    y = y + Resultado2.getDouble(2) + ",";
                    P1=Resultado2.getDouble(2);
                    unts_k100 = unts_k100 + Resultado2.getInt(3);
                }

                if(contador==2)   //contabiliza parametros para k = 200
                {
                    x = x + Resultado2.getInt(1) + ",";
                    y = y + Resultado2.getDouble(2) + ",";
                    P2=Resultado2.getDouble(2);
                    unts_k200= unts_k200 + Resultado2.getInt(3);
                }

                if (contador==3)   //contabiliza parametros para k = 300
                {
                    
                    x = x + Resultado2.getInt(1);
                    y = y + Resultado2.getDouble(2);
                    P3=Resultado2.getDouble(2);
                    unts_k300= unts_k300 + Resultado2.getInt(3);
                    XY = XY + "points(c("+x+"),c("+y+"),type=\"b\",lty=2,col="+linhatipo+",pch=("+linhatipo+"))" + "\n";
                    contador=0;
                    x="";
                    y="";
                    linhatipo++;

                    System.out.println(P1+","+P2+","+P3);
                    if(P2<P1)           { variak100_200R++; }
                    if(P2.equals(P1))   { variak100_200I++; }
                    if(P2>P1)           { variak100_200B++; }

                    if(P3<P2)           { variak200_300R++; }
                    if(P3.equals(P2))   { variak200_300I++; }
                    if(P3>P2)           { variak200_300B++; }

                }
              }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }

        String nomeArquivo = "c:/analise/bdados/unts"+this.getnumerodaCombinacao()+".csv";
        String Unts = "\n\n";
        try
        {
            FileWriter fwunts;
            fwunts = new FileWriter(nomeArquivo, false);

            String temporario = "";
            temporario = temporario+"k100;k200;k300" + "\n";
            temporario = temporario +unts_k100 + ";" + unts_k200+ ";" + unts_k300  + "\n";
            fwunts.write(temporario);
            fwunts.close();
            fwunts = null;
            //System.out.println(temporario);
            Unts = Unts + "unts <- read.csv(\"C:/analise/bdados/unts" +this.getnumerodaCombinacao()+".csv\", header=T, sep=\";\")"+ "\n";
            Unts = Unts + "barplot(as.matrix(unts), main=\"Numero de Documentos analisados\", xlab= \"Unts\", ,ylim=c(0,1100), ylab= \"Frequencia\", beside=TRUE, col=rainbow(3))"+ "\n";
            Unts = Unts + "legend(\"topright\", c(\"k100 ["+unts_k100+"]\",\"k200  ["+unts_k200+"]\",\"k300  ["+unts_k300+"]\"), cex=1.3, bty=\"n\", fill=rainbow(3))"+ "\n";
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        contaMenorTrabalho(unts_k100, unts_k200, unts_k300);

        XY = XY + Unts;
        return XY;
    }

    private void contaMenorTrabalho(int unts_k100, int unts_k200, int unts_k300)
    {
        if ((unts_k100==unts_k200) && (unts_k200==unts_k300))
        {
            contaMenorTrabalhok100++;  contaMenorTrabalhok200++; contaMenorTrabalhok300++;
        }
        if ((unts_k100==unts_k200) && (unts_k100<unts_k300))
        {
            contaMenorTrabalhok100++;  contaMenorTrabalhok200++;
        }
        if ((unts_k100==unts_k300) && (unts_k100<unts_k200))
        {
            contaMenorTrabalhok100++;  contaMenorTrabalhok300++;
        }
        if ((unts_k200==unts_k300) && (unts_k200<unts_k100))
        {
            contaMenorTrabalhok200++;  contaMenorTrabalhok300++;
        }

        if ((unts_k100!=unts_k200) && (unts_k100!=unts_k300) && (unts_k200!=unts_k300))
        {
            if(unts_k200<unts_k100)
            {
                if(unts_k200<unts_k300)
                {
                    contaMenorTrabalhok200++;
                }else
                {
                    contaMenorTrabalhok300++;
                }
            }else
            {
                if(unts_k100<unts_k300)
                {
                    contaMenorTrabalhok100++;
                }else
                {
                    contaMenorTrabalhok300++;
                }
            }
        }
    }

    private String obtemTertisK(Integer PM, Integer PFM, Integer PC, Integer PFC, Integer STW)
    {
        String Selecao="";
        String Tertis="";
        Selecao = " SELECT \"TL_VALORK\", \"TL_MEDIAPRECISAO\" FROM \"TESTELSI\" WHERE \"TL_PESOMETODO\"=" + PM + " AND \"TL_PESOFRAGMETODO\"=" + PFM +
        " AND \"TL_PESOCLASSE\"=" + PC + " AND \"TL_PESOFRAGCLASSE\"=" + PFC  + " AND \"TL_STOPWORDS\"=" + STW;
        if (this.getSoftware() == 1)
        {
           Selecao = Selecao + " AND \"BG_CODIGO\">=1  AND \"BG_CODIGO\"<=10";
        }

         if (this.getSoftware() == 2)
         {
            Selecao = Selecao + " AND \"BG_CODIGO\">=11  AND \"BG_CODIGO\"<=20";
         }
        Selecao = Selecao + "  ORDER BY \"TL_MEDIAPRECISAO\" DESC";
        String x = "", y = "";
        String Legenda="";

        int contador=1,linhatipo=1,contaK300_T1=0, contaK200_T1=0, contaK100_T1=0,contaK300_T2=0, contaK200_T2=0, contaK100_T2=0,contaK300_T3=0, contaK200_T3=0, contaK100_T3=0;
        try
        {
            Comando2.execute(Selecao);
            Resultado2 = Comando2.getResultSet();
            System.out.println("----------------------------------------------------------------------");
            while (Resultado2.next())
            {
                
                //System.out.println(Resultado2.getInt(1)+"-"+Resultado2.getDouble(2) );
                if (contador>=1 && contador<=10)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 100: contaK100_T1++; break;
                        case 200: contaK200_T1++; break;
                        case 300: contaK300_T1++; break;
                    }
                }
                if (contador>=11 && contador<=20)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 100: contaK100_T2++; break;
                        case 200: contaK200_T2++; break;
                        case 300: contaK300_T2++; break;
                    }

                }
                if (contador>=21 && contador<=30)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 100: contaK100_T3++; break;
                        case 200: contaK200_T3++; break;
                        case 300: contaK300_T3++; break;
                    }
                }
                contador++;
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }


        String nomeArquivo = "c:/analise/bdados/tertis"+this.getnumerodaCombinacao()+".csv";
        try
        {
            FileWriter fwTertis;
            fwTertis = new FileWriter(nomeArquivo, false);

            String temporario = "";
            temporario = temporario+"T1;T2;T3" + "\n";
            temporario = temporario +contaK100_T1 + ";" + contaK100_T2+ ";" + contaK100_T3  + "\n";
            temporario = temporario +contaK200_T1 + ";" + contaK200_T2+ ";" + contaK200_T3  + "\n";
            temporario = temporario +contaK300_T1 + ";" + contaK300_T2+ ";" + contaK300_T3  + "\n";
            fwTertis.write(temporario);
            fwTertis.close();
            fwTertis = null;
            System.out.println(temporario);
            Tertis = Tertis +"tertis <- read.csv(\"C:/analise/bdados/tertis" +this.getnumerodaCombinacao()+".csv\", header=T, sep=\";\")"+ "\n";
            Tertis = Tertis +"barplot(as.matrix(tertis), main=\"Distribuição de K pelos Tertis\", xlab= \"Tertis\", ylab= \"Frequencia\", beside=TRUE, col=rainbow(3))"+ "\n";
            Tertis = Tertis + "legend(\"topright\", c(\"k100\",\"k200\",\"k300\"), cex=1.3, bty=\"n\", fill=rainbow(3))"+ "\n";
        } catch (IOException e)
        {
            e.printStackTrace();
        }
         //System.out.println(Tertis);
         return Tertis;
    }

    private String obtemPorcentagemVariacaoK()
    {
            String porcentagemVariacaoK = "valoresK <- c("+ variak100_200R+","+variak100_200I+","+variak100_200B+")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "colors <- c(\"white\",\"grey50\",\"black\")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "K_labels <- round(valoresK/sum(valoresK) * 100, 1)"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "K_labels <- paste(K_labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "pie(valoresK, main=\"Variação de K de 100 para 200\", col=colors, labels=K_labels, cex=1.3)"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "legend(\"topright\", c(\"RUIM\",\"INDIFERENTE \",\"BOM\"), cex=1.3, fill=colors)"+"\n";

            porcentagemVariacaoK = porcentagemVariacaoK + "valoresK <- c("+ variak200_300R+","+variak200_300I+","+variak200_300B+")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "colors <- c(\"white\",\"grey50\",\"black\")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "K_labels <- round(valoresK/sum(valoresK) * 100, 1)"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "K_labels <- paste(K_labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "pie(valoresK, main=\"Variação de K de 200 para 300\", col=colors, labels=K_labels, cex=1.3)"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "legend(\"topright\", c(\"RUIM\",\"INDIFERENTE \",\"BOM\"), cex=1.3, fill=colors)"+"\n";

            porcentagemVariacaoK = porcentagemVariacaoK + "valoresK <- c("+ contaMenorTrabalhok100+","+contaMenorTrabalhok200+","+contaMenorTrabalhok300+")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "colors <- c(\"white\",\"grey50\",\"black\")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "K_labels <- round(valoresK/sum(valoresK) * 100, 1)"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "K_labels <- paste(K_labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "pie(valoresK, main=\"Porcentagem de combinações em que a utilização\n de um k resultou em menos trabalho\", col=colors, labels=K_labels, cex=1.3)"+"\n";
            porcentagemVariacaoK = porcentagemVariacaoK + "legend(\"topright\", c(\"k=100\",\"k=200 \",\"k=300\"), cex=1.3, fill=colors)"+"\n";

            return porcentagemVariacaoK;
    }

    public void executaComandosR() {
        // Executando os comandos R preliminares de acordo com a configuracao acima.
        try {
            Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos_grafico.r");
            p.waitFor();
            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void visualizarGrafico() {
        // Vizualizando o gráfico Precisão versus Revocação em visualizador externo
        try {
            Process p = Runtime.getRuntime().exec("xnview c:\\analise\\resultado\\grafico.jpg");
        } catch (Exception err) {
            err.printStackTrace();
        }

    }
}

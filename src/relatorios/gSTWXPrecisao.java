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
public class gSTWXPrecisao
{

    private Conexao Conexao;
    private Statement Comando1, Comando2, Comando3;
    private ResultSet Resultado1, Resultado2;
    private FileWriter fw;
    private Integer Software;
    private Integer numerodaCombinacao;
    private int  variaSTW1_2B, variaSTW1_2I, variaSTW1_2R, variaSTW2_3B, variaSTW2_3I, variaSTW2_3R;
    private int contaMenorTrabalhoSTW1, contaMenorTrabalhoSTW2, contaMenorTrabalhoSTW3;


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

    public gSTWXPrecisao() {
        Conexao = new Conexao();
        try {
            Comando1 = Conexao.conexao.createStatement();
            Comando2 = Conexao.conexao.createStatement();
            Comando3 = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        variaSTW1_2B=0;
        variaSTW1_2I=0;
        variaSTW1_2R=0;
        variaSTW2_3B=0;
        variaSTW2_3I=0;
        variaSTW2_3R=0;

        contaMenorTrabalhoSTW1=0;
        contaMenorTrabalhoSTW2=0;
        contaMenorTrabalhoSTW3=0;
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

        String Selecao = " SELECT DISTINCT \"CB_VALORK\",\"CB_PESOMETODO\",\"CB_PESOFRAGMETODO\",\"CB_PESOCLASSE\",\"CB_PESOFRAGCLASSE\"  FROM \"COMBINACOES\"";

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
                    String titulo = "<K=" + Resultado1.getInt(1) + "><PM=" + Resultado1.getInt(2)
                    + "><PFM=" + Resultado1.getInt(3) + ">\n<PC=" + Resultado1.getInt(4)
                    + "><PFC=" + Resultado1.getInt(5) + ">";
                    this.fw.write("heading = paste(\"" + titulo + "\")\n");
                    this.fw.write("plot(0,0,type=\"b\",lty=1,main=heading,xlab=\"Conjunto de Stopwords\",ylab=\"Precisão\",ylim=c(0.0,1.1),xlim=c(0,4))" + "\n");
                    this.fw.write("legend(\"topright\",c(\"BUG1\",\"BUG2\",\"BUG3\",\"BUG4\",\"BUG5\",\"BUG6\",\"BUG7\",\"BUG8\",\"BUG9\",\"BUG10\"),cex=1.0, col=1:10, pch=1:10,lty=1:10)"+ "\n");
                    String XY,Tertis;
                    XY = obtemXY(Resultado1.getInt(1), Resultado1.getInt(2), Resultado1.getInt(3), Resultado1.getInt(4), Resultado1.getInt(5));
                    this.fw.write(XY + "\n");
                    Tertis = obtemTertis(Resultado1.getInt(1), Resultado1.getInt(2), Resultado1.getInt(3), Resultado1.getInt(4), Resultado1.getInt(5));
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
            this.fw.write(obtemPorcentagemVariacao());
            this.fw.write("dev.off()" + "\n");
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private String obtemXY(Integer K, Integer PM, Integer PFM, Integer PC, Integer PFC)
    {
        String Selecao="";
        String XY="";
        Selecao = " SELECT \"TL\".\"TL_STOPWORDS\",\"TL\".\"TL_MEDIAPRECISAO\" , (SELECT MAX(\"PR\".\"PR_POSICAO\") FROM  \"PREC_REVOCACAO\" \"PR\" WHERE \"PR\".\"TL_CODIGO\"=\"TL\".\"TL_CODIGO\") AS \"MAX_POSICAO\"" +
                  " FROM \"TESTELSI\"  \"TL\"  WHERE   \"TL\".\"TL_VALORK\"=" + K +  " AND \"TL\".\"TL_PESOMETODO\"=" + PM + " AND  \"TL\".\"TL_PESOFRAGMETODO\"=" + PFM +
                  " AND  \"TL\".\"TL_PESOCLASSE\"=" + PC + " AND  \"TL\".\"TL_PESOFRAGCLASSE\"=" + PFC;

        

        if (this.getSoftware() == 1)
        {
           Selecao = Selecao + " AND \"BG_CODIGO\">=1  AND \"BG_CODIGO\"<=10";
        }

         if (this.getSoftware() == 2)
         {
            Selecao = Selecao + " AND \"BG_CODIGO\">=11  AND \"BG_CODIGO\"<=20";
         }
        Selecao = Selecao + "  ORDER BY \"BG_CODIGO\", \"TL_STOPWORDS\" ";


        System.out.println(Selecao);

        
        String x = "", y = "";
        String Legenda="";
        int unts_stw1=0, unts_stw2=0, unts_stw3=0;
        try
        {
            Comando2.execute(Selecao);
            Resultado2 = Comando2.getResultSet();
            int contador=0,linhatipo=1;
            Double P1=0.0,P2=0.0,P3=0.0;
            while (Resultado2.next())
            {
                contador++;
                if(contador==1)   //contabiliza parametros para stw = 1
                {
                    x = x + Resultado2.getInt(1) + ",";
                    y = y + Resultado2.getDouble(2) + ",";
                    P1=Resultado2.getDouble(2);
                    unts_stw1 = unts_stw1 + Resultado2.getInt(3);
                }

                if(contador==2)   //contabiliza parametros para stw = 2
                {
                    x = x + Resultado2.getInt(1) + ",";
                    y = y + Resultado2.getDouble(2) + ",";
                    P2=Resultado2.getDouble(2);
                    unts_stw2= unts_stw2 + Resultado2.getInt(3);
                }

                if (contador==3)   //contabiliza parametros para stw = 3
                {

                    x = x + Resultado2.getInt(1);
                    y = y + Resultado2.getDouble(2);
                    P3=Resultado2.getDouble(2);
                    unts_stw3= unts_stw3 + Resultado2.getInt(3);
                    XY = XY + "points(c("+x+"),c("+y+"),type=\"b\",lty=2,col="+linhatipo+",pch=("+linhatipo+"))" + "\n";
                    contador=0;
                    x="";
                    y="";
                    linhatipo++;

                    System.out.println(P1+","+P2+","+P3);
                    if(P2<P1)           { variaSTW1_2R++; }
                    if(P2.equals(P1))   { variaSTW1_2I++; }
                    if(P2>P1)           { variaSTW1_2B++; }

                    if(P3<P2)           { variaSTW2_3R++; }
                    if(P3.equals(P2))   { variaSTW2_3I++; }
                    if(P3>P2)           { variaSTW2_3B++; }

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
            temporario = temporario+"stw1;stw2;stw3" + "\n";
            temporario = temporario +unts_stw1 + ";" + unts_stw2+ ";" + unts_stw3  + "\n";
            fwunts.write(temporario);
            fwunts.close();
            fwunts = null;
            //System.out.println(temporario);
            Unts = Unts + "unts <- read.csv(\"C:/analise/bdados/unts" +this.getnumerodaCombinacao()+".csv\", header=T, sep=\";\")"+ "\n";
            Unts = Unts + "barplot(as.matrix(unts), main=\"Numero de Documentos analisados\", xlab= \"Unts\", ,ylim=c(0,1100), ylab= \"Frequencia\", beside=TRUE, col=rainbow(3))"+ "\n";
            Unts = Unts + "legend(\"topright\", c(\"stw1 ["+unts_stw1+"]\",\"stw2  ["+unts_stw2+"]\",\"stw3  ["+unts_stw3+"]\"), cex=1.3, bty=\"n\", fill=rainbow(3))"+ "\n";
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        contaMenorTrabalho(unts_stw1, unts_stw2, unts_stw3);

        XY = XY + Unts;
        return XY;
    }

    private void contaMenorTrabalho(int unts_stw1, int unts_stw2, int unts_stw3)
    {

        if ((unts_stw1==unts_stw2) && (unts_stw2==unts_stw3))
        {
            contaMenorTrabalhoSTW1++;  contaMenorTrabalhoSTW2++; contaMenorTrabalhoSTW3++;
        }
        if ((unts_stw1==unts_stw2) && (unts_stw1<unts_stw3))
        {
            contaMenorTrabalhoSTW1++;  contaMenorTrabalhoSTW2++;
        }
        if ((unts_stw1==unts_stw3) && (unts_stw1<unts_stw2))
        {
            contaMenorTrabalhoSTW1++;  contaMenorTrabalhoSTW3++;
        }
        if ((unts_stw2==unts_stw3) && (unts_stw2<unts_stw1))
        {
            contaMenorTrabalhoSTW2++;  contaMenorTrabalhoSTW3++;
        }

        if ((unts_stw1!=unts_stw2) && (unts_stw1!=unts_stw3) && (unts_stw2!=unts_stw3))
        {
            if(unts_stw2>unts_stw1)
            {
                if(unts_stw2<unts_stw3)
                {
                    contaMenorTrabalhoSTW2++;
                }else
                {
                    contaMenorTrabalhoSTW3++;
                }
            }else
            {
                if(unts_stw1<unts_stw3)
                {
                    contaMenorTrabalhoSTW1++;
                }else
                {
                    contaMenorTrabalhoSTW3++;
                }
            }
        }
    }

    private String obtemTertis(Integer K, Integer PM, Integer PFM, Integer PC, Integer PFC)
    {
        String Selecao="";
        String Tertis="";
        Selecao = " SELECT \"TL_STOPWORDS\", \"TL_MEDIAPRECISAO\" FROM \"TESTELSI\" WHERE  \"TL_VALORK\"=" + K + " AND \"TL_PESOMETODO\"=" + PM + " AND \"TL_PESOFRAGMETODO\"=" + PFM +
        " AND \"TL_PESOCLASSE\"=" + PC + " AND \"TL_PESOFRAGCLASSE\"=" + PFC;
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

        int contador=1,linhatipo=1,contaSTW3_T1=0, contaSTW2_T1=0, contaSTW1_T1=0,contaSTW3_T2=0, contaSTW2_T2=0, contaSTW1_T2=0,contaSTW3_T3=0, contaSTW2_T3=0, contaSTW1_T3=0;
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
                        case 1: contaSTW1_T1++; break;
                        case 2: contaSTW2_T1++; break;
                        case 3: contaSTW3_T1++; break;
                    }
                }
                if (contador>=11 && contador<=20)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 1: contaSTW1_T2++; break;
                        case 2: contaSTW2_T2++; break;
                        case 3: contaSTW3_T2++; break;
                    }

                }
                if (contador>=21 && contador<=30)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 1: contaSTW1_T3++; break;
                        case 2: contaSTW2_T3++; break;
                        case 3: contaSTW3_T3++; break;
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
            temporario = temporario +contaSTW1_T1 + ";" + contaSTW1_T2+ ";" + contaSTW1_T3  + "\n";
            temporario = temporario +contaSTW2_T1 + ";" + contaSTW2_T2+ ";" + contaSTW2_T3  + "\n";
            temporario = temporario +contaSTW3_T1 + ";" + contaSTW3_T2+ ";" + contaSTW3_T3  + "\n";
            fwTertis.write(temporario);
            fwTertis.close();
            fwTertis = null;
            System.out.println(temporario);
            Tertis = Tertis +"tertis <- read.csv(\"C:/analise/bdados/tertis" +this.getnumerodaCombinacao()+".csv\", header=T, sep=\";\")"+ "\n";
            Tertis = Tertis +"barplot(as.matrix(tertis), main=\"Distribuição de STW pelos Tertis\", xlab= \"Tertis\", ylab= \"Frequencia\", beside=TRUE, col=rainbow(3))"+ "\n";
            Tertis = Tertis + "legend(\"topright\", c(\"STW1\",\"STW2\",\"STW3\"), cex=1.3, bty=\"n\", fill=rainbow(3))"+ "\n";
        } catch (IOException e)
        {
            e.printStackTrace();
        }
         //System.out.println(Tertis);
         return Tertis;
    }

    private String obtemPorcentagemVariacao()
    {
            String porcentagemVariacao = "valores <- c("+ variaSTW1_2R+","+variaSTW1_2I+","+variaSTW1_2B+")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "colors <- c(\"white\",\"grey50\",\"black\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- round(valores/sum(valores) * 100, 1)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- paste(labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "pie(valores, main=\"Variação de STW de 1 para 2\", col=colors, labels=labels, cex=1.3)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "legend(\"topright\", c(\"RUIM\",\"INDIFERENTE \",\"BOM\"), cex=1.3, fill=colors)"+"\n";

            porcentagemVariacao = porcentagemVariacao + "valores <- c("+ variaSTW2_3R+","+variaSTW2_3I+","+variaSTW2_3B+")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "colors <- c(\"white\",\"grey50\",\"black\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- round(valores/sum(valores) * 100, 1)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- paste(labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "pie(valores, main=\"Variação de STW de 2 para 3\", col=colors, labels=labels, cex=1.3)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "legend(\"topright\", c(\"RUIM\",\"INDIFERENTE \",\"BOM\"), cex=1.3, fill=colors)"+"\n";

            porcentagemVariacao = porcentagemVariacao + "valores <- c("+ contaMenorTrabalhoSTW1+","+contaMenorTrabalhoSTW2+","+contaMenorTrabalhoSTW3+")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "colors <- c(\"white\",\"grey50\",\"black\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- round(valores/sum(valores) * 100, 1)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- paste(labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "pie(valores, main=\"Porcentagem de combinações em que a utilização\n de um STW resultou em menos trabalho\", col=colors, labels=labels, cex=1.3)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "legend(\"topright\", c(\"STW=Todas\",\"STW=English \",\"STW=Java\"), cex=1.3, fill=colors)"+"\n";

            return porcentagemVariacao;
    }


    private void mensuraMenorTrabalho()
    {

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

    public void visualizarGrafico()
    {
        // Vizualizando o gráfico Precisão versus Revocação em visualizador externo
        try {
            Process p = Runtime.getRuntime().exec("xnview c:\\analise\\resultado\\grafico.jpg");
        } catch (Exception err) {
            err.printStackTrace();
        }

    }
}

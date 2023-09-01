package relatorios;

import bdados.Conexao;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.openide.util.Exceptions;


public class  gPFMXPrecisao
{

    private Conexao Conexao;
    private Statement Comando1, Comando2, Comando3;
    private ResultSet Resultado1, Resultado2;
    private FileWriter fw;
    private Integer Software;
    private Integer numerodaCombinacao;
    private int varia0_1R, varia0_1I, varia0_1B;
    private int contaMenorTrabalho0, contaMenorTrabalho1;

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

    public  gPFMXPrecisao()
    {
        Conexao = new Conexao();
        try {
            Comando1 = Conexao.conexao.createStatement();
            Comando2 = Conexao.conexao.createStatement();
            Comando3 = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        varia0_1R=0;
        varia0_1I=0;
        varia0_1B=0;

        contaMenorTrabalho0=0;
        contaMenorTrabalho1=0;
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
            this.fw.write("jpeg(\"c:/analise/resultado/grafico.jpg\",width = 1500, height = 15000, quality = 100)" + "\n");
            this.fw.write("par(mfrow=c(37,6))" + "\n");


        } catch (IOException e) {
            e.printStackTrace();
        }

        String Selecao = " SELECT DISTINCT \"CB_VALORK\",\"CB_PESOMETODO\",\"CB_PESOCLASSE\",\"CB_PESOFRAGCLASSE\",\"CB_STOPWORDS\" FROM \"COMBINACOES\"";
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
                    + "><PC=" + Resultado1.getInt(3) + ">\n<PFC=" + Resultado1.getInt(4)
                    + "><STW=" + Resultado1.getInt(5) + ">";
                    this.fw.write("heading = paste(\"" + titulo + "\")\n");
                    this.fw.write("plot(0,0,type=\"b\",lty=1,main=heading,xlab=\"Peso do Fragmento do Método\",ylab=\"Precisão\",ylim=c(0.0,1.0),xlim=c(-0.2,1.5))" + "\n");
                    this.fw.write("legend(\"topright\",c(\"BUG1\",\"BUG2\",\"BUG3\",\"BUG4\",\"BUG5\",\"BUG6\",\"BUG7\",\"BUG8\",\"BUG9\",\"BUG10\"),cex=1.0, col=1:10, pch=1:10,lty=1:10)"+ "\n");
                    String XY,Tertis;
                    XY = obtemXY(Resultado1.getInt(1), Resultado1.getInt(2), Resultado1.getInt(3), Resultado1.getInt(4), Resultado1.getInt(5));
                    this.fw.write(XY + "\n");
                    Tertis = obtemQuartis(Resultado1.getInt(1), Resultado1.getInt(2), Resultado1.getInt(3), Resultado1.getInt(4), Resultado1.getInt(5));
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
        System.out.println(this.getnumerodaCombinacao());

        try {
            this.fw.write(obtemPorcentagemVariacao());
            this.fw.write("dev.off()" + "\n");
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private String obtemXY(Integer K, Integer PM, Integer PC, Integer PFC, Integer STW)
    {
        String Selecao="";
        String XY="";
        Selecao = " SELECT \"TL_PESOFRAGMETODO\", \"TL_MEDIAPRECISAO\", (SELECT MAX(\"PR\".\"PR_POSICAO\") FROM  \"PREC_REVOCACAO\" \"PR\" WHERE \"PR\".\"TL_CODIGO\"=\"TL\".\"TL_CODIGO\") AS \"MAX_POSICAO\"" +
        " FROM \"TESTELSI\"   \"TL\" WHERE  \"TL\".\"TL_VALORK\"=" + K + " AND  \"TL\".\"TL_PESOMETODO\"=" + PM +
        " AND  \"TL\".\"TL_PESOCLASSE\"=" + PC + " AND  \"TL\".\"TL_PESOFRAGCLASSE\"=" + PFC  + " AND  \"TL\".\"TL_STOPWORDS\"=" + STW;
        if (this.getSoftware() == 1)
        {
           Selecao = Selecao + " AND \"BG_CODIGO\">=1  AND \"BG_CODIGO\"<=10";
        }

         if (this.getSoftware() == 2)
         {
            Selecao = Selecao + " AND \"BG_CODIGO\">=11  AND \"BG_CODIGO\"<=20";
         }
        Selecao = Selecao + "  ORDER BY \"BG_CODIGO\", \"TL_PESOFRAGMETODO\" ";
        String x = "", y = "";
        String Legenda="";
        int unts_0=0, unts_1=0;
        try
        {
            Comando2.execute(Selecao);
            Resultado2 = Comando2.getResultSet();
            int contador=0,linhatipo=1;
            Double P1=0.0,P2=0.0;
            while (Resultado2.next())
            {
                contador++;
                if(contador==1)
                {
                    x = x + Resultado2.getInt(1) + ",";
                    y = y + Resultado2.getDouble(2) + ",";
                    P1=Resultado2.getDouble(2);
                    unts_0 = unts_0 + Resultado2.getInt(3);
                }

                if (contador==2)
                {

                    x = x + Resultado2.getInt(1);
                    y = y + Resultado2.getDouble(2);
                    P2=Resultado2.getDouble(2);
                    unts_1 = unts_1 + Resultado2.getInt(3);
                    XY = XY + "points(c("+x+"),c("+y+"),type=\"b\",lty=2,col="+linhatipo+",pch=("+linhatipo+"))" + "\n";
                    contador=0;
                    x="";
                    y="";
                    linhatipo++;

                    System.out.println(P1+","+P2);
                    if(P2<P1)           { varia0_1R++; }
                    if(P2.equals(P1))   { varia0_1I++; }
                    if(P2>P1)           { varia0_1B++; }
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
            temporario = temporario+"PFM=0;PFM=1" + "\n";
            temporario = temporario +unts_0 + ";" + unts_1+ "\n";
            fwunts.write(temporario);
            fwunts.close();
            fwunts = null;
            //System.out.println(temporario);
            Unts = Unts + "unts <- read.csv(\"C:/analise/bdados/unts" +this.getnumerodaCombinacao()+".csv\", header=T, sep=\";\")"+ "\n";
            Unts = Unts + "barplot(as.matrix(unts), main=\"Numero de Documentos analisados\", xlab= \"Unts\", ,ylim=c(0,1100), ylab= \"Documentos Analisados\", beside=TRUE, col=rainbow(2))"+ "\n";
            Unts = Unts + "legend(\"topright\", c(\"PFM = 0 ["+unts_0+"]\",\" PFM = 1  ["+unts_1+"]\"), cex=1.3, bty=\"n\", fill=rainbow(2))"+ "\n";
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        if(unts_0!=unts_1)
        {
            if(unts_0<unts_1)
            {
                contaMenorTrabalho0++;
            }else
            {
                contaMenorTrabalho1++;
            }
        }else
        {
            contaMenorTrabalho0++;
            contaMenorTrabalho1++;
        }



        XY = XY + Unts;
        return XY;
    }



    private String obtemQuartis(Integer K, Integer PM, Integer PC, Integer PFC, Integer STW)
    {
        String Selecao="";
        String Quartis="";
        Selecao = " SELECT \"TL_PESOFRAGMETODO\", \"TL_MEDIAPRECISAO\" FROM \"TESTELSI\" WHERE \"TL_VALORK\"=" + K + " AND \"TL_PESOMETODO\"=" + PM +
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

        int contador=1, conta0_Q1=0, conta1_Q1=0, conta0_Q2=0, conta1_Q2=0, conta0_Q3=0, conta1_Q3=0, conta0_Q4=0, conta1_Q4=0;
        try
        {
            Comando2.execute(Selecao);
            Resultado2 = Comando2.getResultSet();
            System.out.println("----------------------------------------------------------------------");
            while (Resultado2.next())
            {
                //System.out.println(Resultado2.getInt(1)+"-"+Resultado2.getDouble(2) );
                if (contador>=1 && contador<=5)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 0: conta0_Q1++; break;
                        case 1: conta1_Q1++; break;
                    }
                }
                if (contador>=6 && contador<=10)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 0: conta0_Q2++; break;
                        case 1: conta1_Q2++; break;
                    }

                }
                if (contador>=11 && contador<=15)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 0: conta0_Q3++; break;
                        case 1: conta1_Q3++; break;
                    }
                }

                if (contador>=16 && contador<=20)
                {
                    switch (Resultado2.getInt(1))
                    {
                        case 0: conta0_Q4++; break;
                        case 1: conta1_Q4++; break;
                    }
                }
                contador++;
            }
        } catch (SQLException ex)
        {
            Exceptions.printStackTrace(ex);
        }


        String nomeArquivo = "c:/analise/bdados/quartis"+this.getnumerodaCombinacao()+".csv";
        try
        {
            FileWriter fwTertis;
            fwTertis = new FileWriter(nomeArquivo, false);

            String temporario = "";
            temporario = temporario+"Q1;Q2;Q3;Q4" + "\n";
            temporario = temporario +conta0_Q1 + ";" + conta0_Q2+ ";" + conta0_Q3  + ";" + conta0_Q4  + "\n";
            temporario = temporario +conta1_Q1 + ";" + conta1_Q2+ ";" + conta1_Q3  + ";" + conta1_Q4  + "\n";
            fwTertis.write(temporario);
            fwTertis.close();
            fwTertis = null;
            //System.out.println(temporario);
            Quartis = Quartis + "quartis <- read.csv(\"C:/analise/bdados/quartis" +this.getnumerodaCombinacao()+".csv\", header=T, sep=\";\")"+ "\n";
            Quartis = Quartis + "barplot(as.matrix(quartis), main=\"Distribuição de PFM pelos Quartis\", xlab= \"Quartis\", ylab= \"Frequencia\", beside=TRUE, col=rainbow(2))"+ "\n";
            Quartis = Quartis + "legend(\"topright\", c(\"PFM0\",\"PFM1\"), cex=1.3, bty=\"n\", fill=rainbow(2))"+ "\n";


        } catch (IOException e)
        {
            e.printStackTrace();
        }
         //System.out.println(Quartis);
         return Quartis;
    }

    private String obtemPorcentagemVariacao()
    {
            String porcentagemVariacao = "valores <- c("+ varia0_1R+","+varia0_1I+","+varia0_1B+")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "colors <- c(\"white\",\"grey50\",\"black\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- round(valores/sum(valores) * 100, 1)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- paste(labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "pie(valores, main=\"Variação do Peso do Fragmento \n do Método de 0 para 1\", col=colors, labels=labels, cex=1.3)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "legend(\"topleft\", c(\"RUIM\",\"INDIFERENTE \",\"BOM\"), cex=1.2, fill=colors)"+"\n";

            porcentagemVariacao = porcentagemVariacao + "valores <- c("+ contaMenorTrabalho0+","+contaMenorTrabalho1 +")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "colors <- c(\"white\",\"grey50\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- round(valores/sum(valores) * 100, 1)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "labels <- paste(labels, \"%\", sep=\"\")"+"\n";
            porcentagemVariacao = porcentagemVariacao + "pie(valores, main=\"Porcentagem de combinações em que o menor Trabalho\n foi observado em cada valor de PFM\", col=colors, labels=labels, cex=1.3)"+"\n";
            porcentagemVariacao = porcentagemVariacao + "legend(\"topleft\", c(\"PFM=0\",\"PFM=1 \"), cex=1.2, fill=colors)"+"\n";

            return porcentagemVariacao;
    }

    public void executaComandosR()
    {
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

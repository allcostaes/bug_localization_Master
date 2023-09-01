
package relatorios;

import bdados.Conexao;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class graficoSituacaoKna3parte
{
    private Conexao Conexao;
    private Statement Comando;
    private ResultSet Resultado;
    private FileWriter fw;
    private Integer Software;

     public void setSoftware(Integer Software)
    {
        this.Software = Software;
    }


    private Integer getSoftware()
    {
        return this.Software;
    }


    public graficoSituacaoKna3parte()
    {
        Conexao = new Conexao();
        try
        {
            Comando = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void gerarDados()
    {
            Integer contador=1;
            Double k300_1P=0.0,k200_1P=0.0,k100_1P=0.0,k300_2P=0.0,k200_2P=0.0,k100_2P=0.0,k300_3P=0.0,k200_3P=0.0,k100_3P=0.0;
            try
            {
                String BG_INICIAL="", BG_FINAL="";
                switch (this.getSoftware())
            {
                case 1:    BG_INICIAL="1"; BG_FINAL="10";  break;  // selecionou argouml
                case 2:    BG_INICIAL="11"; BG_FINAL="20";  break;  // selecionou jedit
            }

                String Selecao = "";
                //Selecao = " SELECT \"CB_VALORK\" FROM \"COMB10\"("+Software+") ORDER BY \"CB_MEDIAPRECISAO\" DESC ";


                Selecao = " SELECT \"TL_VALORK\",\"TL_PESOMETODO\",\"TL_PESOFRAGMETODO\",\"TL_STOPWORDS\",\"TL_PESOCLASSE\",\"TL_PESOFRAGCLASSE\",SUM(\"NDOCS\") AS \"NUMDOCS\" FROM "+
                "("+
                " SELECT *, (SELECT max(\"PR_POSICAO\") FROM \"PREC_REVOCACAO\" WHERE \"TL_CODIGO\"=\"TL\".\"TL_CODIGO\") AS \"NDOCS\" "+
                " FROM \"TESTELSI\" \"TL\" "+
                " WHERE \"BG_CODIGO\">=" + BG_INICIAL + " AND \"BG_CODIGO\"<=" + BG_FINAL +
                " ORDER BY \"NDOCS\" "+
                ") AS CONSULTA "+
                " GROUP BY \"TL_VALORK\",\"TL_PESOMETODO\",\"TL_PESOFRAGMETODO\",\"TL_STOPWORDS\",\"TL_PESOCLASSE\",\"TL_PESOFRAGCLASSE\" "+
                " ORDER BY \"NUMDOCS\" ";
                System.out.println(Selecao);
                Comando.execute(Selecao);
                Resultado = Comando.getResultSet();
           
                while (Resultado.next())
                {

                    if (contador<=48)
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 100:   k100_1P++;  break;
                            case 200:   k200_1P++;  break;
                            case 300:   k300_1P++;  break;
                        }
                    }

                    if ((contador>=49) && (contador<=96))
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 100:   k100_2P++;  break;
                            case 200:   k200_2P++;  break;
                            case 300:   k300_2P++;  break;
                        }

                    }

                    if ((contador>=97) && (contador<=144))
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 100:   k100_3P++;  break;
                            case 200:   k200_3P++;  break;
                            case 300:   k300_3P++;  break;
                        }

                    }

                contador++;
                }
             } catch (SQLException e)
             {
                 System.out.println("SQL Exception");
                 e.printStackTrace();
             }


        String nomeArquivo = "c:/analise/bdados/graficoSituacaoKna3parte.csv";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("Frequencia;Valork;Parte3"+"\n");
            Double porcentagemna3Parte;

            porcentagemna3Parte = k100_1P/24;
            this.fw.write(porcentagemna3Parte+";100;1"+"\n");

            porcentagemna3Parte = k200_1P/24;
            this.fw.write(porcentagemna3Parte+";200;1"+"\n");

            porcentagemna3Parte = k300_1P/24;
            this.fw.write(porcentagemna3Parte+";300;1"+"\n");

            porcentagemna3Parte = k100_2P/24;
            this.fw.write(porcentagemna3Parte+";100;2"+"\n");

            porcentagemna3Parte = k200_2P/24;
            this.fw.write(porcentagemna3Parte+";200;2"+"\n");

            porcentagemna3Parte = k300_2P/24;
            this.fw.write(porcentagemna3Parte+";300;2"+"\n");

            porcentagemna3Parte = k100_3P/24;
            this.fw.write(porcentagemna3Parte+";100;3"+"\n");

            porcentagemna3Parte = k200_3P/24;
            this.fw.write(porcentagemna3Parte+";200;3"+"\n");

            porcentagemna3Parte = k300_3P/24;
            this.fw.write(porcentagemna3Parte+";300;3"+"\n");

            this.fw.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

       }


    public void montaComandosR()
    {
      // Gerando os comandos do R dinamicamente - segunda parte

        String nomeArquivo = "c:/analise/cmdr/comandos11.r";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("jpeg(\"c:/analise/resultado/graficoSituacaoKna3parte.jpg\",width = 400, height = 400, quality = 100)"+"\n");
            this.fw.write("library(ggplot2)"+"\n");
            this.fw.write("library(sqldf)"+"\n");
            this.fw.write("df = read.csv('c:/analise/bdados/graficoSituacaoKna3parte.csv',header=TRUE, sep=\";\")"+"\n");
            this.fw.write("df=sqldf(\"select Frequencia,"+"\n");
            this.fw.write("CASE WHEN Parte3==1 THEN '1P'"+"\n");
            this.fw.write("WHEN Parte3==2 THEN '2P'"+"\n");
	    this.fw.write("WHEN Parte3==3 THEN '3P'"+"\n");
            this.fw.write("END parte3,"+"\n");
            this.fw.write("CASE WHEN Valork==300  THEN 'K300' "+"\n");
            this.fw.write("WHEN Valork==200  THEN 'K200'" +"\n");
            this.fw.write("WHEN Valork==100  THEN 'K100' "+"\n");
            this.fw.write("END valork from df\")"+"\n");
            this.fw.write("p = ggplot(data=df, aes(x=factor(1),y=Frequencia, fill = factor(valork)),)" +"\n");
            this.fw.write("p=p + geom_bar(width = 1)"+"\n");
            this.fw.write("p=p+facet_grid(facets=. ~ parte3)"+"\n");
            this.fw.write("p"+"\n");
            this.fw.write("dev.off()" + "\n");
            this.fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void executaComandosR()
    {
          // Executando os comandos R preliminares de acordo com a configuracao acima.
        try
        {
            Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos11.r");
            p.waitFor();
            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }


    public void visualizarGrafico()
    {
           // Vizualizando o gráfico Precisão versus Revocação em visualizador externo
        try
        {
            Process p = Runtime.getRuntime().exec("xnview c:\\analise\\resultado\\graficoSituacaoKna3parte.jpg");
        } catch (Exception err)
        {
            err.printStackTrace();
        }

    }


}

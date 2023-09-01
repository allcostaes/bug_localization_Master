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
public class graficoSituacaoPFCna3parte
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


    public graficoSituacaoPFCna3parte()
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
            Double PFC1_1P=0.0,PFC0_1P=0.0,PFC1_2P=0.0,PFC0_2P=0.0,PFC1_3P=0.0,PFC0_3P=0.0;
            Double PFC1_4P=0.0,PFC0_4P=0.0,PFC1_5P=0.0,PFC0_5P=0.0,PFC1_6P=0.0,PFC0_6P=0.0;
            try
            {
                String Software="";
                switch (this.getSoftware())
            {
                case 1:       Software="1,10";  break;  // selecionou argouml
                case 2:       Software="11,20";  break;  // selecionou jedit
            }

                String Selecao = "";
                Selecao = " SELECT \"CB_PESOFRAGCLASSE\" FROM \"COMB10\"("+Software+") ORDER BY \"CB_MEDIAPRECISAO\" DESC ";

                Comando.execute(Selecao);
                Resultado = Comando.getResultSet();

                while (Resultado.next())
                {

                    if (contador<=24)
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 0:   PFC0_1P++;  break;
                            case 1:   PFC1_1P++;  break;
                        }
                    }

                    if ((contador>=25) && (contador<=48))
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 0:   PFC0_2P++;  break;
                            case 1:   PFC1_2P++;  break;
                        }

                    }

                    if ((contador>=49) && (contador<=72))
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 0:   PFC0_3P++;  break;
                            case 1:   PFC1_3P++;  break;
                        }

                    }

                    if ((contador>=73) && (contador<=96))
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 0:   PFC0_4P++;  break;
                            case 1:   PFC1_4P++;  break;
                        }

                    }

                    if ((contador>=97) && (contador<=120))
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 0:   PFC0_5P++;  break;
                            case 1:   PFC1_5P++;  break;
                        }

                    }


                    if ((contador>=121) && (contador<=144))
                    {
                        switch (Resultado.getInt(1))
                        {
                            case 0:   PFC0_6P++;  break;
                            case 1:   PFC1_6P++;  break;
                        }
                    }
                contador++;
                }
             } catch (SQLException e)
             {
                 System.out.println("SQL Exception");
                 e.printStackTrace();
             }


        String nomeArquivo = "c:/analise/bdados/graficoSituacaoPFCna6parte.csv";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("Frequencia;PFC;Parte6"+"\n");
            Double porcentagemna6Parte;

            porcentagemna6Parte = PFC0_1P/24;
            this.fw.write(porcentagemna6Parte+";0;1"+"\n");

            porcentagemna6Parte = PFC1_1P/24;
            this.fw.write(porcentagemna6Parte+";1;1"+"\n");


            porcentagemna6Parte = PFC0_2P/24;
            this.fw.write(porcentagemna6Parte+";0;2"+"\n");

            porcentagemna6Parte = PFC1_2P/24;
            this.fw.write(porcentagemna6Parte+";1;2"+"\n");

            porcentagemna6Parte = PFC0_3P/24;
            this.fw.write(porcentagemna6Parte+";0;3"+"\n");

            porcentagemna6Parte = PFC1_3P/24;
            this.fw.write(porcentagemna6Parte+";1;3"+"\n");

            porcentagemna6Parte = PFC0_4P/24;
            this.fw.write(porcentagemna6Parte+";0;4"+"\n");

            porcentagemna6Parte = PFC1_4P/24;
            this.fw.write(porcentagemna6Parte+";1;4"+"\n");


            porcentagemna6Parte = PFC0_5P/24;
            this.fw.write(porcentagemna6Parte+";0;5"+"\n");

            porcentagemna6Parte = PFC1_5P/24;
            this.fw.write(porcentagemna6Parte+";1;5"+"\n");


            porcentagemna6Parte = PFC0_6P/24;
            this.fw.write(porcentagemna6Parte+";0;6"+"\n");

            porcentagemna6Parte = PFC1_6P/24;
            this.fw.write(porcentagemna6Parte+";1;6"+"\n");



            this.fw.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

       }


    public void montaComandosR()
    {
      // Gerando os comandos do R dinamicamente - segunda parte

        String nomeArquivo = "c:/analise/cmdr/comandos14.r";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("jpeg(\"c:/analise/resultado/graficoSituacaoPFCna6parte.jpg\",width = 400, height = 400, quality = 100)"+"\n");
            this.fw.write("library(ggplot2)"+"\n");
            this.fw.write("library(sqldf)"+"\n");
            this.fw.write("df = read.csv('c:/analise/bdados/graficoSituacaoPFCna6parte.csv',header=TRUE, sep=\";\")"+"\n");
            this.fw.write("df=sqldf(\"select Frequencia,"+"\n");
            this.fw.write("CASE WHEN Parte6==1 THEN '1P'"+"\n");
            this.fw.write("WHEN Parte6==2 THEN '2P'"+"\n");
	    this.fw.write("WHEN Parte6==3 THEN '3P'"+"\n");
            this.fw.write("WHEN Parte6==4 THEN '4P'"+"\n");
	    this.fw.write("WHEN Parte6==5 THEN '5P'"+"\n");
            this.fw.write("WHEN Parte6==6 THEN '6P'"+"\n");
            this.fw.write("END parte6,"+"\n");
            this.fw.write("CASE WHEN PFC==0  THEN 'PFC0'" +"\n");
            this.fw.write("WHEN PFC==1  THEN 'PFC1' "+"\n");
            this.fw.write("END pfc from df\")"+"\n");
            this.fw.write("p = ggplot(data=df, aes(x=factor(1),y=Frequencia, fill = factor(pfc)),)" +"\n");
            this.fw.write("p=p + geom_bar(width = 1)"+"\n");
            this.fw.write("p=p+facet_grid(facets=. ~ parte6)"+"\n");
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
            Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos14.r");
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
            Process p = Runtime.getRuntime().exec("xnview c:\\analise\\resultado\\graficoSituacaoPFCna6parte.jpg");
        } catch (Exception err)
        {
            err.printStackTrace();
        }

    }




}

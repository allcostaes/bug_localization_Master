package relatorios;

import bdados.Conexao;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class graficoPrecisaoRevocacao
{
    private Conexao Conexao;
    private Statement Comando;
    private ResultSet Resultado;
    private String TL_CODIGO;
    private ArrayList<Double[]> pontosGrafico;
    
    public graficoPrecisaoRevocacao()
    {
      Conexao = new Conexao();
        try
        {
            Comando = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      pontosGrafico = new ArrayList<Double[]>();
    }



    public void setTL_CODIGO(String TL_CODIGO)
    {
        this.TL_CODIGO = TL_CODIGO;
    }
    private String getTL_CODIGO()
    {
        return this.TL_CODIGO;
    }

    public void selecionaPontos()
    {
        //Seleciona os metodos corrigidos na correção do bug e armazena dentro de um Arraylist
        this.pontosGrafico.removeAll(this.pontosGrafico);
        try {
            String Selecao = "";
            Selecao = "SELECT \"PR_REVOCACAO\", \"PR_PRECISAO\"  FROM \"PREC_REVOCACAO\"  WHERE \"TL_CODIGO\" = " + this.getTL_CODIGO();
            Comando.execute(Selecao);
            Resultado = Comando.getResultSet();
            while (Resultado.next())
            {
                pontosGrafico.add(new Double[]{Resultado.getDouble(1),Resultado.getDouble(2)});

            }
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }


    public void plotarGrafico()
    {


           // Gerando os comandos do R dinamicamente - segunda parte
        String nomeArquivo = "c:\\analise\\cmdr\\comandos3.r";
        try
        {
            FileWriter fw = new FileWriter(nomeArquivo, false);
            fw.write("jpeg(\"c:/analise/resultado/precrevocacao.jpg\")"+"\n");

            Double Pontos[] = new Double[2];
            String x="",y="";

            for(int i=0; i<=pontosGrafico.size()-1;i++)
            {
                Pontos = pontosGrafico.get(i);

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

            fw.write("x<-" + x + "\n");
            fw.write("y<-" + y + "\n");
            fw.write("par(pch=22, col=\"red\")" + "\n");
            fw.write("heading = paste(\"CURVA PRECISÃO X REVOCAÇÃO\")" + "\n");

            fw.write("plot(x,y,main=heading,xlab=\"Revocação\",ylab=\"Precisão\",ylim=c(0.0,1.0),xlim=c(0.0,1.0))"+"\n");
            fw.write("lines(x, y, type=\"b\")"+"\n");

            fw.write("dev.off()"+"\n");
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

     // Executando os comandos R preliminares de acordo com a configuracao acima.
        try
        {
            Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos3.r");
            p.waitFor();
            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }

     // Vizualizando o gráfico Precisão versus Revocação em visualizador externo
        try
        {
            Process p = Runtime.getRuntime().exec("xnview c:\\analise\\resultado\\precrevocacao.jpg");
            p.waitFor();
            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }

    }
}

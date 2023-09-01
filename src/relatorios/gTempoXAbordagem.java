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

public class gTempoXAbordagem
{
    private Conexao Conexao;
    private Statement Comando1, Comando2, Comando3;
    private ResultSet Resultado1, Resultado2;
    private FileWriter fw;


  public gTempoXAbordagem()
  {
        Conexao = new Conexao();
        try
        {
            Comando1 = Conexao.conexao.createStatement();
            Comando2 = Conexao.conexao.createStatement();
            Comando3 = Conexao.conexao.createStatement();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
  }
     public void montarComandosR()
    {
        String nomeArquivo = "c:/analise/cmdr/comandos_grafico.r";
        try
        {
            this.fw = new FileWriter(nomeArquivo, false);
            this.fw.write("jpeg(\"c:/analise/resultado/grafico.jpg\",width = 400, height = 400, quality = 100)" + "\n");
            this.fw.write("par(mfrow=c(1,1))" + "\n");


        } catch (IOException e) {
            e.printStackTrace();
        }

        String Selecao = " SELECT \"TL_CORPUS\",\"TL_TEMPO\" FROM \"TESTELSI\"";
        Selecao = Selecao+" ORDER BY \"BG_CODIGO\",\"TL_CORPUS\" ";
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
            while (Resultado1.next())
            {
                try
                {
                    String titulo = "<PM=1><PFM=1><PC=1><PFC=1><STW=3>";
                    this.fw.write("heading = paste(\"" + titulo + "\")\n");
                    this.fw.write("plot(0,0,type=\"b\",lty=1,main=heading,xlab=\"Abordagem [1-Tudo][2-Rastro][3-Rastro+Grep]\",ylab=\"Tempo(segundos)\",ylim=c(0,3),xlim=c(0,5000))" + "\n");
                    this.fw.write("legend(\"topright\",c(\"BUG1\",\"BUG2\",\"BUG3\"),cex=1.0, col=1:10, pch=1:10,lty=1:10)"+ "\n");
                    String XY="";
//                    XY = "points(c("+Resultado1.getInt(1)x+"),c("+Resultado1.getInt(2)+"),type=\"b\",lty=2,col="+linhatipo+",pch=("+linhatipo+"))" + "\n";
                    this.fw.write(XY + "\n");
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
            this.fw.write("dev.off()" + "\n");
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(contaMenorTrabalhok100);
//        System.out.println(contaMenorTrabalhok200);
//        System.out.println(contaMenorTrabalhok300);
    }

}

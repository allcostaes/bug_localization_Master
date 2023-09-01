/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorios;

import java.io.FileWriter;
import java.io.IOException;
import org.openide.util.Exceptions;

/**
 *
 * @author user
 */
public class graficoCombinacaoXPrecisao_valorK
{
       private FileWriter fw;

       public void plotarGrafico()
       {

           String nomeArquivo = "c:\\analise\\cmdr\\comandos10.r";
            try
            {
                this.fw = new FileWriter(nomeArquivo, false);
                this.fw.write("jpeg(\"c:/analise/resultado/graficoCombinacaoXPrecisao_valorK.jpg\",width = 600, height = 600, quality = 100)"+"\n");
                this.fw.write("Orange$Tree <- as.numeric(Orange$Tree)"+"\n");
                this.fw.write("ntrees <- max(Orange$Tree)" +"\n");
                this.fw.write("xrange <- range(Orange$age)" +"\n");
                this.fw.write("yrange <- range(Orange$circumference)"+"\n");
                this.fw.write("plot(xrange, yrange, type=\"n\", xlab=\"Age (days)\", ylab=\"Circumference (mm)\" )"+"\n");
                this.fw.write("colors <- rainbow(ntrees)"+"\n");
                this.fw.write("linetype <- c(1:ntrees)"+"\n");
                this.fw.write("plotchar <- seq(18,18+ntrees,1)"+"\n");
                this.fw.write("for (i in 1:ntrees) {"+"\n");
                this.fw.write("tree <- subset(Orange, Tree==i)"+"\n");
                this.fw.write("lines(tree$age, tree$circumference, type=\"b\", lwd=1.5, lty=linetype[i], col=colors[i], pch=plotchar[i])"+"\n");
                this.fw.write("}"+"\n");
                this.fw.write("title(\"Tree Growth\", \"example of line plot\")"+"\n");
                this.fw.write("legend(xrange[1], yrange[2], 1:ntrees, cex=0.8, col=colors,pch=plotchar, lty=linetype, title=\"Tree\")"+"\n");
                this.fw.write("dev.off()" + "\n");
                this.fw.close();
            } catch (IOException ex)
            {
                Exceptions.printStackTrace(ex);
            }
       }

        public void executaComandosR()
        {
              // Executando os comandos R preliminares de acordo com a configuracao acima.
            try
            {
                Process p = Runtime.getRuntime().exec("R CMD BATCH c:\\analise\\cmdr\\comandos10.r");
                p.waitFor();
                System.out.println(p.exitValue());
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

    public void visualizarGrafico()
    {
        try
        {
            Process p = Runtime.getRuntime().exec("xnview c:\\analise\\resultado\\graficoCombinacaoXPrecisao_valorK.jpg");
        } catch (Exception err) {
            err.printStackTrace();
        }

    }


}

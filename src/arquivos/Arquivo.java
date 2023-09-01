package arquivos;

import java.io.File;

/** Realiza operacoes basicas sobre arquivos no sistema */
 public class Arquivo 
{
	 private String Nome;
	 private static File f;
	 public Arquivo(String Nome)
	 {
		 	String fileName = Nome;
		 	this.Nome = Nome;
		 	// A File object to represent the filename
		 	f = new File(fileName);
	 }
	 
	 public void SetNome(String Nome)
	 {
		 this.Nome = Nome;
	 }
	 
	 /** Deleta arquivo pelo nome */
	 public static void deletarArquivo()
	 {
 	    boolean success = f.delete();
	 }	
 
}

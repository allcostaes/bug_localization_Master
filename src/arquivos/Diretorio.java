package arquivos;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.io.FilenameFilter;
import java.lang.*;

public class Diretorio 
{
	private String Nome;
	private static ArrayList<String> Nome_de_arquivos = new ArrayList<String>();		
	private String  Extensao_arquivo;

        public Diretorio()
        {
            Nome_de_arquivos.removeAll(Nome_de_arquivos);
        }
	public void setExtensao(String Extensao_arquivo)
	{
		this.Extensao_arquivo=Extensao_arquivo;
	}

	public void setNome(String Nome)
	{
		this.Nome = Nome;
	}
	
//////Pecorre diretorios e sub-diretorios capturando o nome dos arquivos em um vetor com base na extensao do arquivo
	 public ArrayList<String> listaDiretorio(File diretorio)
	{
		 
		File[] filhos = diretorio.listFiles(new FileFilter() 
		{
			public boolean accept(File file) 
			{
					return file.isDirectory() || file.getName().toLowerCase().endsWith(Extensao_arquivo);

			}
		});
		for (int i = 0; i < filhos.length; i++) 
		{
			if (filhos[i].isDirectory()) 
			{
				listaDiretorio(filhos[i]);
			} else {
				String nome_arquivo_completo = filhos[i].getAbsoluteFile().getAbsolutePath();
				String nome_arquivo_simples = filhos[i].getAbsoluteFile().getName();
				String nome_diretorio = filhos[i].getAbsoluteFile().getParent();
				String nome = nome_arquivo_completo;
				Nome_de_arquivos.add(new String(nome));
			}
		}
		return Nome_de_arquivos;
	}

	  public void apagaArquivo( String d, String e ) 
	  {
		 ExtensionFilter filter = new ExtensionFilter(e);
		 File dir = new File(d);
		 String[] list = dir.list(filter);
		 File file;
		 if (list.length == 0) return;

		 for (int i = 0; i < list.length; i++) 
		 {
			 file = new File(d + list[i]);
		     boolean isdeleted =   file.delete();
		     System.out.print(file);
		     System.out.println( "  deleted " + isdeleted);
		 }
	  }
	  
	 /** Apaga todos os arquivos de um dado diretorio */
	  public void apagaTodosArquivos()
	  {
		  File directory = new File(this.Nome);

		// Get all files in directory

		File[] files = directory.listFiles();
		for (File file : files)
		{
		   // Delete each file

		   if (!file.delete())
		   {
		       // Failed to delete file

		       System.out.println("Failed to delete "+file);
		   }
		}

		  
	  }
	  
	  
	  

	  class ExtensionFilter implements FilenameFilter 
	  {

		  private String extension;
		  public ExtensionFilter( String extension ) 
		  {
			this.extension = extension;             
		  }
		  public boolean accept(File dir, String name) 
		  {
			return (name.endsWith(extension));
		  }
	   }
	  
	  
}

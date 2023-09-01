package pacote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import  arquivos.Diretorio;
import bdados.Conexao;

public class Rastro 
{

	private static Integer contador = 0;
	private static ArrayList<String> metodosFeature = new ArrayList();                             // metodos sem tratamento
	private static ArrayList<String> metodosFiltrados = new ArrayList();		                   // metodos sem repeti��o

	private File Origem;
	
	public void setOrigem(String Origem)
	{
		this.Origem = new File(Origem);
	}
	
	
	public ArrayList<String> retornaMetodosRastro()
	{
                //Recebe o timestamp de inicio e de fim da ultima feature executada em um vetor de longs
                long featureTimestamps[];
                featureTimestamps = getFeatureTimestamps();
                
		Diretorio Diretorio = new Diretorio();
		Diretorio.setExtensao(".trace");
		metodosFeature = Diretorio.listaDiretorio(this.Origem);

		Iterator itr = metodosFeature.iterator();
		metodosFiltrados.removeAll(metodosFiltrados);
		while( itr.hasNext() ) 
		{
			String nomeArquivo = (String)itr.next();
			System.out.println(nomeArquivo);
			// Passa o valor dos timestamps da �ltima feature executada para extra��o dos m�todos dentro do intervalo dos timestamps
			extraiMetodosFeature(nomeArquivo, featureTimestamps[0], featureTimestamps[1]);
		}

		metodosFiltrados = removeDuplicateWithOrder(metodosFiltrados);
		return metodosFiltrados;
	}
	

	/** 
	Extrai cada linha de um arquivo do tipo "arquivo.trace" para dentro de um Arraylist
	**/
	private static void extraiMetodosFeature(String Nome_Arquivo, long inicioFeature, long fimFeature)
	{
        	try
		{
			FileReader fileReader = new FileReader(Nome_Arquivo);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String Linha_Trace[] = null;
			String Pacote_Classe[] = null;
			String Pacote, Classe, Metodo;
			long Tempo;
			String Linha_Conteudo = null;

			
			while ((Linha_Conteudo = bufferedReader.readLine()) != null) 
			{

				Linha_Trace = null;
				Pacote_Classe = null;
				Pacote="";
				Classe="";
				Metodo="";

				Linha_Trace = Linha_Conteudo.split(",");
				Pacote_Classe = ((Linha_Trace[0]).split("\\."));
			
				for(int i = 0; i<Pacote_Classe.length-1 ; i++)
				{
					if (i!=Pacote_Classe.length-2)
					{
						Pacote = Pacote + Pacote_Classe[i] + ".";
					}
					else
					{
						Pacote = Pacote + Pacote_Classe[i];
					}
				}
				Classe = Pacote_Classe[Pacote_Classe.length-1];
				Metodo = Linha_Trace[1];


				Tempo = Long.parseLong(Linha_Trace[4].trim()); 
				
				
				if ((Tempo>=inicioFeature) && (Tempo<=fimFeature))
				{
					//if (Metodo.equalsIgnoreCase("getimportsettings"))
						//System.out.println("NO RASTRO.JAVA: " + Pacote+" -- "+Classe+" -- "+Metodo);
					
					metodosFiltrados.add(Pacote+";"+Classe+";"+Metodo);
					//System.out.println("M�todo da Feature encontrado" + Pacote+";"+Classe+";"+Metodo);
				}
			}
			bufferedReader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

	}

	private static ArrayList removeDuplicateWithOrder(ArrayList arlList)
	 {

		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = arlList.iterator();    iter.hasNext(); ) 
		{
			Object element = iter.next();
			if (set.add(element))
			newList.add(element);
	    }
	    arlList.clear();
	    arlList.addAll(newList);
	    return arlList;
	}

        public void resetaRatroBD()
	{
		try
	     {		
			Conexao Conexao = new Conexao();;
			Statement Comando= Conexao.conexao.createStatement();;

			String Delecao = "";
			Delecao = "DELETE FROM \"METODO_CARACTERISTICA\"";
			Comando.execute(Delecao);
			
			String resetaMC_CODIGO = "ALTER SEQUENCE \"public\".\"METODO_CARACTERISTICA_MC_CODIGO_seq\"	  INCREMENT 1 MINVALUE 0	  MAXVALUE 9223372036854775807 START 0		  RESTART 1 CACHE 1		  NO CYCLE;";
			Comando.execute(resetaMC_CODIGO);
		
			
	     }
	      catch(SQLException e)
	      {
	    	  System.out.println("SQL Exception");
	          e.printStackTrace();
	      }
	}

        private long[] getFeatureTimestamps()
        {
                long inicioFeature=0, fimFeature=0;
      		try
		{
                	FileReader fileReader = new FileReader("C:\\analise\\rastros\\trace.mark.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String linhaConteudo = null;

			// Contador que controla os indices do jcombobox
			int j=0,k=0;
			String Timestamp="";
			String Timestamps[]=null;

			while ((linhaConteudo = bufferedReader.readLine()) != null)
			{
				Timestamp = Timestamp + linhaConteudo;
				if (k==1)
				{
					Timestamps = Timestamp.split(",");
					//System.out.println(Timestamps[1].trim() + ";" + (Timestamps[2].replaceAll("#END","")).trim() + ";" + Timestamps[4].trim());
                                        inicioFeature = Long.parseLong((Timestamps[2].replaceAll("#END","")).trim());
                                        fimFeature = Long.parseLong(Timestamps[4].trim());
                                        Timestamp="";
					k=-1;
					j++;
				}
				k++;
			}

			bufferedReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
                long featureTimestamps[] = { inicioFeature, fimFeature};
                return featureTimestamps;
        }
}

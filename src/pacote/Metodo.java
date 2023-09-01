package pacote;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Types;

import bdados.Conexao;

public class Metodo
{
	
	String PACOTE, CLASSE, METODO;
	Conexao Conexao;
	Statement Comando;
	CallableStatement procComando;
	
	Metodo()
	{
		this.Conexao = new Conexao();
		try
		{
			this.Comando = Conexao.conexao.createStatement();
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////	
	public void setPACOTE(String PACOTE)
	{
		this.PACOTE = PACOTE;
	}

	public void setCLASSE(String CLASSE)
	{
		this.CLASSE = CLASSE;
	}	

	public void setMETODO(String METODO)
	{
		this.METODO = METODO;
	}	

///////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getPACOTE()
	{
		return this.PACOTE;
	}

	public String getCLASSE()
	{
		return this.CLASSE;
	}	

	public String getMETODO()
	{
		return this.METODO;
	}	

}

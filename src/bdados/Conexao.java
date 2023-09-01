package bdados;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;
import java.sql.PreparedStatement;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;
import org.openide.util.Exceptions;


import org.postgresql.*;

public class Conexao
{

    public  Connection conexao;
    public  Connection conexaoCommitFalse;
    private String stringConexao;

    /** Metodo construtor **/
    public Conexao()
    {
        //bdados5 = rastros+grep
        //bdados6 = rastros
        //bdados7 = todo o código fonte


        stringConexao="jdbc:postgresql://127.0.0.1/bdados7?user=postgres&password=masterkey";
        criaConexao();
        criaConexaoCommitFalse();
    }

    /** Cria a conexao com o banco de dados onde os metodos serao armazenados  **/
    private void criaConexao() {
        try {
            Class.forName("org.postgresql.Driver");
            this.conexao = DriverManager.getConnection(this.stringConexao);
                        this.conexao.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            System.out.println("excessao ClassNotFound...");
            e.printStackTrace();
        } catch (SQLException e)
        {
            System.out.println("SQL Exception...");
            e.printStackTrace();
        }
    }

    /** Cria a conexao com o banco de dados onde os metodos serao armazenados  **/
    private void criaConexaoCommitFalse()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            this.conexaoCommitFalse = DriverManager.getConnection(this.stringConexao);
            this.conexaoCommitFalse.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            System.out.println("excessao ClassNotFound...");
            e.printStackTrace();
        } catch (SQLException e)
        {
            System.out.println("SQL Exception...");
            e.printStackTrace();
        }
    }

}

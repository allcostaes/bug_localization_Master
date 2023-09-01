package pacote;


import java.sql.SQLException;
import java.sql.Statement;
import bdados.Conexao;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.openide.util.Exceptions;

public class codigoFonte {

    private Conexao Conexao;
    private Statement Comando;
    private ResultSet Resultado;

    public codigoFonte() {
        Conexao = new Conexao();
        try {
            Comando = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetaCodigoBD() {
        try {
            Conexao Conexao = new Conexao();
            ;
            Statement Comando = Conexao.conexao.createStatement();
            ;

            String Delecao = "";
            Delecao = "DELETE FROM \"METODO_BRUTO\"";
            Comando.execute(Delecao);

            String resetaMC_CODIGO = "ALTER SEQUENCE \"public\".\"METODO_BRUTO_MB_CODIGO_seq\"	  INCREMENT 1 MINVALUE 0	  MAXVALUE 9223372036854775807 START 0		  RESTART 1 CACHE 1		  NO CYCLE;";
            Comando.execute(resetaMC_CODIGO);

            Delecao = "";
            Delecao = "DELETE FROM \"PACOTE_CLASSE_METODO\"";
            Comando.execute(Delecao);

            String resetaPCM_CODIGO = "ALTER SEQUENCE \"public\".\"PACOTE_CLASSE_METODO_PCM_CODIGO_seq\"	  INCREMENT 1 MINVALUE 0	  MAXVALUE 9223372036854775807 START 0		  RESTART 1 CACHE 1		  NO CYCLE;";
            Comando.execute(resetaMC_CODIGO);

        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }

    public Integer Cadastrar(String CF_DESCRICAO) {
        Integer CF_CODIGO = 0;
        String metodo = "";
        String Inclusao = "INSERT INTO \"CODIGO_FONTE\" (\"CF_DESCRICAO\") VALUES ('" + CF_DESCRICAO + "') RETURNING  \"CF_CODIGO\"";
        try {
            Comando.execute(Inclusao);
            Resultado = Comando.getResultSet();
            Resultado.next();
            CF_CODIGO = Resultado.getInt("CF_CODIGO");
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        return CF_CODIGO;
    }

    public ArrayList<String> getCadastrados()
    {
        ArrayList<String> codigoFontes = new ArrayList<String>();
        try
        {
                String Selecao = "";
                Selecao = "SELECT \"CF_CODIGO\", \"CF_DESCRICAO\" ";
                Selecao = Selecao + " FROM \"CODIGO_FONTE\"";
                Selecao = Selecao + " WHERE \"CF_CODIGO\"<>0";
                Comando.execute(Selecao);
                Resultado = Comando.getResultSet();
                while (Resultado.next())
                {
                    codigoFontes.add(Resultado.getInt(1)+"-"+Resultado.getString(2));
                }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        try {
            this.Conexao.conexao.commit();
            this.Comando.close();
            this.Resultado.close();
            this.Conexao.conexao.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }



        return codigoFontes;
    }
}

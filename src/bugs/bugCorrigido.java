package bugs;


import bdados.Conexao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bugCorrigido {

    private Conexao Conexao;
    private Statement Comando;
    private ResultSet Resultado;

    public bugCorrigido()
    {
        Conexao = new Conexao();
        try
        {
            Comando = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void persistePrecisaoRevocacao(String TL_CODIGO, String MB_CODIGO, String PR_POSICAO, String PR_PRECISAO, String PR_REVOCACAO)
    {
        String Inclusao = "INSERT INTO \"PREC_REVOCACAO\" (\"TL_CODIGO\",\"MB_CODIGO\",\"PR_POSICAO\",\"PR_PRECISAO\",\"PR_REVOCACAO\") VALUES "
             + "("+  TL_CODIGO + "," + MB_CODIGO + "," + PR_POSICAO + "," + PR_PRECISAO + "," + PR_REVOCACAO + ")";
        System.out.println(Inclusao);

        try
        {
            Comando.execute(Inclusao);
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }
}

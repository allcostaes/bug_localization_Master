/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utilidades;

import bdados.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.openide.util.Exceptions;

/**
 *
 * @author user
 */
public class Principal
{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        Conexao Conexao;
        Statement Comando1 = null;
        ResultSet Resultado1;
        PreparedStatement comandoPreparado1 = null;


        Conexao = new Conexao();
        try
        {
            Comando1 = Conexao.conexao.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        try
        {
            StringBuilder comandoPreparado = new StringBuilder("INSERT INTO \"COMBINACOES\" ");
            comandoPreparado.append("(\"CB_VALORK\",\"CB_PESOMETODO\",\"CB_PESOFRAGMETODO\",\"CB_STOPWORDS\",\"CB_CORPUS\", \"CB_PESOCLASSE\", \"CB_PESOFRAGCLASSE\") ");
            comandoPreparado.append(" VALUES (?,?,?,?,?,?,?) ");
            comandoPreparado1 = Conexao.conexaoCommitFalse.prepareStatement(comandoPreparado.toString());
        } catch (SQLException ex)
        {
           Exceptions.printStackTrace(ex);
        }



        StringBuilder Selecao = new StringBuilder();

        try
        {
            Selecao.append(" SELECT DISTINCT \"TL_VALORK\",\"TL_PESOMETODO\",\"TL_PESOFRAGMETODO\",\"TL_STOPWORDS\", \"TL_CORPUS\",\"TL_PESOCLASSE\",\"TL_PESOFRAGCLASSE\" ");
            Selecao.append(" FROM \"TESTELSI\"");
            Selecao.append(" ORDER BY \"TL_VALORK\" ASC,\"TL_PESOMETODO\" ASC,\"TL_PESOFRAGMETODO\" ASC,\"TL_STOPWORDS\" ASC, \"TL_CORPUS\" ASC,\"TL_PESOCLASSE\" ASC,\"TL_PESOFRAGCLASSE\" ASC ");

            Comando1.execute(Selecao.toString());
            Resultado1 = Comando1.getResultSet();
            int teste=0;
            while (Resultado1.next())
            {
                        comandoPreparado1.setInt(1, Resultado1.getInt(1));
                        comandoPreparado1.setInt(2, Resultado1.getInt(2));
                        comandoPreparado1.setInt(3, Resultado1.getInt(3));
                        comandoPreparado1.setInt(4, Resultado1.getInt(4));
                        comandoPreparado1.setInt(5, Resultado1.getInt(5));
                        comandoPreparado1.setInt(6, Resultado1.getInt(6));
                        comandoPreparado1.setInt(7, Resultado1.getInt(7));
                        comandoPreparado1.execute( );
                        comandoPreparado1.clearParameters( );

            }
            Conexao.conexaoCommitFalse.commit();
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }



}

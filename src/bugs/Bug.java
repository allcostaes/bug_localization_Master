package bugs;

import pacote.Rastro;
import pacote.Metodo_Caracteristica;
import java.sql.SQLException;
import java.sql.Statement;
import bdados.Conexao;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.openide.util.Exceptions;

public class Bug {

    private Conexao Conexao;
    private Statement Comando;
    private ResultSet Resultado;

    public Bug() {
        try
        {
            Conexao = new Conexao();
            Comando = Conexao.conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer Cadastrar(String BG_DESCRICAO, Integer CF_CODIGO)
    {
        // Cadastrando o bug
        Integer BG_CODIGO=0;
        String Inclusao = "INSERT INTO \"BUGS\" (\"BG_DESCRICAO\",\"CF_CODIGO\") VALUES ('" + BG_DESCRICAO + "', "+ CF_CODIGO + ") RETURNING  \"BG_CODIGO\"";
        try {
            Comando.execute(Inclusao);
            Resultado = Comando.getResultSet();
            Resultado.next();
            BG_CODIGO = Resultado.getInt("BG_CODIGO");
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    

        // Cadastrando o metodos do rastro associado a característica com bug

        Rastro Rastro = new Rastro();
        Rastro.setOrigem("c:\\analise\\rastros\\");
        Metodo_Caracteristica metodoCaracteristica = new Metodo_Caracteristica();
        ArrayList<String> metodosFiltrados = new ArrayList();
        System.out.println("---------- Arquivos de Rastro sendo analisados ----------");
        metodosFiltrados = Rastro.retornaMetodosRastro();
        System.out.println("---------- *********************************** ----------");
        System.out.println("---------- Metodos da ultima feature executada ----------");


        for(int i=0; i<=metodosFiltrados.size()-1;i++)
        {
            String Metodo[] = ((metodosFiltrados.get(i)).split(";"));
            System.out.print(Metodo[0] + "--");
            System.out.print(Metodo[1] + "--");
            System.out.println(Metodo[2] + "--");

            metodoCaracteristica.setPACOTE(Metodo[0]);
            metodoCaracteristica.setCLASSE(Metodo[1]);
            metodoCaracteristica.setMETODO(Metodo[2]);
            metodoCaracteristica.setBG_CODIGO(BG_CODIGO);
            metodoCaracteristica.setCF_CODIGO(CF_CODIGO);
            metodoCaracteristica.persisteMetodo();
        }
        System.out.println("---------- ********************************* ----------");
        return BG_CODIGO;

    }



    public void cadastrarBugMetodosAlvo(String BG_CODIGO, String MB_CODIGO)
    {
        String Inclusao = "INSERT INTO \"BUGS_CORRIGIDOS\" (\"BG_CODIGO\",\"MB_CODIGO\") VALUES (" + BG_CODIGO + ", "+ MB_CODIGO + ") ";
        System.out.println(Inclusao);
        try {
            Comando.execute(Inclusao);
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }

    public String[] pesquisarBugSumarioPorCodigo(Integer BG_CODIGO)
    {
        String[] Bug = new String[2];
        try
        {
                String Selecao = "";
                Selecao = "SELECT \"BG\".\"BG_DESCRICAO\", \"CF\".\"CF_DESCRICAO\" ";
                Selecao = Selecao + " FROM \"BUGS\"  \"BG\", \"CODIGO_FONTE\"  \"CF\" WHERE ";
                Selecao = Selecao + " \"BG\".\"CF_CODIGO\"="+ BG_CODIGO.toString() + " AND (\"BG\".\"CF_CODIGO\" = \"CF\".\"CF_CODIGO\")";
                Comando.execute(Selecao);
                Resultado = Comando.getResultSet();
                while (Resultado.next())
                {
                    Bug[0] = Resultado.getString(1);
                    Bug[1] = Resultado.getString(2);
                }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        return Bug;
    }

}

package pacote;


import java.sql.SQLException;
import java.sql.Types;

public class Metodo_Bruto extends Metodo {

    String IMPLEMENTACAO, DIRETORIO_ARQUIVO, NOME_ARQUIVO;
    Integer SOBRECARGA, CF_CODIGO;

    public void setIMPLEMENTACAO(String IMPLEMENTACAO) {
        this.IMPLEMENTACAO = IMPLEMENTACAO;
    }

    public void setDIRETORIO_ARQUIVO(String DIRETORIO_ARQUIVO) {
        this.DIRETORIO_ARQUIVO = DIRETORIO_ARQUIVO;
    }

    public void setNOME_ARQUIVO(String NOME_ARQUIVO) {
        this.NOME_ARQUIVO = NOME_ARQUIVO;
    }

    public void setSOBRECARGA(Integer SOBRECARGA) {
        this.SOBRECARGA = SOBRECARGA;
    }

    public void setCF_CODIGO(Integer CF_CODIGO) {
        this.CF_CODIGO = CF_CODIGO;
    }

    public String getIMPLEMENTACAO() {
        return this.IMPLEMENTACAO;
    }

    public String getDIRETORIO_ARQUIVO() {
        return this.DIRETORIO_ARQUIVO;
    }

    public String getNOME_ARQUIVO() {
        return this.NOME_ARQUIVO;
    }

    public Integer getSOBRECARGA()
    {
        procComando = null;
        try
        {
            this.procComando = Conexao.conexao.prepareCall("{ ? = call \"RETORNA_MB_SOBRECARGA\"(?,?,?,?) }");
            this.procComando.registerOutParameter(1, Types.INTEGER);
            this.procComando.setString(2, this.getPACOTE());
            this.procComando.setString(3, this.getCLASSE());
            this.procComando.setString(4, this.getMETODO());
            this.procComando.setInt(5, this.CF_CODIGO);
            procComando.execute();
            this.setSOBRECARGA(procComando.getInt(1) + 1);
        } catch (SQLException e)
        {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        return this.SOBRECARGA;
    }

    public Integer getCF_CODIGO() {
        return this.CF_CODIGO;
    }

    public void persisteMetodo()
    {
        try {
            String Inclusao = "";
            Inclusao = "INSERT INTO \"METODO_BRUTO\" (\"MB_PACOTE\", \"MB_CLASSE\", \"MB_METODO\", \"MB_IMPLEMENTACAO\", \"MB_DIRETORIO_ARQUIVO\", \"MB_NOME_ARQUIVO\", \"MB_SOBRECARGA\",\"CF_CODIGO\" ) VALUES ";
            Inclusao = Inclusao + "('" + this.getPACOTE() + "','" + this.getCLASSE() + "','" + this.getMETODO() + "','" + this.getIMPLEMENTACAO() + "',E'" + this.getDIRETORIO_ARQUIVO() + "','" + this.getNOME_ARQUIVO() + "'," + this.getSOBRECARGA() + "," + this.getCF_CODIGO() + ")";
            Comando.execute(Inclusao);
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }
}

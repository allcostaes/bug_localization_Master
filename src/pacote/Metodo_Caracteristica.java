package pacote;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

public class Metodo_Caracteristica extends Metodo {

    private String PACOTE, CLASSE;
    private Integer BG_CODIGO, CF_CODIGO;


    public void setBG_CODIGO(Integer BG_CODIGO)
    {
        this.BG_CODIGO = BG_CODIGO;
    }

    public void setCF_CODIGO(Integer CF_CODIGO)
    {
        this.CF_CODIGO = CF_CODIGO;
    }

   public Integer getBG_CODIGO()
    {
        return this.BG_CODIGO;
    }

    public Integer getCF_CODIGO()
    {
        return this.CF_CODIGO;
    }

    public void persisteMetodo() {

        // Verifica se foi chamado o metodo construtor da classe identificado no rastro como <init>
        if (this.getMETODO().equalsIgnoreCase("<init>"))
        {
            if (this.getCLASSE().indexOf('$') == -1)
            {
                this.setMETODO(this.getCLASSE());
            } else
            {
                this.setMETODO((this.getCLASSE().subSequence(this.getCLASSE().indexOf('$') + 1, this.getCLASSE().length())).toString());
            }
        }

        // Verifica se foi chamado uma classe no padrão nomedaclasse$1 e retira o $1 do nome da classe

        /*
        if (this.getCLASSE().indexOf("$1") != -1)
        {
            this.setCLASSE(this.getCLASSE().replaceAll("\\$1", ""));
        }

        if (this.getCLASSE().indexOf("$2") != -1)
        {
            this.setCLASSE(this.getCLASSE().replaceAll("\\$2", ""));
        }
*/

        try {
            String Inclusao = "";
            Inclusao = "INSERT INTO \"METODO_CARACTERISTICA\" (\"MC_PACOTE\", \"MC_CLASSE\", \"MC_METODO\", \"MB_CODIGO\",\"BG_CODIGO\",\"CF_CODIGO\") VALUES ";
            Inclusao = Inclusao + "('" + this.getPACOTE() + "','" + this.getCLASSE() + "','" + this.getMETODO() + "'," + consisteMetodo() + ","+ this.getBG_CODIGO() + ","+ this.getCF_CODIGO() + ")";
            //System.out.println(Inclusao);
            Comando.execute(Inclusao);
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }

    /** Retorna o metodo bruto com o cedigo do mesmo referente onde se esta a implementaï¿½ï¿½o */
    private Integer consisteMetodo() {
        int MB_CODIGO = 0;
        ResultSet Resultado;
        try {
            String Selecao = "";
            Selecao = "SELECT \"MB_CODIGO\" FROM \"METODO_BRUTO\" WHERE ";
            Selecao = Selecao + " (\"MB_PACOTE\" = '" + this.getPACOTE() + "') AND (\"MB_CLASSE\" = '" + this.getCLASSE() + "') AND (\"MB_METODO\" = '" + this.getMETODO() + "') AND (\"CF_CODIGO\" = '" + this.getCF_CODIGO() + "') ";
            Comando.execute(Selecao);
            Resultado = Comando.getResultSet();
            while (Resultado.next()) {
                MB_CODIGO = Resultado.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        // O metodo executado na caracteristica nao encontrou equivalencia por isso é necessario verificar em que super classe ele está implementado...
        if (MB_CODIGO == 0) {
            MB_CODIGO = consisteMetodoHerdado(this.getPACOTE(), this.getCLASSE(), this.getMETODO(), this.getCF_CODIGO());
        }

        return MB_CODIGO;
    }

    private Integer consisteMetodoHerdado(String Pacote, String Classe, String Metodo, Integer CF_CODIGO) {
        int MB_CODIGO = 0;
        Class subClasse = null;
        Class superClasse = null;
        // Carrega a classe a partir dos pacotes .jar presentes no CLASSPATH do projeto
        try
        {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            subClasse = Class.forName(Pacote + "." + Classe, false, loader);
            superClasse = subClasse.getSuperclass();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        // Se esta condiï¿½ï¿½o for falsa verificase-se a super classe em busca da implementaï¿½ï¿½o do mï¿½todo
        if (((superClasse.getPackage().getName().equals("java.lang")) && (superClasse.getSimpleName().equals("Object"))))
        {
            return MB_CODIGO;
        }
        // Fazendo um tratamento no nome da classe para recuperar o nome da super classe no nome da classe
        String classePacote[] = (superClasse.getName()).split("\\.");
        String Classe1 = classePacote[(classePacote.length) - 1];

        // Pesquisa o banco de dados em busca do cï¿½digo do mï¿½todo na super classe
        ResultSet Resultado;
        try {
            String Selecao = "";
            Selecao = "SELECT \"MB_CODIGO\" FROM \"METODO_BRUTO\" WHERE ";
            Selecao = Selecao + " (\"MB_PACOTE\" = '" + superClasse.getPackage().getName() + "') AND (\"MB_CLASSE\" = '" + Classe1 + "') AND (\"MB_METODO\" = '" + Metodo + "') AND (\"CF_CODIGO\" = '" + CF_CODIGO + "')";
            //System.out.println(Selecao);
            Comando.execute(Selecao);
            Resultado = Comando.getResultSet();
            while (Resultado.next()) {
                MB_CODIGO = Resultado.getInt(1);
                //System.out.println("Cï¿½DIGO:" + MB_CODIGO);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
        if (MB_CODIGO == 0) {
            MB_CODIGO = consisteMetodoHerdado(superClasse.getPackage().getName(), Classe1, Metodo, CF_CODIGO);
        }
        return MB_CODIGO;
    }
}

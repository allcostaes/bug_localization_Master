package relatorios;

import bdados.Conexao;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.io.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.view.*;
import org.openide.util.Exceptions;

public class RelatoriosLSI extends JFrame {

    static Conexao Conexao;
    static Statement Comando;


    private Conexao getConexao()
    {
        return this.Conexao;
    }

    public RelatoriosLSI(String fileName) {
        this(fileName, null);
    }

    public RelatoriosLSI(String fileName, HashMap parameter) {
        super("View Report");
       Conexao = new Conexao();
        try {
            Comando = Conexao.conexao.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }

            JasperPrint print=null;

        try {
            print = JasperFillManager.fillReport(fileName, parameter, getConexao().conexao);
        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        }
        setBounds(10, 10, 600, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}

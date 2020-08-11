/*github
* */


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class Main extends JFrame {

    private JLabel clientNoLabel;
    private JLabel clientNameLabel;
    private JLabel loanAmountLabel;
    private JLabel numberOfYearsLabel;
    private JLabel typeOfLoanLabel;


    private JTextField nameTxtField;
    private JTextField numberTextField;
    private JTextField loanAmountTxtField;
    private JTextField yearsTxtField;

    private JComboBox loanTypeComboBx;

    private JScrollPane scrollPane;
    private JTable table;

    private JScrollPane secondScrollPane;
    private JTable secondTable;

    private JButton bAdd;
    private JButton bedit;
    private JButton bdelete;

    Connection con;
    PreparedStatement putin;
    String number;

    public Main(){
        initComponents();
    }

    private void initComponents(){

        clientNoLabel = new JLabel();
        clientNameLabel = new JLabel();
        loanAmountLabel = new JLabel();
        numberOfYearsLabel = new JLabel();
        typeOfLoanLabel = new JLabel();

        nameTxtField = new JTextField();
        numberTextField = new JTextField();
        loanAmountTxtField = new JTextField();
        yearsTxtField = new JTextField();

        Vector items = new Vector();
        items.add("Business");
        items.add("Personal");
        loanTypeComboBx = new JComboBox(items);

        scrollPane = new JScrollPane();
        table = new JTable();

        secondScrollPane = new JScrollPane();
        secondTable = new JTable();

        bAdd = new JButton();
        bedit = new JButton();
        bdelete = new JButton();

        var contentPane = getContentPane();
        /*contentPane.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));*/

        contentPane.setLayout(new MigLayout());
        clientNoLabel.setText("Enter the client number: ");
        contentPane.add(clientNoLabel, "spanx 3");
        contentPane.add(numberTextField, "spanx 4, growx 1, align right, wrap");

        clientNameLabel.setText("Enter the client name: ");
        contentPane.add(clientNameLabel, "spanx 3");
        contentPane.add(nameTxtField, "spanx 4, growx 1, align right, wrap");

        loanAmountLabel.setText("Enter the client loan amount: ");
        contentPane.add(loanAmountLabel, "spanx 3");
        contentPane.add(loanAmountTxtField, "spanx 4, growx 1, align right, wrap");

        numberOfYearsLabel.setText("Enter the number of years to pay: ");
        contentPane.add(numberOfYearsLabel, "spanx 3");
        contentPane.add(yearsTxtField, "spanx 4, growx 1, align right, wrap");

        typeOfLoanLabel.setText("Enter the loan type: ");
        contentPane.add(typeOfLoanLabel, "spanx 3");
        contentPane.add(loanTypeComboBx, "spanx 4, growx 1, align right, wrap");

        {
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    table_MouseClicked(e);
                }
            });
            scrollPane.setViewportView(table);
        }
        contentPane.add(scrollPane, "spanx 3 ,height 100:100:100");

        {
            secondTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //table_MouseClicked(e);
                }
            });
            secondScrollPane.setViewportView(secondTable);
        }
        contentPane.add(secondScrollPane, "spanx 3 ,height 100:100:100, wrap");

        bAdd.setText("Add");
        bAdd.addActionListener(e -> {
            try {
                button_addActionPerformed(e);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        contentPane.add(bAdd, "width 150:150:150");

        bedit.setText("Edit");
        bedit.addActionListener(e -> {
            try {
                button_editActionPerformed(e);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        contentPane.add(bedit, "width 150:150:150");

        bdelete.setText("Delete");
        bdelete.addActionListener(e -> {
            try {
                button_deleteActionPerformed(e);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        contentPane.add(bdelete, "width 150:150:150");

        pack();
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private void button_addActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        String number, name, years, loanamount, loantype;

        number = numberTextField.getText();
        name = nameTxtField.getText();
        loanamount = loanAmountTxtField.getText();
        years = yearsTxtField.getText();
        loantype = loanTypeComboBx.getSelectedItem() + "";

        LoanP loans = new LoanP(number, name, Double.parseDouble(loanamount), Integer.parseInt(years), loantype);

        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan?serverTimezone=UTC","root","");

        if(e.getSource()== bAdd) {
            putin = con.prepareStatement("Select * from loantable where clientno = ?");
            putin.setString(1, number);

            ResultSet rs = putin.executeQuery();

            if(rs.isBeforeFirst()){
                JOptionPane.showMessageDialog(null,"The client number you are trying to enter already exists ");

                nameTxtField.setText("");
                numberTextField.setText("");
                yearsTxtField.setText("");
                loanAmountTxtField.setText("");
                loanTypeComboBx.setSelectedIndex(0);
                numberTextField.requestFocus();
                return;
            }


            putin = con.prepareStatement("insert into loantable values(?,?,?,?,?)");

            putin.setString(1,loans.clientno);
            putin.setString(2, loans.clientname);
            putin.setDouble(3, loans.loanamount);
            putin.setInt(4, loans.years);
            putin.setString(5, loans.loantype);

            putin.executeUpdate();

            JOptionPane.showMessageDialog(null, "Record added");

            nameTxtField.setText("");
            numberTextField.setText("");
            yearsTxtField.setText("");
            loanAmountTxtField.setText("");
            loanTypeComboBx.setSelectedIndex(0);
            numberTextField.requestFocus();
            loadTableModel();


        }
    }
    private void button_editActionPerformed(ActionEvent e) throws SQLException, ClassNotFoundException {

        String name, number, years, loan, loantype;

        name = nameTxtField.getText();
        number = numberTextField.getText();
        years = yearsTxtField.getText();
        loan = loanAmountTxtField.getText();
        loantype = loanTypeComboBx.getSelectedItem() + "";

        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan?serverTimezone=UTC","root","");

        putin = con.prepareStatement("update loantable set clientno=?,clientname=?,loanamount=?,years=?,loantype=? where clientno=?");

        putin.setString(1, number);
        putin.setString(2, name);
        putin.setString(3, loan);
        putin.setString(4, years);
        putin.setString(5, loantype);

        putin.setString(6, this.number);

        putin.executeUpdate();

        JOptionPane.showMessageDialog(null, "Record edited");

        nameTxtField.setText("");
        numberTextField.setText("");
        yearsTxtField.setText("");
        loanAmountTxtField.setText("");
        loanTypeComboBx.setSelectedIndex(0);
        numberTextField.requestFocus();

        loadTableModel();
    }

    private void button_deleteActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan?serverTimezone=UTC","root","");

        int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete?", "Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(result == JOptionPane.YES_OPTION){

            putin = con.prepareStatement("delete from loantable where clientno =?");

            putin.setString(1, this.number);

        }

        putin.execute();

        JOptionPane.showMessageDialog(null, "Record deleted");

        nameTxtField.setText("");
        numberTextField.setText("");
        yearsTxtField.setText("");
        loanAmountTxtField.setText("");
        loanTypeComboBx.setSelectedIndex(0);
        numberTextField.requestFocus();

        loadTableModel();
    }

    private void table_MouseClicked(MouseEvent e) {

        DefaultTableModel df = (DefaultTableModel) table.getModel();

        int index1 = table.getSelectedRow();

        numberTextField.setText(df.getValueAt(index1,0).toString());
        this.number = numberTextField.getText();

        nameTxtField.setText(df.getValueAt(index1,1).toString());
        loanAmountTxtField.setText(df.getValueAt(index1,2).toString());
        yearsTxtField.setText(df.getValueAt(index1,3).toString());

        generateTable generateTable;

        switch (df.getValueAt(index1,4).toString()){
            case "Business":
                loanTypeComboBx.setSelectedIndex(0);
                generateTable = new Business(this.number, nameTxtField.getText(), Double.parseDouble(loanAmountTxtField.getText()), Integer.parseInt(yearsTxtField.getText()), "Business");
                generateTable.generateTable(secondTable);
                break;
            case "Personal":
                loanTypeComboBx.setSelectedIndex(1);
                generateTable = new Personal(this.number, nameTxtField.getText(), Double.parseDouble(loanAmountTxtField.getText()), Integer.parseInt(yearsTxtField.getText()), "Personal");
                generateTable.generateTable(secondTable);
                break;
        }

    }

    public void loadTableModel() throws ClassNotFoundException, SQLException {

        int c;
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan?serverTimezone=UTC","root","");

        putin = con.prepareStatement("Select * from loantable");

        ResultSet rs = putin.executeQuery();

        ResultSetMetaData Res = rs.getMetaData();
        c = Res.getColumnCount();
        DefaultTableModel df = (DefaultTableModel) table.getModel();
        df.setRowCount(0);

        while(rs.next()) {
            Vector v2 = new Vector();

            for(int a =1;a<=c;a++){
                v2.add(rs.getString("clientno"));
                v2.add(rs.getString("clientname"));
                v2.add(rs.getString("loanamount"));
                v2.add(rs.getString("years"));
                v2.add(rs.getString("loantype"));

            }
            df.addRow(v2);
        }
    }

    public void setTableModel(){
        String[] cols = {"Number", "Name", "Amount", "Years", "Type of Loan"};
        String[][] data = {{"d1", "d1.1"},{"d2", "d2.1"},{"d3", "d3.1"},{"d4", "d2.4"},{"d5", "d5.1"}};
        DefaultTableModel model = new DefaultTableModel(data, cols);
        table.setModel(model);
    }

    public void setTableModel2(){
        String[] cols = {"Payment", "Principal", "Interest", "Monthly Payment", "Balance"};
        DefaultTableModel model = new DefaultTableModel(null, cols);
        secondTable.setModel(model);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Main frame =  new Main();

        frame.setTableModel();
        frame.setTableModel2();
        frame.loadTableModel();

        frame.setVisible(true);
    }
}

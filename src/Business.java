import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.Vector;

public class Business extends LoanP implements generateTable {
    double monthlyRate = 0.09;

    public Business(String clientno, String clientname, double loanamount, int years, String loantype) {
        super(clientno, clientname, loanamount, years, loantype);
    }

    @Override
    public void generateTable(JTable table) {
        DefaultTableModel dt = (DefaultTableModel) table.getModel();
        dt.setRowCount(0);

        double loan = this.loanamount;
        int years = this.years;

        double Balance = loan;
//        double monthlyPayment = (loanamount* monthlyRate) / (1-Math.pow(1+monthlyRate, -termInMonths));

        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i <= years; i++) {
            Vector v2 = new Vector();
            v2.add(i);
            v2.add(monthlyRate * Balance);
            //v2.add(monthlyPayment-monthlyRate)
            v2.add(df.format(Balance));
            v2.add(df.format(Balance * monthlyRate));
            v2.add(df.format(Balance + (Balance - monthlyRate)));

            Balance = Balance + (Balance - monthlyRate);

            dt.addRow(v2);
        }
    }
}

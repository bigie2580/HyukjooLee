import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.Vector;

public class Personal extends LoanP implements generateTable {
    double INTEREST_RATE = 0.06;

    public Personal(String clientno, String clientname, double loanamount, int years, String loantype) {
        super(clientno, clientname, loanamount, years, loantype);
    }

    @Override
    public void generateTable(JTable table) {
        DefaultTableModel dt = (DefaultTableModel) table.getModel();
        dt.setRowCount(0);

        double loan = this.loanamount;
        int years = this.years;
        double Balance = loan;

        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 1; i <= years; i++) {
            Vector v2 = new Vector();

            v2.add(i);
            v2.add(df.format(Balance));
            v2.add(df.format(Balance * INTEREST_RATE));
            v2.add(df.format(Balance + (Balance * INTEREST_RATE)));

            Balance = Balance + (Balance * INTEREST_RATE);

            dt.addRow(v2);
        }
    }
}

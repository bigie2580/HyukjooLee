public class LoanP {
    String clientno;
    String clientname;
    double loanamount;
    int years;
    String loantype;

    public LoanP(String clientno, String clientname, double loanamount, int years, String loantype) {
        this.clientno = clientno;
        this.clientname = clientname;
        this.loanamount = loanamount;
        this.years = years;
        this.loantype = loantype;
    }
}

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tax {


    private static final double INSURANCE_CONTRIBUTION_PERCENTAGE = initializeInsuranceContributionPercentage();
    private static final double MAX_INSURANCE_CONTRIBUTION_AMOUNT = initializeMaxInsuranceContributionAmount();


    //private static double initializeInsuranceContributionPercentage() { return 0.13867; }
    private static double initializeInsuranceContributionPercentage() {
        final String url = "jdbc:postgresql://localhost/aftertax";
        final String user = "aftertax";
        final String password = "aftertax";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            Statement statement = connection.createStatement();
            String selectRecords = "SELECT VALUE FROM PARAMETERS WHERE CODE = 'INSUR_CONTR_PERC'";
            ResultSet resultSet = statement.executeQuery(selectRecords);
            resultSet.next();
            //resultSet.close();
            //connection.close();
            return resultSet.getDouble("VALUE");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1.0;
        }
    }    

    //private static double initializeMaxInsuranceContributionAmount() { return 13836.1; }
    private static double initializeMaxInsuranceContributionAmount() {
        final String url = "jdbc:postgresql://localhost/aftertax";
        final String user = "aftertax";
        final String password = "aftertax";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            Statement statement = connection.createStatement();
            String selectRecords = "SELECT VALUE FROM PARAMETERS WHERE CODE = 'MAX_INSUR_CONTR_AMT'";
            ResultSet resultSet = statement.executeQuery(selectRecords);
            resultSet.next();
            //resultSet.close();
            //connection.close();
            return resultSet.getDouble("VALUE");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1.0;
        }
    }    


    //private static final double MAX_SCALE_1_TAX_AMOUNT = 10000 * 0.09;
    //private static final double MAX_SCALE_2_TAX_AMOUNT = 10000 * 0.09;
    //private static final double MAX_SCALE_3_TAX_AMOUNT = 10000 * 0.09;
    //private static final double MAX_SCALE_4_TAX_AMOUNT = 10000 * 0.09;
    private static final double [] taxPercentage= {0.09, 0.22, 0.28, 0.36, 0.44};

    /** 
     * Calculate the amount of money directed towards insurance contributions given the gross (pretax) annual earnings 
    */
    public static double caclculateAnnualInsuranceContributions(double grossAnnualAmount) {
        double temp = new BigDecimal(Double.toString(grossAnnualAmount * (INSURANCE_CONTRIBUTION_PERCENTAGE))).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return (temp > MAX_INSURANCE_CONTRIBUTION_AMOUNT) ? MAX_INSURANCE_CONTRIBUTION_AMOUNT : temp;
    }
    
    public static double calculateAnnualTaxableAmount (double grossAnnualAmount) {
        return calculateAnnualTaxableAmount(grossAnnualAmount, caclculateAnnualInsuranceContributions(grossAnnualAmount));
    }

    public static double calculateAnnualTaxableAmount (double grossAnnualAmount, double annualInsuranceContributionAmount) {
        double temp = grossAnnualAmount - annualInsuranceContributionAmount;
        return new BigDecimal(Double.toString(temp)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    public static double calculateAnnualTax (double grossAnnualAmount) {
        return calculateAnnualTax2(calculateAnnualTaxableAmount (grossAnnualAmount));
    }


    public static double calculateAnnualTax2 (double annualTaxableAmount) {
        if (annualTaxableAmount < 10000)    return 0.0;

        double remainingAmount = annualTaxableAmount;
        int taxScale = 0;
        double taxAmount = 0.0;
        while (remainingAmount > 10000.0 && taxScale <=3) {
            taxAmount += 10000.0 * taxPercentage[taxScale];
            remainingAmount -= 10000.0;
            taxScale ++;
        }
        taxAmount += remainingAmount * taxPercentage[taxScale];

        return new BigDecimal(Double.toString(taxAmount - calculateAnnualTaxDiscount(annualTaxableAmount))).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    public static double calculateAnnualTaxDiscount(double annualTaxableAmount) {
        return Math.max(0.0, (double) (777 - (20 * ( (int) ((annualTaxableAmount - 12000.0) / 1000))))); 
    }

    /*
    public static void main(String[] args) {
        System.out.println(MAX_INSURANCE_CONTRIBUTION_AMOUNT);
        System.out.println(INSURANCE_CONTRIBUTION_PERCENTAGE);
    }
    */
}

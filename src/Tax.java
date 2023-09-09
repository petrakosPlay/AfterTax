import java.math.BigDecimal;
import java.math.RoundingMode;

public class Tax {


    private static final double INSURANCE_CONTRIBUTION_PERCENTAGE = 0.13867;
    private static final double MAX_INSURANCE_CONTRIBUTION_AMOUNT = 7126.95;
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
        System.out.println("Gross: " + grossAnnualAmount + " temp: " + temp);
        return (temp > MAX_INSURANCE_CONTRIBUTION_AMOUNT) ? MAX_INSURANCE_CONTRIBUTION_AMOUNT : temp;
    }
    
    public static double calculateAnnualTaxableAmount (double grossAnnualAmount) {
        return calculateAnnualTaxableAmount(grossAnnualAmount, caclculateAnnualInsuranceContributions(grossAnnualAmount));
    }

    public static double calculateAnnualTaxableAmount (double grossAnnualAmount, double annualInsuranceContributionAmount) {
        double temp = grossAnnualAmount - annualInsuranceContributionAmount;
        return new BigDecimal(Double.toString(temp)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    public static double calculateAnnualTax (double annualTaxableAmount) {
        if (annualTaxableAmount < 10000)    return 0.0;

        double remainingAmount = annualTaxableAmount;
        int taxScale = 0;
        double taxAmount = 0;
        while (remainingAmount > 10000.0 && taxScale <=3) {
            taxAmount += 10000.0 * taxPercentage[taxScale];
            remainingAmount -= 10000.0;
            taxScale ++;
        }
        taxAmount += remainingAmount * taxPercentage[taxScale];
        return taxAmount;

    }
    
}

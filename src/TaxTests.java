import java.math.BigDecimal;
import java.math.RoundingMode;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;



public class TaxTests {
    
    private static final double MAX_INSURANCE_CONTRIBUTION_AMOUNT = 7126.95;
    private static final double INSURANCE_CONTRIBUTION_PERCENTAGE = 0.13867;
    private static final double MIN_GROSS_AMT = 100.0;
    double grossAmountThreshold = new BigDecimal(Double.toString(MAX_INSURANCE_CONTRIBUTION_AMOUNT / INSURANCE_CONTRIBUTION_PERCENTAGE)).setScale(2, RoundingMode.HALF_UP).doubleValue();


    /**
     * This method tests for cases where the gross income should produce tax insurance contribution that is less than the maximum amount.
    */
    @Test
    public void testForAmountsThatProduceLessThanMaxInsuranceContribution () {
        double testAmount = Math.random() * (grossAmountThreshold - MIN_GROSS_AMT) + MIN_GROSS_AMT;
        Assertions.assertTrue(Tax.caclculateAnnualInsuranceContributions(testAmount) < MAX_INSURANCE_CONTRIBUTION_AMOUNT);
    }

    /**
     * This method tests for cases where the gross income should produce tax insurance contribution that is equal to the maximum amount.
    */
    @Test
    public void testForAmountsThatProduceMoreThanMaxInsuranceContribution () {
        double testAmount = Math.random() * grossAmountThreshold + grossAmountThreshold + 1;
        Assertions.assertEquals(Tax.caclculateAnnualInsuranceContributions(testAmount), MAX_INSURANCE_CONTRIBUTION_AMOUNT);
    }

    @Test
    public void calculateAnnualTaxableAmountTestScenario1() {
        Assertions.assertEquals(10000.0, Tax.calculateAnnualTaxableAmount(13000.0, 3000.0));
    }

    @Test
    public void calculateAnnualTaxableAmountTestScenario2() {
        Assertions.assertEquals(23704.8, Tax.calculateAnnualTaxableAmount(26600.123, 2895.328));
    }


}

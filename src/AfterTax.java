import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AfterTax extends Application {

    
    private static final double INSURANCE_CONTRIBUTION_PERCENTAGE = 0.13867;
    private static final double MAX_INSURANCE_CONTRIBUTION_AMOUNT = 7126.95;
    //private static final double MAX_SCALE_1_TAX_AMOUNT = 10000 * 0.09;
    //private static final double MAX_SCALE_2_TAX_AMOUNT = 10000 * 0.09;
    //private static final double MAX_SCALE_3_TAX_AMOUNT = 10000 * 0.09;
    //private static final double MAX_SCALE_4_TAX_AMOUNT = 10000 * 0.09;
    private static final double [] taxPercentage= {0.09, 0.22, 0.28, 0.36, 0.44};
    
    
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("AfterTax");
        stage.setResizable(true);
        stage.getIcons().add(0, new Image("AfterTax.png"));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle (WindowEvent we) {
                //we.consume();
                //System.out.println(we.toString());
                System.out.println("Exiting...");
                Platform.exit();
                System.exit(0);
            }
        });

        
        Label messageLabel = new Label();
        messageLabel.setLayoutX(100);
        messageLabel.setLayoutY(200);
        messageLabel.setVisible(false);

        Text amountText = new Text(); 
        amountText.setText("Enter amount:");
        amountText.setFont(new Font(24));
        amountText.setX(100);
        amountText.setY(100);
        
        TextField amountTextField = new TextField();
        amountTextField.setLayoutX(100);
        amountTextField.setLayoutY(120);        

        Button submitButton = new Button("Submit");
        submitButton.setLayoutX(100);
        submitButton.setLayoutY(150);



        amountTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if(ke.getCode() == KeyCode.ENTER)   submitButton.fire();
            }    
        });


        submitButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle (ActionEvent event) {
                try {
                    messageLabel.setText("");
                    messageLabel.setVisible(false);
                    double grossAnnualAmount = Double.parseDouble(amountTextField.getText());
                    double annualInsuranceContributionAmount = caclculateAnnualInsuranceContributions(grossAnnualAmount);
                    double annualTaxableAmount = calculateAnnualTaxableAmount(grossAnnualAmount); 
                    double annualTax = calculateAnnualTax(annualTaxableAmount);
                    messageLabel.setText("The annual insurance contributon amount is: " + String.format("%.2f", annualInsuranceContributionAmount) + "\n" +
                                         "The annual taxable amount is: " + String.format("%.2f", annualTaxableAmount) + "\n" +
                                         "The annual tax is: " + String.format("%.2f", annualTax) + "\n" +
                                         "The net income is: " + String.format("%.2f", annualTaxableAmount - annualTax));
                    messageLabel.setFont(new Font(24));
                    messageLabel.setTextFill(Color.BLUE);
                    messageLabel.setVisible(true);
                } catch (NumberFormatException nfe) {
                    messageLabel.setText("Please provide a decimal value");
                    messageLabel.setFont(new Font(24));
                    messageLabel.setTextFill(Color.RED);
                    messageLabel.setVisible(true);
                } finally {
                    amountTextField.clear();
                }              
           } 
        });

        Group group = new Group();
        group.getChildren().add(0, amountText);
        group.getChildren().add(1, amountTextField);
        group.getChildren().add(2, submitButton);
        group.getChildren().add(3, messageLabel);

        
        //Scene scene = new Scene(group, 1000, 600, Color.BLUEVIOLET);
        Scene scene = new Scene(group, 1000, 600);
        stage.setScene(scene);        
        stage.show();

    }

    /** 
     * Calculate the amount of money directed towards insurance contributions given the gross (pretax) annual earnings 
    */
    private double caclculateAnnualInsuranceContributions(double grossAnnualAmount) {
        double temp = new BigDecimal(Double.toString(grossAnnualAmount * (INSURANCE_CONTRIBUTION_PERCENTAGE))).setScale(2, RoundingMode.HALF_UP).doubleValue();
        System.out.println("Gross: " + grossAnnualAmount + " temp: " + temp);
        return (temp > MAX_INSURANCE_CONTRIBUTION_AMOUNT) ? MAX_INSURANCE_CONTRIBUTION_AMOUNT : temp;
    }
    
    private double calculateAnnualTaxableAmount (double grossAnnualAmount) {
        return calculateAnnualTaxableAmount(grossAnnualAmount, caclculateAnnualInsuranceContributions(grossAnnualAmount));
    }

    private double calculateAnnualTaxableAmount (double grossAnnualAmount, double annualInsuranceContributionAmount) {
        double temp = grossAnnualAmount - annualInsuranceContributionAmount;
        return new BigDecimal(Double.toString(temp)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    private double calculateAnnualTax (double annualTaxableAmount) {
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



/*Popup popup = new Popup();
        popup.setX(100);
        popup.setY(100);
        popup.getContent().addAll(new Circle(25,25,50, Color.AZURE));
        popup.show(s);
        Thread.sleep(5000);
        popup.hide();
    }*/
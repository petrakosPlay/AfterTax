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
       
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("AfterTax");
        stage.setResizable(true);
        stage.getIcons().add(0, new Image("AfterTax.png"));
        
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

        Group group = new Group();
        group.getChildren().add(0, amountText);
        group.getChildren().add(1, amountTextField);
        group.getChildren().add(2, submitButton);
        group.getChildren().add(3, messageLabel);



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
                    double annualInsuranceContributionAmount = Tax.caclculateAnnualInsuranceContributions(grossAnnualAmount);
                    double annualTaxableAmount = Tax.calculateAnnualTaxableAmount(grossAnnualAmount); 
                    double annualTax = Tax.calculateAnnualTax(annualTaxableAmount);
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
        
        //Scene scene = new Scene(group, 1000, 600, Color.BLUEVIOLET);
        Scene scene = new Scene(group, 1000, 600);
        stage.setScene(scene);        
        stage.show();

    }
}
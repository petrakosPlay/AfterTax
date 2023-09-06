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
        amountTextField.setOnKeyPressed(null);
        

        Button submitButton = new Button("Submit");
        submitButton.setLayoutX(100);
        submitButton.setLayoutY(150);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle (ActionEvent event) {
                try {
                    int amount = Integer.parseInt(amountTextField.getText());
                    //System.out.println("You have typed '" + amount + "'");
                    int taxedAmount = calculateTax(amount);
                    messageLabel.setText("The taxable amount is: " + taxedAmount);
                    messageLabel.setVisible(true);

                } catch (NumberFormatException nfe) {
                    System.out.println("Please try again");                 
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

    private int calculateTax (int amount) {
        //System.out.println("Received " + amount);
        return (int) (amount * 0.75);
        /*Popup popup = new Popup();
        popup.setX(100);
        popup.setY(100);
        popup.getContent().addAll(new Circle(25,25,50, Color.AZURE));
        popup.show(s);
        Thread.sleep(5000);
        popup.hide();*/

    
    }
}

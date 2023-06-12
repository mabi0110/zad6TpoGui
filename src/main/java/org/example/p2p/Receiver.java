package org.example.p2p;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Receiver extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Receiver");
        stage.setWidth(300);
        stage.setHeight(300);
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox();
        Button recieveButton = new Button("Get message");
        Label messageLabel = new Label("Message: ");
        TextField messageField = new TextField();
        vBox.getChildren().addAll(recieveButton, messageLabel, messageField);

        recieveButton.setOnAction(event -> {
            messageField.setText(getMessage());
        });

        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public static String getMessage() {
        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) initialContext.lookup("queue1");
            MessageConsumer consumer1 = session.createConsumer(queue);

            connection.start();
            TextMessage messageReceived = (TextMessage) consumer1.receive(3000);
            System.out.println("Message received: " + messageReceived.getText());
            return messageReceived.getText();
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

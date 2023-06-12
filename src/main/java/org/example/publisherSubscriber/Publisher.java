package org.example.publisherSubscriber;


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

public class Publisher extends Application {
    static String messageToSend = null;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Publisher");
        stage.setWidth(300);
        stage.setHeight(300);
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox();
        Label messageLabel = new Label("Enter message to publish");
        TextField messageField = new TextField();
        Button sendButton = new Button("Publish message");
        vBox.getChildren().addAll(messageLabel, messageField, sendButton);

        sendButton.setOnAction(event -> {
            messageToSend = messageField.getText();
            sendMessage(messageToSend);
            messageField.setText("");
        });

        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }



    public static void sendMessage(String messageToSend) {
        InitialContext initialContext = null;
        Connection connection = null;
        try {
            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) initialContext.lookup("topic1");
            MessageProducer producer = session.createProducer(topic);

            connection.start();
            TextMessage message = session.createTextMessage(messageToSend);
            producer.send(message);
            System.out.println("Message send: " + message.getText());
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







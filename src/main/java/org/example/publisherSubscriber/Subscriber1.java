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

public class Subscriber1 {

    public static void main(String[] args) {
        getMessage();
    }


    public static String getMessage() {
        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) initialContext.lookup("topic1");
            MessageConsumer consumer1 = session.createConsumer(topic);

            connection.start();
            TextMessage messageReceived = (TextMessage) consumer1.receive();
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

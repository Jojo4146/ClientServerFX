package com.jojo.clientserverfx;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage) {
        // BorderPane to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: LIGHTBLUE");
        paneForTextField.setLeft(new Label("Enter number to find if it's prime: "));
        // Textfield
        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);
        // BorderPane
        BorderPane mainPane = new BorderPane();
        // Text area to display content
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client"); // Stage title
        primaryStage.setScene(scene); // Scene in the stage
        primaryStage.show(); // Display the stage

        // Client actions
        tf.setOnAction(e -> {
            try {
                // Get the user's number
                int user = Integer.parseInt(tf.getText().trim());
                // Send the user's number to the server
                toServer.writeInt(user);
                toServer.flush();
                // Get if number is prime
                boolean isPrime = fromServer.readBoolean();
                // Display in the Client GUI if number is prime or not
                ta.appendText("Is number " + user + " prime?\n");
                if(!isPrime){
                    ta.appendText("Yes\n");
                } else {
                    ta.appendText("No\n");
                }
            }
            catch (Throwable throwable) {
                System.err.println(throwable);
            }
        });

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }
    }

    /**
     * main method
     *
     */
    public static void main(String[] args) {
        launch(args);
    }
}

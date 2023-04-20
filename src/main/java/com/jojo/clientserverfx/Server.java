package com.jojo.clientserverfx;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();
        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the title
        primaryStage.setScene(scene); // Place the stage
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        ta.appendText("New Server on " + new Date() + '\n'));

                // Listen for a connection request
                Socket socket = serverSocket.accept();

                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());
                // From Client
                while (true) {
                    // Receive number from the client side
                    int user = inputFromClient.readInt();
                    // Formula for if number is prime or not
                    boolean isPrime = IntStream.rangeClosed(2, user / 2).anyMatch(i -> user % i == 0);
                    outputToClient.writeBoolean(isPrime);
                    Platform.runLater(() -> {   // Prints in the Server side
                        if(!isPrime){
                            ta.appendText(user + "? Yes, is prime.\n");
                        } else {
                            ta.appendText(user + "? No, is not prime.\n");
                        }
                    });
                }
            }
            catch(Throwable ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args){

        launch(args);
    }
}

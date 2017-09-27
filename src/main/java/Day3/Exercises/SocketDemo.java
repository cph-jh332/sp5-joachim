/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Day3.Exercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author craci
 */
public class SocketDemo {

    private ServerSocket serverSocket;

    public static void main(String[] args) {
        SocketDemo server = new SocketDemo();
        server.start(6666);
    }
    
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new EchoClientHandler(serverSocket.accept()).run();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class EchoClientHandler extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public EchoClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine = "";
            boolean running = true;
            out.println("Write any of these commands and exit to exit");
            out.println("UPPER#Hello World");
            out.println("LOWER#Hello World");
            out.println("REVERSE#abcd");
            out.println("TRANSLATE#hund");
            out.println("input any message (will just respond with the same text)");
            while (running && (inputLine = in.readLine()) != null) {
                switch(inputLine){
                    case "UPPER#Hello World":{
                        out.println("HELLO WORLD");
                        break;
                    }
                    
                    case "LOWER#Hello World":{
                        out.println("hello world");
                        break;
                    }
                    
                    case "REVERSE#abcd":{
                        out.println("dcba");
                        break;
                    }
                    
                    case "TRANSLATE#hund":{
                        out.println("dog");
                        break;
                    }
                    
                    case "exit":{
                        out.println("bye");
                        running = false;
                        break;
                    }
                    
                    default:{
                        out.println(inputLine);
                        break;
                    }
                        
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketDemo.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExamPrep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author craci
 */
public class Socket {

    private ServerSocket serverSocket;

    ExecutorService es = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        Socket server = new Socket();
        server.start(7777);
    }

    public void start(int port) {

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                es.execute(new EchoClientHandler(serverSocket.accept()));
            }
        } catch (IOException ex) {
            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class EchoClientHandler extends Thread {

    Turnstile turnstile = new Turnstile();
    private java.net.Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public EchoClientHandler(java.net.Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            out.println("turnstile or monitor");
            String answer = in.readLine();

            switch (answer) {
                case "turnstile": {
                    out.println("write turn to count people and exit to exit");
                    boolean running = true;
                    while (running) {
                        String toDo = in.readLine();
                        if (toDo.equalsIgnoreCase("turn")) {
                            turnstile.count();
                            System.out.println(Turnstile.peopleCounter);
                        } else if (toDo.equalsIgnoreCase("exit")) {
                            running = false;
                        } else {
                            out.println("that is not a command, try again");
                        }
                    }
                    break;
                }

                case "monitor": {

                    Thread t1 = new Thread(() -> {
                        while (true) {
                            out.println(Turnstile.peopleCounter);
                            try {
                                sleep(3000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    t1.start();
                    try {
                        boolean running = true;
                        while (running) {
                            if (in.readLine().equalsIgnoreCase("exit")) {
                                t1.stop();
                                running = false;
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

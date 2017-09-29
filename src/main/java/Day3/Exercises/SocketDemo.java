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
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
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
public class SocketDemo {

    private ServerSocket serverSocket;

    ExecutorService es = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        SocketDemo server = new SocketDemo();
        server.start(6767);
    }

    public void start(int port) {

        try {
            serverSocket = new ServerSocket(port);
            Thread t1 = new Thread(() -> {
                while (true) {
                    Future<String> f1 = es.submit(new ReadingMessages());
                    try {
                        
                        EchoClientHandler.message = f1.get();
                        

                        sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ExecutionException ex) {
                        Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t1.start();
            while (true) {
                es.execute(new EchoClientHandler(serverSocket.accept()));
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

    ExecutorService es = Executors.newCachedThreadPool();
    static String message = "";
    static BlockingQueue<String> messages = new ArrayBlockingQueue(100);
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
            Thread t1 = new Thread(() -> {
                String temp = "";
                while (true) {
                    try {
                        if (!temp.equalsIgnoreCase(message)) {
                            out.println(message);
                            temp = message;
                        }

                        sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t1.start();
            while (running && (inputLine = in.readLine()) != null) {
                String[] sArray = inputLine.split("#");
                String command = sArray[0];
                switch (command.toLowerCase()) {
                    case "upper": {
                        try {
                            //out.println("HELLO WORLD");
                            messages.put(sArray[1].toUpperCase());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }

                    case "lower": {
                        try {
                            //out.println("hello world");
                            messages.put(sArray[1].toLowerCase());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }

                    case "reverse": {
                        try {
                            //out.println("dcba");
                            StringBuilder sb = new StringBuilder();
                            sb.append(sArray[1]);
                            messages.put(sb.reverse().toString());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }

                    case "translate": {
                        try {
                            //out.println("dog");
                            if (sArray[1].toLowerCase().equals("hund")) {
                                messages.put("dog");
                            } else {
                                messages.put("can't translate: " + sArray[1]);
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }

                    case "exit": {
                        out.println("bye");
                        t1.stop();
                        running = false;
                        break;
                    }

                    default: {
                        try {
                            //out.println(inputLine);
                            messages.put(inputLine);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
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

class ReadingMessages implements Callable {

    @Override
    public Object call() throws Exception {
        return EchoClientHandler.messages.take();
    }

}

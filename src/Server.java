import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    static int clientCounter = 1;

    public static int GetClientNumber(){
        return clientCounter++;
    }

    public static void main(String[] args) {

        final ServerSocket serverSocket;
        final Socket clientSocket;
        final BufferedReader userInput;
        final PrintWriter serverOutput;
        final Scanner input = new Scanner(System.in);

        final int PORT = 8888;


        try {
            //Instantiate new Server socket and listen for incoming connections on PORT
            serverSocket = new ServerSocket(PORT);
            System.out.println("***** SERVER ON PORT " + PORT + " IS OPEN *****");

            clientSocket = serverSocket.accept();
            System.out.println("CLIENT " + clientSocket.getInetAddress() + " has connected");

            userInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Reads data from user as bytes and converts them into text
            serverOutput = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            serverOutput.println("Welcome user " + GetClientNumber());
            serverOutput.println("Write a message: ");

            Thread serverSenderThread = new Thread(new Runnable() {
                String userMessage;

                @Override
                public void run() {
                    while (true){
                        System.out.println("---> ");
                        serverOutput.println(userMessage);
                        userMessage = input.nextLine();
                        serverOutput.flush();
                    }
                }
            });
            serverSenderThread.start();

            Thread serverReceiveThread = new Thread(new Runnable() {
                String userMessage;

                @Override
                public void run() {
                    try {
                        userMessage = userInput.readLine();

                        while (userMessage != null){
                            System.out.println("Client " + GetClientNumber() + " " + clientSocket.getInetAddress() + " says: " + userMessage);
                            userMessage = userInput.readLine();
                        }

                        System.out.println("Client has disconected");

                        serverOutput.close();
                        clientSocket.close();
                        serverSocket.close();

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            serverReceiveThread.start();


        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

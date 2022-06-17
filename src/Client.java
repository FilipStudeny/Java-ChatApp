import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        final Socket clientSocket;
        final BufferedReader serverInput;
        final PrintWriter userInput;
        final Scanner input = new Scanner(System.in);

        final String hostAdress = "127.0.0.1";
        final int PORT = 8888;

        try {
            clientSocket = new Socket(hostAdress, PORT);
            System.out.println("***** Connected to the server *****");

            userInput = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread clientSendThread = new Thread(new Runnable() {
                String userMessage;

                @Override
                public void run() {
                    while (true){
                        System.out.print("---> ");
                        userMessage = input.nextLine();
                        userInput.println(userMessage);
                        userInput.flush();
                    }
                }
            });
            clientSendThread.start();

            Thread clientReceiveThread = new Thread(new Runnable() {
                String serverMessage;

                @Override
                public void run() {
                    try {
                        serverMessage = serverInput.readLine();

                        while (serverMessage != null){
                            serverMessage = serverInput.readLine();
                            System.out.println("Server says: " + serverMessage);

                        }

                        System.out.println("***** SERVER IS NOT ACTIVE *****");
                        userInput.close();
                        clientSocket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            clientReceiveThread.start();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

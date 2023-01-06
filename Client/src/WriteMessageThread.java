import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteMessageThread extends Thread {

    private PrintWriter writer;
    private OutputStream outputStream;
    private Socket socket;
    private Client client;

    public WriteMessageThread(Socket socket, Client client){
        this.socket = socket;
        this.client = client;

    }

    @Override
    public void run() {
        try{
            this.outputStream = this.socket.getOutputStream();
            this.writer = new PrintWriter(outputStream, true);
        } catch (IOException e) {
            System.out.println("*** ERROR IN USER THREAD ***");
            System.out.println("=======> " + e.getMessage() );
        }

        Scanner input = new Scanner(System.in);

        //GET USER USERNAME
        System.out.println("Enter your username: ");
        String username = input.nextLine();
        this.client.setUsername(username);
        this.writer.println(username);

        //PRINT USERNAME AFTER RECEIVING MESSAGE
        if(this.client.getUsername() != null){
            System.out.println("-> :");
        }

        String userMessage;

        while(!this.socket.isOutputShutdown()){
            //SEND MESSAGE TO SERVER SOCKET OUTPUT STREAM
            userMessage = input.nextLine();

            if(userMessage.equals("$QUIT")){
                this.client.DisconnectFromServer(this.socket, this.writer, this.outputStream);
                try {
                    this.client.clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            this.writer.println(userMessage);
        }

        try{
            this.socket.close();
            this.writer.close();
            this.outputStream.close();

        } catch (IOException e) {
            System.out.println("*** ERROR IN USER WRITE THREAD ***");
            System.out.println("=======> " + e.getMessage() );
        }

    }

}

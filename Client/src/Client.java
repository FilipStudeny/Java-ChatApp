import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public Socket clientSocket;
    private final Scanner input = new Scanner(System.in);
    private String username;

    private final String hostAdress = "127.0.0.1";
    private final int PORT = 8888;

    public void run(){
        try{
            //GET IP ADRESS
            InetAddress userAdress = InetAddress.getByName(this.hostAdress);
            this.clientSocket = new Socket(userAdress, PORT);
            System.out.println("***** Connected to the server *****");

            //THREADS
            new WriteMessageThread(this.clientSocket, this).start();
            new ReadMessagesThread(this.clientSocket, this).start();


        } catch (IOException e) {
            System.out.println("*** ERROR IN USER  ***");
            System.out.println("=======> " + e.getMessage() );
        }
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void DisconnectFromServer(Socket socket, BufferedReader reader, InputStream inputStream){
        try{
            socket.close();
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("*** ERROR IN USER READ THREAD ***");
            System.out.println("=======> " + e.getMessage() );
        }
    }

    public void DisconnectFromServer(Socket socket, PrintWriter writer, OutputStream outputStream){
        try{
            socket.close();
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            System.out.println("*** ERROR IN USER READ THREAD ***");
            System.out.println("=======> " + e.getMessage() );
        }
    }
}

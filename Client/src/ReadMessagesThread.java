import java.io.*;
import java.net.Socket;

public class ReadMessagesThread extends Thread{

    private Socket socket;
    private Client client;
    private BufferedReader reader;
    private InputStream inputStream;

    public ReadMessagesThread(Socket socket, Client client){
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        try{
            this.inputStream = this.socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(this.inputStream));
        } catch (IOException e) {
            System.out.println("*** ERROR IN USER READ THREAD ***");
            System.out.println("=======> " + e.getMessage() );
        }

        while(!this.socket.isClosed()){
            try{

                String serverMessage = this.reader.readLine();

                System.out.println(serverMessage);

                //PRINT USERNAME AFTER RECEIVING MESSAGE
                if(this.client.getUsername() != null){
                    System.out.print("-> :");
                }

            } catch (IOException e) {
                System.out.println("*** ERROR IN USER READ asdasds THREAD ***");
                System.out.println("=======> " + e.getMessage() );
                e.printStackTrace();
                break;
            }
        }

        try{
            this.socket.close();
            this.reader.close();
            this.inputStream.close();
        } catch (IOException e) {
            System.out.println("*** ERROR IN USER READ THREAD ***");
            System.out.println("=======> " + e.getMessage() );
        }
    }


}

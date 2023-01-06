import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private User user;

    private PrintWriter writer;
    private BufferedReader bufferedReader;
    private String username;


    public UserThread(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        this.username = "Anonymous";
    }

    @Override
    public void run() {
        try{
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

            //PRINT NUM OF CONNECTED USERS
            PrintConnectedUsers();

            //READ USER USERNAME FROM CONNECTED USER
            this.username = bufferedReader.readLine();
            User user = new User(this.username, this.socket.getInetAddress());
            this.user = user;
            this.server.AddUser( user );

            //MESSAGE SEND BY SERVER INFORMING OF NEW CONNECTION
            String serverMessage = "New user connected: " + user.getUserName();
            this.server.BroadCastMessage(serverMessage,this);

            //USER MESSAGE
            String clientMessage = "";

            //Messages send by user
            while(this.socket.isConnected() && !this.socket.isClosed()){
                clientMessage = bufferedReader.readLine();

                if(clientMessage.equals("$QUIT")){
                    this.server.BroadCastToUser("$QUIT",this);
                    DisconnectUser();
                    break;
                }

                serverMessage = "[" + user.getUserName() + "]:" + clientMessage;
                this.server.BroadCastMessage(serverMessage, this);
            }

        } catch (IOException e) {
            System.out.println("*** ERROR IN USER THREAD ***");
            System.out.println("=======> " + e.getMessage() );
            e.printStackTrace();

        }finally {

            DisconnectUser();
        }
    }

    private void DisconnectUser() {

        this.server.RemoveUser(this.user, this);

        try{
            this.socket.close();
            this.writer.close();
            this.bufferedReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.server.BroadCastMessage(this.user.getUserName() + " has disconnected", this);
    }

    private void PrintConnectedUsers() {
        if(this.server.hasUsers()){
            this.writer.println("[SERVER]: Number of connected users (" + this.server.getUsers() + ")");
        }else{
            this.writer.println("No users connected");
        }
    }

    public void SendMessage(String message){
        this.writer.println(message);
    }
}

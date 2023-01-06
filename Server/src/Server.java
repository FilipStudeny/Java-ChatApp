import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Server {

    private int PORT;
    private ServerSocket serverSocket;

    private HashSet<User> connectedUsers = new HashSet<User>();
    private HashSet<UserThread> connectedClients = new HashSet<UserThread>();

    public Server(int PORT){
        this.PORT = PORT;
    }

    public void run(){

        try{
            //Instantiate new Server socket and listen for incoming connections on PORT
            this.serverSocket = new ServerSocket(this.PORT);
            System.out.println("***** SERVER ON PORT " + PORT + " IS OPEN *****");

            while(true){

                Socket clientSocket = this.serverSocket.accept();
                System.out.println("CLIENT " + clientSocket.getInetAddress() + " has connected");

                UserThread newConnection = new UserThread(clientSocket, this);
                connectedClients.add(newConnection);
                newConnection.start();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void BroadCastMessage(String message, UserThread excludeUser){
        for(UserThread user: this.connectedClients){
            if(user != excludeUser){
                user.SendMessage(message);
            }
        }
    }

    void BroadCastToUser(String message, UserThread user){
        for(UserThread userThread: this.connectedClients){
            if(userThread == user){
                userThread.SendMessage(message);
            }
        }
    }

    public void RemoveUser(User user, UserThread userThread){
        boolean userRemoved = this.connectedUsers.remove(user);

        if(userRemoved){
            this.connectedClients.remove(userThread);
            System.out.println("*** The user " + user.getUserName() + " has disconnected ! ***");
        }

    }

    void CloseServer() throws IOException {

        this.serverSocket.close();


        this.connectedClients.clear();
        this.connectedUsers.clear();
    }

    Set<User> getUsers(){
        return this.connectedUsers;
    }

    boolean hasUsers(){
        return !this.connectedUsers.isEmpty();
    }

    boolean hasUserThreads(){
        return !this.connectedClients.isEmpty();
    }

    Set<UserThread> getUserThreads(){
        return this.connectedClients;
    }
    public void AddUser(User user){
        this.connectedUsers.add(user);
    }

}

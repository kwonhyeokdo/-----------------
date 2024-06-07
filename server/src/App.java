import java.net.ServerSocket;
import java.net.Socket;

import smallcat.ServerThread;

public class App {
    public static void main(String[] args) throws Exception {
        try(ServerSocket server = new ServerSocket(8001)){
            for(;;){
                Socket socket = server.accept();

                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }
    }
}

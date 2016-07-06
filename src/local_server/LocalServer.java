package local_server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hrant on 11/15/15.
 */
public class LocalServer implements Runnable {

    private int PORT;
    private String SITES_DIR_PATH = null;
    private ServerSocket serverSocket;

    public LocalServer(int port, String SITES_DIR_PATH) {
        this.PORT = port;
        this.SITES_DIR_PATH = SITES_DIR_PATH;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new FileReader(socket, SITES_DIR_PATH)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

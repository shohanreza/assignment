package FileSender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private FileEvent fileEvent;
    private File dstFile = null;
    private FileOutputStream fileOutputStream = null;

    public Server() {

    }

    /**
          * Accepts socket connection
          */
    public void connect() {
        try {
            serverSocket = new ServerSocket(4445);
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Do Not connect with any client!!!!!!!!!");
        }
    }

    /**
          * Reading the FileEvent object and copying the file to disk.
          */
    public void receiveFile() {
        try {
            fileEvent = (FileEvent) inputStream.readObject();
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
                System.out.println("Error occured.. .. ..So exiting!!!");
                System.exit(0);
            }
            String outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename();
            if (!new File(fileEvent.getDestinationDirectory()).exists()) {
                new File(fileEvent.getDestinationDirectory()).mkdirs();
            }
            dstFile = new File(outputFile);
            fileOutputStream = new FileOutputStream(dstFile);
            fileOutputStream.write(fileEvent.getFileData());
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("Output file : " + outputFile + " is successfully saved ");
            Thread.sleep(3000);
            System.exit(0);

        } catch (Exception e) {
            System.err.println("Unsucesfull!!!!!!!!!");
        }


    }

    public static void main(String[] args) {
        Server server = new Server();
        server.connect();
        server.receiveFile();
    }
}

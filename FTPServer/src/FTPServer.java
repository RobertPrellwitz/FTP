import java.net.*;
import java.io.*;
import java.util.*;

public class FTPServer {

    public static void main(String args[]) throws Exception
    {
        FTPServer FTP = new FTPServer();
        FTP.run();

    }

    void run() throws Exception
    {
        while(true)
        {
            ServerSocket serverSock = new ServerSocket(5521);
            System.out.println("FTP Server Started on Port: 5520");
            System.out.println("Waiting For Connection ...");
            try{
            FTPThread transFile = new FTPThread(serverSock.accept());

            }
            catch(Exception except)
            {
                System.out.println("Error :" + except);
            }
        }
    }

}

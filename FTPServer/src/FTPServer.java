import com.sun.corba.se.spi.activation.Server;

import java.net.*;
import java.io.*;
import java.util.*;

public class FTPServer {

    public static void main(String args[]) throws Exception
    {

        FTPServer FTP = new FTPServer();
        FTP.run();

    }

    public void run() throws Exception
    {
        ServerSocket serverSock = new ServerSocket(5521);
        System.out.println("FTP Server Started on Port: 5520");


        while(true)
        {
            try{
                System.out.println("Waiting For Connection ...");
                FTPThread transFile = new FTPThread(serverSock.accept());
                System.out.println("Connection Recieved \n");
                transFile.start();
            }
            catch(Exception except)
            {
                System.out.println("Error :" + except);
            }



        }
    }

    public void handShake(ServerSocket serverSock) throws Exception
    {
        try{
            FTPThread transFile = new FTPThread(serverSock.accept());
            System.out.println("Connection Recieved \n");
            transFile.start();
            System.out.println("File List Transfer");
            transFile.listFiles();
            transFile.option();
        }
        catch(Exception except)
        {
            System.out.println("Error :" + except);
        }
    }

}

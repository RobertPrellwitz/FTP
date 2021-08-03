import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

public class FTPThread extends Thread{
    Socket clientSock;
    DataInputStream dataIn;
    DataOutputStream dataOut;

    FTPThread(Socket serverSock)
    {
     try
     {
         clientSock = serverSock;
         dataIn = new DataInputStream(clientSock.getInputStream());
         dataOut = new DataOutputStream(clientSock.getOutputStream());

     }
     catch(Exception except)
     {
        System.out.println("Error with connection: " + except);
     }
    }
    String listFiles()
    {
        String fileName = "";
        File dir = new File("Files");
        File [] files = dir.listFiles();

        for (int i =0; i < files.length; i++) {

           if  (files[i].isFile()){
           fileName= fileName.concat(files[i].getName());
           }
        }
        return fileName;

    }
    void sendFile(String sendFile)throws Exception
    {
        dataOut.writeUTF(sendFile);
    }
    void getFile (String receiveFile)
    {
        dataIn.readUTF(receiveFile);
    }

}

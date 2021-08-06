import java.io.*;
import java.net.Socket;

public class FTPThread extends Thread{
    Socket clientSock;
    DataInputStream dataIn;
    DataOutputStream dataOut;
    PrintWriter writeSock;
    BufferedReader command;

    FTPThread(Socket socket)
    {
        clientSock = socket;
     try
     {
         dataIn = new DataInputStream(new BufferedInputStream(clientSock.getInputStream()));
         writeSock = new PrintWriter(clientSock.getOutputStream(),true);
         command = new BufferedReader( new InputStreamReader(clientSock.getInputStream()));
     }
     catch(Exception except)
     {
        System.out.println("Error with connection: " + except);
     }
    }
    void listFiles()
    {
        String fileNames = "";
        File dir = new File("FTPServer/Files");
        File [] files = dir.listFiles();

        for (int i =0; i < files.length; i++) {

           if  (files[i].isFile()){
           fileNames=fileNames.concat(files[i].getName()+ " ");
           }
        }
       writeSock.println(fileNames);

    }
    public void option () throws Exception
    {
        String check = ""; String fileName="";
        int test;
        try
        {
            check = command.readLine();
            String [] instructions = check.split(" ");
            check = instructions[0]; fileName=instructions[1];
        }
        catch(IOException exp){}
        if (check == "GET"){ test = 0;}else if (check=="PUT"){test = 1;}else{test = 2;}

        switch (test)
        {
            case 0:
               sendFile(fileName);
            case 1:
                getFile(fileName);
            case 2:
                System.out.println("Error with FTP Command");
        }

    }
   public void sendFile(String sendFile)throws Exception
    {
        dataOut.writeUTF(sendFile);
    }
    public void getFile (String receiveFile) throws Exception
    {
        try
        {
            dataOut = new DataOutputStream(new BufferedOutputStream( new FileOutputStream("FTPServer/Files"+receiveFile)));
            System.out.println("Recieving File: " + receiveFile);
            byte[] data = new byte[1024];
            while((dataIn.read(data))!=-1)
            {
                dataOut.write(data,0,data.length);
            }
            System.out.println("File Received, saved  as: FTPServer/Files/" + receiveFile);
            dataOut.close();

        }
        catch (Exception exp)
        {
            System.out.println("Error receiving message");
            exp.printStackTrace();
            return;
        }

    }

}

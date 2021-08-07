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
            System.out.println("Waiting for Command from Client");
            check = command.readLine();
            String [] instructions = check.split(" ");
            check = instructions[0]; fileName="FTPServer/Files/" + instructions[1];
            System.out.println(check + " Request Recieved");
            writeSock.println(check + "\nFile Transfer Request Recieved for file: "+ fileName);
            System.out.println(fileName +" is the file Name");
        }
        catch(IOException exp){}
        if (check.equals("GET")){ test = 0;}else if (check.equals("PUT")){test = 1;}else{test = 2;}

        switch (test)
        {
            case 0:
               sendFile(fileName);
               break;
            case 1:
                getFile(fileName);
                break;
            case 2:
                System.out.println("Error with FTP Command");
        }
    }
   public void sendFile(String sendFile)throws Exception
    {
       try
       {
           File file = new File(sendFile);
           System.out.println("File Length: "+ (int) file.length());
           dataIn = new DataInputStream(new FileInputStream(file));
           dataOut = new DataOutputStream(clientSock.getOutputStream());
           byte[] data = new byte[1024];
           while ((dataIn.read(data)!=-1))
           {
               dataOut.write(data,0,data.length);
           }
           dataOut.flush();
           dataIn.close();
           dataOut.close();
           System.out.println("File Sent");
       }
       catch(Exception exp)
       {System.out.println("Error Sending file : " + exp);}
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

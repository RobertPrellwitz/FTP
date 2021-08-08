import java.io.*;
import java.net.Socket;

public class FTPThread extends Thread{
    Socket serverSock;
    //DataInputStream dataIn;
    //DataOutputStream dataOut;
    DataOutputStream fileOut;
    PrintWriter writeSock;
    BufferedReader command;

    FTPThread(Socket socket)
    {
        serverSock = socket;
     try
     {
        // dataIn = new DataInputStream(new BufferedInputStream(clientSock.getInputStream()));
        // dataOut = new DataOutputStream(new BufferedOutputStream(clientSock.getOutputStream()));
         writeSock = new PrintWriter(serverSock.getOutputStream(),true);
         command = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
     }
     catch(Exception except)
     {
        System.out.println("Error with connection: " + except);
     }
    }

    public void run()
    {
        System.out.println("File List Transfer");
        listFiles();

              boolean run = true;

              while (run) {
                  int test = 2;
                  try {
                      System.out.println("Waiting for Command from Client");
                      String input = command.readLine();
                      String[] instructions = input.split(" ");
                      String check = instructions[0];
                      String fileName = "FTPServer/Files/" + instructions[1];
                      System.out.println(fileName + " is the file Name");
                      if (check.equals("GET")) {
                          System.out.println(check + " Request Recieved");
                          sendFile(fileName);
                      } else if (check.equals("PUT"))
                      {
                          System.out.println(check + " Request Recieved");
                          getFile(fileName);
                      } else
                      {
                          System.out.println(check + " Request Recieved");
                          System.out.println("Closing Connection");
                          run = false;
                         // command.close();
                      }
                  } catch (Exception exp) {
                      System.out.println("FTP Error: " + exp);
                  }
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
        boolean run = true;
        while(run) {
            int test = 2;
            try {
                //command = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                System.out.println("Waiting for Command from Client");
//                String check = command.readLine();
//                writeSock.println(check + " Request Received.");
//                String fileName = "FTPServer/Files/" +command.readLine();
//                writeSock.println(fileName  + " Being transferred now.");
                String input = command.readLine();
                String[] instructions = input.split(" ");
                String check = instructions[0];
                String fileName = "FTPServer/Files/" + instructions[1];


            //writeSock.println(check + "\nFile Transfer Request Recieved for file: " + fileName);
            System.out.println(fileName + " is the file Name");
            if (check.equals("GET")) {
                System.out.println(check + " Request Recieved");
                test = 0;
            } else if (check.equals("PUT")) {
                System.out.println(check + " Request Recieved");
                test = 1;
            } else if (check.equals("quit")) {
                System.out.println(check + " Request Recieved");
                test = 2;
            }

            switch (test) {
                case 0:
                    sendFile(fileName);
                    break;
                case 1:
                    getFile(fileName);
                    break;
                case 2:
                    System.out.println("Closing Connection");
                    run = false;
                    command.close();
                    break;
                default:
                    System.out.println("FTP Error :");
                    run = false;
                    break;
            }
            }catch (IOException exp) {
                System.out.println("FTP Error: " + exp);
            }
        }
    }
   public void sendFile(String sendFile)throws Exception
    {
       try
       {
           File file = new File(sendFile);
           System.out.println("File Length: "+ (int) file.length());
           DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(serverSock.getOutputStream()));
           DataInputStream dataIn = new DataInputStream(new FileInputStream(file));
           byte[] data = new byte[1024];
           while ((dataIn.read(data)!=-1))
           {
               dataOut.write(data,0,data.length);
           }
           dataOut.write(-1);
           dataIn.close();
           dataOut.flush();
           //dataOut.close();
           System.out.println("File " + sendFile +" Sent.");
       }
       catch(Exception exp)
       {System.out.println("Error Sending file : " + exp);}
    }
    public void getFile (String receiveFile) throws Exception
    {
        try
        {
            fileOut = new DataOutputStream(( new FileOutputStream(receiveFile)));
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(serverSock.getInputStream()));
            System.out.println("Recieving File: " + receiveFile);
            byte[] data = new byte[1024];
            while((dataIn.read(data)) !=-1)
            {
                fileOut.write(data,0,data.length);
            }
            listFiles();
            System.out.println("File Received, saved  as: FTPServer/Files/" + receiveFile);
            fileOut.close();
            dataIn.close();
        }
        catch (Exception exp)
        {
            System.out.println("Error receiving message");
            exp.printStackTrace();
        }

    }

}

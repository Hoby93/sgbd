package socket;

import java.util.Vector;
import java.net.Socket;
import java.io.*;

import base.*;
import sql.*;

public class ThreadClient extends Thread {

    private Socket client;

    public ThreadClient()
    {
        super();
    }

    public ThreadClient(Socket client) {
        super();
        this.client = client;
    }
    
    public void run() {

        try {
            System.out.println("\n ** Client [Adresse:" + client.getLocalSocketAddress() + "] Connectee ** \n");
            String requette = "";

            ObjectInputStream dataIn = new ObjectInputStream(this.getSocket().getInputStream()); 

            Sql statement = new Sql();

            while(true)
            {       
                ObjectOutputStream dataOut = new ObjectOutputStream(this.getSocket().getOutputStream());
                try 
                {  
                    requette  = (String) dataIn.readObject();
                    
                    String response = requette + "(Recue)";

                    if(requette.equalsIgnoreCase("quit")) { break; }

                    Vector<Table> R = statement.executeQuery(requette);

                    dataOut.writeObject(R);

                    dataOut.flush();

                } catch (Exception e) {
                    //e.printStackTrace();
                    dataOut.writeObject(e.getMessage());
                } finally {
                    if(!requette.equalsIgnoreCase("QUIT")) {
                        System.out.println("[Requette] : " + requette + " (Ok)\n");
                    }
                }    
            }  
            System.out.println(" ** Client [Adresse:" + client.getLocalSocketAddress() + "] Deconnectee **\n");

            this.getSocket().close();  

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket()
    {
        return this.client;
    }

}
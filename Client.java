package socket;

import java.util.Vector;
import java.net.Socket;
import java.io.*;

import base.*;
import sql.*;

public class Client {

    Socket socket ;
    String nomClient = "Client" ;

    public Client()
    {

    }
    
    public Client(String nom)
    {
        this.nomClient =  nom;
    }

    public void setUpClient(String server,int port) throws IOException, ClassNotFoundException
    {

        System.out.println("\n** Vous etes connectees **");
        this.socket = new Socket(server, port);

        ObjectOutputStream dataOut = new ObjectOutputStream(this.getSocket().getOutputStream());

        while(true)
        {
            try {
                String msg = "";
                System.out.print("\n W93 DB [Mon_Sql] > ");

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                msg = br.readLine();

                System.out.println();
                dataOut.writeObject(msg);
                dataOut.flush();

                if(msg.equalsIgnoreCase("quit")) { break; }
                
                ObjectInputStream dataIn = new ObjectInputStream(this.getSocket().getInputStream());
                Object resultRest = dataIn.readObject();

                if(resultRest instanceof String)
                {
                    System.out.println(resultRest);
                } else {
                    Vector<Table> R  = (Vector<Table>) resultRest;
                    DisplayResult(R);
                }

               
            } catch (Exception e) {
                System.out.println(e);
            }
   
        }
        
        System.out.println("** Au revoir **");
    }

    public Socket getSocket()
    {
        return this.socket;
    }
    public void setNomClient(String nom)
    {
        this.nomClient = nom ;
    }

    public String getNomClient()
    {
        return this.nomClient;
    }

    public void DisplayResult(Vector<Table> R) throws Exception {
        int row = 0;
        String trait = "";
        String col = " ";

        if(R.size() == 0) {
            throw new Exception("** Aucun element pour l'instant **");
        }

        for(int i = 0; i < R.get(0).getAllNom().length; i++) {
            String next = R.get(0).getAllNom()[i];
            col = col +  next;
            int z = 16 - next.toCharArray().length;
            
            for(int x = 0;x < z; x++ ) {
                col = col + " ";
            }
            trait = trait + "------------------";
            col = col + "| ";
        }

        System.out.println(trait);
        System.out.println(col);
        System.out.println(trait);


        for(int i=0; i < R.size(); i++) {
            col = " ";
            for(int x = 0; x < R.get(i).getAllNom().length; x++) {
                String next = R.get(i).getAllVal()[x].toString();
                col = col +  next;
                int z = 16 - next.toCharArray().length;
                for(int n = 0; n < z; n++ ) {
                    col = col + " ";
                }
                col = col + "| ";
            }
            System.out.println(col);
            row++;
        }
        System.out.println(trait);
        System.out.println("0" + row + " ligne(s)");
    }

    

}
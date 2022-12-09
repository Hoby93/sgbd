package socket;

import java.net.ServerSocket;
import java.util.Vector;
import java.net.Socket;
import java.io.*;

import base.*;
import sql.*;

public class Server {

    ServerSocket serverSocket  ;

    public Server()
    {

    }
    
    public void setUpServer(int port) throws IOException, ClassNotFoundException
    {
        System.out.println("\n-- SERVEUR LISTE ACTIONS --");
        System.out.println("\n... En attente d'un Client ...");

        this.serverSocket = new ServerSocket(port);
    
        String requette = "";

        while(true) 
        {       
            Socket client = this.serverSocket.accept();
            new ThreadClient(client).start();  
        }
    }

    public ServerSocket getServerSocket()
    {
        return this.serverSocket;
    }
}
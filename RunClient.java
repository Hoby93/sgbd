package main;

import socket.*;

public class RunClient {

    public static void main(String[] args) {
        
        Client cl = new Client("Paul");
        try {          
            cl.setUpClient("localhost", 6666);
              
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
}
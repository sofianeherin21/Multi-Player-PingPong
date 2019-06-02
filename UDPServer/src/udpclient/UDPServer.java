package udpclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer {

    private String players[][]=new String [50][2]; 
    private InetAddress playersIP[]=new InetAddress[50];
    private int playerCount=0;
    private DatagramSocket serverSocket=null; 
    private byte[] receiveData ;
    private byte[] sendData ;
    int i=0;
    int deadPosition=4;
    int[] alive = new int[]{0,0, 0, 0};
    
    
    public static void main(String[] args) throws Exception {
         UDPServer server=new UDPServer(9876);
        }
    private void send(String res,InetAddress IPAddress,int receivePort){
      sendData = res.getBytes();
//create the datagram to send to client
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePort);
            try {
                i++;
               // System.out.println(i+" "+receivePort);
                serverSocket.send(sendPacket);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    
    
    }
    private void handle(String input,int port,InetAddress _IP){
        
        int playerExist=0;
        String newInput="";
        String tokens[]=input.split(":");
        
       if(tokens[0].equals("name")){
        for(int i=0;i<playerCount;i++){
          if(tokens[1].equals(players[i][0])){
             playerExist=1;
             break;
          } 
       }
        newInput="name:"+Integer.toString(playerExist);
        if(playerExist==0){
          players[playerCount][0]=tokens[1];
          players[playerCount][1]=Integer.toString(port);
          playersIP[playerCount]=_IP;
          
          newInput="name:"+Integer.toString(playerExist)+":"+Integer.toString(playerCount);
          playerCount++;
          for(int i=0;i< playerCount;i++){
             if(i!=(playerCount-1)){
                 if(playerCount==4){
                    send("AN:"+players[0][0]+":0",playersIP[i],Integer.valueOf(players[i][1]));
                    send("AN:"+players[1][0]+":1",playersIP[i],Integer.valueOf(players[i][1]));
                    send("AN:"+players[2][0]+":2",playersIP[i],Integer.valueOf(players[i][1]));
                    send("AN:"+players[3][0]+":3",playersIP[i],Integer.valueOf(players[i][1]));
                   
                 }
                 
                 String response="window:"+Integer.toString(playerCount-1);
                 send(response,playersIP[i],Integer.valueOf(players[i][1]));
             }
          }
          //System.out.println(newInput);
          
        }
        if(playerCount==4){
                    send("AN:"+players[0][0]+":0",_IP,port);
                    send("AN:"+players[1][0]+":1",_IP,port);
                    send("AN:"+players[2][0]+":2",_IP,port);
                    send("AN:"+players[3][0]+":3",_IP,port);
                   
        
        }
        send(newInput,_IP,port);
       }
       
       else if(tokens[0].equals("board") || tokens[0].equals("x")){
           
              for (int i = 0; i < playerCount; i++) {
                send(input,playersIP[i],Integer.valueOf(players[i][1]));
              }
            
       }
       else if(tokens[0].equals("die")){
          int no = Integer.parseInt(tokens[1]);
          
          for (int i = 0; i < playerCount; i++) {
              
              
              if(i!=no && alive[i]==0){
                  if(deadPosition<=2){
                    send("dead:1:0",playersIP[i],Integer.valueOf(players[i][1]));  
                  }
                  else{
                send(input,playersIP[i],Integer.valueOf(players[i][1]));
                  }
                  System.out.println(input+" "+i);
              }
          }
          send("dead:"+deadPosition+":0",playersIP[no],Integer.valueOf(players[no][1]));
          deadPosition--;
          alive[no]=1;
         System.out.println("dead "+i);
         if(deadPosition==1)
         {
             playerCount=0;
         }
       
       }
    }
    
    public UDPServer(int port) {
        
        try {
            serverSocket =new DatagramSocket(port);
        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        }
         
        
        while (true) {
            receiveData= new byte[1024];
            sendData = new byte[1024];

            DatagramPacket receivePacket
                    = new DatagramPacket(receiveData, receiveData.length);
            try {
                
                serverSocket.receive(receivePacket);
//retrive the data from the buffer
            } catch (IOException ex) {
               System.out.println(ex.getMessage());
            }
                   
            String sentence = new String(receivePacket.getData());
            //System.out.println(sentence);
//get IP address and port# of the sender
            InetAddress IPAddress = receivePacket.getAddress();
            int receivePort = receivePacket.getPort();
//modify the data and convert to byte array
            handle(sentence,receivePort,IPAddress);
           // send(response,IPAddress,receivePort);
            
            
           }
    }
    
    }
    

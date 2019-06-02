/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Windows
 */
public class UDPClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
//create input stream for reading from standard input
       
        
//create a DatagramSocket object
        DatagramSocket clientSocket = new DatagramSocket();
//translate host name to IP Address using DNS
        InetAddress IPAddress = InetAddress.getByName("localhost");
//create buffers for incoming and outgoing datagram
       int flag=0; 
       while(flag==0){
           
        BufferedReader inFromUser
                = new BufferedReader(new InputStreamReader(System.in));    
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
//read data from standard input and convert to byte array
        String sentence = inFromUser.readLine();
        sendData = sentence.getBytes();
//create datagram with data-to-send, length, IP address and port
        DatagramPacket sendPacket
                = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
//send datagram to server
        clientSocket.send(sendPacket);
//create datagram packet for incoming datagram
        DatagramPacket receivePacket
                = new DatagramPacket(receiveData, receiveData.length);
//receive datagram from server
        clientSocket.receive(receivePacket);
//retrive the data from the buffer
        String modifiedSentence
                = new String(receivePacket.getData());
//display the data
        System.out.println("FROM SERVER:" + modifiedSentence);
        }
        clientSocket.close();
    }
}

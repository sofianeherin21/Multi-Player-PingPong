/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpclient;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class pong extends Applet implements Runnable,KeyListener,ActionListener{
      
    ///UDP connection
    private static DatagramSocket clientSocket =null;
    private static InetAddress IPAddress = null;
    
    private static int flag=0;
    private static byte[] sendData ;
    private static byte[] receiveData;
    private static String name="";
    ///UDP connection
    private String[][] AllPlayers=new String[4][2];
    String sendingString="";
    final int HEIGHT=580 ,WIDTH=800;
    int score=0;
    Thread thread;
    leaderboard pl;
    leaderboard p2;
    leaderboard2 p3;
    leaderboard2 p4;
    ball bl;
    double ballSetX,ballSetY;
    
    
    int noOfplayer=0;
    int deadPosition=0;
    String nameOfplayer="";
    Label Entername;
    TextField n;
    
    int down1=0;
    int down2=0;
    int down3=0;
    int down4=0;
    int step=0;
    TextField t1;
    Label l1;
    Button btn;
    int players=0;
    int mainBall=0;
    
    int[] alive = new int[]{0, 0, 0,0};
    public void init(){
       
       this.resize(300, 300);
       this.addKeyListener(this);
       
        try {
            ///UDP connection
            clientSocket= new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(pong.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            IPAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException ex) {
            Logger.getLogger(pong.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      
      ballSetX=350;
      ballSetY=250;
       
       
       pl=new leaderboard(1);
       p2=new leaderboard(2);
       p3=new leaderboard2(3);
       p4=new leaderboard2(4);
       
       bl=new ball();
       
       ///form 1
       t1=new TextField(20);
       
    l1=new Label();
    l1.setText("Enter name:");
    
    l1.setBounds(350, 350,80,50);
    
    add(l1);
    add(t1);
    btn=new Button("Submit");
    add(btn);
    
    btn.addActionListener(this);
       
       
       
       thread = new Thread(this);
       thread.start();
    }
    
    
    public void actionPerformed(ActionEvent e){
       
        String name=t1.getText();
        nameOfplayer=name;
        System.out.println(name);
        if(name.equals("")){
            JOptionPane.showMessageDialog(null,"Enter your name first");
        }
        else{
               sendToServer("name:"+name);
        
      }
    }
    public void paint(Graphics g){
      //g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
      g.setColor(Color.DARK_GRAY);
      
      
     /* if(bl.getX()<-10 ||  bl.getX()>710 ){
          if(die>1){
         g.setColor(Color.MAGENTA);
         g.drawString("Game Over",350,250);
          }
      }
      
      else if(bl.getY()<10 || bl.getY()>590){
         if(die>1){
         g.setColor(Color.MAGENTA);
         g.drawString("Game Over",350,250);
         }
      }
      */
     if(step==3){
       this.resize(300, 300);
      g.fillRect(0,0,300,300);
      g.setColor(Color.red);
      
      
      if(deadPosition==1){
        g.drawString("You Won !!! ",50,50);
      }
      else{
      g.drawString("You lost !!! ",50,50);
      }
      g.drawString("Your position:"+deadPosition+"  ",50,150);
     
     }
     else if(step==0){
     g.fillRect(0,0,300,300);
     }
     else if(step==1){
     
      g.fillRect(0,0,300,300);
      g.setColor(Color.MAGENTA);
      
      g.drawString("Hey "+nameOfplayer,50,50);
      g.drawString("Wait for more "+players+" player...",50,150);
     
     }    
         
     else if(step==2){
         this.resize(WIDTH, HEIGHT);
      g.fillRect(0,0,WIDTH,HEIGHT);
       pl.draw(g);
       p2.draw(g);
       p3.draw(g);
       p4.draw(g);
       bl.draw(g);
        
       
        g.drawString("Player:"+nameOfplayer,720,30);
        if(noOfplayer==0){
            g.drawString("Green Board",720,50);
            g.drawString("Move:up down",720,70);
            g.drawString("Score :"+pl.score,720,90);
            
        }
        else if(noOfplayer==1){
           g.drawString("Yellow Board",720,50);
           g.drawString("Move:up down",720,70);
           g.drawString("Score :"+p2.score,720,90);
        }
        else if(noOfplayer==2){
        g.drawString("Blue Board",720,50);
        g.drawString("Move:left right",720,70);
        g.drawString("Score :"+p3.score,720,90);
        }
        else if(noOfplayer==3){
        g.drawString("Red Board",720,50);
        g.drawString("Move:left right",720,70);
        g.drawString("Score :"+p4.score,720,90);
        }
        g.drawString("Live Players :",720,120);
        
        if(AllPlayers[0][0].equals("0")) g.drawString(AllPlayers[0][1],720,140);
        if(AllPlayers[1][0].equals("0")) g.drawString(AllPlayers[1][1],720,160);
        if(AllPlayers[2][0].equals("0")) g.drawString(AllPlayers[2][1],720,180);
        if(AllPlayers[3][0].equals("0")) g.drawString(AllPlayers[3][1],720,200);
        
        
        g.drawString("Eliminated:",720,230);
        
        if(AllPlayers[0][0].equals("1")) g.drawString(AllPlayers[0][1],720,250);
        if(AllPlayers[1][0].equals("1")) g.drawString(AllPlayers[1][1],720,270);
        if(AllPlayers[2][0].equals("1")) g.drawString(AllPlayers[2][1],720,290);
        if(AllPlayers[3][0].equals("1")) g.drawString(AllPlayers[3][1],720,310);
        
       
      }
     
    }
      
    public void update(Graphics g){
       paint(g);
    }
    void selectMainBall(){
      int i;
        for(i=0;i<4;i++){
       if(alive[i]==0){
           break;
       }
      }
      mainBall=i;
      System.out.println("mainball is :"+mainBall);
    }
    
    
    public void handle(String result){
        
    String tokens[]=result.split(":");
    
    if(tokens[0].equals("name")){
            if(tokens[1].equals("0")){
                int number = tokens[2].charAt(0)-'0';
              //int number=Integer.parseInt(tokens[2]);
              step=1;
              remove(btn);
              remove(l1);
              remove(t1);
              players=3-(tokens[2].charAt(0)-'0');
              noOfplayer=(tokens[2].charAt(0)-'0');
              
              System.out.println(number);
              if(players==0){
                 
                  bl.start_game();
                  step=2;
                  
              }
              repaint();
               
           }
            else{
               
               JOptionPane.showMessageDialog(null,"This name exist choose another name");
           }
    }
    else if(tokens[0].equals("AN")){
      
        int number = tokens[2].charAt(0)-'0';
      AllPlayers[number][0]="0";
      AllPlayers[number][1]=tokens[1];
      System.out.println(AllPlayers[number][1]);
    
    }
    else if(tokens[0].equals("window")){
        
        
          players=3-(tokens[1].charAt(0)-'0');
          if(players==0){
           // sendToServer("allNames:0");
           step=2;
           bl.start_game();
            
           repaint();
        }
        
    
    
    }
    else if(tokens[0].equals("dead")){
        
        deadPosition=Integer.parseInt(tokens[1]);
        step=3;
        repaint();
        
    
    }
    else if(tokens[0].equals("die")){
        System.out.println("die"+" "+tokens[1]);
        if(tokens[1].equals("0")){
           bl.x=350;
           bl.y=250;  
           
           pl.setXY();
           alive[0]=1;
           AllPlayers[0][0]="1";
           selectMainBall();
          
        }
        else if(tokens[1].equals("1")){
            bl.x=350;
            bl.y=250;
            
            p2.setXY();
            alive[1]=1;
           AllPlayers[1][0]="1";
            selectMainBall();
        }
        else if(tokens[1].equals("2")){
            bl.x=350;
            bl.y=250;
            
            p3.setXY();
            alive[2]=1;
            AllPlayers[2][0]="1";
            selectMainBall();
        }
        else if(tokens[1].equals("3")){
            bl.x=350;
            bl.y=250;
            
            p4.setXY();
            alive[3]=1;
            AllPlayers[3][0]="1";
            selectMainBall();
        }
    
    }
    else if(step==2){
       if(tokens[0].equals("board")){
        
         //////True case///////
         if(tokens[1].equals("10")){
          pl.setUp(true);
        }
        else if(tokens[1].equals("20")){
          p2.setUp(true);
        }
        else if(tokens[1].equals("30")){
          p3.setRight(true);
        }
        else if(tokens[1].equals("40")){
          p4.setRight(true);
        }
        
        else if(tokens[1].equals("11")){
            
           pl.setDown(true);
        }
        else if(tokens[1].equals("21")){
           p2.setDown(true);
        }
        else if(tokens[1].equals("31")){
           p3.setLeft(true);
        }
        else if(tokens[1].equals("41")){
           p4.setLeft(true);
        }
        //////False case///////
        
        else if(tokens[1].equals("100")){
          pl.setUp(false);
        }
        else if(tokens[1].equals("200")){
          p2.setUp(false);
        }
        else if(tokens[1].equals("300")){
          p3.setRight(false);
        }
        else if(tokens[1].equals("400")){
          p4.setRight(false);
        }
        else if(tokens[1].equals("110")){
           pl.setDown(false);
        }
        else if(tokens[1].equals("210")){
           p2.setDown(false);
        }
        else if(tokens[1].equals("310")){
           p3.setLeft(false);
        }
        else if(tokens[1].equals("410")){
           p4.setLeft(false);
        }
        else{
        
        System.out.println("kisui nai:"+tokens[1]);
        
        }
        
        
       }
      if(noOfplayer!=mainBall && (tokens[2].equals("x") || tokens[0].equals("x"))){
         if(tokens[2].equals("x")){
         ballSetX=Double.parseDouble(tokens[3]);
         ballSetY=Double.parseDouble(tokens[5]);
         }
         else if(tokens[0].equals("x")){
         ballSetX=Double.parseDouble(tokens[1]);
         ballSetY=Double.parseDouble(tokens[3]);
         
         }
        }
      }
    }
    @Override
    public void run(){
       
        
        
        while(true){
            
        String signal="";
            byte[] receiveData = new byte[1024];
         DatagramPacket receivePacket
                = new DatagramPacket(receiveData, receiveData.length);
        try {
            
            clientSocket.receive(receivePacket);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        signal = new String(receivePacket.getData());
        
        handle(signal);
           
            if(step==2){
           sendingString="";
           pl.move();
           p2.move();
           p3.move();
           p4.move();
           
           
          bl.move(pl, p2, p3, p4,noOfplayer,ballSetX,ballSetY,mainBall);
          bl.checkBoardCollision(pl, p2, p3, p4);
            }
             
            repaint();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(pong.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(step==2){
                
                
               if(noOfplayer==mainBall){ 
                 sendingString="x:"+bl.getDoubleX()+":y:"+bl.getDoubleY()+":0";
                 sendToServer(sendingString);

                    }
            //   if(!sendingString.equals("")){
            //     sendToServer(sendingString);
            //   }
           
           
           
           // }
           }
        }
        
        
    }
    
    public void keyPressed(KeyEvent e){
        
      if(e.getKeyCode()== KeyEvent.VK_UP){
        //pl.setUp(true);
        
        
        if(noOfplayer==0){
        sendToServer("board:10:0");
        //sendingString+="board:10:";
        
        }
        else if(noOfplayer==1){
         sendToServer("board:20:0");
        //sendingString+="board:20:"; 
        
        }
      }
      else if(e.getKeyCode()== KeyEvent.VK_DOWN){
        //pl.setDown(true);
        
        if(noOfplayer==0){
        sendToServer("board:11:0");
        //sendingString+="board:11:";
        
        }
        else if(noOfplayer==1){
        sendToServer("board:21:0");
        //sendingString+="board:21:";
        
        }
      }
      else if(e.getKeyCode()== KeyEvent.VK_RIGHT){
        //p3.setRight(true);
        
      if(noOfplayer==2){
        sendToServer("board:30:0");
        
        //sendingString+="board:30:";
      
      }
        else if(noOfplayer==3){
         sendToServer("board:40:0");
        //sendingString+="board:40:";
       
        }
      
      }
      else if(e.getKeyCode()== KeyEvent.VK_LEFT){
        //p3.setLeft(true);
        
        if(noOfplayer==2){
         //   sendingString+="board:31:";
                     
            sendToServer("board:31:0");
        
        }
        else if(noOfplayer==3){
         //   sendingString+="board:41:";
                   
          sendToServer("board:41:0");
        
        }
      
      
      
      }
     /* else if(e.getKeyCode()== KeyEvent.VK_NUMPAD8){
        //p2.setUp(true);
        sendToServer("board:20");
      }
      else if(e.getKeyCode()== KeyEvent.VK_NUMPAD2){
        //p2.setDown(true);
        sendToServer("board:21");
       }
      else if(e.getKeyCode()== KeyEvent.VK_RIGHT){
        //p3.setRight(true);
        sendToServer("board:30");
      }
      else if(e.getKeyCode()== KeyEvent.VK_LEFT){
        //p3.setLeft(true);
        sendToServer("board:31");
       }
      else if(e.getKeyCode()== KeyEvent.VK_NUMPAD6){
        //p4.setRight(true);
        sendToServer("board:40");
      }
      else if(e.getKeyCode()== KeyEvent.VK_NUMPAD4){
        //p4.setLeft(true);
        sendToServer("board:41");
       }
    */  
    }
    public void keyReleased(KeyEvent e){
     
      if(e.getKeyCode()== KeyEvent.VK_UP){
        //pl.setUp(true);
        if(noOfplayer==0){
           // sendingString+="board:100:";
            
            sendToServer("board:100:0");
        }
        else if(noOfplayer==1){
           // sendingString+="board:200:";
          
         sendToServer("board:200:0");
        }
      }
      else if(e.getKeyCode()== KeyEvent.VK_DOWN){
        //pl.setDown(true);
         
        if(noOfplayer==0){
         //   sendingString+="board:110:";
            
           sendToServer("board:110:0");
        }
        else if(noOfplayer==1){
         //   sendingString+="board:210:";
            
           sendToServer("board:210:0");
        }
      }
      else if(e.getKeyCode()== KeyEvent.VK_RIGHT){
        //p3.setRight(true);
      
        if(noOfplayer==2){
           // sendingString+="board:300:";
           sendToServer("board:300:0");
        }
        else if(noOfplayer==3){
          //  sendingString+="board:400:";
          sendToServer("board:400:0");
        }
      
      }
      else if(e.getKeyCode()== KeyEvent.VK_LEFT){
        //p3.setLeft(true);
        if(noOfplayer==2){
          //  sendingString+="board:310:";
          sendToServer("board:310:0");
        }
        else if(noOfplayer==3){
           // sendingString+="board:410:";
          sendToServer("board:410:0");
        }
      
      
      
      }  
        
        
        
        
        
        /* if(e.getKeyCode()== KeyEvent.VK_UP){
        // pl.setUp(false);
        sendToServer("board:010");
      }
      else if(e.getKeyCode()== KeyEvent.VK_DOWN){
        // pl.setDown(false);
        sendToServer("board:011");
      }
      else if(e.getKeyCode()== KeyEvent.VK_NUMPAD8){
       // p2.setUp(false);
       sendToServer("board:020");
       
      }
      else if(e.getKeyCode()== KeyEvent.VK_NUMPAD2){
      //  p2.setDown(false);
      sendToServer("board:021");
       }
      else if(e.getKeyCode()== KeyEvent.VK_RIGHT){
       // p3.setRight(false);
       sendToServer("board:030");
      }
      else if(e.getKeyCode()== KeyEvent.VK_LEFT){
       // p3.setLeft(false);
       sendToServer("board:031");
       }
      else if(e.getKeyCode()== KeyEvent.VK_NUMPAD6){
       // p4.setRight(false);
       sendToServer("board:040");
      }
      else if(e.getKeyCode()== KeyEvent.VK_NUMPAD4){
       // p4.setLeft(false);
       sendToServer("board:041");
       }
    */
    
    }
    public void keyTyped(KeyEvent e){
    
    
    }
    private void sendToServer(String s){
        String sentence = s;
        byte[] sendData = new byte[500];
        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        try {
            clientSocket.send(sendPacket);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        
    
    }
    
}

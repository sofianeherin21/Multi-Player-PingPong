/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpclient;

import java.awt.Color;
import java.awt.Graphics;


public class leaderboard implements LeaderboardMain{
    double y , yValue;
    boolean up , down;
    int player  , x;
    int score=0;
    int playerDown=0;
    
    public leaderboard(int player){
    up=down=false;
    y=210;
    yValue=0;
    
    this.player=player;
    if(player==1) x=20;
    else if(player==2) x=660;
    
    }
    public void draw(Graphics g){
      if(player==1) g.setColor(Color.GREEN);
      else if(player==2) g.setColor(Color.YELLOW);
      g.fillRect(x, (int)y, 20, 80);
       
      
      
    }
    public void setUp(boolean input){
       
           up=input;
        
    }
    public void setDown(boolean input){
        down=input;
      
    }
      
    public void move(){
     if(playerDown==0){
        if(up){
            yValue-=2;
        }
        else if(down){
            yValue+=2;
        }
        else if(!up && !down){
            yValue=0;
        }
    
        if(yValue>=8) yValue=8;
        else if(yValue<=-8) yValue=-8;
        
        y+=yValue;
        
        if(y<40) y=40;
        else if(y>460) y=460;
     }
       
    }
    public int getY(){
       
       return (int)y;
    }
    public int getX(){
       
       return (int)x;
    }
    public void setXY(){
       playerDown=1;
       x=-40;
       y=-40;        
    }
    public void scorePlus(){
       score++;
     System.out.println(score);
   }
    
}

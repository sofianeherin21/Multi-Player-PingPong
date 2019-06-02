
package udpclient;

import java.awt.Color;
import java.awt.Graphics;

public class leaderboard2 implements LeaderboardMain{
    double x , xValue;
    boolean right , left;
   int player  , y;
   int score=0;
   int playerDown=0;
   
    public leaderboard2(int player){
    right=left=false;
    y=210;
    xValue=0;
    this.player=player;
    if(player==3) y=20;
    else if(player==4) y=540;
    
    }
    public void draw(Graphics g){
      if(player==3) g.setColor(Color.BLUE);
      else if(player==4) g.setColor(Color.RED);
      
      g.fillRect((int)x, y, 80, 20);
     
    }
    public void setRight(boolean input){
      right=input;
    }
    public void setLeft(boolean input){
        left=input;
    }
    public void move(){
        if(playerDown==0){
        if(left){
            xValue-=3;
        }
        else if(right){
            xValue+=3;
        }
        else if(!right && !left){
            xValue=0;
        }
    
        if(xValue>=8) xValue=8;
        else if(xValue<=-8) xValue=-8;
        
        x+=xValue;
        
        if(x<40) x=40;
        else if(x>580) x=580;
        }
    }
    public int getY(){
       
       return (int)y;
    }
    public int getX(){
       
       return (int)x;
    }
    public void setXY(){
       x=-40;
       y=-40; 
       playerDown=1;       
    }
    
    public void scorePlus(){
       score++;
     System.out.println(score);
   }

   
}

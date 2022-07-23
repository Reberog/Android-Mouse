package com.mycompany.androidmouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.abs;
import java.net.ServerSocket;
import java.net.Socket;

public class AndroidMouse {

    public static void main(String[] args) throws AWTException, IOException{
        System.out.println("Welcome to the Android Mouse!");
        float x,y,click,cnt;
        String sx="",sy="",word="";
        int posx=675,posy=375;
        Robot robot = new Robot();
        robot.delay(500);
        robot.mouseMove(posx, posy);
        try{
            ServerSocket ss = new ServerSocket(5000);  
            while(true){
                sx="";sy="";word="";cnt=0;
                Socket s = ss.accept();  
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream())); 
                String vals = in.readLine(); 
                for(int i=0;i<vals.length();i++){
                    if(vals.charAt(i)==' '){
                        if(cnt==0){
                            sx=word;
                            word="";
                            cnt++;
                        }else if(cnt==1){
                            sy=word;
                            word="";
                        }
                    }
                    else word+=vals.charAt(i);
                }
                click = Float.parseFloat(word);
                if(click==1){
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.delay(10);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    System.out.println("You Clicked!!!");
                    continue;
                }
                x = Float.parseFloat(sx);
                y = Float.parseFloat(sy);
                System.out.println("Values: "+x+" "+y);
                if(abs(y)>1 || abs(x)>1){
                    posx = posx+((int)-x*5);
                    posy = posy+((int)y*5);
                    if(posx<=0)posx=0;
                    if(posy<=0)posy=0;
                    if(posx>=1300)posx=1300;
                    if(posy>=750)posy=750;
                    robot.mouseMove(posx,posy);
                    System.out.println("posx: "+posx+" posy: "+posy);
                }
            }
            
        }catch(IOException e){
            System.out.println(e);
        }
        
    }
}

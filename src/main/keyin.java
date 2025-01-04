/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Tusk
 */
public class keyin implements KeyListener{
    
    public boolean up,down,left,right,restart,anykey,esc = false;
    private gamepanel gamePanel;
    
    public keyin(gamepanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    
    
    @Override
    public void keyTyped(KeyEvent e) {
 
    }

    @Override
    public void keyPressed(KeyEvent e) {
     
        int code = e.getKeyCode();
        
        System.out.println("Key pressed: " + KeyEvent.getKeyText(code) + " (" + code + ")");
        
        
            anykey = true;
        if (code == KeyEvent.VK_W){
            up = true;
        }
        if (code == KeyEvent.VK_A){
            left = true;
        }
        if (code == KeyEvent.VK_S){
            down = true;
        }
        if (code == KeyEvent.VK_D){
            right = true;
        }
        if (code == KeyEvent.VK_R) {
             restart = true;
         }
        if(code == KeyEvent.VK_ESCAPE){
            esc = true;
        } 
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
 
        int code = e.getKeyCode();
       
        if (code == KeyEvent.VK_W){
            up = false;
        }
        if (code == KeyEvent.VK_A){
            left = false;
        }
        if (code == KeyEvent.VK_S){
            down = false;
        }
        if (code == KeyEvent.VK_D){
            right = false;
        }
         if (code == KeyEvent.VK_R && gamePanel.gameOver ) {
            restart = false; 
            gamePanel.resetGame();
        }
        if (gamePanel.startScreen) {
            anykey = false;
        }
        if(code == KeyEvent.VK_ESCAPE){
            esc = false;
        }
         }
    }

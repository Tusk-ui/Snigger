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
    
    
    private gamepanel gamePanel;
    
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    public boolean esc = false;
    public boolean anykey = false;
    public boolean restart = false;
   
    // New properties for volume control
    public boolean volumeUp = false;
    public boolean volumeDown = false;
    public boolean effectVolumeUp = false;
    public boolean effectVolumeDown = false;
    public boolean volumeSettings = false;
    public boolean flashlightToggle = false;
    
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
        } if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            volumeUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            volumeDown = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            effectVolumeUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            effectVolumeDown = true;
        }
          anykey = true;

        // Handle volume settings key (V)
        if (code == KeyEvent.VK_V) {
            volumeSettings = true;
        }  if (code == KeyEvent.VK_V) {
            volumeSettings = false;
        } if (e.getKeyCode() == KeyEvent.VK_F) {
            flashlightToggle = true; // Set the flag when 'F' is pressed
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
        }if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            volumeUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            volumeDown = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            effectVolumeUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            effectVolumeDown = false;
        }
         }
    }

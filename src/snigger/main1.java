/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snigger;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import main.gamepanel;
/**
 *
 * @author Tusk
 */
public class main1 {
    
        
    public static void main (String[]args){
    
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("Snigger");
    
   ImageIcon icon = new ImageIcon("res/snake/snikhead.png");
             window.setIconImage(icon.getImage());

    gamepanel gamepane = new gamepanel();
    window.add(gamepane);
    gamepane.requestFocusInWindow();
    window.pack(); 
    
    window.setLocationRelativeTo(null);
    window.setVisible(true);
    

    gamepane.startime();
    }
}

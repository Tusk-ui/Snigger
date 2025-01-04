/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.snik;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import main.gamepanel;
import main.keyin;

/**
 *
 * @author Tusk
 */
public class snik extends entity {
    
    gamepanel gp;
    keyin key;
  
    
    public snik(gamepanel gp, keyin key){
    
        this.gp = gp;
        this.key = key;
        
    }
    
    public void defval(){
    
    x = 100;
    y = 100;
    speed = 4;
    direction = "right";
    
    }
    public void getSnake(){
    
       try {
        up1 = ImageIO.read(getClass().getResourceAsStream("/snake/up.png"));
        down1 = ImageIO.read(getClass().getResourceAsStream("/snake/snikhead.png"));
        right1 = ImageIO.read(getClass().getResourceAsStream("/snake/right.png"));
        left1 = ImageIO.read(getClass().getResourceAsStream("/snake/left.png"));
        
        body = ImageIO.read(getClass().getResourceAsStream("/snake/Snikbody.png"));
        
        tail = ImageIO.read(getClass().getResourceAsStream("/snake/sniktil.png"));
        uptail = ImageIO.read(getClass().getResourceAsStream("/snake/uptail.png"));
        lefttail = ImageIO.read(getClass().getResourceAsStream("/snake/lefttail.png"));
        rightail = ImageIO.read(getClass().getResourceAsStream("/snake/rightail.png"));
        
    } catch(IOException e) {
        e.printStackTrace();
                }
    } 
    
    public void snakeupdate() {
       
        if (key.up) {
            direction = "up";
            y -= speed;
        } else if (key.down) {
            direction = "down";
            y += speed;
        } else if (key.left) {
            direction = "left";
            x -= speed;
        } else if (key.right) {
            direction = "right";
            x += speed;
        }
   
    }

  
    public void draw(java.awt.Graphics2D g2) {
       BufferedImage image = null;

    // Determine head direction
    switch (gp.lastDirection) {
        case "up":
            image = up1;
            break;
        case "down":
            image = down1;
            break;
        case "right":
            image = right1;
            break;
        case "left":
            image = left1;
            break;
    }

    // Draw the head
    g2.drawImage(image, gp.plax, gp.play, gp.tilesize, gp.tilesize, null);
    
  
    
     drawSnakeBody(g2);
     
}

    private String getTailDirection(int[] secondLastPart, int[] tailPart) {
     int dx = secondLastPart[0] - tailPart[0];
    int dy = secondLastPart[1] - tailPart[1];

    if (dx > 0) return "right"; // Second-last part is to the right of the tail
    if (dx < 0) return "left";  // Second-last part is to the left of the tail
    if (dy > 0) return "down";  // Second-last part is below the tail
    if (dy < 0) return "up";    // Second-last part is above the tail

    return "down";
}
     public void someMethod() {
        int nextPlax = 100; // Example next X position
        int nextPlay = 100; // Example next Y position

        // Call the checkCollisions method
        boolean isCollision = gp.checkCollisions(nextPlax, nextPlay);

        if (isCollision) {
            System.out.println("Collision detected!");
        } else {
            System.out.println("No collision.");
        }
     }
    
    
   public void drawSnakeBody(Graphics2D g2) {
    // Iterate through the snake body (excluding the head) and draw each segment
    for (int i = 1; i < gp.snakeBody.size(); i++) {
        int[] bodyPart = gp.snakeBody.get(i);
        
        if (i == gp.snakeBody.size() - 1) {
            // Tail segment
            int[] secondLastPart = gp.snakeBody.get(i - 1); // The segment before the tail
            String tailDirection = getTailDirection(secondLastPart, bodyPart);

            // Select the correct tail image based on direction
            BufferedImage tailImage = null;
            switch (tailDirection) {
                case "up":
                    tailImage = uptail;
                    break;
                case "down":
                    tailImage = tail;
                    break;
                case "right":
                    tailImage = rightail;
                    break;
                case "left":
                    tailImage = lefttail;
                    break;
            }

            // Draw the tail
            g2.drawImage(tailImage, bodyPart[0], bodyPart[1], gp.tilesize, gp.tilesize, null);
        } else {
            // Body segment
            g2.drawImage(body, bodyPart[0], bodyPart[1], gp.tilesize, gp.tilesize, null);
    }
}
   }
}

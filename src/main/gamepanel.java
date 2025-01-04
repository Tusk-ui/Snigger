/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;
import static java.awt.Color.BLACK;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.Timer;
import main.snik.snik;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Tusk
 */
public class gamepanel extends JPanel implements Runnable {
    
    
    public String nextDirection = "right";
    private BufferedImage logo;
    Sound sound = Sound.getInstance(); 
    public boolean startScreen = true;
    private boolean esc = false; 
    public int highScore = 0;
    public int score = 0;
    final int orsize = 16;
    final int scale = 2;
    public boolean appleEaten = false;
    public final int tilesize = orsize * scale;
    final int maxcol = 20;
    final int maxrow = 14;
    public final int screenwid = tilesize * maxcol;
    public final int screenheight = tilesize * maxrow;
    Random random = new Random();
    int applex;
    int appley;
    
    int FPS = 60;
    snik snake;
    keyin yabi;
    
    private boolean isPaused = false;
    public BufferedImage tail;
    Thread gametime;
    boolean canMove = true;
    public String lastDirection = "";
    boolean isMovingAutomatically = true;
    Timer moveTimer;
    int moveDelay = 150; 
     
    public ArrayList<int[]> snakeBody = new ArrayList<>();
    public static boolean gameOver = false;
    
    public int plax = 0;
    public int play = 0;
    
    int centerX = (maxcol / 2) * tilesize;
    int centerY = (maxrow / 2) * tilesize;
    
    public gamepanel(){
         try {
             logo = ImageIO.read(getClass().getResource("/snake/snikhead.png"));
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Failed to load logo image.");
    }
        
        this.setPreferredSize(new Dimension(screenwid, screenheight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        
        
        this.setLayout(null);  // Allows absolute positioning for the button
       
        yabi = new keyin(this);
        this.addKeyListener(yabi);
        
        snake = new snik(this, yabi); 
        snake.defval();
        snake.getSnake();
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        // Initialize other game elements like timers, apple, etc.
        moveTimer = new Timer(moveDelay, e -> { update(); });
           resetGame(); 
        apple(); 
     
        
    }
    
    public void apple(){
   do {
        int randomTileX = random.nextInt(maxcol);
        int randomTileY = random.nextInt(maxrow);
        applex = randomTileX * tilesize;
        appley = randomTileY * tilesize;
    } while (snakeBodyContains(applex, appley)); // Ensure the apple is not placed on the snake's body
}
    public void togglePause() {
     isPaused = !isPaused; // Toggle the pause state
    if (isPaused) {
        System.out.println("Game Paused");
        moveTimer.stop(); // Stop the movement timer
    } else {
        System.out.println("Game Resumed");
        moveTimer.start(); // Restart the movement timer
    }
}
    
    public void startime(){
   
    gametime = new Thread(this);
    gametime.start();
    moveTimer.start();
    playMusic(3);
    }

    @Override
   public void run() {
    double drawInterval = 1000000000.0 / FPS; 
    double nextDrawTime = System.nanoTime() + drawInterval;
    
    while (gametime != null) {
        handleKeyInput();

        if (startScreen && yabi.anykey) {
            startScreen = false;
            yabi.anykey = false;
            resetGame();
        }

        if (!startScreen) {
            repaint();
        }

        if (gameOver) {
            stopGame();
            break;
        }

        try {
            double remainingTime = (nextDrawTime - System.nanoTime()) / 1000000;
            if (remainingTime < 0) remainingTime = 0;
            Thread.sleep((long) remainingTime);
            nextDrawTime += drawInterval;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
   public void stopGame() {
      gametime = null;
    if (moveTimer != null && moveTimer.isRunning()) {
        moveTimer.stop();
    }

    // Add a small delay to allow the game-over sound to play
    try {
        Thread.sleep(500); // 500ms delay
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
   }
   
   public void handleKeyInput() {
       if (startScreen && yabi.anykey) {
            startScreen = false;
            yabi.anykey = false; // Reset anykey
            resetGame();
        }

        if (!startScreen && gameOver && yabi.restart) {
           
            stopGame();
            resetGame();
            yabi.restart = false;
        }

        // Update nextDirection based on key input (without reversing direction)
        if (!startScreen) {
            if (yabi.up && !lastDirection.equals("down")) {
                nextDirection = "up";
            } else if (yabi.down && !lastDirection.equals("up")) {
                nextDirection = "down";
            } else if (yabi.left && !lastDirection.equals("right")) {
                nextDirection = "left";
            } else if (yabi.right && !lastDirection.equals("left")) {
                nextDirection = "right";
            }
        }

        // Pause game when ESC is pressed
        if (yabi.esc && !startScreen) {
            togglePause(); // Toggle pause state when Escape is pressed
            yabi.esc = false;
    }

    // Pause game when ESC is pressed
    if (yabi.esc && !startScreen) {
        togglePause(); // Toggle pause state when Escape is pressed
        yabi.esc = false;
    }
}

public void resetGame() {
      stopGame(); // Stop the game thread and timers
    stopMusic(); // Stop any currently playing music (including game-over music)

    // Calculate the center coordinates
    int centerX = (maxcol / 2) * tilesize;
    int centerY = (maxrow / 2) * tilesize;

    // Reset game state
    plax = centerX; // Set the snake's head to the center X position
    play = centerY; // Set the snake's head to the center Y position
    snakeBody.clear();
    gameOver = false;
    snakeBody.add(new int[]{plax, play}); // Add the head to the snake's body
    lastDirection = "right";
    isMovingAutomatically = true;
    apple();

    // Restart the game thread and music
    startime(); // Restart the game thread
    score = 0;
    playMusic(2);
    }

public void update() {
  if (startScreen || isPaused) {
        return; // Skip updating the game logic if the start screen is active or the game is paused
    }

    if (isPaused) {
        return; // Skip updating the game logic if paused
    }

    if (gameOver) {
        System.out.println("Game Over detected. Playing game-over sound.");
        playSE(0); // Play the game-over sound effect
        stopGame(); // Play the game-over music (soundURL[0])
        return;
    }

    if (canMove) {
        lastDirection = nextDirection;
        moveSnake(lastDirection);
        canMove = false;
    }

    if (plax % tilesize == 0 && play % tilesize == 0) {
        canMove = true; 
    }
}

/*public void checkCollisions() {
    // Check for wall collisions
    if (plax < -tilesize || plax >= screenwid + tilesize || play < -tilesize || play >= screenheight + tilesize) {
        gameOver = true;
        System.out.println("Game Over: Snake (head or body) passed through the wall!");
        playSE(0);
        return;
    }

    // Check for self-collision
    if (snakeBody.size() > 1) { // Only check self-collision if there's a body
        for (int i = 1; i < snakeBody.size(); i++) { // Start at 1 to skip the head
            int[] bodyPart = snakeBody.get(i);
            if (plax == bodyPart[0] && play == bodyPart[1]) {
                gameOver = true;
                System.out.println("Game Over: Snake collided with itself!");
                playSE(0);
                return;
            }
        }
    }
}
*/

public boolean checkCollisions(int nextPlax, int nextPlay) {
    // Check for wall collisions
    if (nextPlax < 0 || nextPlax >= screenwid || nextPlay < 0 || nextPlay >= screenheight) {
        System.out.println("Game Over: Snake hit the wall!");
         playSE(0);
        return true;
    }

    // Check for self-collision
    if (snakeBody.size() > 1) { // Only check self-collision if there's a body
        for (int i = 1; i < snakeBody.size(); i++) { // Start at 1 to skip the head
            int[] bodyPart = snakeBody.get(i);
            if (nextPlax == bodyPart[0] && nextPlay == bodyPart[1]) {
                System.out.println("Game Over: Snake collided with itself!");
                 playSE(0);
                return true;
            }
        }
    }

    return false; // No collision detected
}

private void moveSnake(String direction) {
 /*   // Move the head
    int prevX = plax;
    int prevY = play;

    if (direction.equals("up") && play > 0) {
        play -= tilesize;
    } else if (direction.equals("down") && play < screenheight - tilesize) {
        play += tilesize;
    } else if (direction.equals("left") && plax > 0) {
        plax -= tilesize;
    } else if (direction.equals("right") && plax < screenwid - tilesize) {
        plax += tilesize;
    }

    // Add the new head position to the snake's body
    snakeBody.add(0, new int[]{plax, play}); // Insert at the beginning of the snakeBody list

    // Handle apple consumption
    if (plax == applex && play == appley && !appleEaten) {
        apple(); // Reposition apple
        appleEaten = true; // Flag that apple is eaten
        System.out.println("Apple eaten! Snake will grow.");
    }

    // If the snake is growing (apple eaten), do not remove the tail yet
    if (!appleEaten && snakeBody.size() > 1) {
        snakeBody.remove(snakeBody.size() - 1); // Remove the last segment (tail)
    }

    // After the head has passed the apple, grow the snake
    if (appleEaten) {
    // Get the position of the last segment (tail)
    int[] lastSegment = snakeBody.get(snakeBody.size() - 1);

    // Add a new segment at the same position as the tail
    snakeBody.add(new int[]{lastSegment[0], lastSegment[1]}); 

    appleEaten = false; // Reset the apple eaten flag
    System.out.println("Snake grew! New size: " + snakeBody.size());
}

    // Update body segments (except head)
    for (int i = snakeBody.size() - 1; i > 0; i--) {
        snakeBody.set(i, new int[]{snakeBody.get(i - 1)[0], snakeBody.get(i - 1)[1]});
    }
    if (!snakeBody.isEmpty()) {
        snakeBody.set(0, new int[]{plax, play});
*/
    int nextPlax = plax;
    int nextPlay = play;

    if (direction.equals("up")) {
        nextPlay -= tilesize;
    } else if (direction.equals("down")) {
        nextPlay += tilesize;
    } else if (direction.equals("left")) {
        nextPlax -= tilesize;
    } else if (direction.equals("right")) {
        nextPlax += tilesize;
    }

    // Check for collisions at the next position
    if (checkCollisions(nextPlax, nextPlay)) {
        gameOver = true;
        System.out.println("Game Over: Collision detected!");
        playSE(0); // Play the game-over sound effect
        return; // Stop further movement if a collision is detected
    }

    // Insert the new head position
    snakeBody.add(0, new int[]{nextPlax, nextPlay});

    // Check for apple consumption
    if (nextPlax == applex && nextPlay == appley) {
        apple(); // Reposition apple
        appleEaten = true; // Mark apple eaten
        score++;
        if (score > highScore) {
            highScore = score; // Update high score
        }
        playSE(1); // Play the sound effect for eating the apple
        System.out.println("Apple eaten!");
    } else {
        // Remove tail segment if no apple was eaten
        snakeBody.remove(snakeBody.size() - 1);
    }

    // Update the snake's head position
    plax = nextPlax;
    play = nextPlay;
}


private boolean snakeBodyContains(int x, int y) {
    for (int[] bodyPart : snakeBody) {
        if (bodyPart[0] == x && bodyPart[1] == y) {
            return true;
        }
    }
    return false;

}

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    
  
     draw(g2);

    // Draw apple (as red square for now)
    g2.setColor(Color.RED);
    g2.fillRect(applex, appley, tilesize, tilesize);

    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(24f));
    String scoreText = "Score: " + score;
    g2.drawString(scoreText, 10, 30);

    // Display high score
    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(24f));
    String highScoreText = "Highscore: " + highScore;
    g2.drawString(highScoreText, 10, 50);

       if (startScreen) {
            g2.setColor(Color.GRAY);
            g2.fillRect(0, 0, screenwid, screenheight); // Fill the screen with black

            // Draw the logo (scaled up)
            if (logo != null) {
                int logoWidth = logo.getWidth() * 20; // Scale up the width (16x4 = 64)
                int logoHeight = logo.getHeight() * 20; // Scale up the height (16x4 = 64)
                int logoX = (screenwid - logoWidth) / 2; // Center horizontally
                int logoY = (screenheight / 2) - logoHeight + 200; // Position above the title text

                g2.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);
            }

            // Draw the title text
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(48f)); // Set font size for title
            String startText = "Welcome to Snigger!";
            int textWidth = g2.getFontMetrics().stringWidth(startText);
            g2.drawString(startText, (screenwid - textWidth) / 2, screenheight / 2 - 20);

            // Draw the subtitle text
            g2.setFont(g2.getFont().deriveFont(24f));
            String subtitle = "Press any key to begin!";
            int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
            g2.drawString(subtitle, (screenwid - subtitleWidth) / 2, screenheight / 2 + 10);
        } else {
            // Draw game elements after the start screen
            // E.g., snake, apple, etc.
            snake.draw(g2);
            snake.drawSnakeBody(g2);
        }
    
    if (isPaused) {
        g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent overlay
        g2.fillRect(0, 0, screenwid, screenheight); // Darken the screen

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(24f));
        String pauseText = "Game Paused - Press ESC to Resume";
        int textWidth = g2.getFontMetrics().stringWidth(pauseText);
        g2.drawString(pauseText, (screenwid - textWidth) / 2, screenheight / 2);
    }

    if (gameOver) {
        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(48f));
        String gameOverText = "Game Over";
        String resetText = "Press 'R' to reset";

        int gameOverTextWidth = g2.getFontMetrics().stringWidth(gameOverText);
        int resetTextWidth = g2.getFontMetrics().stringWidth(resetText);

        g2.drawString(gameOverText, (screenwid - gameOverTextWidth) / 2, screenheight / 2 - 20);
        g2.setFont(g2.getFont().deriveFont(24f));
        g2.drawString(resetText, (screenwid - resetTextWidth - 50), screenheight / 2 + 10);
    }

    g2.dispose();
}

  public void draw(Graphics g){
       g.setColor(Color.GRAY);
       
      for (int i = 0; i <= maxcol; i++) {
            int x = i * tilesize;
            g.drawLine(x, 0, x, screenheight);
        }

    if (isPaused) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight()); // Optional: Darken the screen when paused
            g.setColor(Color.WHITE);
            g.drawString("Game Paused - Press ESC to Resume", getWidth() / 2 - 100, getHeight() / 2);
            return; // Skip drawing the game elements if paused
        }
      
      
        for (int i = 0; i <= maxrow; i++) {
            int y = i * tilesize;
            g.drawLine(0, y, screenwid, y);
        }
  }
  public void playMusic(int i) {
    sound.stop(true); // Stop any existing background music
    sound.setFile(i, true); // Load the background music
    sound.play(true); // Play the background music
    sound.loop(true); // Loop the background music
}

public void stopMusic() {
    sound.stop(true); // Stop background music
    sound.stop(false); // Stop sound effects
    System.out.println("Music stopped.");
}

public void playSE(int i) {
      System.out.println("Attempting to play sound effect for index: " + i);
    sound.stop(false); // Stop any existing sound effects
    sound.setFile(i, false); // Load the sound effect
    sound.play(false); // Play the sound effect
}
    }

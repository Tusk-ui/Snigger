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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Tusk
 */
public class gamepanel extends JPanel implements Runnable {
    
    private boolean isFlashlightModeEnabled = false;
    private float currentBackgroundVolume; // Store current background music volume
    private float currentSoundEffectVolume; // Store current sound effects volume
    private boolean isDraggingBgMusicSlider = false;
    private boolean isDraggingSfxSlider = false;
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
    public int applex;
    public int appley;
    
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
        moveTimer = new Timer(moveDelay, e -> {
            update();
        });
        resetGame();
       

        // Add mouse listeners
        this.addMouseListener(new MouseAdapter() {
           @Override
public void mouseClicked(MouseEvent e) {
    if (isPaused) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Define the checkbox area
        int borderX = (screenwid - 400) / 2; // Center horizontally
        int borderY = (screenheight - 300) / 2; // Center vertically
        int checkboxX = borderX + 110; // Position the checkbox below the sliders
        int checkboxY = borderY + 220; // Adjust Y position as needed
        int checkboxSize = 24; // Size of the checkbox

        // Debug: Print mouse click coordinates and checkbox area
        System.out.println("Mouse Clicked at: (" + mouseX + ", " + mouseY + ")");
        System.out.println("Checkbox Area: (" + checkboxX + ", " + checkboxY + ") to (" + (checkboxX + checkboxSize) + ", " + (checkboxY + checkboxSize) + ")");

        // Check if the mouse click is within the checkbox area
        if (mouseX >= checkboxX && mouseX <= checkboxX + checkboxSize &&
            mouseY >= checkboxY && mouseY <= checkboxY + checkboxSize) {
            isFlashlightModeEnabled = !isFlashlightModeEnabled; // Toggle flashlight mode
            System.out.println("Checkbox Clicked! Flashlight Mode: " + (isFlashlightModeEnabled ? "ON" : "OFF")); // Debug message
            repaint(); // Redraw the pause screen to reflect the change
        }

        // Handle slider clicks (existing code)
        handleSliderClick(mouseX, mouseY);
    }
}
            @Override
            public void mouseReleased(MouseEvent e) {
                isDraggingBgMusicSlider = false;
                isDraggingSfxSlider = false;
            }
        });

        // Add mouse motion listener for slider dragging
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPaused) {
                    handleSliderDrag(e.getX(), e.getY());
                    repaint(); // Redraw the pause screen to reflect slider changes
                }
            }
        });
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
        moveTimer.stop();
        this.requestFocusInWindow();
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
                playSE(0);
                stopGame();
                break;
            } if (isPaused && yabi.flashlightToggle) {
        isFlashlightModeEnabled = !isFlashlightModeEnabled; // Toggle flashlight mode
        yabi.flashlightToggle = false; // Reset the flag
        System.out.println("Flashlight Mode: " + (isFlashlightModeEnabled ? "ON" : "OFF"));
        repaint(); // Redraw the pause screen to reflect the change
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
        stopMusic();
        resetGame();
    }

       if (!startScreen && gameOver && yabi.restart) {
        System.out.println("Restarting game..."); // Debug statement
        yabi.restart = false; // Reset the restart flag
        resetGame(); // Restart the game
    }
       
    // Update nextDirection based on key input (without reversing direction)
    if (!startScreen && !isPaused) {
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
        togglePause();
        yabi.esc = false;
    }

    // Open volume settings dialog when 'V' is pressed during pause
    if (isPaused && yabi.volumeSettings) {
        VolumeDialog volumeDialog = new VolumeDialog((JFrame) SwingUtilities.getWindowAncestor(this), sound);
        volumeDialog.setVisible(true); // Show the dialog
        yabi.volumeSettings = false;
    }
    }


    public void adjustVolume(float delta, boolean isBackground) {
    float currentVolume = isBackground ? sound.getBackgroundVolume() : sound.getSoundEffectVolume();
    System.out.println("Current Volume: " + currentVolume); // Debug statement

    float newVolume = currentVolume + delta;
    System.out.println("New Volume: " + newVolume); // Debug statement

    sound.setVolume(newVolume, isBackground);
}
   
public void resetGame() {
   currentBackgroundVolume = sound.getBackgroundVolume();
    currentSoundEffectVolume = sound.getSoundEffectVolume();
   stopGame(); // Stop the game thread and timers
    stopMusic(); // Stop any currently playing music (including game-over music)

    // Calculate the center coordinates
    int centerX = (maxcol / 2) * tilesize;
    int centerY = (maxrow / 2) * tilesize;

    // Reset game state
   plax = (maxcol / 2) * tilesize; // Reset snake head position to the center
    play = (maxrow / 2) * tilesize;
    snakeBody.clear(); // Clear the snake's body
    snakeBody.add(new int[]{plax, play}); // Add the head to the snake's body
    lastDirection = "right"; // Reset the snake's direction
    gameOver = false; // Reset the game-over state
    score = 0; // Reset the score
    apple(); // Reposition the apple

    // Restart the game thread and music
    startime(); // Restart the game thread
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

    // Draw the game elements
    draw(g2);
    snake.draw(g2);
    snake.drawApple(g2);

    // Draw the flashlight effect (if enabled)
    if (isFlashlightModeEnabled) {
        drawFlashlight(g2);
    }

    // Draw the score and high score
    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(24f));
    g2.drawString("Score: " + score, 10, 30);
    g2.drawString("Highscore: " + highScore, 10, 50);

    // Draw the start screen if the game hasn't started
    if (startScreen) {
        drawStartScreen(g2);
    }

    // Draw the pause screen if the game is paused
    if (isPaused) {
        int frameX = (screenwid - 400) / 2; // Center the pause screen horizontally
        int frameY = (screenheight - 300) / 2; // Center the pause screen vertically
        drawPauseScreen(g2, frameX, frameY); // Call the updated method with frameX and frameY
    }

    // Draw the game-over screen if the game is over
    if (gameOver) {
        drawGameOverScreen(g2);
    }

    g2.dispose();
}


private void drawGameOverScreen(Graphics2D g2) {
    // Draw a semi-transparent overlay
    g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
    g2.fillRect(0, 0, screenwid, screenheight);

    // Draw the "Game Over" text
    g2.setColor(Color.RED);
    g2.setFont(g2.getFont().deriveFont(48f)); // Set font size
    String gameOverText = "Game Over";
    int textWidth = g2.getFontMetrics().stringWidth(gameOverText);
    g2.drawString(gameOverText, (screenwid - textWidth) / 2, screenheight / 2 - 20);

    // Draw instructions to restart
    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(24f));
    String restartText = "Press 'R' to Restart";
    int restartWidth = g2.getFontMetrics().stringWidth(restartText);
    g2.drawString(restartText, (screenwid - restartWidth) / 2, screenheight / 2 + 20);
}

private void drawPauseScreen(Graphics2D g2, int frameX, int frameY) {
    // Draw a semi-transparent overlay
    g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
    g2.fillRect(0, 0, screenwid, screenheight);

    // Define the dimensions and position of the pause screen border
    int borderWidth = 400; // Width of the border
    int borderHeight = 300; // Height of the border
    int borderX = (screenwid - borderWidth) / 2; // Center horizontally
    int borderY = (screenheight - borderHeight) / 2; // Center vertically

    // Draw a semi-transparent background for the pause screen
    g2.setColor(new Color(0, 0, 0, 200)); // Darker semi-transparent black
    g2.fillRect(borderX, borderY, borderWidth, borderHeight);

    // Draw a border around the pause screen
    g2.setColor(Color.WHITE);
    g2.drawRect(borderX, borderY, borderWidth, borderHeight);

    // Draw the "Paused" text
    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(48f)); // Set font size
    String pauseText = "Paused";
    int textWidth = g2.getFontMetrics().stringWidth(pauseText);
    int textX = borderX + (borderWidth - textWidth) / 2; // Center text horizontally within the border
    int textY = borderY + 60; // Position text vertically
    g2.drawString(pauseText, textX, textY);

    // Draw instructions to resume
    g2.setFont(g2.getFont().deriveFont(24f));
    String resumeText = "Press ESC to Resume";
    int resumeWidth = g2.getFontMetrics().stringWidth(resumeText);
    int resumeX = borderX + (borderWidth - resumeWidth) / 2; // Center text horizontally within the border
    int resumeY = textY + 50; // Position text below the "Paused" text
    g2.drawString(resumeText, resumeX, resumeY);

    // Draw minimalist sliders for volume adjustment
    drawVolumeSliders(g2, borderX + 50, borderY + 120, borderWidth - 100, 100);

    // Draw the flashlight mode checkbox and label
    int checkboxX = borderX + 110; // Position the checkbox below the sliders
    int checkboxY = borderY + 220; // Adjust Y position as needed
    int checkboxSize = 24; // Size of the checkbox

    // Draw the checkbox
    g2.setColor(Color.WHITE);
    g2.drawRect(checkboxX, checkboxY, checkboxSize, checkboxSize);

    // Fill the checkbox if flashlight mode is enabled
    if (isFlashlightModeEnabled) {
        g2.fillRect(checkboxX, checkboxY, checkboxSize, checkboxSize);
    }

    // Draw the flashlight mode label
    String flashlightLabel = "Flashlight Mode";
    g2.setFont(g2.getFont().deriveFont(18f));
    int labelX = checkboxX + checkboxSize + 22; // Position the label next to the checkbox
    int labelY = checkboxY + checkboxSize - 5; // Align the label vertically with the checkbox
    g2.drawString(flashlightLabel, labelX, labelY);

    // Draw instructions to toggle flashlight mode
    String toggleFlashlightText = "Click box or Press 'F' to Toggle Flashlight Mode";
    g2.setFont(g2.getFont().deriveFont(14f)); // Smaller font for instructions
    int toggleFlashlightTextWidth = g2.getFontMetrics().stringWidth(toggleFlashlightText);
    int toggleFlashlightTextX = borderX + (borderWidth - toggleFlashlightTextWidth) / 2; // Center text horizontally
    int toggleFlashlightTextY = checkboxY + checkboxSize + 20; // Position text below the checkbox
    

    g2.drawString(toggleFlashlightText, toggleFlashlightTextX, toggleFlashlightTextY);
}

private void drawVolumeSliders(Graphics2D g2, int frameX, int frameY, int framewid, int frameheight) {
    int sliderWidth = 200; // Width of the slider
    int sliderHeight = 10; // Height of the slider (minimalist)
    int sliderX = frameX + (framewid - sliderWidth) / 2; // Center the slider horizontally
    int sliderY = frameY + 30; // Position the slider vertically

    // Draw background music slider
    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(18f));
    String bgMusicLabel = "Music Volume:";
    int bgMusicLabelWidth = g2.getFontMetrics().stringWidth(bgMusicLabel);
    int bgMusicLabelX = frameX + (framewid - bgMusicLabelWidth) / 2; // Center label horizontally
    int bgMusicLabelY = sliderY - 10; // Position label above the slider
    g2.drawString(bgMusicLabel, bgMusicLabelX, bgMusicLabelY);

    // Draw the slider track
    g2.setColor(Color.GRAY);
    g2.fillRect(sliderX, sliderY, sliderWidth, sliderHeight);

    // Calculate the position of the slider thumb for background music
    float bgMusicVolume = sound.getBackgroundVolume();
    int bgMusicThumbX = sliderX + (int) (bgMusicVolume * sliderWidth);
    bgMusicThumbX = Math.max(sliderX, Math.min(sliderX + sliderWidth - 10, bgMusicThumbX)); // Clamp thumb position

    // Draw the slider thumb (current volume level)
    g2.setColor(Color.GREEN);
    g2.fillRect(bgMusicThumbX - 5, sliderY - 5, 10, sliderHeight + 10);

    // Draw sound effects slider
    sliderY += 40; // Move down for the next slider
    String sfxLabel = "SFX Volume:";
    int sfxLabelWidth = g2.getFontMetrics().stringWidth(sfxLabel);
    int sfxLabelX = frameX + (framewid - sfxLabelWidth) / 2; // Center label horizontally
    int sfxLabelY = sliderY - 10; // Position label above the slider
    g2.drawString(sfxLabel, sfxLabelX, sfxLabelY);

    // Draw the slider track
    g2.setColor(Color.GRAY);
    g2.fillRect(sliderX, sliderY, sliderWidth, sliderHeight);

    // Calculate the position of the slider thumb for sound effects
    float sfxVolume = sound.getSoundEffectVolume();
    int sfxThumbX = sliderX + (int) (sfxVolume * sliderWidth);
    sfxThumbX = Math.max(sliderX, Math.min(sliderX + sliderWidth - 10, sfxThumbX)); // Clamp thumb position

    // Draw the slider thumb (current volume level)
    g2.setColor(Color.BLUE);
    g2.fillRect(sfxThumbX - 5, sliderY - 5, 10, sliderHeight + 10);
}

private void drawStartScreen(Graphics2D g2) {
    // Fill the background with a dark color
    g2.setColor(new Color(178, 178, 178)); // Semi-transparent black
    g2.fillRect(0, 0, screenwid, screenheight);

    // Draw the logo (scaled up)
    if (logo != null) {
        int logoWidth = logo.getWidth() * 20; // Scale up the width
        int logoHeight = logo.getHeight() * 20; // Scale up the height
        int logoX = (screenwid - logoWidth) / 2; // Center horizontally
        int logoY = (screenheight / 2) - logoHeight + 200; // Position above the title text
        g2.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);
    }

//     Draw the title text
    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(48f)); // Set font size for title
    String startText = "Welcome to Snigger!";
    int textWidth = g2.getFontMetrics().stringWidth(startText);
    g2.drawString(startText, (screenwid - textWidth) / 2, screenheight / 2 - 20);

    // Draw the subtitle text
    g2.setFont(g2.getFont().deriveFont(24f));
    String subtitle = "Press any key to begin!";
    int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
    g2.drawString(subtitle, (screenwid - subtitleWidth) / 2, screenheight / 2 + 30);
    
    g2.setFont(g2.getFont().deriveFont(20f)); // Smaller font for controls
    String controlsText = "Controls: W, A, S, D to move | ESC to pause";
    int controlsWidth = g2.getFontMetrics().stringWidth(controlsText);
    g2.drawString(controlsText, (screenwid - controlsWidth) / 2, screenheight / 2 + 200); 
}



  public void draw(Graphics g){
       g.setColor(Color.GRAY);
       
      for (int i = 0; i <= maxcol; i++) {
            int x = i * tilesize;
            g.drawLine(x, 0, x, screenheight);
        }

        for (int i = 0; i <= maxrow; i++) {
            int y = i * tilesize;
            g.drawLine(0, y, screenwid, y);
        }
  }
  public void playMusic(int i) {
    sound.stop(true); // Stop any existing background music
    sound.setFile(i, true); // Load the background music
    sound.setVolume(currentBackgroundVolume, true);
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
    sound.setFile(i, false);
    sound.setVolume(sound.soundEffectVolume, false);// Load the sound effect
    sound.play(false); // Play the sound effect
}

  public void drawSubWindow(int frameX, int frameY, int framewid, int frameheight) {
    Graphics2D g2 = (Graphics2D) getGraphics(); // Get the current graphics context

    if (g2 != null) {
        // Draw a semi-transparent background
        g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g2.fillRect(frameX, frameY, framewid, frameheight);

        // Draw a border around the sub-window
        g2.setColor(Color.WHITE);
        g2.drawRect(frameX, frameY, framewid, frameheight);

        // Draw the "Paused" text
        g2.setFont(g2.getFont().deriveFont(36f)); // Larger font for the title
        String pausedText = "Paused";
        int pausedTextWidth = g2.getFontMetrics().stringWidth(pausedText);
        int pausedTextX = frameX + (framewid - pausedTextWidth) / 2; // Center text horizontally
        int pausedTextY = frameY + 50; // Position text vertically
        g2.drawString(pausedText, pausedTextX, pausedTextY);

        // Draw volume sliders and labels
        drawVolumeSliders(g2, frameX, frameY, framewid, frameheight);

        // Dispose of the graphics context to free resources
        g2.dispose();
    }
}
  
  
private void handleSliderClick(int mouseX, int mouseY) {
    int frameX = (screenwid - 300) / 2; // X position of the sub-window
    int frameY = screenheight / 2 - 20; // Y position of the sub-window
    int sliderWidth = 200; // Width of the slider
    int sliderX = frameX + (300 - sliderWidth) / 2; // X position of the slider
    int bgMusicSliderY = frameY + 20; // Y position of the background music slider
    int sfxSliderY = bgMusicSliderY + 40; // Y position of the sound effects slider

    // Check if the click is within the background music slider
    if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && mouseY >= bgMusicSliderY && mouseY <= bgMusicSliderY + 10) {
        isDraggingBgMusicSlider = true;
        float newVolume = (float) (mouseX - sliderX) / sliderWidth;
        newVolume = Math.max(0, Math.min(1, newVolume)); // Clamp volume between 0 and 1
        sound.setVolume(newVolume, true); // Set background music volume
        System.out.println("Background Music Volume: " + newVolume); // Debug statement
        repaint(); // Redraw the pause screen to reflect slider changes
    }

    // Check if the click is within the sound effects slider
    if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && mouseY >= sfxSliderY && mouseY <= sfxSliderY + 10) {
        isDraggingSfxSlider = true;
        float newVolume = (float) (mouseX - sliderX) / sliderWidth;
        newVolume = Math.max(0, Math.min(1, newVolume)); // Clamp volume between 0 and 1
        sound.setVolume(newVolume, false); // Set sound effects volume
        System.out.println("SFX Volume: " + newVolume); // Debug statement
        repaint(); // Redraw the pause screen to reflect slider changes
    }
}

private void handleSliderDrag(int mouseX, int mouseY) {
    int frameX = (screenwid - 300) / 2; // X position of the sub-window
    int frameY = screenheight / 2 - 20; // Y position of the sub-window
    int sliderWidth = 200; // Width of the slider
    int sliderX = frameX + (300 - sliderWidth) / 2; // X position of the slider
    int bgMusicSliderY = frameY + 20; // Y position of the background music slider
    int sfxSliderY = bgMusicSliderY + 40; // Y position of the sound effects slider

    if (isDraggingBgMusicSlider) {
        // Update background music volume based on mouse X position
        float newVolume = (float) (mouseX - sliderX) / sliderWidth;
        newVolume = Math.max(0, Math.min(1, newVolume)); // Clamp volume between 0 and 1
        sound.setVolume(newVolume, true); // Set background music volume
        System.out.println("Background Music Volume (Drag): " + newVolume); // Debug statement
        repaint(); // Redraw the pause screen to reflect slider changes
    }

    if (isDraggingSfxSlider) {
        // Update sound effects volume based on mouse X position
        float newVolume = (float) (mouseX - sliderX) / sliderWidth;
        newVolume = Math.max(0, Math.min(1, newVolume)); // Clamp volume between 0 and 1
        sound.setVolume(newVolume, false); // Set sound effects volume
        System.out.println("SFX Volume (Drag): " + newVolume); // Debug statement
        repaint(); // Redraw the pause screen to reflect slider changes
    }
}
private void drawFlashlight(Graphics2D g2) {
    if (!isFlashlightModeEnabled) {
        return; // Skip drawing the flashlight effect if the mode is disabled
    }

    // Draw a pure black overlay to darken the screen
    g2.setColor(new Color(0, 0, 0, 200)); // Semi-transparent black
    g2.fillRect(0, 0, screenwid, screenheight);

    // Define the flashlight area (a circle around the snake's head)
    int flashlightRadius = 100; // Radius of the flashlight
    int flashlightX = plax + tilesize / 2; // Center X of the flashlight (snake's head)
    int flashlightY = play + tilesize / 2; // Center Y of the flashlight (snake's head)

    // Create a circular shape for the flashlight
    java.awt.geom.Area flashlightArea = new java.awt.geom.Area(
        new java.awt.geom.Ellipse2D.Double(
            flashlightX - flashlightRadius,
            flashlightY - flashlightRadius,
            flashlightRadius * 2,
            flashlightRadius * 2
        )
    );

    // Subtract the flashlight area from the dark overlay
    java.awt.geom.Area darkOverlay = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Double(0, 0, screenwid, screenheight));
    darkOverlay.subtract(flashlightArea);

    // Draw the dark overlay with the flashlight area cut out
    g2.setColor(new Color(0, 0, 0, 200)); // Semi-transparent black
    g2.fill(darkOverlay);
}
}

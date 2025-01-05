package main;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.imageio.ImageIO;

/**
 *
 * @author Tusk
 */
public class main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Snigger");

        // Load the icon using getResource()
        try {
            BufferedImage iconImage = ImageIO.read(main.class.getResource("/snake/snikhead.png"));
            window.setIconImage(iconImage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load window icon.");
        }

        gamepanel gamepane = new gamepanel();
        window.add(gamepane);
        gamepane.requestFocusInWindow();
        window.pack();

        window.setLocationRelativeTo(null); // Center the window on the screen
        window.setVisible(true);

        gamepane.startime();
    }
}
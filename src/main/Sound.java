package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    private static Sound instance;
    private Clip backgroundClip; // For background music
    private Clip effectClip;     // For sound effects
    URL soundURL[] = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/gameover.wav");
        soundURL[1] = getClass().getResource("/sound/apple.wav");
        soundURL[2] = getClass().getResource("/sound/soundbg.wav");
        soundURL[3] = getClass().getResource("/sound/soundbg1.wav");

        for (int i = 0; i < soundURL.length; i++) {
            if (soundURL[i] != null) {
                System.out.println("Sound " + i + " loaded: " + soundURL[i]);
            } else {
                System.out.println("Sound " + i + " not found.");
            }
        }
    }

    public static Sound getInstance() {
        if (instance == null) {
            instance = new Sound();
        }
        return instance;
    }

public void setFile(int index, boolean isBackground) {
    try {
        if (isBackground) {
            if (backgroundClip != null && backgroundClip.isOpen()) {
                backgroundClip.close(); // Close the previous background clip
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL[index]);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            System.out.println("Background clip initialized for index: " + index);
        } else {
            if (effectClip != null && effectClip.isOpen()) {
                effectClip.close(); // Close the previous effect clip
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL[index]);
            effectClip = AudioSystem.getClip();
            effectClip.open(audioIn);
            System.out.println("Effect clip initialized for index: " + index);

            // Add a small delay to ensure the clip is ready
            try {
                Thread.sleep(100); // 100ms delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error initializing clip for index: " + index);
    }
}

public void play(boolean isBackground) {
    if (isBackground && backgroundClip != null) {
        backgroundClip.start();
        System.out.println("Background music started.");
    } else if (!isBackground && effectClip != null) {
        effectClip.start();
        System.out.println("Sound effect started.");
    } else {
        System.out.println("Error: Clip not initialized.");
    }
}

    public void loop(boolean isBackground) {
        if (isBackground && backgroundClip != null) {
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public boolean isPlayingBackground() {
        return backgroundClip != null && backgroundClip.isRunning();
    }

  public void stop(boolean isBackground) {
    if (isBackground && backgroundClip != null) {
        if (backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
        if (backgroundClip.isOpen()) {
            backgroundClip.close();
        }
        backgroundClip = null;
        System.out.println("Background music stopped.");
    } else if (!isBackground && effectClip != null) {
        if (effectClip.isRunning()) {
            effectClip.stop();
        }
        if (effectClip.isOpen()) {
            effectClip.close();
        }
        effectClip = null;
        System.out.println("Sound effect stopped.");
    }
}
}
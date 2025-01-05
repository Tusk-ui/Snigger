package main;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sound {
    private static Sound instance;
    private Clip backgroundClip; // For background music
    private Clip soundEffectClip; // For sound effects
    URL soundURL[] = new URL[30];
    private FloatControl backgroundVolumeControl;
    private FloatControl soundEffectVolumeControl;
    private float backgroundVolume = 0.5f; // Default volume for background music
    public float soundEffectVolume = 0.5f; // Default volume for sound effects

    public Sound() {
        // Load sound files as resources
        soundURL[0] = getClass().getResource("/sound/negat.wav");
        soundURL[1] = getClass().getResource("/sound/apple.wav");
        soundURL[2] = getClass().getResource("/sound/soundbg.wav");
        soundURL[3] = getClass().getResource("/sound/soundbg1.wav");

        // Debug: Print loaded sound URLs
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
            // Use the preloaded URL from the soundURL array
            URL soundResource = soundURL[index];
            if (soundResource == null) {
                System.err.println("Sound file not found for index: " + index);
                return;
            }

            // Load the audio input stream from the resource URL
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundResource);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (isBackground) {
                backgroundClip = clip;
                backgroundVolumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                System.out.println("Background music clip initialized.");
            } else {
                soundEffectClip = clip;
                soundEffectVolumeControl = (FloatControl) soundEffectClip.getControl(FloatControl.Type.MASTER_GAIN);
                System.out.println("Sound effect clip initialized.");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.err.println("Failed to load sound file for index: " + index);
        }
    }

    public void play(boolean isBackground) {
        if (isBackground && backgroundClip != null) {
            backgroundClip.start();
            System.out.println("Background music started.");
        } else if (!isBackground && soundEffectClip != null) {
            soundEffectClip.start();
            System.out.println("Sound effect started.");
        }
    }

    public void loop(boolean isBackground) {
        if (isBackground && backgroundClip != null) {
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Background music looped.");
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
        } else if (!isBackground && soundEffectClip != null) {
            if (soundEffectClip.isRunning()) {
                soundEffectClip.stop();
            }
            if (soundEffectClip.isOpen()) {
                soundEffectClip.close();
            }
            soundEffectClip = null;
            System.out.println("Sound effect stopped.");
        }
    }

    public void setVolume(float volume, boolean isBackground) {
        if (isBackground) {
            backgroundVolume = volume;
            if (backgroundVolumeControl != null) {
                // Convert the linear volume (0.0 to 1.0) to decibels (dB)
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                backgroundVolumeControl.setValue(dB);
                System.out.println("Background Music Volume (dB): " + dB);
            }
        } else {
            soundEffectVolume = volume;
            if (soundEffectVolumeControl != null) {
                // Convert the linear volume (0.0 to 1.0) to decibels (dB)
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                soundEffectVolumeControl.setValue(dB);
                System.out.println("SFX Volume (dB): " + dB);
            }
        }
        System.out.println("Volume set: " + (isBackground ? "Background" : "SFX") + " = " + volume);
    }

    public float getBackgroundVolume() {
        return backgroundVolume;
    }

    public float getSoundEffectVolume() {
        return soundEffectVolume;
    }
}
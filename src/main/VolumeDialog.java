package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.Sound;

public class VolumeDialog extends JDialog {
    private JSlider bgMusicSlider; // Slider for background music volume
    private JSlider sfxSlider;     // Slider for sound effects volume
    private Sound sound;           // Reference to the Sound class

    public VolumeDialog(JFrame parent, Sound sound) {
        super(parent, "Volume Settings", true); // Modal dialog
        this.sound = sound;

        setSize(300, 200);
        setLocationRelativeTo(parent); // Center the dialog relative to the game window
        setLayout(new GridLayout(3, 1));

        // Background Music Slider
        JLabel bgMusicLabel = new JLabel("Background Music Volume:");
        bgMusicSlider = new JSlider(0, 100, (int) (sound.getBackgroundVolume() * 100));
        bgMusicSlider.setMajorTickSpacing(20);
        bgMusicSlider.setPaintTicks(true);
        bgMusicSlider.setPaintLabels(true);

        // Sound Effects Slider
        JLabel sfxLabel = new JLabel("Sound Effects Volume:");
        sfxSlider = new JSlider(0, 100, (int) (sound.getSoundEffectVolume() * 100));
        sfxSlider.setMajorTickSpacing(20);
        sfxSlider.setPaintTicks(true);
        sfxSlider.setPaintLabels(true);

        // Apply Button
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyVolumeChanges();
                dispose(); // Close the dialog
            }
        });

        // Add components to the dialog
        add(bgMusicLabel);
        add(bgMusicSlider);
        add(sfxLabel);
        add(sfxSlider);
        add(applyButton);
    }

    private void applyVolumeChanges() {
        // Convert slider values to volume levels (0.0 to 1.0)
        float bgMusicVolume = bgMusicSlider.getValue() / 100f;
        float sfxVolume = sfxSlider.getValue() / 100f;

        // Update the volume in the Sound class
        sound.setVolume(bgMusicVolume, true);  // Set background music volume
        sound.setVolume(sfxVolume, false);     // Set sound effects volume
    }
   }
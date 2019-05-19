package de.slowloris.toaster.controller.gui;

import javax.swing.*;

public class MainGUI {
    private static MainGUI instance;
    public JSlider inL;
    public JSlider inR;
    public JPanel panel;
    public JCheckBox sensitiveCheckBox;
    public JLabel gearLabel;
    public JLabel speedLabel;


    public static void init(){
        instance = new MainGUI();

        instance.inR.setEnabled(false);
        instance.inR.setMinimum(0);
        instance.inR.setMaximum(255);
        instance.inR.setValue(0);
        instance.inL.setEnabled(false);
        instance.inL.setMinimum(0);
        instance.inL.setMaximum(255);
        instance.inL.setValue(0);
        instance.gearLabel.setEnabled(false);
        instance.gearLabel.setText("Gear: Forward");
        instance.sensitiveCheckBox.setEnabled(false);
        instance.sensitiveCheckBox.setSelected(false);
        instance.speedLabel.setEnabled(false);
        instance.speedLabel.setText("0 | 0");
    }

    public static MainGUI getInstance() {
        return instance;
    }

}

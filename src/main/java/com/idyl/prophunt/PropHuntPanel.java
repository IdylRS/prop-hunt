package com.idyl.prophunt;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropHuntPanel extends PluginPanel implements ActionListener {
    private PropHuntPlugin plugin;

    private JButton myButton;

    public PropHuntPanel(PropHuntPlugin plugin) {
        this.plugin = plugin;
        myButton = new JButton("Randomize Model");
        myButton.addActionListener(this);
        add(myButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        plugin.setRandomModelID();
    }

}
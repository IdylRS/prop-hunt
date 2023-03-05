package com.idyl.prophunt;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropHuntPanel extends PluginPanel implements ActionListener {
    private PropHuntPlugin plugin;

    private JButton myButton;
    private JComboBox<String> comboBox;

    public PropHuntPanel(PropHuntPlugin plugin) {
        this.plugin = plugin;

        comboBox = new JComboBox<>();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboBox.getSelectedItem();
                if(PropHuntModelId.map.get(selectedOption) == null) return;

                plugin.setModelID(PropHuntModelId.valueOf(selectedOption));
            }
        });
        updateComboBox();

        myButton = new JButton("Randomize Model");
        myButton.addActionListener(this);
        add(myButton);
        add(comboBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        plugin.setRandomModelID();
    }

    public void updateComboBox() {
        comboBox.removeAllItems();
        PropHuntModelId.map.keySet().forEach(item -> comboBox.addItem(item));
    }

}
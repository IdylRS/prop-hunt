package com.idyl.prophunt;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropHuntPanel extends PluginPanel implements ActionListener {
    private PropHuntPlugin plugin;

    private JButton myButton;
    private JComboBox<String> comboBox;
    private JLabel title;

    public PropHuntPanel(PropHuntPlugin plugin) {
        this.plugin = plugin;

        // Set layout to GridBagLayout
        setLayout(new GridBagLayout());

        // Create GridBagConstraints object to set component constraints
        GridBagConstraints gbc = new GridBagConstraints();

        // Set title constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        title = new JLabel("Prop Hunt");
        title.setFont(FontManager.getRunescapeBoldFont());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, gbc);

        // Set label and button constraints for second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
//        gbc.insets = new Insets(5, 10, 10, 10); // add padding
        myButton = new JButton("Randomize Model");
        myButton.addActionListener(this);
        add(myButton, gbc);

        // Set label and button constraints for third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
//        gbc.insets = new Insets(5, 10, 10, 10); // add padding
        JLabel label2 = new JLabel("Custom Model List:");
        add(label2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
//        gbc.insets = new Insets(5, 10, 10, 10); // add padding
        comboBox = new JComboBox<>();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboBox.getSelectedItem();
                if(PropHuntModelId.map.get(selectedOption) == null) return;

                plugin.setModelID(PropHuntModelId.valueOf(selectedOption));
            }
        });
        updateComboBox();
        add(comboBox, gbc);
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
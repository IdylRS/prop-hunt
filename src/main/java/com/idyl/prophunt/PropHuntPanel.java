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
        gbc.insets = new Insets(0, 0, 10, 0); // add padding
        title = new JLabel("Prop Hunt");
        title.setFont(FontManager.getRunescapeBoldFont());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, gbc);

        // Set label and button constraints for third row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 0); // add padding
        JLabel label3 = new JLabel("Custom Model List:");
        add(label3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 0); // add padding
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

        // Hide mode toggle button for panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 5, 0); // add padding
        JButton toggleHideMode = new JButton("Toggle Hide Mode");
        toggleHideMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plugin.toggleHide();
            }
        });
        add(toggleHideMode, gbc);

        // Set label and button constraints for second row
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 5, 3); // add padding
        myButton = new JButton("Randomize Model");
        myButton.addActionListener(this);
        add(myButton, gbc);

        // Set label and button constraints for second row
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 3); // add padding
        JButton rotateClockwiseButton = new JButton("↻");
        rotateClockwiseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plugin.rotateModel(1);
            }
        });
        add(rotateClockwiseButton, gbc);

        // Set label and button constraints for second row
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 3, 5, 0); // add padding
        JButton rotateCounterButton = new JButton("↺");
        rotateCounterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plugin.rotateModel(-1);
            }
        });
        add(rotateCounterButton, gbc);
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
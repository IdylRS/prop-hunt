package com.idyl.prophunt;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropHuntPanel extends PluginPanel implements ActionListener {
    private PropHuntPlugin plugin;

    private JButton myButton;
    private JToggleButton hideModeToggleButton;
    private JTextField modelIdTextField;

    public PropHuntPanel(PropHuntPlugin plugin) {
        this.plugin = plugin;

        // Set layout of the overall panel to BorderLayout
        setLayout(new BorderLayout());

        // Add padding to the top of the whole panel
        setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));  // 20px padding to the top

        // Create a container panel for the title with padding
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());

        // Title label outside the bordered panel
        JLabel title = new JLabel("Prop Hunt", SwingConstants.CENTER);
        title.setFont(FontManager.getRunescapeBoldFont().deriveFont(30f));
        title.setForeground(Color.YELLOW);

        // Add the title label to the title panel
        titlePanel.add(title, BorderLayout.CENTER);

        // Add padding to the bottom of the title by setting an EmptyBorder on the title panel
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 0 top, 0 left, 10 bottom, 0 right padding

        // Add the titlePanel (with padding) to the top of the main panel
        add(titlePanel, BorderLayout.NORTH);

        // Create subtitle section below the title
        JPanel subtitlePanel = new JPanel();
        JLabel subtitle = new JLabel("Hider Settings:");
        subtitle.setFont(FontManager.getRunescapeBoldFont().deriveFont(20f));  // Slightly smaller size
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitlePanel.add(subtitle);

        // Add padding to the subtitle
        subtitlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Padding below subtitle

        // Add the subtitlePanel right below the title section
        add(subtitlePanel, BorderLayout.CENTER);

        // Create the "Model ID" section panel with components
        JPanel modelPanel = createModelPanel();

        // Create the "Orientation" section panel with components
        JPanel orientationPanel = createOrientationPanel();

        // Create the "Hide Mode" section panel with the toggle button
        JPanel hideModePanel = createHideModePanel();

        // Add all sections to the main panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Arrange sections vertically
        contentPanel.add(modelPanel);
        contentPanel.add(orientationPanel);
        contentPanel.add(hideModePanel);

        // Add the content panel with all sections to the center of the overall panel
        add(contentPanel, BorderLayout.SOUTH); // Placing the sections below the subtitle
    }

    private JPanel createModelPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setToolTipText("Set your Model ID.");
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Model"),  // Original titled border
                BorderFactory.createEmptyBorder(2, 0, 7, 0)  // 10px top padding, 0 for left, right, and bottom
        ));
        GridBagConstraints gbc = new GridBagConstraints();

        // Add label for the model ID text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel modelIdLabel = new JLabel("Model ID");
        panel.add(modelIdLabel, gbc);

        // Create and configure the text field for numeric input (Model ID)
        modelIdTextField = new JTextField(10);
        modelIdTextField.setText(String.valueOf(plugin.getModelId()));  // Set default value from plugin
        ((AbstractDocument) modelIdTextField.getDocument()).setDocumentFilter(new NumericDocumentFilter());  // Allow only numeric input
        modelIdTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateModelId();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateModelId();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateModelId();
            }

            private void updateModelId() {
                try {
                    int modelId = Integer.parseInt(modelIdTextField.getText());  // Parse the model ID
                    plugin.setModelID(new PropHuntModelId("", modelId));  // Update the plugin with the new model ID
                } catch (NumberFormatException ex) {
                    // In case of invalid input, keep the text field content as-is or reset it
                    modelIdTextField.setText("");  // Clear or reset on invalid input
                }
            }
        });
        gbc.gridx = 1;
        panel.add(modelIdTextField, gbc);

        // Create a panel to encapsulate min and max ID settings
        JPanel randomPanel = new JPanel();
        randomPanel.setLayout(new GridBagLayout());
        randomPanel.setToolTipText("Set the range of the model IDs you would like to randomize from.");
        randomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Randomizer"),
                BorderFactory.createEmptyBorder(2, 30, 7, 30)
        ));  // Updated to "Randomizer" title

        // Add min ID label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel minLabel = new JLabel("Min ID");
        randomPanel.add(minLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);
        JTextField minField = new JTextField(Integer.toString(plugin.getMin()), 4);
        ((AbstractDocument) minField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        minField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateMinField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateMinField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateMinField();
            }

            private void updateMinField() {
                try {
                    int minValue = Integer.parseInt(minField.getText());
                    plugin.setMinModelID(minValue);
                } catch (NumberFormatException ex) {
                    minField.setText("0");
                }
            }
        });
        randomPanel.add(minField, gbc);

        // Add max ID label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel maxLabel = new JLabel("Max ID");
        randomPanel.add(maxLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField maxField = new JTextField(Integer.toString(plugin.getMax()), 4);
        ((AbstractDocument) maxField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        maxField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateMaxField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateMaxField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateMaxField();
            }

            private void updateMaxField() {
                try {
                    int maxValue = Integer.parseInt(maxField.getText());
                    plugin.setMaxModelID(maxValue);
                } catch (NumberFormatException ex) {
                    maxField.setText("0");
                }
            }
        });
        randomPanel.add(maxField, gbc);

        // Add randomize button inside the randomPanel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;  // Make the button span both columns
        gbc.insets = new Insets(10, 0, 10, 0);
        myButton = new JButton("Randomize Model");
        myButton.addActionListener(this);
        randomPanel.add(myButton, gbc);

        // Add the randomPanel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;  // Make this panel span across both columns of the grid
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(randomPanel, gbc);

        return panel;
    }


    private JPanel createOrientationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setToolTipText("Rotate model orientation.");
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Orientation"),  // Original titled border
                BorderFactory.createEmptyBorder(2, 0, 7, 0)  // 10px top padding, 0 for left, right, and bottom
        ));
        GridBagConstraints gbc = new GridBagConstraints();

        // Rotate clockwise button
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton rotateClockwiseButton = new JButton("↻");
        rotateClockwiseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plugin.rotateModel(1);
            }
        });
        panel.add(rotateClockwiseButton, gbc);

        // Rotate label
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel rotateLabel = new JLabel("Rotate");
        panel.add(rotateLabel, gbc);

        // Rotate counter-clockwise button
        gbc.gridx = 2;
        gbc.gridy = 0;
        JButton rotateCounterButton = new JButton("↺");
        rotateCounterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plugin.rotateModel(-1);
            }
        });
        panel.add(rotateCounterButton, gbc);

        return panel;
    }

    private JPanel createHideModePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // No label, just padding

        // Hide Mode toggle button
        hideModeToggleButton = new JToggleButton();
        hideModeToggleButton.setSelected(plugin.getHideMode());
        if(plugin.getHideMode()) {
            hideModeToggleButton.setText("Hide Mode: ON");
        }
        else {
            hideModeToggleButton.setText("Hide Mode: OFF");
        }
        hideModeToggleButton.addActionListener(e -> toggleHideMode());
        hideModeToggleButton.setFont(FontManager.getRunescapeBoldFont());

        // Highlight the toggle button when selected
        hideModeToggleButton.setBackground(Color.DARK_GRAY);
        hideModeToggleButton.setForeground(hideModeToggleButton.isSelected() ? Color.GREEN : Color.RED);

        // Listen for state change to update the appearance
        hideModeToggleButton.addChangeListener(e -> {
            if (hideModeToggleButton.isSelected()) {
                hideModeToggleButton.setForeground(Color.WHITE); // Highlight when selected
                hideModeToggleButton.setText("Hide Mode: ON");
            } else {
                hideModeToggleButton.setForeground(Color.RED); // Unhighlight when deselected
                hideModeToggleButton.setText("Hide Mode: OFF");
            }
        });

        panel.add(hideModeToggleButton, new GridBagConstraints());

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        plugin.setRandomModelID();

        // Get the new model ID (after randomization)
        int newModelID = plugin.getModelId();  // Get the current model ID from the plugin

        // Set the new model ID in the text field
        modelIdTextField.setText(String.valueOf(newModelID));

        // Also update the plugin with the newly randomized model ID
        plugin.setModelID(new PropHuntModelId("", newModelID));

    }

    // Method to toggle hideMode when the toggle button is clicked
    private void toggleHideMode() {
        boolean currentHideMode = hideModeToggleButton.isSelected();
        plugin.setHideMode(currentHideMode);  // Update the config and the plugin state
    }

    // Custom DocumentFilter to allow only numeric input
    static class NumericDocumentFilter extends javax.swing.text.DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                throws javax.swing.text.BadLocationException {
            if (string.matches("[0-9]")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs)
                throws javax.swing.text.BadLocationException {
            if (text.matches("[0-9]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}

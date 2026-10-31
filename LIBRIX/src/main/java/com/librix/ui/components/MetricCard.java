package com.librix.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class MetricCard extends JPanel {
    public MetricCard(String label, String value, Color accent) {
        setLayout(new BorderLayout(8, 8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(226, 231, 242)), new EmptyBorder(18, 18, 18, 18)));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(new Color(16, 27, 53));
        JLabel titleLabel = new JLabel(label.toUpperCase());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLabel.setForeground(new Color(105, 115, 137));
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(5, 1));
        add(accentBar, BorderLayout.WEST);
        add(valueLabel, BorderLayout.CENTER);
        add(titleLabel, BorderLayout.SOUTH);
    }
}

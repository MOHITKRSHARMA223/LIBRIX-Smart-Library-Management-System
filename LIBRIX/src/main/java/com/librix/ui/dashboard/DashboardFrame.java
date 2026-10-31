package com.librix.ui.dashboard;

import com.librix.model.Role;
import com.librix.model.UserSession;
import com.librix.ui.auth.LoginFrame;
import com.librix.ui.components.MetricCard;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class DashboardFrame extends JFrame {
    private final JPanel content = new JPanel(new BorderLayout(24, 24));
    private boolean darkMode;

    public DashboardFrame(UserSession session) {
        super("LIBRIX | Smart Library. Smarter Learning.");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 820);
        setLocationRelativeTo(null);
        build(session);
    }

    private void build(UserSession session) {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(16, 27, 53));
        sidebar.setBorder(new EmptyBorder(28, 20, 28, 20));
        sidebar.setPreferredSize(new Dimension(235, 1));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("LIBRIX");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 26));
        JLabel tagline = new JLabel("Smart Library");
        tagline.setForeground(new Color(171, 188, 228));
        tagline.setBorder(new EmptyBorder(2, 0, 36, 0));
        sidebar.add(brand); sidebar.add(tagline);
        for (String item : session.role() == Role.STUDENT ? new String[]{"Overview", "Browse Books", "My Loans", "Reservations", "Notifications"} : new String[]{"Overview", "Books", "Members", "Borrowing", "Reservations", "Fines & Payments", "Reports"}) {
            JButton nav = new JButton(item);
            nav.setHorizontalAlignment(SwingConstants.LEFT);
            nav.setForeground(new Color(222, 230, 249));
            nav.setBackground(new Color(16, 27, 53));
            nav.setBorder(new EmptyBorder(12, 12, 12, 12));
            nav.setFocusPainted(false);
            nav.setContentAreaFilled(false);
            sidebar.add(nav);
        }
        sidebar.add(Box.createVerticalGlue());
        JButton logout = new JButton("Sign out");
        logout.setForeground(new Color(255, 190, 190));
        logout.setContentAreaFilled(false);
        logout.setBorder(new EmptyBorder(12, 12, 12, 12));
        logout.setHorizontalAlignment(SwingConstants.LEFT);
        logout.addActionListener(e -> { dispose(); new LoginFrame(new com.librix.security.AuthenticationService()).setVisible(true); });
        sidebar.add(logout);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel heading = new JLabel("Good morning, " + session.username());
        heading.setFont(new Font("SansSerif", Font.BOLD, 25));
        heading.setForeground(new Color(16, 27, 53));
        JLabel role = new JLabel(session.role().label());
        role.setForeground(new Color(105, 115, 137));
        JButton theme = new JButton("Toggle theme");
        theme.addActionListener(e -> toggleTheme());
        header.add(heading, BorderLayout.WEST);
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerRight.setOpaque(false); headerRight.add(role); headerRight.add(theme); header.add(headerRight, BorderLayout.EAST);

        JPanel metrics = new JPanel(new GridLayout(1, 4, 16, 0)); metrics.setOpaque(false);
        metrics.add(new MetricCard("Total books", "1,284", new Color(49, 94, 202)));
        metrics.add(new MetricCard("Available now", "936", new Color(42, 159, 122)));
        metrics.add(new MetricCard("Active members", "417", new Color(121, 87, 232)));
        metrics.add(new MetricCard("Overdue loans", "18", new Color(218, 91, 91)));

        JPanel activity = new JPanel(new BorderLayout(12, 12)); activity.setBackground(Color.WHITE); activity.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(226, 231, 242)), new EmptyBorder(18, 18, 18, 18)));
        JLabel activityTitle = new JLabel("Recent activity"); activityTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        JTextArea activityText = new JTextArea("08:42   Aisha Rahman borrowed Clean Code\n08:16   New member registration completed\nYesterday   24 books returned\nYesterday   Fine payment recorded"); activityText.setEditable(false); activityText.setFont(new Font("SansSerif", Font.PLAIN, 14)); activityText.setForeground(new Color(67, 76, 97)); activityText.setBackground(Color.WHITE); activityText.setBorder(new EmptyBorder(8, 0, 0, 0));
        activity.add(activityTitle, BorderLayout.NORTH); activity.add(activityText, BorderLayout.CENTER);
        content.setBorder(new EmptyBorder(30, 30, 30, 30)); content.add(header, BorderLayout.NORTH); content.add(metrics, BorderLayout.CENTER); content.add(activity, BorderLayout.SOUTH);
        add(sidebar, BorderLayout.WEST); add(content, BorderLayout.CENTER);
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        Color background = darkMode ? new Color(20, 25, 35) : new Color(245, 247, 252);
        content.setBackground(background);
        repaint();
    }
}

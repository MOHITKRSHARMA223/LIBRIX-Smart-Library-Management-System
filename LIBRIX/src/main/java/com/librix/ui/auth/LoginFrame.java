package com.librix.ui.auth;

import com.librix.model.UserSession;
import com.librix.security.AuthenticationService;
import com.librix.ui.dashboard.DashboardFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class LoginFrame extends JFrame {
    private final AuthenticationService authenticationService;
    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JLabel message = new JLabel(" ");

    public LoginFrame(AuthenticationService authenticationService) {
        super("LIBRIX | Sign in");
        this.authenticationService = authenticationService;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        build();
    }

    private void build() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        JPanel brand = new JPanel(); brand.setBackground(new Color(16, 27, 53)); brand.setBorder(new EmptyBorder(70, 55, 55, 45)); brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("LIBRIX"); name.setForeground(Color.WHITE); name.setFont(new Font("SansSerif", Font.BOLD, 38));
        JLabel tagline = new JLabel("Smart Library. Smarter Learning."); tagline.setForeground(new Color(171, 188, 228)); tagline.setFont(new Font("SansSerif", Font.PLAIN, 17)); tagline.setBorder(new EmptyBorder(8, 0, 28, 0));
        JTextArea copy = new JTextArea("A calm, intelligent workspace for every book, member, and learning journey."); copy.setWrapStyleWord(true); copy.setLineWrap(true); copy.setEditable(false); copy.setOpaque(false); copy.setForeground(new Color(225, 232, 247)); copy.setFont(new Font("SansSerif", Font.PLAIN, 18));
        brand.add(name); brand.add(tagline); brand.add(copy); root.add(brand);
        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(new Color(245, 247, 252));
        JPanel card = new JPanel(); card.setBackground(Color.WHITE); card.setBorder(new EmptyBorder(34, 38, 34, 38)); card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS)); card.setPreferredSize(new Dimension(350, 370));
        JLabel title = new JLabel("Welcome back"); title.setFont(new Font("SansSerif", Font.BOLD, 25)); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel hint = new JLabel("Sign in to continue to your workspace"); hint.setForeground(new Color(105, 115, 137)); hint.setBorder(new EmptyBorder(5, 0, 22, 0)); hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        username.setBorder(BorderFactory.createTitledBorder("Username")); password.setBorder(BorderFactory.createTitledBorder("Password")); username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52)); password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        JButton login = new JButton("Sign in"); login.setBackground(new Color(49, 94, 202)); login.setForeground(Color.WHITE); login.setFocusPainted(false); login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44)); login.setAlignmentX(Component.LEFT_ALIGNMENT); login.addActionListener(e -> signIn());
        message.setForeground(new Color(190, 60, 70)); message.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title); card.add(hint); card.add(username); card.add(Box.createVerticalStrut(12)); card.add(password); card.add(Box.createVerticalStrut(18)); card.add(login); card.add(Box.createVerticalStrut(10)); card.add(message); form.add(card); root.add(form); add(root);
        getRootPane().setDefaultButton(login);
    }

    private void signIn() {
        try {
            UserSession session = authenticationService.authenticate(username.getText(), password.getPassword());
            dispose(); new DashboardFrame(session).setVisible(true);
        } catch (RuntimeException ex) { message.setText(ex.getMessage()); password.setText(""); }
    }
}

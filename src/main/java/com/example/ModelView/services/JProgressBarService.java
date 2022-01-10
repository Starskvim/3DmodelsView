package com.example.ModelView.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@Service
@NoArgsConstructor
@Setter
@Getter
public class JProgressBarService extends JFrame {

    static private int BOR = 10;

    private JFrame frame;

    private JPanel panel;

    private JProgressBar progressBar;


    public JProgressBarService (String frameName, int maxValue) {
        this.frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.toFront();
        frame.setState(JFrame.NORMAL);

        this.panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(BOR, BOR, BOR, BOR));

        this.progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(maxValue);
        progressBar.setValue(0);
        panel.add(progressBar);

        panel.add(Box.createVerticalGlue());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        buttonsPanel.add(Box.createHorizontalGlue());

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.setPreferredSize(new Dimension(200, 100));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public synchronized JProgressBar createGUI(String frameName, int minValue, int maxValue) {

        this.frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.toFront();
        frame.setState(JFrame.NORMAL);

        this.panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(BOR, BOR, BOR, BOR));

        this.progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(minValue);
        progressBar.setMaximum(maxValue);
        progressBar.setValue(0);
        panel.add(progressBar);

        panel.add(Box.createVerticalGlue());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        buttonsPanel.add(Box.createHorizontalGlue());

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.setPreferredSize(new Dimension(260, 225));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return progressBar;
    }

    public void updateBar (int current){
        progressBar.setValue(current);
        frame.repaint();
        //frame.pack();
        //frame.setLocationRelativeTo(null);
        //frame.setVisible(true);
    }

}

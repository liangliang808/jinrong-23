package com.softeem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TxSlider extends JPanel {

    private int value;
    private boolean isDragging;
    private int xOffset;

    public TxSlider() {
        value = 0;
        isDragging = false;
        xOffset = 0;

        setPreferredSize(new Dimension(200, 20));
        setBackground(Color.WHITE);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                xOffset = mouseX;
                isDragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    int mouseX = e.getX();
                    int offset = mouseX - xOffset;
                    int width = getWidth();
                    value = (int) (100 * offset / (double) width);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.GREEN);
        int fillWidth = (int) (value * width / 100.0);
        g.fillRect(0, 0, fillWidth, height);
    }

    public int getValue() {
        return value;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TX Slider");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TxSlider slider = new TxSlider();
        frame.getContentPane().add(slider);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
package org.example;

import javax.swing.*;

public class Window extends JFrame {
    public final int WIDTH=800;
    public final int HEIGHT=650;

    private MainSence mainSence;
    public Window(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.mainSence =new MainSence(this);
        this.add(mainSence);
        this.setVisible(true);
    }
}

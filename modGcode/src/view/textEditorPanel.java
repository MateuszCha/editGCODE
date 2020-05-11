package view;

import javax.swing.*;
import java.awt.*;

public class textEditorPanel extends JPanel {
    private final Color FONT_COLOR = new Color(141, 87, 0);
    private final Font FONT = new Font("Serif",Font.BOLD,10);
    private int xScreenSize;
    private int yScreenSize;
    String[] str = new String[100];

//////////////////////// Constructors //////////////////////////
    public textEditorPanel(int xScreenSize, int yScreenSize){
        this.xScreenSize = xScreenSize;
        this.yScreenSize = yScreenSize;
        System.out.println("Creating JPanel + x:" + xScreenSize + " y: "+ yScreenSize );
        for(int i = 0; i<str.length;i++){
            str[i] = "Test" + i;
        }
        this.init();
    }

////////////////////////METHODS ///////////////////////////////////
    private void init(){
        System.out.println("Initializing JPanel" + xScreenSize);
        this.setBounds(0,0,xScreenSize/10,yScreenSize/10);
        this.setBackground(Color.RED);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.add(this);
        this.setVisible(true);
    }
    private void paint(Graphics2D g){
        System.out.println("Painting Jpanel");
        g.setColor(FONT_COLOR);
        g.setFont(FONT);
        for(int i = 0; i<str.length; i++) {
            g.drawString(str[i],xScreenSize+10,FONT.getSize());
        }
    }

///////////////////////OVERRIDE METHODS//////////////////////////////

    @Override
    protected void paintComponent(Graphics g) {
       this.paint(g);
    }

////////////////////////////SETTER ANG GETTER ////////////////////
////////////////////////////INNER & NESTED CLASS ////////////////////////////////////////////////////

}

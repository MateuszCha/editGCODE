package view;

import javax.swing.*;
import java.awt.*;

public class ViewFrame extends JFrame {
    private final int X_WIDOWS_SIZE;
    private final int Y_WIDOWS_SIZE;

        public ViewFrame(){
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            this.X_WIDOWS_SIZE = dimension.width/2;
            this.Y_WIDOWS_SIZE = dimension.height/2;
            this.init();

        }

    private void init(){
        int x = this.X_WIDOWS_SIZE - X_WIDOWS_SIZE/2;
        int y = this.Y_WIDOWS_SIZE - Y_WIDOWS_SIZE/2;
        Container contTEMp = this.getContentPane();


        this.setTitle("GCodeEditor v 1.00.01");
        this.setBounds(x,y,X_WIDOWS_SIZE,Y_WIDOWS_SIZE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(new textEditorPanel(X_WIDOWS_SIZE/2,Y_WIDOWS_SIZE));
        this.setVisible(true);
    }
}

package game2048;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    // The Main Method
    public static void main(String[] args) {

        // Instantiation of the class
        JFrame game2048 = new JFrame();
        game2048.setTitle("2048 GAME By Group 3");

        // Button to close the game
        game2048.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        game2048.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to EXIT", "Close", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if(choice == JOptionPane.YES_OPTION)
                {
                    e.getWindow().dispose();
                }
            }

        });

        // Setting the size of the window interface
        game2048.setSize(340, 400);

        // Setting the window form irresizeable
        game2048.setResizable(false);

        // Adding the object of the Main Class in the form
        game2048.add(new Game2048());

        // Not giving the location relative to the other frame
        game2048.setLocationRelativeTo(null);

        // Making the Window Game Frame Visible
        game2048.setVisible(true);
    }   // End of the Main Method
}

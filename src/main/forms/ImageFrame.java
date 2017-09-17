package main.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Андрей on 17.09.2017.
 */
public class ImageFrame extends JFrame{

    public static String expansion = null;


    private static JLabel jlabel = new JLabel();
    private  JButton saveButton = new JButton("Save file with differences");

    public ImageFrame(){
        super("Image with differences");
        this.setBounds(150, 150, 1000, 1000);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        saveButton.addActionListener(new SaveListener());
        container.add(saveButton, BorderLayout.SOUTH);
        container.add(jlabel, BorderLayout.CENTER);


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void addInJLable(File file){
        jlabel.setIcon(new ImageIcon(file.toString()));
    }

    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    File f = new File("rez.png");
                    FileOutputStream fileStream = new FileOutputStream(fc.getSelectedFile());
                    ObjectOutputStream os = new ObjectOutputStream(fileStream);
                    os.writeObject(f);
                }
                catch (Exception e1) {
                    System.out.println("Что-то пошло не так...");
                }
            }
        }
    }

}

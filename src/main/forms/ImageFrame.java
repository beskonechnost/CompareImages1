package main.forms;

import main.extra.Extra;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Андрей on 17.09.2017.
 */
public class ImageFrame extends JFrame{

    public static String expansion = null;
    public static String path = null;
    public static BufferedImage bf = null;


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
            try {
                Calendar c = new GregorianCalendar();
                ImageIO.write(bf, expansion, new File(path+"result_"+c.get(Calendar.DAY_OF_MONTH)+"-"+c.get(Calendar.MONTH)+"__"+c.get(Calendar.HOUR)+"-"+c.get(Calendar.MINUTE)+"."+expansion));
                JDialog jd = new JDialog();
                JOptionPane.showMessageDialog(jd,
                        "Success",
                        "Differences saved",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e1) {
                Extra.showError("Error","Something went wrong. File not saved");
            }
        }
    }


}

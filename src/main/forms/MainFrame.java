package main.forms;

import main.extra.Extra;
import main.extra.ForCirculating;
import main.fromImge.WorkerWithImages;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static main.fromImge.WorkerWithImages.*;

/**
 * Created by ������ on 14.09.2017.
 */
public class MainFrame extends JFrame {

    File first = null;
    File second = null;

    private JButton addFirstImage = new JButton("Add First Image");
    private JLabel label1 = new JLabel("Add image");
    private JButton clearFirstImage = new JButton("Clear First Image");

    private JButton addSecondImage = new JButton("Add Second Image");
    private JLabel label2 = new JLabel("Add image");
    private JButton clearSecondImage = new JButton("Clear Second Image");

    private JButton compareImages = new JButton("Compare");


    public MainFrame(){
        super("Compare Images");
        this.setBounds(150, 150, 250, 250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());

        Box box1 = new Box(BoxLayout.Y_AXIS);
        addFirstImage.addActionListener(new SelectFirstImageListener());
        box1.add(Box.createVerticalStrut(10));
        box1.add(addFirstImage);
        box1.add(Box.createVerticalStrut(10));
        box1.add(label1);
        box1.add(Box.createVerticalStrut(10));
        clearFirstImage.addActionListener(new ClearFirstListener());
        box1.add(clearFirstImage);
        box1.add(Box.createVerticalStrut(10));
        box1.setBorder(new TitledBorder("First image"));

        Box box2 = new Box(BoxLayout.Y_AXIS);
        addSecondImage.addActionListener(new SelectSecondImageListener());
        box2.add(Box.createVerticalStrut(10));
        box2.add(addSecondImage);
        box2.add(Box.createVerticalStrut(10));
        box2.add(label2);
        box2.add(Box.createVerticalStrut(10));
        clearSecondImage.addActionListener(new ClearSecondListener());
        box2.add(clearSecondImage);
        box2.add(Box.createVerticalStrut(10));
        box2.setBorder(new TitledBorder("Second image"));

        container.add(box1);
        container.add(box2);
        compareImages.addActionListener(new CompareImagesListener());
        container.add(compareImages);

        this.setPreferredSize(new Dimension(440, 180));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public File selectFile(JLabel label){
        JFileChooser jfc = new JFileChooser();
        File file = null;
        if(jfc.showOpenDialog(label) == JFileChooser.APPROVE_OPTION){
            file = jfc.getSelectedFile();
        }
        if(WorkerWithImages.formatImagesTest(file)) {
            label.setText("Selected");
        }else {
            file = null;
            label.setText("Add image");
            Extra.showError("Invalid file extension selected!", "Extension file error");
        }
        return file;
    }
    public File clearFile(JLabel label){
        File file = null;
        label.setText("Add image");
        return file;
    }

    private class SelectFirstImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            first = selectFile(label1);
        }
    }
    private class SelectSecondImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            second = selectFile(label2);
        }
    }

    private class ClearFirstListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            first = clearFile(label1);
        }
    }
    private class ClearSecondListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            second = clearFile(label2);
        }
    }

    private class CompareImagesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(first == null || second == null){
                Extra.showError("One or more files are not selected!", "Select file error");
            }else{
                BufferedImage firstInNewSize = imageNewSize(getBufferedImage(first), commonImageSizes(getBufferedImage(first), getBufferedImage(second)), "first", getFileExtension(first));
                BufferedImage secondInNewSize = imageNewSize(getBufferedImage(second), commonImageSizes(getBufferedImage(second), getBufferedImage(first)), "second", getFileExtension(first));

                BufferedImage filed = imageNewSize(getBufferedImage(second), commonImageSizes(getBufferedImage(second), getBufferedImage(first)), "CreateFiled", getFileExtension(first));
                filed = cleanFiled(filed);

                BufferedImage result = imageNewSize(getBufferedImage(second), commonImageSizes(getBufferedImage(second), getBufferedImage(first)), "result", getFileExtension(first));

                ForCirculating massPixels[][] = WorkerWithImages.compareImagesByPixels(firstInNewSize, secondInNewSize, filed);


                result = WorkerWithImages.drawLines(WorkerWithImages.bezel(WorkerWithImages.composePixels(massPixels)), result);

                try {
                    ImageIO.write(result, getFileExtension(first), new File("filed.png"));
                    ImageIO.write(result, getFileExtension(first), new File("rez.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                ImageFrame.expansion = getFileExtension(first);
                ImageFrame.addInJLable(new File("rez.png"));

                ImageFrame imageFrame = new ImageFrame();

            }
        }
    }
}

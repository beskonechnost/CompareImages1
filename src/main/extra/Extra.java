package main.extra;

import javax.swing.*;

/**
 * Created by Андрей on 14.09.2017.
 */
public class Extra {

    public static void showError(String text, String header){
        JDialog jd = new JDialog();
        JOptionPane.showMessageDialog(jd, text, header, JOptionPane.ERROR_MESSAGE);
    }

    public static int groupSize (int row, int col){
        int size = 0;
        long v = row*col;
        if(v<160000){
            size = 2;
        }else{
            if(v>1440000){
                size = 7;
            }else{
                size = 5;
            }
        }
        return size;
    }
}

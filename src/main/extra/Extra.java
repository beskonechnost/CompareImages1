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
}

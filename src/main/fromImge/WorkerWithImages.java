package main.fromImge;

import main.extra.Extra;
import main.extra.ForCirculating;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Андрей on 14.09.2017.
 */
public class WorkerWithImages {

    //get the file extension
    public static String getFileExtension(File file) {
        if(file==null) {
            return "";
        }else {
            String fileName = file.getName();
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            else return "";
        }
    }

    //check if the extension of this file is suitable for us
    public static boolean formatImagesTest(File file){
        boolean resultTest = false;

        String extensionFile = getFileExtension(file);

        String[] okFormat = ImageIO.getReaderFileSuffixes();
        for(String format : okFormat){
            if(extensionFile.equals(format)){
                resultTest = true;
                break;
            }
        }
        return resultTest;
    }

    public static BufferedImage getBufferedImage(File file){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            Extra.showError("File error/Select other file", "File Error");
        }
        return bufferedImage;
    }

    public static int[] commonImageSizes(BufferedImage bufferedImage1, BufferedImage bufferedImage2){
        int shorterWidth = 0;
        int shorterHeight = 0;

        if(bufferedImage1.getWidth()<bufferedImage2.getWidth()){
            shorterWidth = bufferedImage1.getWidth();
        }else{
            shorterWidth = bufferedImage2.getWidth();
        }

        if(bufferedImage1.getHeight()<bufferedImage2.getHeight()){
            shorterHeight = bufferedImage1.getHeight();
        }else{
            shorterHeight = bufferedImage2.getHeight();
        }

        int[] alignSize = new int[2];
        alignSize[0] = shorterWidth;
        alignSize[1] = shorterHeight;
        return alignSize;
    }

    public static BufferedImage imageNewSize(Image bufferedImage, int[] commonSize, String name, String extension){


        BufferedImage common = new BufferedImage(commonSize[0], commonSize[1], BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = common.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, commonSize[0], commonSize[1], null);
        graphics.dispose();

        BufferedImage bi = null;

        try {
            ImageIO.write(common, extension, new File(name + "." + extension));
            bi = ImageIO.read(new File(name + "." + extension));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bi;
    }
     ///////////////////////////

    public static BufferedImage cleanFiled(BufferedImage filed){
        WritableRaster raster = filed.getRaster();
        for(int i = 0; i<raster.getWidth(); i++){
            for(int j = 0; j<raster.getHeight(); j++){
                int[] pixel = raster.getPixel(i, j, new int[4]);
                    for(int f = 0; f<3; f++){
                        pixel[f]=255;
                    }
                raster.setPixel(i, j, pixel);
            }
        }
        filed.setData(raster);
        return filed;
    }

    public static ForCirculating[][] compareImagesByPixels(BufferedImage bufferedImage1, BufferedImage bufferedImage2, BufferedImage filed){
        WritableRaster rasterBI1 = bufferedImage1.getRaster();
        WritableRaster rasterBI2 = bufferedImage2.getRaster();
        WritableRaster rasterFiled = filed.getRaster();

        ForCirculating[][] massPixels = new ForCirculating[filed.getHeight()][filed.getWidth()];


        for(int i = 0; i<rasterBI1.getHeight(); i++){
            for(int j = 0; j<rasterBI1.getWidth(); j++){

                int[] rBI1 = rasterBI1.getPixel(j,i, new int[4]);
                int[] rBI2 = rasterBI2.getPixel(j,i, new int[4]);
                for(int f = 0; f<3; f++){
                    if(Math.abs(rBI1[f] - rBI2[f])>25){
                        rasterFiled.setPixel(j,i,rBI2);
                        massPixels[i][j] = new ForCirculating(true, false, 1);
                        break;
                    }else {
                        massPixels[i][j] = new ForCirculating(false, false, 0);
                    }
                }
            }
        }

        filed.setData(rasterFiled);
        return massPixels;
  }

    //////////////////////////

    public static ForCirculating[][] composePixels (ForCirculating[][] forCirculatings){
        int col=forCirculatings[0].length;
        int row=forCirculatings.length;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                int hig = i - 5;
                if (hig < 0) hig = 0;
                int bot = i + 5;
                if (bot > row - 1) bot = row - 1;
                int left = j - 5;
                if (left < 0) left = 0;
                int right = j + 5;
                if (right > col - 1) right = col - 1;

                if(forCirculatings[i][j].isPixelIsNotEmpty() && !forCirculatings[i][j].isPixelProcessed()){

                    forCirculatings[i][j].setPixelProcessed(true);
                    forCirculatings[i][j].setPixelEntersAnotherPath(1);
                    for (int ii = hig; ii <= bot; ii++) {
                        for (int jj = left; jj <= right; jj++) {
                            if(forCirculatings[ii][jj].getPixelEntersAnotherPath()==0){
                                forCirculatings[ii][jj].setPixelEntersAnotherPath(1);
                                forCirculatings[ii][jj].setPixelProcessed(true);
                            }

                            if(forCirculatings[ii][jj].getPixelEntersAnotherPath()==1){
                                forCirculatings[ii][jj].setPixelEntersAnotherPath(1);
                            }
                        }
                    }


                }
            }
        }
        //Selecting rows without distinction
        for(int i = 0; i<row; i++){
            boolean flag = true;
            for (int j = 0; j < col; j++) {
                if(forCirculatings[i][j].getPixelEntersAnotherPath()==1){
                    flag = false;
                    break;
                }
            }
            if (flag){
                for (int j = 0; j < col; j++) {
                    forCirculatings[i][j].setPixelEntersAnotherPath(2);
                    forCirculatings[i][j].setPixelTrue(true);
                }
            }

        }
        //Selecting column without distinction
        for(int j = 0; j<col; j++){
            boolean flag = true;
            for(int i = 0; i<row; i++){
                if(forCirculatings[i][j].getPixelEntersAnotherPath()==1){
                    flag = false;
                    break;
                }
            }
            if (flag){
                for(int i = 0; i<row; i++){
                    if(forCirculatings[i][j].getPixelEntersAnotherPath()==2) {
                        forCirculatings[i][j].setPixelEntersAnotherPath(4);
                        forCirculatings[i][j].setPixelTrue(true);
                    }
                    if(forCirculatings[i][j].getPixelEntersAnotherPath()==0) {
                        forCirculatings[i][j].setPixelEntersAnotherPath(3);
                        forCirculatings[i][j].setPixelTrue(true);
                    }
                }
            }
        }

        for(int i = 0; i<row; i++){
            if(forCirculatings[i][0].getPixelEntersAnotherPath()==2 || forCirculatings[i][0].getPixelEntersAnotherPath()==4){
                continue;
            }else{
                for(int j = 0; j < col; j++) {
                    if(forCirculatings[i][j].getPixelEntersAnotherPath() == 3) {
                        continue;
                    }
                    if(!forCirculatings[i][j].isPixelTrue()){
                        int stopCol = col - 1;
                        for (int startCol = j; startCol < col; startCol++) {
                            if (forCirculatings[i][startCol].getPixelEntersAnotherPath() == 3) {
                                stopCol = startCol - 1;
                                break;
                            }
                        }
                        boolean distinction = false;
                        for (int startCol = j; startCol <= stopCol; startCol++) {
                            if (forCirculatings[i][startCol].getPixelEntersAnotherPath() == 1) {
                                distinction = true;
                                break;
                            }
                        }

                        int stopRow = row - 1;
                        for (int startRow = i; startRow < row; startRow++) {
                            if (forCirculatings[startRow][j].getPixelEntersAnotherPath() == 2) {
                                stopRow = startRow - 1;
                                break;
                            }
                        }

                        if (!distinction) {
                            for (int startRow = i; startRow <= stopRow; startRow++) {
                                for (int startCol = j; startCol <= stopCol; startCol++) {
                                    forCirculatings[startRow][startCol].setPixelEntersAnotherPath(3);
                                    forCirculatings[startRow][startCol].setPixelTrue(true);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if(!forCirculatings[i][j].isPixelTrue()) {
                        int stopCol = col - 1;
                        for (int startCol = j; startCol < col; startCol++) {
                            if (forCirculatings[i][startCol].getPixelEntersAnotherPath() == 3) {
                                stopCol = startCol - 1;
                                break;
                            }
                        }
                        int stopRow = row - 1;
                        for (int startRow = i; startRow < row; startRow++) {
                            if (forCirculatings[startRow][j].getPixelEntersAnotherPath() == 2) {
                                stopRow = startRow - 1;
                                break;
                            }
                        }

                        for (int startRow = i; startRow <= stopRow; startRow++) {
                            forCirculatings[startRow][j].setPixelEntersAnotherPath(9);
                            forCirculatings[startRow][j].setPixelTrue(true);
                            forCirculatings[startRow][stopCol].setPixelEntersAnotherPath(9);
                            forCirculatings[startRow][stopCol].setPixelTrue(true);

                        }

                        for (int startCol = j; startCol <= stopCol; startCol++) {
                            forCirculatings[i][startCol].setPixelEntersAnotherPath(9);
                            forCirculatings[i][startCol].setPixelTrue(true);
                            forCirculatings[stopRow][startCol].setPixelEntersAnotherPath(9);
                            forCirculatings[stopRow][startCol].setPixelTrue(true);
                        }

                        for(int startRow = i; startRow <= stopRow; startRow++){
                            for (int startCol = j; startCol <= stopCol; startCol++) {
                                forCirculatings[startRow][startCol].setPixelTrue(true);
                            }
                        }
                }
            }
        }



        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(forCirculatings[i][j].getPixelEntersAnotherPath());
            }
            System.out.println("#");
        }

        return forCirculatings;
    }
    ///////////////////
    /*
    public static ForCirculating[][] bezel (ForCirculating[][] forCirculatings){
        int wwi = ForCirculating.count;

        int col = forCirculatings[0].length;
        int row = forCirculatings.length;

        for(int k=1; k<wwi; k++) {

            int hig = row;
            int bot = 0;
            int left = col;
            int right = 0;

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if(forCirculatings[i][j].getPixelEntersAnotherPath()==k){
                        if(i<hig) hig = i;
                        if(i>bot) bot = i;
                        if(j<left) left = j;
                        if(j>right) right = j;
                    }
                }
            }

            for(int i = hig; i<=bot; i++){
                forCirculatings[i][left].setPixelEntersAnotherPath(-1);
                forCirculatings[i][right].setPixelEntersAnotherPath(-1);
            }

            for(int j = left; j<=right; j++){
                forCirculatings[hig][j].setPixelEntersAnotherPath(-1);
                forCirculatings[bot][j].setPixelEntersAnotherPath(-1);
            }
        }

        return forCirculatings;
    }
    */
    public static BufferedImage drawLines (ForCirculating[][] forCirculatings, BufferedImage bf){
        WritableRaster raster = bf.getRaster();
        for(int i = 0; i<raster.getWidth(); i++){
            for(int j = 0; j<raster.getHeight(); j++){
                int[] pixel = raster.getPixel(i, j, new int[4]);
                if(forCirculatings[j][i].getPixelEntersAnotherPath()==9) {
                    pixel[0] = 255;
                    pixel[1] = 0;
                    pixel[2] = 0;
                }
                raster.setPixel(i, j, pixel);
            }
        }
        bf.setData(raster);
        return bf;
    }

}

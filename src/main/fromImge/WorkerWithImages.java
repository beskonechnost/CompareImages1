package main.fromImge;

import main.extra.Extra;
import main.extra.ForCirculating;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

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
                    int count = ForCirculating.count++;
                    int count1 = -1;
                    forCirculatings[i][j].setPixelProcessed(true);
                    forCirculatings[i][j].setPixelEntersAnotherPath(count);
                    for (int ii = hig; ii <= bot; ii++) {
                        for (int jj = left; jj <= right; jj++) {
                            if(forCirculatings[ii][jj].getPixelEntersAnotherPath()==0){
                                forCirculatings[ii][jj].setPixelEntersAnotherPath(count);
                                forCirculatings[ii][jj].setPixelProcessed(true);
                            }
                            if(forCirculatings[ii][jj].getPixelEntersAnotherPath()==1){
                                forCirculatings[ii][jj].setPixelEntersAnotherPath(count);
                            }
                            if(forCirculatings[ii][jj].getPixelEntersAnotherPath()>1){
                                count1 = forCirculatings[ii][jj].getPixelEntersAnotherPath();
                                for (int iii = 0; iii < row; iii++) {
                                    for (int jjj = 0; jjj < col; jjj++) {
                                        if(forCirculatings[iii][jjj].getPixelEntersAnotherPath()==count1){
                                            forCirculatings[iii][jjj].setPixelEntersAnotherPath(count);
                                        }
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }
        return forCirculatings;
    }

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

    public static BufferedImage drawLines (ForCirculating[][] forCirculatings, BufferedImage bf){
        WritableRaster raster = bf.getRaster();
        for(int i = 0; i<raster.getWidth(); i++){
            for(int j = 0; j<raster.getHeight(); j++){
                int[] pixel = raster.getPixel(i, j, new int[4]);
                if(forCirculatings[j][i].getPixelEntersAnotherPath()==-1) {
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

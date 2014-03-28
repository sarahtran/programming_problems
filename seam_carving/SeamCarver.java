/*************************************************************************
 *  Compilation:  javac SeamCarver.java
 *  Execution:    java SeamCarver input.png columnsToRemove rowsToRemove
 *  Dependencies: MinimumSeam.java TopologicalSort.java Picture.java
 *                Stopwatch.java StdDraw.java
 *
 *  Read image from file specified as command line argument. Use SeamCarver
 *  to remove number of rows and columns specified as command line arguments.
 *  Show the images in StdDraw and print time elapsed to screen.
 * 
 *************************************************************************/

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
  private int width;
  private int height;
  private int[][] colorMatrix;

  public SeamCarver(Picture picture) {
    width = picture.width();
    height = picture.height();

    colorMatrix = new int[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Color color = picture.get(i, j);
        colorMatrix[i][j] = intRGB(color.getRed(), color.getGreen(), color.getBlue());
      }
    }
  }

  // integer representation of RGB
  private int intRGB(int red, int green, int blue) {
    int rgb = red;
    rgb = rgb << 8;
    rgb = rgb | green;
    rgb = rgb << 8;
    rgb = rgb | blue;
    return rgb;
  }

  // extracts the integer red value from the integer RGB value
  private int red(int rgb) {
    return rgb >> 16;
  }
 
  // extracts the integer green value from the integer RGB value
  private int green(int rgb) {
    return (rgb >> 8) & 0x00FF;
  }
 
  // extracts the integer blue value from the integer RGB value
  private int blue(int rgb) {
    return rgb & 0x00FF;
  }

  // current picture
  public Picture picture() {
    Picture picture = new Picture(width, height);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int rgb = colorMatrix[i][j];
        Color color = new Color(red(rgb), green(rgb), blue(rgb));
        picture.set(i, j, color);
      }
    }
    return picture;
  }

  // width  of current picture
  public int width() {
    return width;
  }

  // height of current picture
  public int height() {
    return height;
  }

  // energy of pixel at column x and row y in current picture
  public double energy(int x, int y) {
    if (x < 0 || (x >= width) || y < 0 || (y >= height)) {
      throw new java.lang.IndexOutOfBoundsException();
    }

    if (x == 0 || (x == width - 1) || y == 0 || (y == height - 1)) {
      return 195075.0;
    }

    int xMinus1 = colorMatrix[x - 1][y];
    int xPlus1 = colorMatrix[x + 1][y];
    int yMinus1 = colorMatrix[x][y - 1];
    int yPlus1 = colorMatrix[x][y + 1];
    
    return squaredGradient(xMinus1, xPlus1) + squaredGradient(yMinus1, yPlus1);
  }

  // squared gradient between two pixels
  private double squaredGradient(int minusPixel, int plusPixel) {
    double redSquared = Math.pow(red(minusPixel) - red(plusPixel), 2);
    double greenSquared = Math.pow(green(minusPixel) - green(plusPixel), 2);
    double blueSquared = Math.pow(blue(minusPixel) - blue(plusPixel), 2);

    return redSquared + greenSquared + blueSquared;
  }

  // energy matrix
  private double[][] energyMatrix() {
    double[][] energyMatrix = new double[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        energyMatrix[i][j] = energy(i, j);
      }
    }
    return energyMatrix;
  }

  // transposed energy matrix
  private double[][] transposedEnergyMatrix() {
    double[][] transposedEnergyMatrix = new double[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        transposedEnergyMatrix[i][j] = energy(j, i);
      }
    }
    return transposedEnergyMatrix;
  }

  // sequence of indices for horizontal seam in current picture
  public int[] findHorizontalSeam() {
    MinimumSeam ms = new MinimumSeam(transposedEnergyMatrix(), height, width);
    return ms.minimumSeam();
  }

  // sequence of indices for vertical seam in current picture
  public int[] findVerticalSeam() {
    MinimumSeam ms = new MinimumSeam(energyMatrix(), width, height);
    return ms.minimumSeam();
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] a) {
    if (width <= 1 || height <= 1 || a.length != width) {
      throw new java.lang.IllegalArgumentException();
    }

    int[][] newColorMatrix = new int[width][height - 1];
    int i = 0;
    for (int remove : a) {
      if (remove < 0 || remove >= height) {
        throw new java.lang.IllegalArgumentException();
      }

      for (int j = 0; j < remove; j++) {
        newColorMatrix[i][j] = colorMatrix[i][j];
      }
      for (int j = remove; j < height - 1; j++) {
        newColorMatrix[i][j] = colorMatrix[i][j + 1];
      }
      i++;
    }
    colorMatrix = newColorMatrix;
    height--;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] a) {
    if (width <= 1 || height <= 1 || a.length != height) {
      throw new java.lang.IllegalArgumentException();
    }

    int[][] newColorMatrix = new int[width - 1][height];
    int j = 0;
    for (int remove : a) {
      if (remove < 0 || remove >= width) {
        throw new java.lang.IllegalArgumentException();
      }

      for (int i = 0; i < remove; i++) {
        newColorMatrix[i][j] = colorMatrix[i][j];
      }
      for (int i = remove; i < width - 1; i++) {
        newColorMatrix[i][j] = colorMatrix[i + 1][j];
      }
      j++;
    }
    colorMatrix = newColorMatrix;
    width--;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Usage:\njava SeamCarver [image filename] [num cols to remove] [num rows to remove]");
      return;
    }

    Picture inputImg = new Picture(args[0]);
    int removeColumns = Integer.parseInt(args[1]);
    int removeRows = Integer.parseInt(args[2]); 

    System.out.printf("Image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
    SeamCarver sc = new SeamCarver(inputImg);

    Stopwatch sw = new Stopwatch();

    for (int i = 0; i < removeRows; i++) {
      int[] horizontalSeam = sc.findHorizontalSeam();
      sc.removeHorizontalSeam(horizontalSeam);
    }

    for (int i = 0; i < removeColumns; i++) {
      int[] verticalSeam = sc.findVerticalSeam();
      sc.removeVerticalSeam(verticalSeam);
    }
    Picture outputImg = sc.picture();

    System.out.printf("New image size is %d columns by %d rows\n", sc.width(), sc.height());

    System.out.println("Resizing time: " + sw.elapsedTime() + " seconds.");
    inputImg.show();
    outputImg.show();
  }
}

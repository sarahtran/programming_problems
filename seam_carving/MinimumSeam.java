public class MinimumSeam {
  private double[][] energy;
  private int width;
  private int height;
  private int[][] edgeTo;
  private double[][] distTo;
  private int[] minSeam;

  public MinimumSeam(double[][] energy, int width, int height) {
    this.energy = energy;
    this.width = width;
    this.height = height;

    edgeTo = new int[width][height];

    distTo = new double[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (j == 0) {
          distTo[i][j] = 195075.0;
        }
        else {
          distTo[i][j] = Double.POSITIVE_INFINITY;
        }
      }
    }

    TopologicalSort ts = new TopologicalSort(width, height);
    for (int[] pixel : ts.order()) {
      int i = pixel[0];
      int j = pixel[1];

      if (j + 1 >= height) { continue; }

      if (i - 1 >= 0) { relax(i - 1, j + 1, i); }
      relax(i, j + 1, i);
      if (i + 1 < width) { relax(i + 1, j + 1, i); }
    }

    int indexOfMin = 0;
    double min = distTo[0][height - 1];
    for (int i = 1; i < width; i++) {
      if (distTo[i][height - 1] < min) {
        min = distTo[i][height - 1];
        indexOfMin = i;
      }
    }
    minSeam = new int[height];
    minSeam[height - 1] = indexOfMin;
    for (int j = height - 2; j >= 0; j--) {
      minSeam[j] = edgeTo[minSeam[j + 1]][j + 1];
    }
  }

  public int[] minimumSeam() {
    return minSeam;
  }

  private void relax(int i, int j, int prev_i) {
    if (distTo[i][j] > distTo[prev_i][j - 1] + energy[i][j]) {
      distTo[i][j] = distTo[prev_i][j - 1] + energy[i][j];
      edgeTo[i][j] = prev_i;
    }
  }
}

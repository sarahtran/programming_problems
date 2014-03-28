public class TopologicalSort {
  private Stack<int[]> reversePost;
  private boolean[][] marked;
  private int width;
  private int height;

  public TopologicalSort(int width, int height) {
    this.width = width;
    this.height = height;

    reversePost = new Stack<int[]>();
    marked = new boolean[width][height];

    for (int i = width - 1; i >= 0; i--) {
      for (int j = height - 1; j >= 0; j--) {
        if (!marked[i][j]) {
          dfs(i, j);
        }
      }
    }
  }

  public Iterable<int[]> order() {
    return reversePost;
  }

  private void dfs(int i, int j) {
    marked[i][j] = true;

    if ((i - 1) >= 0 && (j + 1) < height && !marked[i - 1][j + 1]) {
      dfs(i - 1, j + 1);
    }
    if ((j + 1) < height && !marked[i][j + 1]) {
      dfs(i, j + 1);
    }
    if ((i + 1) < width && (j + 1) < height && !marked[i + 1][j + 1]) {
      dfs(i + 1, j + 1);
    }

    int[] pixel = {i, j};
    reversePost.push(pixel);
  }
}

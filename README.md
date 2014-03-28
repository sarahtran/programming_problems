Programming Problems
====================

<h2>Seam Carving</h2>

<p>
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. Unlike standard content-agnostic resizing techniques such as cropping or scaling, the most interesting features like aspect ratio, set of objects present, etc. of the image are preserved.
</p>

<p>
A seam in an image is a path of pixels vertically connected (from the top to the bottom with one pixel in each row) or horizontally connected (from the left to the right with one pixel in each column).
</p>

<h4>Method and Algorithms</h4>
The picture is converted into an energy matrix using the dual gradient energy function. The minimum seam is found by treating the energy matrix as a directed acyclic graph and finding the shortest path from one edge to the other edge, which can then be removed.

<h4>Complilation and Execution</h4>
<p>Compilation: javac SeamCarver.java</p>
<p>Execution: java SeamCarver input.png columnsToRemove rowsToRemove</p>



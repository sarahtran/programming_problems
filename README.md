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

<h2>Keyword Context</h2>

<p>
Keyword context search provides the context around all the designated keywords in a large text corpus. This method is similar to the find methods implemented in word processor programs.
</p>

<h4>Method and Algorithms</h4>
The text corpus is first sanitized and then constructed into a sorted suffix array. A keyword occurrence is then found using binary search and scanned backwards and forwards for all occurrences of the keyword. An optimization made was to limit the suffix length since the assumption is that keywords would not exceed a certain length.

<h4>Complilation and Execution</h4>
<p>Compilation: javac KeywordContext.java</p>
<p>Execution: java KeywordContext corpus.txt keyword1 keyword2 ...</p>

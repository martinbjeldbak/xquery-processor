# Milestone 2 
Due Thursday, June 11, 2015 (during finals week).

## Efficient Evaluation
 Implement a join operator as defined in Section 7 of [this](http://db.ucsd.edu/cse232b/project-notes.pdf) note. Implement an algorithm which detects the fact that the FOR and WHERE clause computation can be implemented using the join operator. You may assume that the input XQueries to be optimized are in the simplified "Core" syntax given in the note. No need to first normalize your queries to this form.

## Input/otput
Our application should be an executable jar, that:

  1. Should take a file name as input
  2. Write the result of xquery to a file 'output.txt'

The file name as input to your jar which has xquery in it. Your code should read the file, execute the xquery and print the results to 'output.txt'.

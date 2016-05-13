# CSC-365-A3

This is an assignment from [Doug Lea's](http://gee.cs.oswego.edu/) CSC 365, Data Structures and File Processing, Course at SUNY Oswego.

"Write a program that collects at least 500 Wikipedia pages and links from these pages to other Wikipedia pages.
Collect word frequencies as in Assignments 1 and 2 (but feel free to limit to a fixed number of most frequent words). 

As a connectivity check, report the number of spanning trees (for any arbitrary node as initial starting point). 
Store persistently (possibly just in a Serialized file). 

Write a program (either GUI or web-based) that reads the graph from step 1, allows a user to select any two pages (by title) and displays graphically the shortest (weighted by any similarity metric) path between them, if one exists."
 
I haven't had a chance to clean up this directory from previous assignments, so setting up and running may require some careful navigating.

#Algorithms
The program makes use of two important external algorithms:
##[Prim's Algorithm](https://www.google.com/search?q=Prim%27s+algorithm&rlz=1C1CHFX_enUS651US651&oq=Prim%27s+algorithm&aqs=chrome..69i57.2479j0j7&sourceid=chrome&ie=UTF-8) for a Minimum Spanning Tree

##[Dijkstra's Algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) For Finding Shortest Paths

Research these algorithms and see how you can make their usage better in the program: `/src/Algorithms`

# Running the Program
##Run `src/gui/A3GUI5`.
This will launch src/frames/A3Frame5 of which you can change the size to your liking.
##Loading Edges
Click "Load" to load your edges from your websites.
###In `src/frames/A3Frame5`
The program is currently set up to load edges from `/websitesA3.txt`.
The program will load the websites from the sites your specify in websitesA3.txt. I currently only have one site in there from which thousands more are parsed from. 
The program will save your edges to the `/edgesA3D.txt` file.

The methods in this class have quite descriptive names, so they document themselves. A year ago, I failed to provide more documentation on the methods with lesser importance, but you should be fine navigating `src/frames/A3Frame5`.

###Most Important Method for Similarity Metric
`compareBaseSiteToItsExternalSites()` in `src/frames/A3Frame5`. This is where the similarity metric begins.
You should not manipulate this method much if at all. The method you may be concerned with for changing your similarity metric is `LHashTable.compare()` with is called at the end of `compareBaseSiteToItsExternalSites()` like so:

```Java
 int weight = (int) LHashTable.compare(baseSiteHashTable, comparisonWebsite).getSimilarityPercentage();
 addEdge(urlStringA, urlStringB, weight);
```

That compares a given LWebsite's data to a another website's hashtable data with `LHashTable.compare()` and calls `LWebsite.getSimilarityPercentage()` on the resultant comapred LWebsite to get its similarity to a base site from which an edge will be created for the graph.

###Important Suggestion for Similarity Metric
I highly suggest you manipulate and adapt `LHashTable.compare()` to implement your own similarity metric.

###IMPORTANT NOTE
I already have the edges loaded. If you want to load your own, I believ you should delete /edgesA3D.txt or change the name of the file to load the edges to. I actually don't suggest you delete `/edgesA3D.txt` because the edges take quite some time to load and you may need `/edgesA3D.txt` for testing in case anything goes wrong when you make changes.

Also, please do put a different starting website in `/websitesA3.txt` to load your egdes from.
I suggest a large wikipedia page because they have the most external links and have an abundance of text.
##After Edges Loaded
Once the loading is complete, you can click "Show Path". Nothing will happen just yet.
On the right side of the gui, there will be a list of "Source Sites" and "Destination Sites" numbered vertically along the left side of that pane.

You need to enter a number representing a source site and a number representing a destination site deliminated by a comma with no spaces.

###Showing Shortest Paths Between Sites
After you enter a pair, e.g. "0,41", click "Show Path" now and the program will display the shortest path from the source site to the destination site in the left pane of the gui. 

#Known Errors
###Inconsitent numbering of source and destination sites
For some reason, the numbers of the source and destination sites don't line up after awhile. Try running the program to see which numbers actually match up before showing the program off.

###Graph Fully Connected
The program is supposed to return the number of spanning trees for any arbitrary node as a starting point, but because I currently only have a single site in my base sites `/websitesA3.txt`, the graph is fully connected and, intuitively, there are no spanning trees reported by Prim's algorithm.



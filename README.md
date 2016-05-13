# CSC-365-A3

This is an assignment from [Doug Lea's](http://gee.cs.oswego.edu/) CSC 365, Data Structures and File Processing, Course at SUNY Oswego.

"Write a program that collects at least 500 Wikipedia pages and links from these pages to other Wikipedia pages. Collect word frequencies as in Assignments 1 and 2 (but feel free to limit to a fixed number of most frequent words). As a connectivity check, report the number of spanning trees (for any arbitrary node as initial starting point). Store persistently (possibly just in a Serialized file). Write a program (either GUI or web-based) that reads the graph from step 1, allows a user to select any two pages (by title) and displays graphically the shortest (weighted by any similarity metric) path between them, if one exists."
 
I haven't had a chance to clean up this directory from previous assignments, so setting up and running may require some careful navigating.

# Running to Program
##Run src/gui/A3GUI5.
This will launch src/frames/A3Frame5 of which you can change the size to your liking.
##Loading Edges
Click "Load" to load your edges from your websites.
###In src/frames/A3Frame5
The program is currently set up to load edges from /websitesA3.txt.
The program will load the websites from the sites your specify in websitesA3.txt. I currently only have one site in there from which thousands more are parsed from. 
The program will save your edges to the /edgesA3D.txt file.

The methods in this class have quite descriptive names, so they document themselves. A year ago, I failed to provide more documentation on the methods with lesser importance, but you should be fine navigating src/frames/A3Frame5.

Note that I already have the edges loaded. If you want to load your own, I believ you should delete /edgesA3D.txt or change the name of the file to load the edges to. I actually don't suggest you delete /edgesA3D.txt because the edges take quite some time to load and you may need /edgesA3D.txt for testing in case anything goes wrong when you make changes.

##After Edges Loaded
Once the loading is complete, you can click "Show Path". Nothing will happen just yet.
On the right side of the gui, there will be a list of "Source Sites" and "Destination Sites" numbered vertically along the left side of that pane.

You need to enter a number representing a source site and a number representing a destination site deliminated by a comma with no spaces.

###Showing Shortest Paths Between Sites
After you enter a pair, e.g. "0,41", click "Show Path" now and the program will display the shortest path from the source site to the destination site in the left pane of the gui. 

#Known Errors
For some reason, the numbers of the source and destination sites don't line up after awhile. Try running the program to see which numbers actually match up before showing the program off.

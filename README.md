# AssortedAndroid
This app contains three main activities, as well as a landing page to choose between them.

The landing page, menuActivity, features three labeled buttons, as well as a color selector.
Each button, when pressed, directs the user to the named activity. The new activity will have its background
color set by the color selection radio buttons present in menuActivity. menuActivity is primarily a demonstration of 
how to move between activities, and how to send data (in this case color) to the new activity.

opt1Activity launches when the 'Linked List Line Drawer' button is pressed.
opt1Activity features the concepts of linked and unlinked lists, as well as an implementation of basic gesture detection.
The number in the center of the screen represents the division between nodes that will be drawn in the canvas below.
The number can be incremented or decremented by swiping up or down on the number. 
The 'Make List' button will draw each node and step through a randomly generated list of nodes, drawing a connection
between each node and another random node. The button text will then update to reflect how many nodes were drawn.
The 'Populate Linked List' button will sort the nodes by position, top down and right to left. Then, the nodes will be linked
to one another in order, creating a doubly linked list which is then traversed to draw the now orderly connections
between the nodes.

opt2Activity launches when the 'Shape Organizing' button is pressed.
opt2Activity features the generation of random sets of blocks, and the different ways to sort them.
'New Load' will generate a set number of block of varying sizes, and visually sort using a method specified
in the 'Sort Type' menu. By default, the blocks are sorted by height.
'Load Differently' will preserve the current set of blocks, but rerun the sorting algorithm using the specified sorting 
method. This allows the user to view the same set of blocks sorted in each different way
'Sort Type' allows the user to specify which of four methods they want to sort the current load with.
Sorting by height,width and area are self explanatory.
Roadie style sorting will take the current set of blocks and place each one randomly in the canvas
wherever there is unoccupied space.
This loading method is applied to each block in the set, and attempted on each block a set number of times
before the algorithm moves on to a new block, whether or not the block was eventually placed. This method
serves to illustrate that less efficient loading algorithms often cannot load objects as efficiently as other more
rule based algorithms, like the sort by height.
After each completed sorting of the current load, a toast is displayed indicating the number of blocks successfully placed.

opt3Activity launches when the 'Pressure Based Touch Detection' button is pressed.
opt3Activity features the usage of Android's onTouchEvent, specifically how pressure can be read. Additionally,
basic animations are implemented as onTouch indicators. This activity is intended to be run on a physical device, as 
emulators cannot convey a pressure associated with mouse clicks.

All three activities make use of the CustomDrawableView class, defined in opt2Activity. This self designed class is 
used as the primary canvas in each activity, and includes several methods for drawing basic shapes and lines.


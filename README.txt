	This application is a distibuted message exchange simulation that is designed to run on various nodes, it uses vector clock algorithm
	to maintain local vector clock. Below are the details and notes on how to run the program.

1) This project is developed in "Eclipse Kepler Service Release 1" on OSX 10.9.1
2) Compile the FidgeMattern.java on any cs*.utdallas.edu machines.
3) Make sure Config.txt file resides in the same directory as FidgeMattern.java
4) Open Config.txt and identify the remote machines in it.
5) Run the program from individual shells for each remote machine using the following command as an example.
	ssh <userid>@<remotemachine_hostname> "cd <project_path>; java FidgeMattern <id>"
	
6) Repeat the procedure from seperate shells for seperate remote machines
7) Program prints clock values and message interactions on the screen at every stage.
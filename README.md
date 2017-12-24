# SimpleDownloadManager
A simple download manager based on Java  --  optimized for Raspberry pi

built up as server and client
* server is running on a raspberry pi for example
* client is running on your pc

## how to start the server?

* java -jar server.jar

## how to start client?

java -jar client --parameter

	--help (showing the help dialog)
	--start (start downloading)
	--stop (stop downloading)
	--status (show the status of the current download)
	--statusloop (same as before but in a infinite loop)
	--deletelinks (deletes all links from list)
	--add 'example.com/video.mp4' (add a link to the list
	--stopserver (Stops the whole server!)
	--delete <destination> (deletes the given position)
	--movlink <destination> <origin> (Moves a link from the origin to the destination)
	--listlinks (returns the list of stored links)
	--movlink <destination> <source>(move link from source to destination)
	--pausedownload (pauses the current downloading)
	--resumedownload (resumes the current downloading)
	--limitbandwidth <number in kbit/s> ()limits the download bandwidth

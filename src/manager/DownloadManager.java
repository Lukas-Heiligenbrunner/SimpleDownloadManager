package manager;

import java.net.Socket;

public class DownloadManager {
    
    Download downloadclass;
    
    public float completefilesize;
    public float loadedSize;
    
    public void startDownloading(mydata[] daten, Socket clientSocket)
    {
	downloadclass = new Download();
	
	completefilesize = 0;
	loadedSize = 0;
	
	for (int i = 0; !daten[i].link.equals(""); i++)
	{
	    if (!daten[i].downloadstate)
	    {
		System.out.println("[Server] Starting download...");
		downloadclass = new Download();
		downloadclass.toDownload(daten[i]);
		System.out.println("[Server] Download started...");
		System.out.println("Filename:" + downloadclass.getFileName());
		System.out.println("Type:" + downloadclass.getType());
		System.out.println("Size:" + downloadclass.getTotalSize() / (1024 * 1024) + "MB");
		
		daten[i].filename = downloadclass.getFileName();
		daten[i].filesize = downloadclass.getTotalSize();
		daten[i].downloadstate = false;
		
		while (!downloadclass.isDownloadcomplete() && !downloadclass.isInterrupted)
		{
			if (downloadclass.getFileName().length() >= 22)
			{
			    
			    System.out.println(String.format("%.2f", downloadclass.getSpeed()) + "kbit/s\t"
				    + String.format("%.2f", (downloadclass.getLoadedSize() / (1024 * 1024))) + " / "
				    + String.valueOf(downloadclass.getTotalSize() / (1024 * 1024)) + "MB\t"
				    + String.format("%.2f", downloadclass.getPercentDownloaded()) + "%\t"
				    + String.format("%.1f", ((loadedSize + downloadclass.getLoadedSize()) / (1024 * 1024)))
				    + " / " + String.format("%.1f", (completefilesize / (1024 * 1024))) + "MB\t"
				    + downloadclass.getFileName().substring(0, 22));
			} else
			{
			    System.out.println(String.format("%.2f", downloadclass.getSpeed()) + "kbit/s\t"
				    + String.format("%.2f", (downloadclass.getLoadedSize() / (1024 * 1024))) + " / "
				    + String.valueOf(downloadclass.getTotalSize() / (1024 * 1024)) + "MB\t"
				    + String.format("%.2f", downloadclass.getPercentDownloaded()) + "%\t"
				    + String.format("%.1f", ((loadedSize + downloadclass.getLoadedSize()) / (1024 * 1024)))
				    + " / " + String.format("%.1f", (completefilesize / (1024 * 1024))) + "MB\t"
				    + downloadclass.getFileName());
			}
		    try
		    {
			Thread.sleep(2000);
		    } catch (InterruptedException e)
		    {
			e.printStackTrace();
		    }
		}
		
		loadedSize += downloadclass.getLoadedSize();
		
		if (downloadclass.isInterrupted())
		{
		    return;
		}
		
		daten[i].downloadstate = downloadclass.isDownloadcomplete();
		
		System.out.println("[Server] Download Complete!");
		
	    }
	    else
	    {
		System.out.println("[Server] "+daten[i].filename +"already downloaded!");
	    }
	}
    }
    
}

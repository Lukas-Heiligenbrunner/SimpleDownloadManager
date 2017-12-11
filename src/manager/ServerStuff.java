package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

@SuppressWarnings("serial")
class mydata implements Serializable {
    String filename = "";
    String link = "";
    long filesize = 0;
    boolean downloadstate = false;
    long downloaded = 0;
    String contenttype = "";
}

public class ServerStuff extends DownloadManager {
    
    int storedvalue;
    
    Socket clientSocket;
    
    Thread TCPServer;
    Thread downloadThread;
    
    boolean stayalive;
    
    static final int maxdownloads = 20;
    
    mydata[] data;
    
    public static final int port = 3000;
    
    public ServerStuff()
    {
	readProperties();
	storedvalue = 0;
	
    }
    
    public void startTCPServer()
    {
	TCPServer = new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		stayalive = true;
		
		while (stayalive)
		{
		    try
		    {
			ServerSocket socket = new ServerSocket(port);
			clientSocket = socket.accept();
			
			Scanner s = new Scanner(
				new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
			
			if (s.hasNextLine())
			{
			    switchcommands(s.nextLine());
			    
			}
			s.close();
			clientSocket.close();
			socket.close();
		    } catch (IOException e)
		    {
			e.printStackTrace();
		    }
		}
		
	    }
	});
	TCPServer.start();
	
	System.out.println("[Server] TCP Server Started!");
	
    }
    
    private void switchcommands(String command)
    {
	if (command.equals("startdownloading"))
	{
	    downloadThread = new Thread(new Runnable()
	    {
		
		@Override
		public void run()
		{
		    System.out.println("[Client] starting the download");
		    startDownloading(data, clientSocket);
		    
		}
	    });
	    downloadThread.start();
	    
	} else if (command.equals("getstatus"))
	{
	    try
	    {
		PrintWriter prwr = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		
		if (downloadclass.getFileName().length() >= 22)
		{
		    
		    prwr.println(String.format("%.2f", downloadclass.getSpeed()) + "kbit/s\t"
			    + String.format("%.2f", (downloadclass.getLoadedSize() / (1024 * 1024))) + " / "
			    + String.valueOf(downloadclass.getTotalSize() / (1024 * 1024)) + "MB\t"
			    + String.format("%.2f", downloadclass.getPercentDownloaded()) + "%\t"
			    + String.format("%.1f", ((loadedSize + downloadclass.getLoadedSize()) / (1024 * 1024)))
			    + " / " + String.format("%.1f", (completefilesize / (1024 * 1024))) + "MB\t"
			    + downloadclass.getFileName().substring(0, 22));
		} else
		{
		    prwr.println(String.format("%.2f", downloadclass.getSpeed()) + "kbit/s\t"
			    + String.format("%.2f", (downloadclass.getLoadedSize() / (1024 * 1024))) + " / "
			    + String.valueOf(downloadclass.getTotalSize() / (1024 * 1024)) + "MB\t"
			    + String.format("%.2f", downloadclass.getPercentDownloaded()) + "%\t"
			    + String.format("%.1f", ((loadedSize + downloadclass.getLoadedSize()) / (1024 * 1024)))
			    + " / " + String.format("%.1f", (completefilesize / (1024 * 1024))) + "MB\t"
			    + downloadclass.getFileName());
		}
		prwr.flush();
		prwr.close();
	    } catch (IOException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (NullPointerException e)
	    {
		System.err.println(
			"[Server] [Warning] couldn´t send download information to client (maybe no download started yet?)");
	    }
	} else if (command.equals("stop"))
	{
	    downloadclass.interruptDownload();
	    
	} else if (command.equals("deletelinks"))
	{
	    data = new mydata[maxdownloads];
	    for (int i = 0; i <= maxdownloads - 1; i++)
	    {
		data[i] = new mydata();
	    }
	    
	    safeProperties(data);
	    storedvalue = 0;
	    System.out.println("[Server] Linklist cleared!");
	} else if (command.equals("listlinks"))
	{
	    try
	    {
		PrintWriter prwr = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		
		for (int i = 0; !data[i].link.equals(""); i++)
		{
		    prwr.println(data[i].filename + " -- " + data[i].link + " -- " + data[i].filesize+" -- "+data[i].downloadstate);
		}
		
		prwr.flush();
		prwr.close();
	    } catch (IOException e)
	    {
		e.printStackTrace();
	    }
	} else if (command.contains("delete"))
	{
	    
	    removeOnArrayPosition(data, Integer.valueOf(command.substring(command.indexOf(" ") + 1)));
	    
	    System.out.println("Removed on: " + command.substring(command.indexOf(" ") + 1));
	    safeProperties(data);
	} else if (command.contains("mov"))
	{
	    String temp = command.substring(command.indexOf(" ") + 1);
	    
	    int destination;
	    int origin;
	    
	    destination = Integer.valueOf(temp.substring(0, temp.indexOf(" ")));
	    origin = Integer.valueOf(temp.substring(temp.indexOf(" ") + 1));
	    
	    mydata tempdata = data[destination];
	    
	    data[destination] = data[origin];
	    data[origin] = tempdata;
	    
	    safeProperties(data);
	} else if (command.equals("stopserver"))
	{
	    System.out.println("[Server] Stopping Server!");
	    stayalive = false;
	} else if (command.equals("pause"))
	{
	    downloadclass.pausedownload();
	} else if (command.equals("resume"))
	{
	    downloadclass.resumedownload();
	} else if (command.contains("add"))
	{
		String temp=command.substring(4);
		
		safeLink(temp);
		System.out.println("[Server] new Downloadlink added!");
	} else if(command.contains("bandwidth "))
	{
		int temp=Integer.valueOf(command.substring(10));
		
		downloadclass.setBandwidth(temp);
		System.out.println("[Server] Bandwidth set to: "+downloadclass.getBandwidth() +" kbit/S");
	}
	else
	{
	    System.out.println("[Server] [WARNING] Wrong command!");
	}
    }
    
    private void safeLink(String link)
    {
	int i;
	for (i = 0; !(data[i].link.equals("")); i++)
	{
	    
	}
	data[i].link = link;
	
	URL url;
	try
	{
	    url = new URL(data[i].link);
	    URLConnection urlcon = url.openConnection();
	    
	    String fieldValue = urlcon.getHeaderField("Content-Disposition");
	    if (fieldValue == null || !fieldValue.contains("filename=\""))
	    {
		fieldValue = ("unnamed"+String.valueOf(i));
	    } else
	    {
		fieldValue = fieldValue.substring(fieldValue.indexOf("filename=") + 10, fieldValue.length() - 1);
	    }
	    
	    data[i].filesize = urlcon.getContentLength();
	    data[i].contenttype = urlcon.getContentType();
	    data[i].filename = fieldValue;
	} catch (MalformedURLException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	safeProperties(data);
    }
    
    private void safeProperties(mydata[] data)
    {
	File file = new File("data.obj");
	
	try
	{
	    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
	    out.writeObject(data);
	    out.close();
	    
	} catch (FileNotFoundException e2)
	{
	    // TODO Auto-generated catch block
	    e2.printStackTrace();
	} catch (IOException e2)
	{
	    // TODO Auto-generated catch block
	    e2.printStackTrace();
	}
    }
    
    private void readProperties()
    {
	File file = new File("data.obj");
	
	try
	{
	    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
	    data = (mydata[]) in.readObject();
	    in.close();
	} catch (FileNotFoundException e)
	{
	    System.out.println("[Server] [Warning] Link data file does not exist!");
	    
	    data = new mydata[maxdownloads];
	    for (int i = 0; i <= maxdownloads - 1; i++)
	    {
		data[i] = new mydata();
	    }
	    
	    safeProperties(data);
	} catch (IOException e)
	{
	    e.printStackTrace();
	} catch (ClassNotFoundException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
    
    private void removeOnArrayPosition(mydata md[], int pos)
    {
	for (int i = 0; !md[i].link.equals(""); i++)
	{
	    if (i >= pos)
	    {
		md[i] = md[i + 1];
	    }
	}
    }
    
}

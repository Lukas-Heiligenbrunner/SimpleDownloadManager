package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class ClientMain {
    
    public static void main(String[] args)
    {
	
	Socket socket = new Socket();
	
	PropertyFunctions propinst = new PropertyFunctions();
	Properties props = propinst.readProperties();
	
	final InetSocketAddress address = new InetSocketAddress(props.getProperty("ipserver"),
		Integer.valueOf(props.getProperty("port")));
	
	try
	{
	    if (args[0].equals("--start"))
	    {
		
		try
		{
		    socket.connect(address, 3000);
		    
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("startdownloading");
		    prwr.flush();
		    
		    // s.close();
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
		
	    } else if (args[0].equals("--status"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("getstatus");
		    prwr.flush();
		    
		    Scanner s = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
		    while (s.hasNextLine())
		    {
			System.out.println(s.nextLine());
		    }
		    
		    s.close();
		    prwr.close();
		    socket.close();
		    
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
		
	    } else if (args[0].equals("--stop"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("stop");
		    prwr.flush();
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if (args[0].equals("--deletelinks"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("deletelinks");
		    prwr.flush();
		    
		    System.out.println("[Client] Clearcommand sent!");
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if (args[0].equals("--statusloop"))
	    {
		while (true)
		{
		    try
		    {
			socket = new Socket();
			socket.connect(address);
			PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			prwr.println("getstatus");
			prwr.flush();
			
			Scanner s = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			while (s.hasNextLine())
			{
			    System.out.println(s.nextLine());
			}
			
			s.close();
			Thread.sleep(500);
			
		    } catch (IOException e)
		    {
			System.err.println("[Client] [ERROR] no route to host!");
			break;
		    } catch (InterruptedException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    } else if (args[0].equals("--help"))
	    {
		System.out.println("--help (showing this help dialog)");
		System.out.println("--start (start downloading)");
		System.out.println("--stop (stop downloading)");
		System.out.println("--status (show the status of the current download)");
		System.out.println("--statusloop (same as before but in a infinite loop)");
		System.out.println("--deletelinks (deletes all links from list)");
		System.out.println("example.com/video.mp4 (add a link to the list without an argument...)");
		System.out.println("--stopserver (Stops the whole server!)");
		System.out.println("--delete <destination> (deletes the given position)");
		System.out
			.println("--movlink <destination> <origin> (Moves a link from the origin to the destination)");
		System.out.println("--listlinks (returns the list of stored links)");
	    } else if (args[0].equals("--listlinks"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("listlinks");
		    prwr.flush();
		    
		    Scanner s = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
		    while (s.hasNextLine())
		    {
			System.out.println(s.nextLine());
		    }
		    
		    s.close();
		    prwr.close();
		    socket.close();
		    
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if (args[0].equals("--delete"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("delete " + args[1]);
		    prwr.flush();
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if (args[0].contains("--movlink"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("mov " + args[1] + " " + args[2]);
		    prwr.flush();
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if (args[0].equals("--stopserver"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("stopserver");
		    prwr.flush();
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if (args[0].equals("--pausedownload"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("pause");
		    prwr.flush();
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if (args[0].equals("--resumedownload"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println("resume");
		    prwr.flush();
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if(args[0].equals("--add"))
	    {
		try
		{
		    socket.connect(address);
		    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    
		    prwr.println(args[0].substring(2)+" "+args[1]);
		    prwr.flush();
		    
		    System.out.println("[Client] added new link to Downloads");
		    
		    prwr.close();
		    socket.close();
		} catch (IOException e)
		{
		    System.err.println("[Client] [ERROR] no route to host!");
		}
	    } else if(args[0].equals("--limitbandwidth"))
	    {
	    	try
			{
			    socket.connect(address);
			    PrintWriter prwr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			    
			    prwr.println("bandwidth "+args[1]);
			    prwr.flush();
			    
			    System.out.println("[Client] set download bandwidth to: " + args[1] +"kbit/s" );
			    
			    prwr.close();
			    socket.close();
			} catch (IOException e)
			{
			    System.err.println("[Client] [ERROR] no route to host!");
			}
	    }
	    else
	    {
	    	System.out.println("[Client] [WARNING] Wrong command!");
	    }
	} catch (ArrayIndexOutOfBoundsException e)
	{
	    System.out.println("--help (showing this help dialog)");
	    System.out.println("--start (start downloading)");
	    System.out.println("--stop (stop downloading)");
	    System.out.println("--status (show the status of the current download)");
	    System.out.println("--statusloop (same as before but in a infinite loop)");
	    System.out.println("--deletelinks (deletes all links from list)");
	    System.out.println("example.com/video.mp4 (add a link to the list without an argument...)");
	    System.out.println("--stopserver (Stops the whole server!)");
	    System.out.println("--delete <destination> (deletes the given position)");
	    System.out.println("--movlink <destination> <origin> (Moves a link from the origin to the destination)");
	    System.out.println("--listlinks (returns the list of stored links)");
	}
	
    }
}

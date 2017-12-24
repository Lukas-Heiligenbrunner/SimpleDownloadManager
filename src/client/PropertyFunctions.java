package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFunctions {
    
    protected void safeProperties(Properties p)
    {
	File file = new File("properties.txt");
	try
	{
	    FileOutputStream outfile = new FileOutputStream(file);
	    p.store(outfile, "properties for client");
	    
	    outfile.close();
	    
	} catch (FileNotFoundException e1)
	{
	    e1.printStackTrace();
	} catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
    
    protected Properties readProperties()
    {
	File file = new File("properties.txt");
	Properties props = new Properties();
	try
	{
	    FileInputStream infile = new FileInputStream(file);
	    props.load(infile);
	    
	    infile.close();
	    
	} catch (FileNotFoundException e2)
	{
	    System.out.println("[Client] [Warning] Config file for clinet not found");
	    //set defaults!
	    props.setProperty("ipserver", "127.0.0.1");
	    props.setProperty("port", "3000");
	    safeProperties(props);
	    System.out.println("[Client] file created!");
	    readProperties(); //does it work?
	} catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return props;
    }
    
}

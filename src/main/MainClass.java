package main;

import manager.ServerStuff;

public class MainClass
{

    public static void main(String[] args)
    {
	ServerStuff server = new ServerStuff();
	server.startTCPServer();
    }

}

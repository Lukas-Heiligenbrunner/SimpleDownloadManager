package manager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Download {

	private static final String path = "/media/10.0.0.4/Download/got/"; // todo forme!

	// private static final String path = "";

	private URL website;
	private float speed;

	private int totalfilesize;
	private String contentType;
	private String fileName;

	private long loadedSize;
	private boolean downloadComplete;

	private int bandwidth;

	private Thread download;

	long timeafter;
	long megabyteafter;
	float speedfirst;
	float speedsecond;
	float speedthird;
	float speedfourth;
	float speedfifth;
	float speedsixth;
	float speedseventh;
	float speedeight;

	boolean isInterrupted;
	boolean pause;

	public Download() {
		timeafter = System.currentTimeMillis();

		loadedSize = 0;
		downloadComplete = false;
		bandwidth = -1; // standard --> infinite
	}

	public boolean toDownload(mydata mdata) {
		try {
			website = new URL(mdata.link);
			totalfilesize = (int) mdata.filesize;
			contentType = mdata.contenttype;
			fileName = mdata.filename;

			downloadComplete = false;
			isInterrupted = false;
			speed = 0;

			megabyteafter = 0;
			speedfirst = 0;
			speedsecond = 0;
			speedthird = 0;
			speedfourth = 0;
			speedfifth = 0;
			speedsixth = 0;
			speedseventh = 0;
			speedeight = 0;

			// fos = new FileOutputStream("/media/10.0.0.4/Download/got/"+fileName);

			InputStream in = website.openStream();

			OutputStream out = new FileOutputStream(path + fileName);

			download = new Thread(new Runnable() {

				@Override
				public void run() {
					boolean read = true;
					int readbytes = 0;
					byte[] buffer = new byte[1024]; // 1 kbyte == 8kbit

					loadedSize = 0;

					int n = 0;
					long timebefore = System.currentTimeMillis();

					try {
						while (read) {

							readbytes = in.read(buffer);

							if (readbytes == -1) {
								read = false; // download finished --> interrupt loop
							}

							if (read) {
								loadedSize += readbytes;
								out.write(buffer, 0, readbytes);
							}

							while (pause) {

								Thread.sleep(10); // didn´t work without this delay

								// do something while download paused...
							}

							n++;
							
							if (n == (bandwidth/10) || bandwidth==-1) // bandwidth/10 per 100ms
							{
								if (bandwidth != -1 && bandwidth != 0) {
									if (System.currentTimeMillis() - timebefore <= 100) {
										Thread.sleep(100 - (System.currentTimeMillis() - timebefore));
									}

									timebefore = System.currentTimeMillis();
								}

								n = 0;
							}

						}
						out.close();
						in.close();

					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					speed = 0;
					downloadComplete = true;

				}
			});
			download.start();

			new Thread(new Runnable() {

				@Override
				public void run() {

					while (download.isAlive()) // calc speed every 500 ms while thread is alive...
					{

						speed = calcSpeed();

						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}

				}
			}).start();

		} catch (MalformedURLException e) {
			System.err.println("MalformedURLException");
			System.out.println("[Server][ERROR]Invalid download link!");
			downloadComplete = true;
			return false;
		} catch (IOException e) {
			System.err.println("IOException");
			System.out.println("Reconnecting!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private float calcSpeed() {
		float speed = 0;

		long megabytebefore;
		long timebefore;

		timebefore = System.currentTimeMillis();
		megabytebefore = loadedSize;

		speedeight = speedseventh;
		speedseventh = speedsixth;
		speedsixth = speedfifth;
		speedfifth = speedfourth;
		speedfourth = speedthird;
		speedthird = speedsecond;
		speedsecond = speedfirst;
		speedfirst = (float) (megabyteafter - megabytebefore) / (float) (timeafter - timebefore);
		speed = (speedfirst + speedsecond + speedthird + speedfourth + speedfifth + speedsixth + speedseventh
				+ speedeight) / 8;

		timeafter = System.currentTimeMillis();
		megabyteafter = loadedSize;

		return speed;
	}

	public float getSpeed() {
		return this.speed;
	}

	public float getLoadedSize() {
		return loadedSize;
	}

	public boolean isDownloadcomplete() {
		return downloadComplete;
	}

	public String getType() {
		return contentType;
	}

	public int getTotalSize() {
		return totalfilesize;
	}

	public String getFileName() {
		return fileName;
	}

	public float getPercentDownloaded() {
		return (float) loadedSize / totalfilesize * 100;
	}

	public void interruptDownload() {
		download.interrupt();
		isInterrupted = true;
		System.out.println("[Server][WARNING] Download Thread Interrupted!");
	}

	public boolean isInterrupted() {
		return isInterrupted;
	}

	public void pausedownload() {
		System.out.println("Download paused");
		pause = true;
	}

	public void resumedownload() {
		pause = false;
		System.out.println("Download resumed");
	}

	public boolean isdownloadpaused() {
		return pause;
	}
	
	public void setBandwidth(int band)
	{
		bandwidth=band;
	}
	
	public int getBandwidth()
	{
		return bandwidth;
	}
}

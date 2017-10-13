package cz.tyckouni.ticketchecker.testmodule;

import cz.tyckouni.ticketchecker.core.FreeSpaceNotificator;
import cz.tyckouni.ticketchecker.core.FreeSpaceNotificatorImpl;

/**
 * Class created to only test if ticket checker can run on a certain platform
 */
public class Main {
	public static void main(String[] args) throws Exception {
		String url = args[0];
		String depart = args[1];
		String arrival = args[2];
		FreeSpaceNotificator notificator = new FreeSpaceNotificatorImpl(url, depart, arrival);
		notificator.setActionOnFreeSpace(() -> System.out.println("All is working, found free space."));
		notificator.setActionOnWaiting(() -> System.out.println("All is working, waiting for free space."));
		notificator.setActionOnError(() -> System.out.println("Something bad happen. Check log."));
		notificator.start();
//		Thread.sleep(60000);
	}
}

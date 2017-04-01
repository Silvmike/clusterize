package ru.hardcoders.simple;

import org.omg.CORBA.TIMEOUT;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michael on 04.04.2015.
 */
public class SessionInvalidator implements ServletContextListener {

    private static final long TIMEOUT = TimeUnit.SECONDS.toMillis(60);
    public static final long START_TIME = System.currentTimeMillis();

    public static final String INSTANCE_NAME;

    static {
        String instance = String.valueOf(System.currentTimeMillis());
        try {
            instance = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        INSTANCE_NAME = instance;
    }

    private final InvalidatorThread thread = new InvalidatorThread();
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        thread.interrupt();
    }

    public static class InvalidatorThread extends Thread {

        private static final Map<HttpSession, Long> sessions = new ConcurrentHashMap<HttpSession, Long>();

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Set<Map.Entry<HttpSession, Long>> entries = sessions.entrySet();
                    for (Iterator<Map.Entry<HttpSession, Long>> iter = entries.iterator(); iter.hasNext();) {
                        Map.Entry<HttpSession, Long> entry = iter.next();
                        if (entry.getValue() < System.currentTimeMillis()) {
                            entry.getKey().invalidate();
                            iter.remove();
                        }
                    }
                    synchronized (this) {
                        wait(TIMEOUT);
                    }
                }
            } catch (InterruptedException e) {
                // exit
            }
        }

        public static void watch(HttpSession session) {
            sessions.put(session, System.currentTimeMillis() + TIMEOUT);
        }
    }

}

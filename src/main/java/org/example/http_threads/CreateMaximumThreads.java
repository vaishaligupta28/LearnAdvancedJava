package org.example.http_threads;

public class CreateMaximumThreads {
    /**
     * Thread class
     * @author Lukas Krecan
     * https://blog.krecan.net/2010/05/02/cool-tomcat-is-able-to-handle-more-than-13000-concurrent-connections/
     *
     */
    private static final class MyThread extends Thread
    {
        private final int number;

        public MyThread(int number) {
            this.number = number;
        }
        @Override
        public void run() {
            if (shouldPrintMessage(number))
            {
                System.out.println("Thread no. "+number+" started.");
            }
            try {
                //sleep forever
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        final int noOfThreads = 1000000;
        int i=0;
        try {
            for (i=0; i < noOfThreads; i++)
            {
                if (shouldPrintMessage(i))
                {
                    System.out.println("Creating thread "+i+" ("+(System.currentTimeMillis()-startTime)+"ms)");
                }
                new MyThread(i).start();
            }
        } catch (Throwable e) {
            System.out.println("Error thrown when creating thread "+i);
            e.printStackTrace();
        }
    }

    private static boolean shouldPrintMessage(int i)
    {
        return i % 100 == 0;
    }
}

package nwscore.utils;


public interface TaskRunnable {
    public boolean isFinished();

    public void run();

    public void interrupt();

    public void join();
}

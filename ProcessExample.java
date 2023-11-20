import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessExample {

    private static void createChildProcess(String processName) {
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", "ps -x");
        try {
            Process childProcess = processBuilder.start();
            System.out.println(processName + " (PID " + getpid() + ") запущен");
            printCurrentTime();
            childProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void createSecondChildProcess(String processName) {
        System.out.println(processName + " (PID " + getpid() + ") запущен");
        printCurrentTime();
        System.out.println("Второй дочерний процесс замещен задачей создания дочерних потоков");
        createChildThreads();
    }

    private static void createChildThreads() {
        Thread thread1 = new Thread(() -> {
            System.out.println("Первый дочерний поток (ID " + Thread.currentThread().getId() +
                    ", PID " + getpid() + ") запущен");
            printCurrentTime();
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Второй дочерний поток (ID " + Thread.currentThread().getId() +
                    ", PID " + getpid() + ") запущен");
            printCurrentTime();
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date());
        System.out.println("Текущее время: " + currentTime);
    }

    private static int getpid() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(processName.split("@")[0]);
    }
    
    public static void main(String[] args) {
        System.out.println("Родительский процесс (PID " + getpid() + ") запущен");
        createChildProcess("Первый дочерний процесс");
        createSecondChildProcess("Второй дочерний процесс");
        System.out.println("Родительский процесс завершен");
    }
}

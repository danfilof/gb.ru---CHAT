package server;

public class TaskOne {

    private final Object mon = new Object();
    private volatile char currentLetter = 'A';
    private volatile char nextLetter;
    private volatile char upComingLetter;

    public static void main(String[] args) throws InterruptedException {

        TaskOne waitNotifyObj = new TaskOne();
        Thread thread1 = new Thread(() -> {
            waitNotifyObj.printA();
        });
        Thread thread2 = new Thread(() -> {
            waitNotifyObj.printB();
        });
        Thread thread3 = new Thread(() -> {
            waitNotifyObj.printC();
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }

    public void printA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'A') {
                        mon.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'B') {
                        mon.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'C') {
                        mon.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'A';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    public  void printLetter() {
//        for (int i = 0; i < 5; i++) {
//            synchronized (mon) {
//                try {
//                    while (currentLetter != upComingLetter) {
//                        mon.wait();
//                        System.out.println(firstLetter);
//                        upComingLetter = nextLetter;
//                        mon.notifyAll();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}

package server;

public class TaskOne {

    private final Object mon = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) throws InterruptedException {
        TaskOne waitNotifyObj = new TaskOne();
        Thread thread1 = new Thread(() -> {
            waitNotifyObj.printA();
        });
        Thread thread2 = new Thread(() -> {
            waitNotifyObj.printB();
        });Thread thread3 = new Thread(() -> {
            waitNotifyObj.printC();
        });
        thread1.start();
        thread2.start();
        thread3.start();

        //waitNotifyObj.printLetter();


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
//
//        Thread threadA = new Thread(() -> {
//            System.out.println("A");
//            currentLetter = 'B';
//            mon.notifyAll();
//        });
//        Thread threadB = new Thread(() -> {
//            System.out.println("B");
//            currentLetter = 'C';
//            mon.notifyAll();
//        });Thread threadC = new Thread(() -> {
//            System.out.println("C");
//            currentLetter = 'A';
//            mon.notifyAll();
//        });
//
//        synchronized (mon) {
//            try {
//                for (int i = 0; i < 5; i++) {
//                    while (currentLetter != 'A') {
//                        threadA.wait();
//                    }
//                    threadA.start();
//
//                    while (currentLetter != 'B') {
//                        threadB.wait();
//                    }
//                    threadB.start();
//
//                    while (currentLetter != 'C') {
//                        threadC.start();
//                    }
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

}

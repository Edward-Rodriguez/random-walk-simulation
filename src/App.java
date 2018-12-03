import java.util.*;
import java.util.concurrent.*;

public class App {

    private static final int numOfThreads = 3;
    private static final int totalNumOfSimulations = 1000000;
    private static final int numOfSimulationsPerThread = (totalNumOfSimulations) / (numOfThreads);
    private static final int k = 100;
    private static final int u = 1;
    private static final int d = 2;

    public static void main(String[] args) {
        HashMap<Integer, Integer> histogram = new HashMap<Integer, Integer>();
        double t_start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        List<Future<HashMap>> list = new ArrayList<Future<HashMap>>();
        int nMax = 0;

        for (int i = 0; i < numOfThreads; ++i) {
            Future<HashMap> future = executorService.submit(new RandomWalkSimulation(k, u, d, numOfSimulationsPerThread));
            list.add(future);
        }

        // shutdown executor service after all tasks are done
        executorService.shutdown();

        // set max wait limit for executor shutdown to 1 hour
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {

        }
        System.out.println("\nFinished all threads");
        double t_end = System.currentTimeMillis();
        System.out.println("time (sec) = " + (t_end - t_start)*0.001);
    }
}

    class RandomWalkSimulation implements Callable<HashMap> {
    private int k;
    private int u;
    private int d;
    private int x;
    private int n;
    private int numOfSimulations;
    private HashMap<Integer, Integer> hashMap;

    public  RandomWalkSimulation(int k, int u, int d, int numOfSimulations) {
        this.k = k;
        this.u = u;
        this.d = d;
        this.x = k;
        hashMap = new HashMap<Integer, Integer>();
        this.numOfSimulations = numOfSimulations;
        n = 0;
    }

    @Override
    public HashMap<Integer, Integer> call() {
        //return the thread name executing this callable task
        //System.out.println(Thread.currentThread().getName() + " Starting ... ");
        runRandomWalkSimulation();
        //System.out.println(Thread.currentThread().getName() + " Completed!");
        return hashMap;
    }

    public void runRandomWalkSimulation(){
        for (int i = 0; i < numOfSimulations; ++i) {
            x = k;
            n = 0;
            while (x > 0) {
                if (ThreadLocalRandom.current().nextInt(2) == 1) {
                    x = x + u;
                } else {
                    x = x - d;
                }
                n++;
            }
            if (hashMap.containsKey(n)) {
                int tempVal = hashMap.get(n) + 1;
                hashMap.put(n, tempVal);
            } else {
                hashMap.put(n, 1);
            }
        }
    }
}




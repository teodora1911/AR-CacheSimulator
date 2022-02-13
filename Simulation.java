import java.io.File;
import java.util.Scanner;

public final class Simulation {

    public static void main(String[] args) {

        int mainMemorySize = 0;
        int cacheMemorySize = 0;
        int cacheLineSize = 0;
        int associativity = 0;

        /*
         * Testni primjeri rade za :
         *      CacheMemorySize = 512
         *      CacheLineSize = 64
         *      Associatvity = 2
         */
        Scanner input = new Scanner(System.in);
        do{
            System.out.print("Unesite velicinu glavne memorije (broj adresa koje se citaju): ");
            mainMemorySize = input.nextInt();
            System.out.print("Unesite velicnu kes memorije (u bajtovima) : ");
            cacheMemorySize = input.nextInt();
            System.out.print("Unesite velicinu kes linije (u bajtovima) : ");
            cacheLineSize = input.nextInt();
            System.out.print("Unesite asocijativnost : ");
            associativity = input.nextInt();
        } while(!powerOf2(associativity) || !powerOf2(cacheLineSize) || !powerOf2(cacheMemorySize) || mainMemorySize < 1);

        input.close();

        Cache.calculateLengths(cacheMemorySize, cacheLineSize, associativity);

        Memory mem1 = new Memory("test" + File.separator + "mem14.txt", mainMemorySize);
        Cache cache1 = new Cache(associativity, ReplacementPolicy.OPTIMAL, mem1);
        Processor proc1 = new Processor(1, mem1, cache1);
        Bus.processors.add(proc1);

        Memory mem2 = new Memory("test" + File.separator + "mem24.txt", mainMemorySize);
        Cache cache2 = new Cache(associativity, ReplacementPolicy.LRU, mem2);
        Processor proc2 = new Processor(2, mem2, cache2);
        Bus.processors.add(proc2);

        Thread p1 = new Thread(proc1);
        Thread p2 = new Thread(proc2);
        p1.start();
        p2.start();

        try{
            p1.join();
            p2.join();
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }

        System.out.println();
        System.out.println("--- Processor 1 ---");
        System.out.println("Cache hits : " + proc1.cache.cacheHitNumber);
        System.out.println("Cache misses : " + proc1.cache.cacheMissNumber);
        System.out.println();

        System.out.println("--- Processor 2 ---");
        System.out.println("Cache hits : " + proc2.cache.cacheHitNumber);
        System.out.println("Cache misses : " + proc2.cache.cacheMissNumber);
    }

    private static boolean powerOf2(int number){
        return (int)(Math.ceil((Math.log(number) / Math.log(2)))) == (int)(Math.floor(((Math.log(number) / Math.log(2)))));
    }

    private Simulation() { }
}

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Processor implements Runnable {

    public static Random rand = new Random();

    private int identification;
    public Cache cache;
    public Memory memory;

    public Semaphore cacheLock = new Semaphore(1);
    
    public Processor(int identification, Memory memory, Cache cache){
        this.identification = identification;
        this.cache = cache;
        this.memory = memory;
    }

    public int getIdentification(){
        return this.identification;
    }

    public CacheLine getCacheLine(Address address){
        return this.cache.data.get(address.getSet()).getCacheLine(address.getTag());
    }

    public static void sleep(){
        try {
            Thread.sleep(rand.nextInt(11) * 100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run(){
        Instruction currentAddress = null;
        while((currentAddress = memory.getNextAddress()) != null){
            sleep();
            if(currentAddress.getInstruction().equals(Instruction.READ)){

                Bus.performRead(identification, currentAddress.getAddress());

                try{
                    cacheLock.acquire();
                    cache.readData(currentAddress.getAddress());
                    cacheLock.release();
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            } else {
                CacheLine line = getCacheLine(currentAddress.getAddress());

                if(line == null || (line != null && line.getState() != MSIState.MODIFIED)){
                    Bus.performWrite(identification, currentAddress.getAddress());
                }

                try{
                    cacheLock.acquire();
                    cache.writeData(currentAddress.getAddress());
                    cacheLock.release();
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }
            sleep();
        }
    }
}

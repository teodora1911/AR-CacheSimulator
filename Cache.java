import java.util.ArrayList;

public class Cache {

    public static final int ADDRESS_SIZE = 32;
     
    public int cacheMissNumber = 0;
    public int cacheHitNumber = 0;

    public ArrayList<Set> data;

    public static int numberOfSets;

    public Cache(int associativity, ReplacementPolicy replacementPolicy, Memory memory){
        this.data = new ArrayList<>();
        
        if(replacementPolicy == ReplacementPolicy.LRU){
            for(int i = 0; i < numberOfSets; ++i){
                data.add(new LRUSet(i, associativity));
            }
        } else if (replacementPolicy == ReplacementPolicy.OPTIMAL){
            for(int i = 0; i < numberOfSets; ++i){
                data.add(new OptimalSet(i, associativity, memory));
            }
        } else {
            System.out.println("Your replacement policy is not supported! :( ");
            System.exit(1);
        }
    }

    public Cache(int cacheMemorySize, int cacheLineSize, ReplacementPolicy replacementPolicy, Memory memory){
        this(1, replacementPolicy, memory);
    }

    // http://www.vlsiip.com/cache/cache_0003.html
    public static void calculateLengths(int cacheMemorySize, int cacheLineSize, int associativity){
        Cache.numberOfSets = cacheMemorySize / (associativity * cacheLineSize);
        int offsetBitsLength = (int)(Math.log(cacheLineSize) / Math.log(2));

        Address.setLength = (int)(Math.log(numberOfSets) / Math.log(2));
        Address.tagLength = ADDRESS_SIZE - Address.setLength - offsetBitsLength;

        System.out.println("Offset length : " + offsetBitsLength);
        System.out.println("Set length : " + Address.setLength);
        System.out.println("Tag length : " + Address.tagLength);
    }

    // read
    public void readData(Address address){
        Set requestedSet = data.get(address.getSet());
        if(requestedSet.readBlock(address.getTag())){
            ++cacheHitNumber;
        } else {
            ++cacheMissNumber;
        }
    }

    // write
    public void writeData(Address address){
        Set requestedSet = data.get(address.getSet());
        if(requestedSet.writeBlock(address.getTag())){
            ++cacheHitNumber;
        } else {
            ++cacheMissNumber;
        }
    }
}

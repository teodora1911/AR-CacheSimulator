import java.util.List;

public class OptimalSet extends Set {

    public static final String CN = "OPTIMAL";

    private Memory memory;

    public OptimalSet(int identification, int setSize, Memory memory) {
        super(identification, setSize);
        this.memory = memory;
    }

    @Override
    public boolean readBlock(int tag) {
        CacheLine requestLine = getCacheLine(tag);

        // adresa je već keširana
        if(requestLine != null){
            if(requestLine.getState() == MSIState.INVALID){
                requestLine.setState(MSIState.SHARED);
                System.out.println("Cache miss! " + CN);
                return false;
            }
            System.out.println("Cache hit! " + CN);
            return true;
        } else { // adresa nije keširana
            CacheLine newCacheLine = new CacheLine(tag, MSIState.SHARED);
            // ako skup nije pun, dodajemo novu keš liniju
            if(set.size() < this.setSize){ 
                set.addFirst(newCacheLine);
            } else {
                // ako je skup pun, tražimo u memoriji onu adresu koja će posljednja biti referencirana (ili neće nikada ni biti)
                List<Integer> tags = getTags();
                int tagToRemove = -1;
                int tagIndex = -1;
                for(int t : tags){
                    int result = memory.getIndexOf(t, identification);
                    if(result > tagIndex){
                        tagToRemove = t;
                        tagIndex = result;
                    }
                }
                // izbacujemo nađenu keš liniju iz keša, i dodajemo novu
                CacheLine lastUsedEntry = getCacheLine(tagToRemove);
                set.remove(lastUsedEntry);
                set.addFirst(newCacheLine);
                // samo ako je podatak u keš liniji koju izbacujemo validan, upisujemo u glavnu memoriju
                if(lastUsedEntry.getState() == MSIState.MODIFIED){
                    Bus.flush(lastUsedEntry);
                }
            }

            System.out.println("Cache miss! " + CN);
            return false;
        }
    }

    @Override
    public boolean writeBlock(int tag) {
        CacheLine requestLine = getCacheLine(tag);

        if(requestLine != null){
            if(requestLine.getState() != MSIState.INVALID){
                requestLine.setState(MSIState.MODIFIED);
                System.out.println("Cache hit! " + CN);
                return true;
            }

            requestLine.setState(MSIState.MODIFIED);
            System.out.println("Cache miss! " + CN);
            return false;
        } else {
            CacheLine newCacheLine = new CacheLine(tag, MSIState.MODIFIED);

            if(set.size() < this.setSize){
                set.addFirst(newCacheLine);
            } else {
                List<Integer> tags = getTags();
                int tagToRemove = -1;
                int tagIndex = -1;
                for(int t : tags){
                    int result = memory.getIndexOf(t, identification);
                    if(result > tagIndex){
                        tagToRemove = t;
                        tagIndex = result;
                    }
                }
                CacheLine lastUsedEntry = getCacheLine(tagToRemove);
                set.remove(lastUsedEntry);
                set.addFirst(newCacheLine);
                if(lastUsedEntry.getState() == MSIState.MODIFIED){
                    Bus.flush(lastUsedEntry);
                } 
            }

            System.out.println("Cache miss! " + CN);
            return false;
        }
    }
    
}

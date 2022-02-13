public class LRUSet extends Set {

    public static final String CN = "LRU";

    public LRUSet(int identification, int setSize){
        super(identification, setSize);
    }

    @Override
    public boolean readBlock(int tag){
        CacheLine requestedLine = getCacheLine(tag);

        // ako je adresa vec keširana
        if(requestedLine != null){
            // stavi blok na početak
            set.remove(requestedLine);
            set.addFirst(requestedLine);

            // ako je keš linija u INVALID stanju, to znači da je podatak u toj keš liniji promijenjen od strane nekog drugog procesora
            // nemamo najnoviju verziju tog podatka i moramo ga čitati iz glavne memorije, što znači da je keš promašaj
            if(requestedLine.getState() == MSIState.INVALID){
                System.out.println("Cache miss! " + CN);
                requestedLine.setState(MSIState.SHARED);
                return false;
            } else {
                // ako imamo najnoviju verziju, ostajemo u istom stanju, keš pogodak
                System.out.println("Cache hit! " + CN);
                return true;
            }
        } else { // adresa nije keširana
            // pravimo novi unos u skup, i uvijek je u SHARED stanju
            CacheLine newCacheLine = new CacheLine(tag, MSIState.SHARED);
            // ako skup nije pun, samo dodajemo novu keš liniju
            if(set.size() < this.setSize){
                set.addFirst(newCacheLine);
            } else {
                // ako je skup pun, izbacujemo posljednji element u skupu (onaj koji je najmanje puta korišten)
                // i dodajemo novu liniju na početak
                CacheLine eldestEntry = set.removeLast();
                set.addFirst(newCacheLine);
                // samo ako je podatak u keš liniji koju izbacujemo validan, upisujemo u glavnu memoriju
                if(eldestEntry.getState() == MSIState.MODIFIED){
                    Bus.flush(eldestEntry);
                }
            }

            System.out.println("Cache miss! " + CN);
            return false;
        }
    }

    @Override
    public boolean writeBlock(int tag){
        CacheLine requestedLine = getCacheLine(tag);

        // adresa je već kreširana
        if(requestedLine != null){
            // LRU algoritam
            set.remove(requestedLine);
            set.addFirst(requestedLine);

            // ako nije tražena linija nije u INVALID stanju
            // posjeduje najnoviju kopiju podatka iz memorije i kako piše u tu adresu, prelazi u stanje MODIFIED
            if(requestedLine.getState() != MSIState.INVALID){
                requestedLine.setState(MSIState.MODIFIED);
                System.out.println("Cache hit! " + CN);
                return true;
            } else {
                // tražena linija je u INVALID stanju i ne posjeduje najnoviju kopiju podatka iz memorije
                // treba prvo da učita traženi podatak pa ga izmijeni
                requestedLine.setState(MSIState.MODIFIED);
                System.out.println("Cache miss! " + CN);
                return false;
            }
        } else {
            // adresa nije keširana isti je slučaj kao kod INVALID stanja
            CacheLine newCacheLine = new CacheLine(tag, MSIState.MODIFIED);
            
            if(set.size() < this.setSize){
                set.addFirst(newCacheLine);
            } else {
                CacheLine eldestEntry = set.removeLast();
                set.addFirst(newCacheLine);
                if(eldestEntry.getState() == MSIState.MODIFIED){
                    Bus.flush(eldestEntry);
                }
            }

            System.out.println("Cache miss! " + CN);
            return false;
        }
    }
}

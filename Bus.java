import java.util.ArrayList;

public final class Bus {
    
    public static ArrayList<Processor> processors = new ArrayList<>();

    public static synchronized void flush(CacheLine cacheLine){
        System.out.println("Write back" + cacheLine);
    }

    public static void performRead(int processorID, Address address){

        for(Processor processor : processors){
            // prolazimo kroz sve ostale procesore
            if(processor.getIdentification() != processorID){
               try{
                   processor.cacheLock.acquire();
                   CacheLine currentCacheLine = processor.getCacheLine(address);
                   if(currentCacheLine == null || currentCacheLine.getState() != MSIState.MODIFIED){
                       //System.out.println("No cache line.");
                       processor.cacheLock.release();
                   } else {
                       // ako postoji tražena adresa u nekom od keševa drugih procesora i ako je u MODIFIED stanju
                       // tada taj procesor treba da upise izmjenjeni podatak u glavnu memoriju i da promijeni svoje stanje u SHARED
                       flush(currentCacheLine);
                       System.out.println("BUS");
                       currentCacheLine.setState(MSIState.SHARED);
                       processor.cacheLock.release();
                   }
               } catch (InterruptedException ex){
                   ex.printStackTrace();
               }
            }
        }
    }

    public static void performWrite(int processorID, Address address){

        for(Processor processor : processors){
            if(processor.getIdentification() != processorID){
                try{
                    processor.cacheLock.acquire();
                    CacheLine currentCacheLine = processor.getCacheLine(address);
                    // ako ne postoji keširana adresa, ništa ne radimo
                    if(currentCacheLine == null){
                        //System.out.println("No cache line.");
                        processor.cacheLock.release();
                    } else { // ako ima keširanu adresu
                        // imali su validnu kopiju tog podatka, ali pošto neki drugi procesor piše, njihova kopija postaje nevalidna
                        if(currentCacheLine.getState() == MSIState.SHARED){
                            currentCacheLine.setState(MSIState.INVALID);
                        } else if(currentCacheLine.getState() == MSIState.MODIFIED){
                            // već je izmjenjen podatak i ne nalazi se u glavnoj memoriji
                            // treba prvo da upišemo u memoriju taj podatak
                            flush(currentCacheLine);
                            System.out.println("BUS");
                            currentCacheLine.setState(MSIState.INVALID);
                        }
                        processor.cacheLock.release();
                    }
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    private Bus() { }
}

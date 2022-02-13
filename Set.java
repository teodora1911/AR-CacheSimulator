import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Set {

    protected LinkedList<CacheLine> set;
    protected int setSize;
    // treba za optimalni algoritam
    protected int identification;

    public Set(int identification, int setSize){
        this.identification = identification;
        this.setSize = setSize;
        this.set = new LinkedList<>();
    }

    protected CacheLine getCacheLine(int tag){
        Optional<CacheLine> requested = set.stream().filter(line -> line.getTag() == tag).findFirst();
        return (requested.isPresent()) ? requested.get() : null;
    }

    protected List<Integer> getTags(){
        return set.stream().map(line -> line.getTag()).collect(Collectors.toList());
    }

    abstract public boolean readBlock(int tag);
    abstract public boolean writeBlock(int tag);
}

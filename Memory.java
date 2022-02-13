import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Memory {

    private String filename;
    private ArrayList<Instruction> addresses;
    private int addressCounter;
    private int limit;
    
    public Memory(String filename, int limit){
        this.filename = filename;
        this.addressCounter = 0;
        this.addresses = new ArrayList<>();
        this.limit = limit;

        initialize();
    }

    private void initialize(){

        try(Stream<String> stream = Files.lines(Paths.get(filename))){
            List<String> lines = stream.limit(limit).collect(Collectors.toList());
            for(String line : lines){
                String[] parts = line.split(",");
                addresses.add(new Instruction(parts[0], new Address(parts[1])));
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public Instruction getNextAddress(){
        if(addressCounter >= addresses.size()){
            return null;
        }
        Instruction toReturn = addresses.get(addressCounter);
        ++addressCounter;
        return toReturn;
    }

    // za optimalni algoritam
    public int getIndexOf(int tag, int set){
        List<Instruction> filtered = addresses.stream().skip(addressCounter).filter(add -> add.getAddress().getSet() == set).collect(Collectors.toList());
        Instruction requiredAddress = new Instruction("", new Address(tag, set));
        int index = filtered.indexOf(requiredAddress);

        return (index != -1) ? index : Integer.MAX_VALUE;
    }
}

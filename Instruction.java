public class Instruction {
    
    public static final String READ = "R";
    public static final String WRITE = "W";

    private String instruction;
    private Address address;

    public Instruction(String instruction, Address address){
        this.instruction = instruction;
        this.address = address;
    }

    public String getInstruction(){
        return this.instruction;
    }

    public void setInstruction(String instruction){
        this.instruction = instruction;
    }

    public Address getAddress(){
        return this.address;
    }

    public void setAddress(Address address){
        this.address = address;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Instruction){
            Instruction other = (Instruction)object;
            return this.address.equals(other.getAddress());
        }

        return false;
    }

    @Override
    public String toString(){
        return " {" + instruction + ", " + address + "} ";
    }
}

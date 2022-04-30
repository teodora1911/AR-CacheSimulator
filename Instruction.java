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
        if(this == object){
            return true;
        }
        if(object == null){
            return false;
        }
        if(getClass() != object.getClass()){
            return false;
        }

        Instruction other = (Instruction)object;
        return address.equals(other.getAddress());
    }

    @Override
    public String toString(){
        return " {" + instruction + ", " + address + "} ";
    }
}

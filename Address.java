import java.math.BigInteger;

public class Address {
    
    public static int tagLength;
    public static int setLength;

    private String address;

    private int tag;
    private int set;

    public Address(String address){
        String binaryAddress = new BigInteger(address, 16).toString(2);

        this.address = address;
        this.tag = Integer.parseInt(binaryAddress.substring(0, tagLength), 2);
        this.set = Integer.parseInt(binaryAddress.substring(tagLength, tagLength + setLength), 2);
    }

    public Address(int tag, int set){
        this.tag = tag;
        this.set = set;
    }

    public int getTag(){
        return this.tag;
    }

    public int getSet(){
        return this.set;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Address){
            Address other = (Address)object;
            return this.tag == other.tag;
        }
        return false;
    }

    @Override
    public String toString(){
        return this.address;
    }
}

public class CacheLine {

    private int tag;
    private MSIState state;

    public CacheLine(int tag, MSIState state){
        this.tag = tag;
        this.state = state;
    }

    public int getTag(){
        return this.tag;
    }

    public void setTag(int tag){
        this.tag = tag;
    }

    public MSIState getState(){
        return this.state;
    }

    public void setState(MSIState state){
        this.state = state;
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

        CacheLine other = (CacheLine)object;
        return tag == other.tag;
    }

    @Override
    public String toString(){
        return " [" + Integer.toHexString(tag) + "] ";
    }
}
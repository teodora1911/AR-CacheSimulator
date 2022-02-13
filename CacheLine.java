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
        if(object instanceof CacheLine){
            CacheLine other = (CacheLine)object;
            return this.tag == other.tag;
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return " [" + Integer.toHexString(tag) + "] ";
    }
}
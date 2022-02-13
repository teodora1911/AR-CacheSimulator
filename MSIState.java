public enum MSIState {
    SHARED(0),
    MODIFIED(1),
    INVALID(2);

    int value;

    private MSIState(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}

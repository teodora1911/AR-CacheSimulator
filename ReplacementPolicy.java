public enum ReplacementPolicy {
    LRU(0),
    OPTIMAL(1);

    private int replacementCode;

    private ReplacementPolicy(int replacementCode){
        this.replacementCode = replacementCode;
    }

    public int getValue(){
        return this.replacementCode;
    }
}

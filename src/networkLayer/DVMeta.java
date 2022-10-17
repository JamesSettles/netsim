package networkLayer;

public class DVMeta {
    private String dest;
    private String source;

    public DVMeta(String dest, String source) {
        this.dest = dest;
        this.source = source;
    }
    public DVMeta(String dest){
        this.dest = dest;
    }

    public void setSource(String source){
        this.source = source;
    }
    public String getDest() { return dest; }
    public String getSource() { return source; }
    
}

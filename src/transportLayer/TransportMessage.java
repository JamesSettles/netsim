package transportLayer;


import java.util.Objects;

public final class TransportMessage {
    private final Object meta;
    private final byte[] data;

    public TransportMessage(Object meta, byte[] data) {
        this.meta = meta;
        this.data = data;
    }

    public Object meta() {
        return meta;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TransportMessage) obj;
        return Objects.equals(this.meta, that.meta) &&
                Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, data);
    }

    @Override
    public String toString() {
        return "TransportMessage[" +
                "meta=" + meta + ", " +
                "data=" + data + ']';
    }
}

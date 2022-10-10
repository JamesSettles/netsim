package physicalLayer;

import configurator.Wire;
import exceptions.*;

import java.io.*;
import java.nio.ByteBuffer;

public class PipeBackedPort extends Port implements Runnable {
    PipedInputStream rcv;
    PipedOutputStream snd;
    Wire wire;

    Thread rcvThread;
    private boolean running;

    public PipeBackedPort() {
    }

    /**
     * Safely start the port
     */
    @Override
    public void bringUp() {
        super.bringUp();
        if(rcv==null || snd==null || wire==null) {
            throw new LayerNotConfigured("physical");
        }
        running = true;
        rcvThread = new Thread(this);
        rcvThread.setDaemon(true);
        rcvThread.start();
    }

    /**
     * Safely shutdown the port
     */
    @Override
    public void bringDown() {
        running = false;
        rcvThread = null;
    }

    public void interrupt() { }

    /**
     * Places a bit pattern on the wire
     * @param bits the sequence to send
     */
    @Override
    public void receiveFromLink(byte[] bits) {
        if(wire.isCut()) { return; }
        if(bits==null) { throw new CantBeNull("bits"); }
        if(bits.length>Integer.MAX_VALUE) { throw new UnsupportedSize("physical",bits.length); }
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(bits.length);
        try {
            snd.write(bb.array());
            snd.write(bits);
        } catch (IOException e) {
            throw new BrokenLayer("physical");
        }
    }

    /**
     * To support the push model, each port needs a thread to grab the next thing
     */
    @Override
    public void run() {
        ByteBuffer bb = ByteBuffer.allocate(4);
        int len;
        byte[] data;
        try {
            while (running) {
                rcv.readNBytes(bb.array(), 0, 4);
                bb.rewind();
                len = bb.getInt();
                data = rcv.readNBytes(len);
                getLinkLayer().receiveFromPhysical(data);
            }
        } catch (IOException e) {
            throw new BrokenLayer("physical");
        }
    }

    /**
     * Converts a logical wire into a concrete wire
     * @param w logical wire to instantiate
     */
    public void connectWire(Wire w) {
        // Get the correct Port instances for both sides
        PipeBackedPort a = (PipeBackedPort) w.getPortA();
        PipeBackedPort b = (PipeBackedPort) w.getPortB();

        // Check that the wire hasn't already been instantiated
        if (a.snd != null || a.rcv != null || b.snd != null || b.rcv != null) {
            throw new CantCreateLayer("physical");
        }

        a.wire = w;
        b.wire = w;

        // Configure the wire
        PipedInputStream aToBin = new PipedInputStream();
        PipedInputStream bToAin = new PipedInputStream();
        try {
            PipedOutputStream aToBout = new PipedOutputStream(aToBin);
            PipedOutputStream bToAout = new PipedOutputStream(bToAin);

            a.snd = aToBout;
            a.rcv = bToAin;

            b.snd = bToAout;
            b.rcv = aToBin;
        } catch (IOException e) {
            throw new CantCreateLayer("physical");
        }

    }
}

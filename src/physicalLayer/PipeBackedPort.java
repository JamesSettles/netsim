package physicalLayer;

import configurator.Wire;
import exceptions.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of a Port that uses a JAVA pipe for the wire
 */
public class PipeBackedPort extends Port implements Runnable {
    PipedInputStream rcv;
    PipedOutputStream snd;
    Wire wire;

    Thread rcvThread;
    Thread sndThread; // Needed to avoid broken pipes when apps finish on a node
    LinkedBlockingQueue<byte[]> sndQueue;
    private boolean running;

    public PipeBackedPort() {
        sndQueue = new LinkedBlockingQueue<byte[]>();
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
        if(rcvThread==null) {
            rcvThread = new Thread(this);
            rcvThread.setDaemon(true);
            rcvThread.start();
        }
        if(sndThread==null) {
            Runnable sender = new Sender();
            sndThread = new Thread(sender);
            sndThread.setDaemon(true);
            sndThread.start();
        }
    }

    /**
     * Safely shutdown the port
     */
    @Override
    public void bringDown() {
        running = false;
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
        ByteBuffer bb = ByteBuffer.allocate(4+bits.length);
        bb.putInt(bits.length);
        bb.put(bits);
        sndQueue.add(bb.array());
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
            while (true) {
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

    private class Sender implements Runnable {

        @Override
        public void run() {
            while(true) {
                byte[] data = new byte[0];
                try {
                    data = sndQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }
                try {
                    snd.write(data);
                } catch (IOException e) {
                    throw new BrokenLayer("Physical");
                }
            }
        }
    }
}

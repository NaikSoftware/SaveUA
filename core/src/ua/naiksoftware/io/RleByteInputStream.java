package ua.naiksoftware.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * http://uk.wikipedia.org/wiki/RLE
 *
 * @author Naik
 */
public class RleByteInputStream extends InputStream {

    private int count;
    private int current;
    private final DataInputStream input;

    public RleByteInputStream(InputStream input) {
        this.input = new DataInputStream(input);
    }

    @Override
    public int read() throws IOException {
        if (count < 1) {
            current = input.read();
            count = input.readInt();
        }
        count--;
        return current;
    }

}

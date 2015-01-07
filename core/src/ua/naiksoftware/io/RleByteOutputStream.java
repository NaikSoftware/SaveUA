package ua.naiksoftware.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * http://uk.wikipedia.org/wiki/RLE
 *
 * @author Naik
 */
public class RleByteOutputStream extends OutputStream {

    private int count;
    private int current;
    private final DataOutputStream out;

    public RleByteOutputStream(OutputStream out) {
        this.out = new DataOutputStream(out);
        count = 0;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == current) {
            count++;
        } else {
            if (count != 0) {
                out.write(current);
                out.writeInt(count);
            }
            current = b;
            count = 1;
        }
    }

    @Override
    public void flush() throws IOException {
        if (count != 0) {
            out.write(current);
            out.writeInt(count);
        }
        out.flush();
        super.flush();
    }

}

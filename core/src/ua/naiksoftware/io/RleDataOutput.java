package ua.naiksoftware.io;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * 
 * @author Naik
 */
public class RleDataOutput extends DataOutputStream {

    public RleDataOutput(OutputStream out) {
        super(out);
    }

}

package ua.naiksoftware.io;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * 
 * @author Naik
 */
public class RleDataInput extends DataInputStream {

    public RleDataInput(InputStream in) {
        super(in);
    }

}

package ua.naiksoftware.waronline.map;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.DataInput;
import java.util.zip.ZipInputStream;

/**
 * Формат хранения данных:
 *
 * String map name (read/write UTF); int max gamers; int map width; int map
 * height;
 *
 * @author Naik
 */
public class MapEntry {

    private boolean local;
    private String path;
    private String name;
    private int maxGamers;
    private int w, h;

    /**
     * @param path путь к карте
     * @param local если true, то карта в локальном хранилище, иначe в internal
     * (assets)
     */
    public MapEntry(String path, boolean local) {
        this.local = local;
        this.path = path;

        FileHandle fh;

        if (local) {
            fh = Gdx.files.local(path);
        } else {
            fh = Gdx.files.internal(path);
        }

        if (!fh.exists()) {
            name = "Not found";
        } else {
            try {
                ZipInputStream zis = new ZipInputStream(fh.read());
                zis.getNextEntry();
                DataInput data = new DataInput(zis);
                name = data.readUTF();
                maxGamers = data.readInt();
                w = data.readInt();
                h = data.readInt();
                data.close();
                zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isLocal() {
        return local;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public int getMaxGamers() {
        return maxGamers;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}

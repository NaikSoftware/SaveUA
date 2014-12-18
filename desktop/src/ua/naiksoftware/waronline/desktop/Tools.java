package ua.naiksoftware.waronline.desktop;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import static java.lang.System.out;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Naik on 04.12.14.
 */
public class Tools {

    private static String srcDir;
    private static String dstDir;
    private static String atlasName;

    public static void main(String[] args) {
        out.println("___ATLAS GENERATOR___");
        Scanner scanner = new Scanner(System.in);
        out.println("Введіть шлях до папки з тайлами:");
        srcDir = scanner.next();
        out.println("Введіть шлях до папки призначення:");
        dstDir = scanner.next();
        out.println("Введіть назву створеного атласу:");
        atlasName = scanner.next();
        scanner.close();

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 512;
        settings.maxHeight = 512;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.format = Pixmap.Format.RGB888;

        try {
            new File(dstDir).mkdirs();
            TexturePacker.process(settings,
                    srcDir,
                    dstDir,
                    atlasName);
        } catch (Exception e) {
            out.println("Помилка!!! " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            out.println("Виконання завершено.");
        }
    }
}

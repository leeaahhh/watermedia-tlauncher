package me.srrapero720.watermedia;

import me.srrapero720.watermedia.api.image.ImageAPI;
import me.srrapero720.watermedia.api.loader.IMediaLoader;
import me.srrapero720.watermedia.core.tools.exceptions.ReInitException;

import java.io.File;
import java.nio.file.Path;

public class Testo {
    public static void main(String[] args) throws ReInitException {
        ImageAPI.init(new IMediaLoader() {
            @Override
            public String name() {
                return "testo";
            }

            @Override
            public Path tmpPath() {
                return null;
            }

            @Override
            public Path processPath() {
                return new File("").toPath();
            }
        });

//        String[] arr1 = new String[] { "value-1", "value-2", "value-3" };
//        String[] arr2 = new String[] { "extra-1", "extra-2", "extra-3" };
//        System.out.println(Arrays.toString(arr1));
//        System.out.println(Arrays.toString(arr2));
//
//        String[] combined = DataTool.data(arr1, arr2);
//        System.out.println(Arrays.toString(combined));


    }
}
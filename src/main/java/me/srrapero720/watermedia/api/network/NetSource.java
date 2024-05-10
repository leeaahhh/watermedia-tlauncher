package me.srrapero720.watermedia.api.network;

import me.srrapero720.watermedia.WaterMedia;
import me.srrapero720.watermedia.api.network.patch.URLPatch;
import me.srrapero720.watermedia.tools.exceptions.PatchingURLException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class NetSource {
    private String source;
    private URL url;
    private URLPatch patch;
    private boolean patched;
    private boolean media;
    private boolean local;
    private boolean stream;

    public NetSource(String source) throws MalformedURLException {
        this.source = source;

        // step 1: validating is a protocol file
        if (source.startsWith("file:///")) {
            this.url = URI.create(source).toURL();
            this.media = false;
            this.local = true;
            this.stream = false;
            this.patch = null;
            return;
        }

        // step 2: validating is a local protocol
        if (source.startsWith("local:///")) {
            String str = source.substring("local:///".length());
            if (str.startsWith("/")) str = str.substring(1);

            this.url = URI.create("file:///" + new File("").toPath().resolve(str)).toURL();
            this.media = false;
            this.local = true;
            this.stream = false;
            this.patch = null;
            return;
        }

        // step 3: validating is game protocl
        if (source.startsWith("game:///")) {
            String str = source.substring("game:///".length());
            if (str.startsWith("/")) str = str.substring(1);

            // TODO: some handling on WM bootstrap side pls
            this.url = URI.create("file:///" + WaterMedia.getLoader().processDir().resolve(str)).toURL();
            this.media = false;
            this.local = true;
            this.stream = false;
            this.patch = null;
            return;
        }

        // step 4: validate is a tmp file
        if (source.startsWith("tmp:///")) {
            String str = source.substring("tmp:///".length());
            if (str.startsWith("/")) str = str.substring(1);

            // TODO: some handling on WM bootstrap side pls
            this.url = URI.create("file:///" + WaterMedia.getLoader().tempDir().resolve(str)).toURL();
            this.media = false;
            this.local = true;
            this.stream = false;
            this.patch = null;
            return;
        }


        // step 5: fuck you
        File f = new File(source).getAbsoluteFile();
        if (f.exists()) {
            this.url = f.toURI().toURL();
            this.media = false;
            this.local = true;
            this.stream = false;
            this.patch = null;
            return;
        }

        // step 5: fuck you x2
        this.url = URI.create(source).toURL();
    }

    public NetSource(String source, boolean media, boolean local, boolean stream) throws MalformedURLException {

    }

    public NetSource(URL url) {

    }

    public URL getUrl() {
        try {
            if (patch != null && !patched) {
                return this.url = patch.patch(null, null).asURL();
            }
        } catch (PatchingURLException e) {
            return this.url;
        }
    }
}

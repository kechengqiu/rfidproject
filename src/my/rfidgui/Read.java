/**
 * Sample program that reads tags for a fixed period of time (500ms) and prints
 * the tags found.
 */
// Import the API
package my.rfidgui;

import com.thingmagic.*;
import java.util.ArrayList;

public class Read {

    static void usage() {
        System.out.printf("Usage: demo reader-uri <command> [args]\n"
                + "  (URI: 'tmr:///COM1' or 'tmr://astra-2100d3/' "
                + "or 'tmr:///dev/ttyS0')\n\n"
                + "Available commands:\n");
        System.exit(1);
    }

    public static void setTrace(Reader r, String args[]) {
        if (args[0].toLowerCase().equals("on")) {
            r.addTransportListener(r.simpleTransportListener);
        }
    }

    static class TagReadListener implements ReadListener {

        public void tagRead(Reader r, TagReadData t) {
            System.out.println("Tag Read " + t);
        }
    }

    public static TagReadData[] getTagInfo(Reader r) {

        TagReadData[] tagReads = new TagReadData[]{};
        // Create Reader object, connecting to physical device
        try {
            r.connect();
            if (Reader.Region.UNSPEC == (Reader.Region) r.paramGet("/reader/region/id")) {
                Reader.Region[] supportedRegions = (Reader.Region[]) r.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
                if (supportedRegions.length < 1) {
                    throw new Exception("Reader doesn't support any regions");
                } else {
                    r.paramSet("/reader/region/id", supportedRegions[0]);
                }
            }
            r.addReadListener(new TagReadListener());
            // Read tags
            tagReads = r.read(500);
            // Shut down reader
            r.destroy();
        } catch (ReaderException re) {
            System.out.println("Reader Exception : " + re.getMessage());
            r.destroy();
        } catch (Exception re) {
            System.out.println("Exception : " + re.getMessage());
            r.destroy();
        }

        return tagReads;
    }
}

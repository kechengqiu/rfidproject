
package rfid_project;

import com.thingmagic.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

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
//the function that reads the tag
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
            tagReads = r.read(10);
            // Shut down reader
            r.destroy();
        } catch (ReaderException re) {
            System.out.println("Reader Exception 12: " + re.getMessage());
            r.destroy();
        } catch (Exception re) {
            System.out.println("Exception : " + re.getMessage());
            r.destroy();
        }
        //returns what's read
        return tagReads;
    }
}
class teststuff{
    public static void  main(String[] args){
                ArrayList<String> tagInfoStrings = new ArrayList<String>();
                String info;
                Reader r = null;
            
                
       
                 try {
            r = Reader.create("tmr:///COM3" );
        } catch (ReaderException re) {
            System.out.println("Reader Exception : " + re.getMessage());
            r.destroy();
        }
                 
        TagReadData[] tagReads = Read.getTagInfo(r);
       // String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()
        long unixTime = System.currentTimeMillis()/1000L;
                
//);
          Scanner s = new Scanner(System.in); 
            System.out.println("please enter reader ID:");
            int readerID = s.nextInt();
       
        for (TagReadData tr : tagReads) 
            tagInfoStrings.add(tr.epcString());
        for (String tagInfo : tagInfoStrings) {
            System.out.println(tagInfo +" " + unixTime);
            try{
            
            URL yahoo = new URL("http://uaf58600.ddns.uark.edu/Database/update.php?id="+tagInfo+"&readerID="+readerID+"&timestamp="+unixTime);
            System.out.println(yahoo);
            URLConnection yc = yahoo.openConnection();
            yc.connect();
            InputStream result = yc.getInputStream();
            
            }
            catch(MalformedURLException e)
            {
                System.out.println("failed to connect");
            }
            catch (IOException e)
            {
             System.out.println("blah");
            }
      }
    }
}

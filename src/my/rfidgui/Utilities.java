package my.rfidgui;

import com.thingmagic.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class Utilities {

    public static byte[] convertToByteArray(short[] value) {
        ByteBuffer buffer = ByteBuffer.allocate(value.length * 2);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asShortBuffer().put(value);
        byte[] bytes = buffer.array();
        return bytes;
    }
    
    public static DefaultListModel readHelper(Reader r) {
        TagReadData[] tagReads = Read.getTagInfo(r);
        DefaultListModel listModel = new DefaultListModel();
        ArrayList<String> tagInfoStrings = new ArrayList<String>();
        for (TagReadData tr : tagReads) {
            tagInfoStrings.add(tr.epcString());
        }
        for (String tagInfo : tagInfoStrings) {
            listModel.addElement(tagInfo);
        }
        if (listModel.isEmpty()) {
            listModel.addElement("No tags found");
        }
        return listModel;
    }
}

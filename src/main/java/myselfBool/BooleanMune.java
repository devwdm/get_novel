package myselfBool;

import org.jsoup.select.Elements;

import javax.swing.text.Document;
import java.util.List;

public class BooleanMune {

    public static boolean strIsNull(String str) {
        if (str == null || str.length() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean listIsNull(List list) {
        if (list == null || list.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean elementsIsNull(Elements elements) {
        if (elements == null || elements.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean documentIsNull(Document document) {
        if (document == null || document.getLength() <= 0) {
            return true;
        } else {
            return false;
        }
    }

}

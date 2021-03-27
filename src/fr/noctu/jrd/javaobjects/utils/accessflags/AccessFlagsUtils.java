package fr.noctu.jrd.javaobjects.utils.accessflags;

import java.util.ArrayList;

public class AccessFlagsUtils {
    public static ArrayList<FieldAccessFlags> getFieldAccessFlags(short accessFlags){
        ArrayList<FieldAccessFlags> returnList = new ArrayList<>();
        for (FieldAccessFlags value : FieldAccessFlags.values()) {
            if((accessFlags & value.getId()) != 0)
                returnList.add(value);
        }
        return returnList;
    }

    public static short buildFieldAccessFlags(FieldAccessFlags... accessFlags){
        short result = 0;
        for (FieldAccessFlags accessFlag : accessFlags) {
            result += accessFlag.getId();
        }
        return result;
    }

    public static ArrayList<MethodAccessFlags> getMethodAccessFlags(short accessFlags){
        ArrayList<MethodAccessFlags> returnList = new ArrayList<>();
        for (MethodAccessFlags value : MethodAccessFlags.values()) {
            if((accessFlags & value.getId()) != 0)
                returnList.add(value);
        }
        return returnList;
    }

    public static short buildMethoddAccessFlags(MethodAccessFlags... accessFlags){
        short result = 0;
        for (MethodAccessFlags accessFlag : accessFlags) {
            result += accessFlag.getId();
        }
        return result;
    }
}

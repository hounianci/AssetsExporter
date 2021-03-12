package com.common;

import java.io.InputStream;

public class StreamUtil {

    public static byte readByte(InputStream is) throws Exception{
        byte[] bb = new byte[1];
        is.read(bb);
        return bb[0];
    }
    public static boolean readBool(InputStream is) throws Exception{
        byte[] bb = new byte[1];
        is.read(bb);
        return bb[0]==1;
    }
    public static byte[] readBytes(InputStream is, int len) throws Exception{
        byte[] bb = new byte[len];
        is.read(bb);
        return bb;
    }
    public static short readShort(InputStream is) throws Exception{
        boolean bigEndian = true;
        if(is instanceof ArrayInputStream){
            bigEndian = ((ArrayInputStream)is).isBigEndian();
        }
        byte[] bb = new byte[2];
        is.read(bb);
        return (short) FileByteUtils.byteToShort(bb, bigEndian);
    }
    public static int readInt(InputStream is) throws Exception{
        boolean bigEndian = true;
        if(is instanceof ArrayInputStream){
            bigEndian = ((ArrayInputStream)is).isBigEndian();
        }
        byte[] bb = new byte[4];
        is.read(bb);
        return (int) FileByteUtils.byteToInt(bb, bigEndian);
    }
    public static long readLong(InputStream is) throws Exception{
        boolean bigEndian = true;
        if(is instanceof ArrayInputStream){
            bigEndian = ((ArrayInputStream)is).isBigEndian();
        }
        byte[] bb = new byte[8];
        is.read(bb);
        return (int) FileByteUtils.byteToLong(bb, bigEndian);
    }

    public static String readAlignedString(ArrayInputStream is) throws Exception {
        int len = readInt(is);
        byte[] data = readBytes(is, len);
        is.alignStream(4);
        return new String(data);
    }

    public static String readString(InputStream is) throws Exception{
        int b = 0;
        ByteArray array = new ByteArray();
        while((b=is.read())!=-1 && b!=0){
            array.addData((byte)b);
        }
        return new String(array.getData());
    }
    public static int[] readIntArray(ArrayInputStream is) throws Exception {
        int[] tmp = new int[readInt(is)];
        for(int i=0; i<tmp.length; i++){
            tmp[0] = readInt(is);
        }
        return tmp;
    }
}

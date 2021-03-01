package com.common;

import java.io.IOException;
import java.io.InputStream;

public class ArrayInputStream extends InputStream {
    private ByteArray data;
    private int idx;
    private boolean isBigEndian;

    public void alignStream(){
        alignStream(4);
    }

    public void alignStream(int alignment){
        int mod = idx%alignment;
        if(mod!=0){
            idx += (alignment-mod);
        }
    }

    public ArrayInputStream(byte[] b){
        data = new ByteArray(b);
        isBigEndian = true;
    }
    public int len(){
        return data.getDataLen();
    }
    @Override
    public int read() throws IOException {
        int t = data.getByteData(idx++)&0xff;
        return t;
    }
    public void setIdx(int idx){
        this.idx = idx;
    }

    public boolean isBigEndian() {
        return isBigEndian;
    }

    public void setBigEndian(boolean bigEndian) {
        isBigEndian = bigEndian;
    }
}

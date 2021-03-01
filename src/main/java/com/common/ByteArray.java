package com.common;

import java.util.Arrays;

public class ByteArray {
	private static final int DEF_INIT_LEN = 1024*10;
	private byte[] data;
	private int writeIdx;
	private int dataLen;
	public ByteArray() {
		this(DEF_INIT_LEN);
	}
	public ByteArray(byte[] data) {
		this.data = data;
		dataLen = data.length;
		writeIdx = 0;
	}
	public ByteArray(int initLen) {
		data = new byte[initLen];
		dataLen = 0;
		writeIdx = 0;
	}
	public byte getByteData(int index) {
		return data[index];
	}
	public void getByteData(int start, int len, byte[] dest) {
		System.arraycopy(data, start, dest, 0, len);
	}
	public void addData(byte b) {
		checkLen(1);
		data[writeIdx++] = b;
		dataLen++;
	}
	
	public void checkLen(int len) {
		while(writeIdx+len >= data.length) {
			grow();
		}
	}

	public boolean setInt(int offset, int value, boolean isBigEndian){
		if(offset+4 > dataLen){
			return false;
		}
		byte[] bb = FileByteUtils.intToByte(value, isBigEndian);
		for(int i=0; i<4; i++){
			data[offset+i] = bb[i];
		}
		return true;
	}

	public boolean setLong(int offset, long value, boolean isBigEndian){
		if(offset+8 > dataLen){
			return false;
		}
		byte[] bb = FileByteUtils.longToByte(value, isBigEndian);
		for(int i=0; i<8; i++){
			data[offset+i] = bb[i];
		}
		return true;
	}
	
	/**
	 * 
	 * @param b
	 * @param srcPos
	 * @param len
	 */
	public void addData(byte[] b, int srcPos, int len) {
		checkLen(len);
		System.arraycopy(b, srcPos, data, writeIdx, len);
		writeIdx += len;
		dataLen += len;
	}
	
	public void addData(byte[] b) {
		addData(b, 0, b.length);
	}
	
	public void grow() {
		int len = data.length==0?1024:data.length<<1;
		data = Arrays.copyOf(data, len);
	}
	
	public byte[] getData() {
		return Arrays.copyOf(data, dataLen);
	}
	public int getDataLen() {
		return dataLen;
	}
	public void setWriteIdx(int writeIdx) {
		this.writeIdx = writeIdx;
	}
}

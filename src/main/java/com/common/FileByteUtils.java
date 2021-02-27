package com.common;

import java.io.IOException;

public class FileByteUtils {
	
	public static void reverseData(int[] data) {
		int left=0, right=data.length-1;
		while(left<right) {
			int tmp = data[left];
			data[left] = data[right];
			data[right] = tmp;
			left++;right--;
		}
	}
	public static void reverseData(byte[] data) {
		int left=0, right=data.length-1;
		while(left<right) {
			byte tmp = data[left];
			data[left] = data[right];
			data[right] = tmp;
			left++;right--;
		}
	}

	/**
	 *
	 * @param val
	 * @param bigEndian 是否大端存储（最高位在最左边）
	 * @return
	 */
	public static byte[] intToByte(int val, boolean bigEndian){
		byte[] bb = new byte[4];
		for(int i=0; i<4; i++){
			byte b = (byte) (val & 0xff);
			bb[i] = b;
			val = (val>>>8);
		}
		if(bigEndian){
			reverseData(bb);
		}
		return bb;
	}
	/**
	 *
	 * @param val
	 * @param bigEndian 是否大端存储（最高位在最左边）
	 * @return
	 */
	public static byte[] longToByte(long val, boolean bigEndian){
		byte[] bb = new byte[8];
		for(int i=0; i<8; i++){
			byte b = (byte) (val & 0xff);
			bb[i] = b;
			val = (val>>>8);
		}
		if(bigEndian){
			reverseData(bb);
		}
		return bb;
	}

	public static long byteToInt(int[] data, boolean bigEndian) {
		long res = 0;
		if(bigEndian) {
			reverseData(data);
		}
		for(int i=0; i<4; i++) {
			long tmp = 0xff&data[i];
			res += tmp<<((3-i)*8);
		}
		return res;
	}
	
	public static long byteToLong(int[] data, boolean bigEndian) {
		long res = 0;
		if(bigEndian) {
			reverseData(data);
		}
		for(int i=0; i<8; i++) {
			int tmp = 0xff&data[i];
			res += tmp<<((7-i)*8);
		}
		return res;
	}
	
	public static int byteToShort(int[] data, boolean bigEndian) {
		int res = 0;
		if(bigEndian) {
			reverseData(data);
		}
		for(int i=0; i<2; i++) {
			int tmp = 0xff&data[i];
			res += tmp<<((1-i)*8);
		}
		return res;
	}
	
}

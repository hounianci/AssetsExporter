package com.analyze;

public class XorRecord {
    public static void main(String[] args) {
        int[] a = {0xf0,0x1b,0x1a,0x00,0xf0,0x1b,0x2e,0x00,
                0xe2,0x18,0x00,0x10};
        int[] b = {0x23,0x0a,0x6f,0xbb,0xef,0x60,0xd9,0x99,
                0x37,0xc3,0x99,0xcb};
        for(int i=0; i<a.length; i++){
            System.out.print("0x"+Integer.toHexString(a[i]^b[i])+" ");
        }
    }
}

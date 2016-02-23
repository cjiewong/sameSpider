package com.stone;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileEncodeReferee
{
    private File file;
    
    public FileEncodeReferee(File file)
    {
        this.file = file;
    }
    
    public FileEncodeReferee(String path)
    {
        file = new File(path);
    }
    
    public String getCharset()
    {
        File file = this.file;
        
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try
        {
            //boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
            {
                return charset;
            }
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE)
            {
                charset = "UTF-16LE";
                //checked = true;
            }
            else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF)
            {
                charset = "UTF-16BE";
                //checked = true;
            }
            else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF)
            {
                charset = "UTF-8";
                //checked = true;
            }
            /*******************************************************************
             * bis.reset(); if (!checked) { int loc = 0; while ((read =
             * bis.read()) != -1) { loc++; if (read >= 0xF0) { break; } if (0x80 <=
             * read && read <= 0xBF) // 单独出现BF以下的，也算是GBK { break; } if (0xC0 <=
             * read && read <= 0xDF) { read = bis.read(); if (0x80 <= read &&
             * read <= 0xBF)// 双字节 (0xC0 - 0xDF) { // (0x80 - 0xBF),也可能在GB编码内
             * continue; } else { break; } } else if (0xE0 <= read && read <=
             * 0xEF) { // 也有可能出错，但是几率较小 read = bis.read(); if (0x80 <= read &&
             * read <= 0xBF) { read = bis.read(); if (0x80 <= read && read <=
             * 0xBF) { charset = "UTF-8"; break; } else { break; } } else {
             * break; } } } System.out.println(loc + " " +
             * Integer.toHexString(read)); }
             ******************************************************************/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return charset;
    }
    
    public static void main(String[] args)
    {
        FileEncodeReferee fer = new FileEncodeReferee("E://Huha.csv");
        System.out.println(fer.getCharset());
    }
}
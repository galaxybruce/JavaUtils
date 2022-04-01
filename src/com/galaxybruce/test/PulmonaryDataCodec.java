package com.galaxybruce.test;


import com.galaxybruce.writebyte.BruteForceCoding;
import com.galaxybruce.writebyte.ByteUtils;
import sun.security.krb5.Checksum;

import java.util.Arrays;

/**
 * @date 2022/3/29 17:30
 * @author bruce.zhang
 * @description 肺活量数据编解码
 *
 *
 * 1 起始头标识位   1个字节 = 0x55
 * 2 长度			1个字节 = (字节数 3) 到 (字节数 n+1)
 * 3 项目编号		1个字节 = 8
 * 4 保留			1个字节
 * 5 命令 		    1个字节 = 3
 * 6 数据位1
 * ...
 * n 数据位n
 * n + 1 校验位 		1个字节 = ~(字节数2+  字节数3 + 字节数4 + ... + 字节数n)
 * n + 2 尾标志		1个字节 = 0xAA
 *
 * modification history:
 */
public class PulmonaryDataCodec {

   private static final String START_IDENTIFY = "55";
   private static final String END_IDENTIFY = "AA";

   public static void main(String[] args) {

      // 发送的数据
      byte[] data = new byte[1 + 1 + 1];
      int offset = BruteForceCoding.encodeIntBigEndian(data, 4567, 0, 2);
      BruteForceCoding.encodeIntBigEndian(data, 1, offset, 1);

      byte[] data1 = PulmonaryDataCodec.buildSendCmd(data);
//      byte[] data2 = PulmonaryDataCodec.buildSendCmd2(null);
      PulmonaryDataCodec.parseData(data1);
   }

   public static byte[] buildSendCmd2(byte[] dataBytes) {
      int dataLength = dataBytes != null ? dataBytes.length : 0;

      byte[] start = ByteUtils.hexStr2Bytes(START_IDENTIFY);
      final byte length = (byte)(1 + 1 + 1 + dataLength + 1);
      final byte projectNo = 8;
      final byte retain = 0;
      final byte cmd = 3;
      byte[] end = ByteUtils.hexStr2Bytes(END_IDENTIFY);

      byte[] data = ByteUtils.concat(start, ByteUtils.getBytes(Byte.toString(length)));
      data = ByteUtils.concat(data, ByteUtils.getBytes(Byte.toString(projectNo)));
      data = ByteUtils.concat(data, ByteUtils.getBytes(Byte.toString(retain)));
      data = ByteUtils.concat(data, ByteUtils.getBytes(Byte.toString(cmd)));
      if(dataLength > 0) {
         data = ByteUtils.concat(data, dataBytes);
      }
      // 校验位 	1个字节 = ~(字节数2+  字节数3 + 字节数4 + ... + 字节数n)
      int checkSum = 0;
      for (int i = 1; i < data.length; i++) {
         checkSum += data[i] & 0xFF;
      }
      checkSum = ~checkSum;
      data = ByteUtils.concat(data, new byte[]{(byte)checkSum});
      data = ByteUtils.concat(data, end);
      return data;
   }

   public static byte[] buildSendCmd(byte[] dataBytes) {
      int dataLength = dataBytes != null ? dataBytes.length : 0;

      byte[] start = ByteUtils.hexStr2Bytes(START_IDENTIFY);
      final byte length = (byte)(1 + 1 + 1 + dataLength + 1);
      final byte projectNo = 8;
      final byte retain = 0;
      final byte cmd = 3;
      byte[] end = ByteUtils.hexStr2Bytes(END_IDENTIFY);

      byte[] message = new byte[1 + 1 + 1 + 1 + 1 + dataLength + 1 + 1];
      message[0] = start[0];
      int offset = BruteForceCoding.encodeIntBigEndian(message, length, 1, 1);
      offset = BruteForceCoding.encodeIntBigEndian(message, projectNo, offset, 1);
      offset = BruteForceCoding.encodeIntBigEndian(message, retain, offset, 1);
      offset = BruteForceCoding.encodeIntBigEndian(message, cmd, offset, 1);
      if(dataLength > 0) {
         System.arraycopy(dataBytes, 0, message, offset, dataLength);
         offset = offset + dataLength;
      }

      // 校验位 	1个字节 = ~(字节数2+  字节数3 + 字节数4 + ... + 字节数n)
      int checkSum = 0;
      for (int i = 1; i < message.length; i++) {
         checkSum += message[i] & 0xFF;
      }
      checkSum = ~checkSum;
      offset = BruteForceCoding.encodeIntBigEndian(message, (byte)checkSum, offset, 1);
      message[offset] = end[0];
      return message;
   }

   public static void parseData(byte[] msgBytes) {
      if(msgBytes == null || msgBytes.length < 7) {
         return;
      }
//      final boolean startValid = START_IDENTIFY.equals(ByteUtils.bytes2HexStr(Arrays.copyOfRange(data, 0, 1)));
//      final boolean endValid = END_IDENTIFY.equals(ByteUtils.bytes2HexStr(Arrays.copyOfRange(data, data.length - 2, data.length - 1)));
//      final byte length = Byte.valueOf(data[1]).byteValue();

      byte start = (byte)BruteForceCoding.decodeIntBigEndian(msgBytes, 0, 1);
      byte end = (byte)BruteForceCoding.decodeIntBigEndian(msgBytes, msgBytes.length - 1, 1);
      byte checkSum = (byte)BruteForceCoding.decodeIntBigEndian(msgBytes, msgBytes.length - 2, 1);
      byte length = (byte)BruteForceCoding.decodeIntBigEndian(msgBytes, 1, 1);
      byte projectNo = (byte)BruteForceCoding.decodeIntBigEndian(msgBytes, 2, 1);
      byte cmd = (byte)BruteForceCoding.decodeIntBigEndian(msgBytes, 4, 1);
      long dataValue = BruteForceCoding.decodeIntBigEndian(msgBytes, 5, 2);
      byte dataStatus = (byte)BruteForceCoding.decodeIntBigEndian(msgBytes, 7, 1);

      // 校验位 	1个字节 = ~(字节数2+  字节数3 + 字节数4 + ... + 字节数n)
      int tCheckSum = 0;
      for (int i = 1; i < msgBytes.length - 2; i++) {
         tCheckSum += msgBytes[i] & 0xFF;
      }
      tCheckSum = ~tCheckSum;

      if(ByteUtils.hexStr2Bytes(START_IDENTIFY)[0] != start
              || ByteUtils.hexStr2Bytes(END_IDENTIFY)[0] != end
              || (byte)tCheckSum != checkSum
              || (byte)tCheckSum != checkSum
              || cmd != 3 || projectNo != 8
         ) {
         return;
      }


   }

   /**
    * 累加和校验，并取反
    */
   public static String makeCheckSum(String data) {
      if (data == null || data.equals("")) {
         return "";
      }
      int total = 0;
      int len = data.length();
      int num = 0;
      while (num < len) {
         String s = data.substring(num, num + 2);
         System.out.println(s);
         total += Integer.parseInt(s, 16);
         num = num + 2;
      }

      //用256求余最大是255，即16进制的FF
      int mod = total % 256;
      if (mod == 0) {
         return "FF";
      } else {
         String hex = Integer.toHexString(mod).toUpperCase();

         //十六进制数取反结果
         hex = parseHex2Opposite(hex);
         return hex;
      }
   }

   /**
    * 取反
    */
   public static String parseHex2Opposite(String str) {
      String hex;
      //十六进制转成二进制
      byte[] er = parseHexStr2Byte(str);

      //取反
      byte erBefore[] = new byte[er.length];
      for (int i = 0; i < er.length; i++) {
         erBefore[i] = (byte) ~er[i];
      }

      //二进制转成十六进制
      hex = parseByte2HexStr(erBefore);

      // 如果不够校验位的长度，补0,这里用的是两位校验
      hex = (hex.length() < 2 ? "0" + hex : hex);

      return hex;
   }

   /**
    * 将十六进制转换为二进制
    */
   public static byte[] parseHexStr2Byte(String hexStr) {
      if (hexStr.length() < 1) {
         return null;
      }
      byte[] result = new byte[hexStr.length() / 2];
      for (int i = 0; i < hexStr.length() / 2; i++) {
         int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
         int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
         result[i] = (byte) (high * 16 + low);
      }
      return result;
   }

   /**
    * 将二进制转换成十六进制
    */
   public static String parseByte2HexStr(byte buf[]) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < buf.length; i++) {
         String hex = Integer.toHexString(buf[i] & 0xFF);
         if (hex.length() == 1) {
            hex = '0' + hex;
         }
         sb.append(hex.toUpperCase());
      }
      return sb.toString();
   }

}

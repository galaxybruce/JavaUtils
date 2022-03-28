package com.galaxybruce.writebyte;

/**
 * @date 2022/3/25 17:43
 * @author bruce.zhang
 * @description 字节转换
 *
 * 注意：具体怎么解析，要看协议中的内容是高低位顺序
 *
 * <p>
 * modification history:
 */
public class ByteUtilsSimple {

    /**
     * @功能 短整型与字节的转换
     * @return 两位的字节数组
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = Integer.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * @功能 字节的转换与短整型
     * @return 短整型
     */
    public static short byteToShort(byte[] b) {
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s0 <<= 8;
        return (short) (s0 | s1);
    }

    /**
     * todo 具体怎么解析，要看协议中的内容是高低位顺序
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes ) {
        //如果不与0xff进行按位与操作，转换结果将出错，有兴趣的同学可以试一下。
        int int1 = bytes[0] & 0xff << 24;
        int int2 = (bytes[1] & 0xff) << 16;
        int int3 = (bytes[2] & 0xff) << 8;
        int int4 = (bytes[3] & 0xff) ;
        return int1 | int2 | int3 | int4;
    }

    /**
     * todo 具体怎么解析，要看协议中的内容是高低位顺序
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

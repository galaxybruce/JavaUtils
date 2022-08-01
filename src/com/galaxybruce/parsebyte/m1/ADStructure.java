package com.galaxybruce.parsebyte.m1;

/**
 * @author bruce.zhang
 * @date 2021/6/9 00:49
 * @description 字节数组解析出来的数据模型
 * <p>
 * modification history:
 */
public class ADStructure {
    public int length;
    public byte type;
    public byte[] data;

    public ADStructure(int length, byte type, byte[] data) {
        this.length = length;
        this.type = type;
        this.data = data;
    }

    public boolean isManufacturerSpecificData() {
        return type == (byte) 0xFF;
    }

    public boolean isTakeDevice() {
        return this.data[0] == (byte) 0x05 && this.data[1] == (byte) 0xFF;
    }

    public int getLength() {
        return length;
    }

    public byte getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public int getHeartRate() {
        if (data != null && data.length > 0 && isManufacturerSpecificData()) {
            return data[data.length - 1] & 0xFF;
        }
        return 0;
    }
}

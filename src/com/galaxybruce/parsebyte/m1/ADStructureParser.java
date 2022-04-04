package com.galaxybruce.parsebyte.m1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bruce.zhang
 * @date 2021/6/9 00:51
 * @description 根据协议长度递归解析数据
 * <p>
 * modification history:
 */
public class ADStructureParser {

    public static void parseData(byte[] dataBytes) {
        List<ADStructure> adStructureList = new ArrayList<>();
        getADStructureList(dataBytes, adStructureList);
    }

    private static void getADStructureList(byte[] dataBytes, List<ADStructure> adStructureList) {
        if (dataBytes == null || dataBytes.length == 0) {
            return;
        }
        int len = dataBytes[0] & 0xFF;
        if (dataBytes.length < len + 1 || len <= 0) {
            return;
        }
        byte type = dataBytes[1];
        byte[] data = Arrays.copyOfRange(dataBytes, 2, len + 1);

        ADStructure adStructure = new ADStructure(len, type, data);
        adStructureList.add(adStructure);

        byte[] dataBytesLeft = Arrays.copyOfRange(dataBytes, len + 1, dataBytes.length);

        getADStructureList(dataBytesLeft, adStructureList);
    }

}
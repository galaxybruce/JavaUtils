import com.galaxybruce.writebyte.ByteUtilsSimple;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) {
        // 新建数组
        // byte[][] array = new byte[1][1];
        // byte[][] array = new byte[][] {{1, 1}, {1, 2}};

        List<Integer> list  = new ArrayList<>();
        System.out.println(list.toString());
    }

    private void dateFormat() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.format(new Date(System.currentTimeMillis()));
    }

    private static int calCount(long n) {
        if(n < 0) {
            return 0;
        }
        
        int count = 0;
        long i = 0;
        String value;
        while(i <= n) {
            value = Long.toString(i * i);
            if(value.endsWith(Long.toString(i))) {
                System.out.println(value + "--" + i);
                count++;
            }
            i++;
        }

        return count;
    }

}

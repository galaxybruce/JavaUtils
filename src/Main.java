import com.galaxybruce.writebyte.ByteUtilsSimple;

public class Main {

    public static void main(String[] args) {

        byte[] b = ByteUtilsSimple.shortToByte(new Integer(2).shortValue());
        short a = ByteUtilsSimple.byteToShort(b);
        System.out.println(a);
    }

}

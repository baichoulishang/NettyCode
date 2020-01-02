package io.netty.channel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 陈宜康
 * @date 2020/1/2 20:53
 * @forWhat
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        System.out.println(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse("2019-12-30 11:11:11"));
    }
}

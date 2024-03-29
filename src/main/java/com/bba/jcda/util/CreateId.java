package com.bba.jcda.util;

import java.text.SimpleDateFormat;

/**
 * @Author bcmaidou
 * @Date 2019/3/13 0013 11:22
 */
public class CreateId {

    /**
     * 20位末尾的数字id
     */
    public static int Guid = 100;

    public static String getGuid() {
        CreateId.Guid += 1;
        long now = System.currentTimeMillis();
        //获取4位年份数字
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        //获取时间戳
        String time = dateFormat.format(now);
        String info = now + "";
        //获取三位随机数
        //int ran=(int) ((Math.random()*9+1)*100);
        //要是一段时间内的数据连过大会有重复的情况，所以做以下修改
        int ran = 0;
        if (CreateId.Guid > 999) {
            CreateId.Guid = 100;
        }
        ran = CreateId.Guid;

        return time + info.substring(2, info.length()) + ran;
    }

    public static void main(String[]args){

        String guid = CreateId.getGuid();
        System.out.println(guid.substring(guid.length() - 7));
    }
}

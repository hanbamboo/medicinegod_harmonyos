package com.daqin.medicinegod.utils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.app.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class util extends AbilitySlice {
    public static int getWindowHeightPx(Context context) {
        return context.getResourceManager().getDeviceCapability().height * context.getResourceManager().getDeviceCapability().screenDensity / 160;
    }
    public static int getWindowWidthPx(Context context) {
        return context.getResourceManager().getDeviceCapability().width * context.getResourceManager().getDeviceCapability().screenDensity / 160;
    }
    public static int isTimeOut(String timeA,String timeB){
        /**
         * @param timeA 第一个时间
         * @param timeB 第二个时间
         * @param return 0为临期；-1过期；1未过期;
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int res = 0;
        try {
            //将日期转成Date对象作比较
            Date fomatDate1 = dateFormat.parse(timeA);
            Date fomatDate2 = dateFormat.parse(timeB);

            Long time1 =  fomatDate1.getTime();
            Long time2 =  fomatDate2.getTime();

            int day = (int) ((time1 - time2) / (24*3600*1000));

            if (day <= 0){
                res = -1;
            }else if(day <= 60){
                res = 0;
            }else {
                res = 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
}

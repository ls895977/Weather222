package com.naran.Utils;

/**
 * Created by darhandarhad on 2017/11/23.
 */

public class StringUtil {
    public static boolean isEmpty(String str){

        if(null==str||"".equals(str)||"null".equals(str)){
            return true;
        }
        return false;
    }
}

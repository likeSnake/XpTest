package com.jcl.xptest.constants;

public class Constants {

    public static String DaMaiYId = "720319036434";
    public static String DaMaiPNum = "1";
    public static String DaMaiPId = "5192856207093";

    public static String LinShi = "717413939954_1_5175962414348";

    public static String IDS = DaMaiYId+"_"+DaMaiPNum+"_"+DaMaiPId;

    public static String DaMai_Build_URL = "https://mtop.damai.cn/gw/mtop.trade.order.build/4.0/";
    public static String DaMai_Crate_URL = "https://mtop.damai.cn/gw/mtop.trade.order.create/4.0/";


    public static String MtopRequestData = "{\"buyNow\":\"true\",\"buyParam\":\"" +LinShi+"\",\"exParams\":\"{\\\"atomSplit\\\":\\\"1\\\",\\\"channel\\\":\\\"damai_app\\\",\\\"coVersion\\\":\\\"2.0\\\",\\\"coupon\\\":\\\"true\\\",\\\"seatInfo\\\":\\\"\\\",\\\"umpChannel\\\":\\\"10001\\\",\\\"websiteLanguage\\\":\\\"zh_CN\\\"}\"}";

}

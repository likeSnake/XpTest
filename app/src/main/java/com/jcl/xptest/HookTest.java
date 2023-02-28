package com.jcl.xptest;

import static com.jcl.xptest.constants.Constants.DaMai_Build_URL;
import static com.jcl.xptest.constants.Constants.DaMai_Crate_URL;
import static com.jcl.xptest.constants.Constants.MtopRequestData;
import static com.jcl.xptest.utli.HttpUtils.sendPostRequest;
import static com.jcl.xptest.utli.MyUtil.MyLog;
import static com.jcl.xptest.utli.MyUtil.MyToast;
import static com.jcl.xptest.utli.MyUtil.addNewLine;
import static com.jcl.xptest.utli.MyUtil.compress;
import static com.jcl.xptest.utli.MyUtil.myCompress;
;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jcl.xptest.pojo.AppInfo;
import com.jcl.xptest.pojo.DaMaiCreate;
import com.jcl.xptest.utli.HttpUtils;
import com.jcl.xptest.utli.MyUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookTest implements IXposedHookLoadPackage, IXposedHookZygoteInit {


    private ArrayList<AppInfo> all;
    public static Context context;
    public static boolean isFirst = true;
    private ArrayList<String> allName;
    private static Object sLiveDataInstance;



    public void loadWindow(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                String activityName = activity.getClass().getName();
                MyLog("Current Activity: " + activityName);


                if (activityName.equals("cn.damai.trade.newtradeorder.ui.projectdetail.ui.activity.ProjectDetailActivity")){
                      //  startFloatingWindow();

                }
            }
        });
    }

    public void hookAll(XC_LoadPackage.LoadPackageParam lpparam){

            XposedHelpers.findAndHookMethod(
                    "*", lpparam.classLoader, ".*",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            try {
                                  // 打印方法名称和路径
                                 MyLog("All-->" + "Method: " + param.method.getName() + " --Path: " + param.method.toString());
                            }catch (Throwable e) {
                                MyLog("All报错："+e);
                            }
                        }
                    });
      /*  XC_MethodHook methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                MyLog("Hooked method: " + param.method.getName() + " in class: " + param.method.getDeclaringClass().getName());
            }
        };

        try {
            XposedBridge.hookAllMethods(Class.forName("java.lang.Class"), "forName", methodHook);
            XposedBridge.hookAllConstructors(Class.forName("java.lang.ClassLoader"), methodHook);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/

    }
    private String getCallingClassName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            if (stackTraceElements[i].getMethodName().equals("setData")) {
                if (i + 2 < stackTraceElements.length) {
                    return stackTraceElements[i + 2].getClassName();
                }
            }
        }
        return "";
    }
    public void MyDaMaiTest(XC_LoadPackage.LoadPackageParam loadPackageParam){


        XposedHelpers.findAndHookMethod("mtopsdk.mtop.domain.MtopRequest", loadPackageParam.classLoader, "setData", java.lang.String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String str = (String) param.args[0];
                String callingClassName = getCallingClassName();
                MyUtil.MyLog("MtopRequest: "+str);
                MyUtil.MyLog("callingClassName: "+callingClassName);

            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

            }
        });

       /* XposedHelpers.findAndHookConstructor("androidx.lifecycle.LiveData", loadPackageParam.classLoader,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                sLiveDataInstance = param.thisObject;
                super.afterHookedMethod(param);
            //    MyUtil.MyLog("androidx.lifecycle.LiveData调用");
            }
        });

        XposedHelpers.findAndHookMethod("androidx.lifecycle.LiveData$1", loadPackageParam.classLoader, "run", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                try{
                    MyUtil.MyLog("androidx.lifecycle.LiveData$1调用");

                    Object mPendingData;
                    Object extra;
                    Object what;
                    Object value;

                    mPendingData = XposedHelpers.getObjectField(sLiveDataInstance, "mPendingData");
                    MyUtil.MyLog("mPendingData--->"+mPendingData.getClass().getName());

                    extra = XposedHelpers.getObjectField(mPendingData, "extra");
                    MyUtil.MyLog("extra--->"+extra);

                    what = XposedHelpers.getObjectField(extra, "what");
                    MyUtil.MyLog("what--->"+what);

                    value = XposedHelpers.getObjectField(extra, "value");
                    MyUtil.MyLog("value--->"+value);
                    // 在这里处理mPendingData的值
                    // ...




                }catch (Throwable e){
                    MyUtil.MyLog("Exception--->"+e);
                }
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);



            }
        });*/

    }


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {


        if (loadPackageParam.packageName.equals("cn.damai")) {
            MyLog("进入hook");
            // runShell(loadPackageParam.packageName);
            MyDaMaiTest(loadPackageParam);
            XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    context = (Context) param.thisObject;
                    Activity activity = (Activity) param.thisObject;
                    String activityName = activity.getClass().getName();
                    MyLog("Current Activity: " + activityName);
                    if (isFirst){
                        MyToast(context,"Hook已加载");
                        isFirst = false;
                    }

                    if (activityName.equals("cn.damai.trade.newtradeorder.ui.projectdetail.ui.activity.ProjectDetailActivity")||
                            activityName.equals("cn.damai.homepage.v2.ChannelPageActivity")) {
                        initBuild(loadPackageParam);

                        //hookAll(loadPackageParam);

                    }



                }
            });
        }

        if (loadPackageParam.packageName.equals("com.jcl.androidtest")){
            // 首先获取MtopRequest类
            try {

                Class<?> mtopRequestClass = XposedHelpers.findClass("com.jcl.androidtest.BTest", loadPackageParam.classLoader);

                // 构造MtopRequest实例
                Object mtopRequest = mtopRequestClass.newInstance();

                Object listener = XposedHelpers.getObjectField(mtopRequest,"myId");


                // 设置MtopRequest的属性值
                XposedHelpers.callMethod(mtopRequest, "SaiHello");
            }catch (Throwable  e){
                MyLog(e);
            }
        }
    }

    public Object crateMtopRequest(XC_LoadPackage.LoadPackageParam loadPackageParam,String Data){
        try{
            // 首先获取MtopRequest类
            Class<?> mtopRequestClass = XposedHelpers.findClass("mtopsdk.mtop.domain.MtopRequest", loadPackageParam.classLoader);
            // 构造MtopRequest实例
            Object mtopRequest = mtopRequestClass.newInstance();
            // 设置MtopRequest的属性值
            XposedHelpers.callMethod(mtopRequest, "setApiName", "mtop.trade.order.build");
            XposedHelpers.callMethod(mtopRequest, "setData", Data);
            XposedHelpers.callMethod(mtopRequest, "setNeedEcode", true);
            XposedHelpers.callMethod(mtopRequest, "setNeedSession", true);
            XposedHelpers.callMethod(mtopRequest, "setVersion", "4.0");
            // 打印MtopRequest实例
            MyLog("MtopRequest-->"+mtopRequest.toString());

            return mtopRequest;
        }catch (Exception e){
            MyLog("crateMtopRequest报错："+e);
            return null;
        }
    }

    public Object crateBuildParams(XC_LoadPackage.LoadPackageParam loadPackageParam,String Data){
        try{

            Object mtopRequest = crateMtopRequest(loadPackageParam,Data);

            // 引入 Java 中的类
            Class<?> MethodEnum = XposedHelpers.findClass("mtopsdk.mtop.domain.MethodEnum", loadPackageParam.classLoader);
            Class<?> MtopListenerProxyFactory = XposedHelpers.findClass("com.taobao.tao.remotebusiness.listener.MtopListenerProxyFactory", loadPackageParam.classLoader);
            Class<?> ApiID = XposedHelpers.findClass("mtopsdk.mtop.common.ApiID", loadPackageParam.classLoader);
            Class<?> MtopStatistics = XposedHelpers.findClass("mtopsdk.mtop.util.MtopStatistics", loadPackageParam.classLoader);
            Class<?> InnerProtocolParamBuilderImpl = XposedHelpers.findClass("mtopsdk.mtop.protocol.builder.impl.InnerProtocolParamBuilderImpl", loadPackageParam.classLoader);


            // 首先获取MtopRequest类
            Class<?> MtopBusiness = XposedHelpers.findClass("com.taobao.tao.remotebusiness.MtopBusiness", loadPackageParam.classLoader);
            Object build1 = XposedHelpers.callStaticMethod(MtopBusiness,"build",mtopRequest);



            XposedHelpers.callMethod(build1, "useWua");
            XposedHelpers.callMethod(build1, "reqMethod", MethodEnum.getField("POST").get(null));
            XposedHelpers.callMethod(build1, "setCustomDomain", "mtop.damai.cn");
            XposedHelpers.callMethod(build1, "setBizId", 24);
            XposedHelpers.callMethod(build1, "setErrorNotifyAfterCache", true);
            XposedHelpers.setLongField(build1, "reqStartTime", System.currentTimeMillis());
            XposedHelpers.setBooleanField(build1, "isCancelled", false);
            XposedHelpers.setBooleanField(build1, "isCached", false);
            XposedHelpers.setObjectField(build1, "clazz", null);
            XposedHelpers.setIntField(build1, "requestType", 0);
            XposedHelpers.setObjectField(build1, "requestContext", null);
            XposedHelpers.callMethod(build1, "mtopCommitStatData", false);
            XposedHelpers.setLongField(build1, "sendStartTime",System.currentTimeMillis());


            Object listener = XposedHelpers.getObjectField(build1,"listener");

            Class<?> mMtopListener = XposedHelpers.findClass("mtopsdk.mtop.intf.MtopBuilder", loadPackageParam.classLoader);
            Method createListenerProxy = XposedHelpers.findMethodExact(mMtopListener, "createListenerProxy", "mtopsdk.mtop.common.MtopListener");
            // 绕过访问限制
            createListenerProxy.setAccessible(true);
            Object ListenerProxy = createListenerProxy.invoke(build1,listener);


            //Object ListenerProxy = XposedHelpers.callMethod(build1, "createListenerProxy", listener);
            Object CMtopContext = XposedHelpers.callMethod(build1, "createMtopContext", ListenerProxy);
            Object mtopStatistics = XposedHelpers.newInstance(MtopStatistics, (Object) null, null);
            MyLog("mtopStatistics:"+mtopStatistics);
            XposedHelpers.setObjectField(CMtopContext,"stats",mtopStatistics);
            XposedHelpers.setObjectField(build1,"mtopContext",CMtopContext);

            Object apiid = XposedHelpers.newInstance(ApiID, (Object) null, CMtopContext);
            MyLog("apiid:"+apiid);
            XposedHelpers.setObjectField(CMtopContext,"apiId",apiid);
            Object mCMtopContext = CMtopContext;


            XposedHelpers.setObjectField(mCMtopContext,"mtopRequest",mtopRequest);
            Object Ipp = XposedHelpers.newInstance(InnerProtocolParamBuilderImpl);

            Object buildParams = XposedHelpers.callMethod(Ipp, "buildParams", mCMtopContext);
            if (buildParams!=null) {
            }else {
                MyLog("crateBuildParams报错：buildParams为null !");
            }
            return buildParams;
        }catch (Exception e){
            MyLog("crateBuildParams："+e);
            return null;
        }
    }
    public void initBuild(XC_LoadPackage.LoadPackageParam loadPackageParam){

        try {

            Object buildParams = crateBuildParams(loadPackageParam,MtopRequestData);
            if (buildParams!=null) {

                Map<String, String> map = (Map<String, String>) buildParams;

                String data = map.get("data");
                String wua = map.get("wua");
                Map<String, String> parameters = BuildParameters(map);

                String data_encode = URLEncoder.encode(data, "UTF-8");
                String wua_encode = URLEncoder.encode(wua, "UTF-8");
                String requestBody = "wua=" + wua_encode + "&data=" + data_encode;
                MyLog(requestBody);
                MyLog("BuildParameters-->"+parameters);

                //请求build
                sendPostRequest(DaMai_Build_URL, requestBody, parameters, new HttpUtils.HttpCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog("请求成功返回值:" + result);

                        ((Activity)(context)).runOnUiThread(()->{
                            MyToast(context, "build请求成功");
                        });

                        initCrate(result,loadPackageParam);

                    }

                    @Override
                    public void onFailure(String failure) {
                        MyLog("请求失败：" + failure);
                    }
                });
          /*  sendGetRequest(DaMai_Build_URL, requestBody, parameters, new HttpUtils.HttpCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    MyLog("Get请求成功返回值:"+result);
                }

                @Override
                public void onFailure(String failure) {
                    MyLog("Get请求失败："+failure);
                }
            });*/
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    sendPost(DaMai_Build_URL, requestBody, parameters, new HttpUtils.HttpCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            MyLog("请求成功返回值:"+result);
                        }

                        @Override
                        public void onFailure(String failure) {
                            MyLog("请求失败："+failure);
                        }
                    });
                }
            }).start();*/
            }else {
                MyLog("initBuild中buildParams为null" );
            }
        }catch (Throwable  e){
            MyLog(e);
        }
    }

    public static String removeSpaces(String s){
        String replace = s.replace(" ","");
        return replace;
    }
    public void initCrate(String result,XC_LoadPackage.LoadPackageParam loadPackageParam){
        try {

            String replace = removeSpaces(result);


            String crateDamai = getCrate(replace);
            String daMaiCrateDate = "{\"feature\":\"{\\\"gzip\\\":\\\"true\\\"}\",\"params\":\""+crateDamai+"\"}";

            MyLog("daMaiCrateDate-->"+daMaiCrateDate);

            Object buildParams = crateBuildParams(loadPackageParam,daMaiCrateDate);
            if (buildParams!=null) {


                Map<String, String> map = (Map<String, String>) buildParams;

                String data = map.get("data");

                String wua = map.get("wua");
                Map<String, String> crateParameters = BuildParameters(map);

                String data_encode = URLEncoder.encode(data, "UTF-8");
                String wua_encode = URLEncoder.encode(wua, "UTF-8");
                String crateRequestBody = "wua=" + wua_encode + "&data=" + data_encode;
                MyLog("crateRequestBody-->"+crateRequestBody);

                MyLog("crateParameters:-->" + crateParameters);
                MyLog("停顿两秒--");

                Thread.sleep(2000);

                sendPostRequest(DaMai_Crate_URL, crateRequestBody, crateParameters, new HttpUtils.HttpCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog("crate请求成功返回值:" + result);

                        ((Activity)(context)).runOnUiThread(()->{
                            MyToast(context, "crate请求成功");
                        });

                      //  initCrate(result,loadPackageParam);

                    }

                    @Override
                    public void onFailure(String failure) {
                        MyLog("请求失败：" + failure);
                    }
                });
            }

        }catch (Throwable  e){
            MyLog(e);
        }


    }
    public String getCrate(String result){
        try {
            JSONObject  js = new JSONObject(result);
            JSONObject allData = js.getJSONObject("data");
            JSONObject data = allData.getJSONObject("data");

            JSONObject newJson = new JSONObject();
            Iterator<String> keys = data.keys();
            allName = new ArrayList<>();
            while(keys.hasNext()) {
                String key = keys.next();

                if (key.startsWith("dmViewer_")){

                    //选择观演人数
                    data.getJSONObject(key).getJSONObject("fields").put("selectedNum","1");


                    for (int i = 0; i < data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").length(); i++) {
                        String viewerName = data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).getString("viewerName");

                        allName.add(viewerName);

                        //去除多余
                        data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).remove("maskedIdentityNo");
                        //更改字段
                        String idType = data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).getString("idType");
                        data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("idType",Integer.parseInt(idType));


                        //新增字段
                        data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("isDisabled","false");//翻译过来是是否禁用，暂不清楚作用
                        data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("disabled",false);
                        data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("seatId","0");//座位id

                        //购票实名制选择,默认第一个
                        //asyncTarget
                        if (i == 0) {
                            data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("used", true);
                            data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("isUsed", "true");
                        }else {
                            data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("used",false);
                            data.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(i).put("isUsed","false");
                        }

                    }
                }

                if (
                        key.startsWith("dmContactName_")||key.startsWith("confirmOrder_")||key.startsWith("dmContactEmail_")
                                ||key.startsWith("dmContactPhone_")||key.startsWith("dmDeliveryAddress_")||key.startsWith("dmDeliverySelectCard_")
                                ||key.startsWith("dmEttributesHiddenBlock_")||key.startsWith("dmPayType_")||key.startsWith("dmViewer_")||key.startsWith("item_")

                ) {
                    JSONObject obj = data.getJSONObject(key);
                    newJson.put(key, obj);

                }

                if(key.startsWith("dmPayType_")){
                    data.getJSONObject(key).getJSONObject("fields").put("cornerType","bottom");
                    for (int i = 0; i < data.getJSONObject(key).getJSONObject("fields").getJSONArray("paytypeList").length(); i++) {
                        String icon = data.getJSONObject(key).getJSONObject("fields").getJSONArray("paytypeList").getJSONObject(i).getString("icon");
                        MyLog("icon URL-->"+icon);
                    }
                }


            }
            MyLog("观影人数信息："+ allName);

            JSONObject endpoint = allData.getJSONObject("endpoint");
            JSONObject hierarchy = allData.getJSONObject("hierarchy");
            hierarchy.remove("root");

            JSONObject linkage = allData.getJSONObject("linkage");
            linkage.remove("input");
            linkage.remove("request");

            JSONObject common = linkage.getJSONObject("common");
            common.remove("queryParams");
            common.remove("structures");
            common.remove("structures");
            common.put("compress",true);


            JSONObject newData = new JSONObject();
            newData.put("data", newJson);
            newData.put("endpoint", endpoint);
            newData.put("hierarchy", hierarchy);
            newData.put("linkage", linkage);



           /* JSONObject data2 = newData.getJSONObject("data");
            Iterator<String> kk = data2.keys();
            while(kk.hasNext()) {
                String key = kk.next();
                if (key.startsWith("dmViewer_")) {
                    MyLog("used--"+data2.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(0).getString("used"));
                    MyLog("isUsed--"+data2.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(0).getString("isUsed"));
                    MyLog("seatId--"+data2.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(0).getString("seatId"));
                    MyLog("isDisabled--"+data2.getJSONObject(key).getJSONObject("fields").getJSONArray("viewerList").getJSONObject(0).getString("isDisabled"));
                }
            }*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String myData = newData.toString();

                // 转义字符串
              //  String escapedString = StringEscapeUtils.escapeJava(originalString);
                myData = myData.replaceAll("\\\\/", "/");


                String s1 = myCompress(myData);

            //    String s2 = "H4SIAAAAAAAAAN" + s1.substring(14);

              //  String crateBase = compress(newData.toString());
                if (s1!=null) {
                //    String s = addNewLine(s1);
                    return s1;
                }else {
                    return null;
                }
            }else {
                if (context!=null){

                    MyToast(context,"手机系统版本过低，不兼容--"+ Build.VERSION.SDK_INT);
                }
                return null;
            }

        } catch (JSONException e) {
            MyLog("JSONException-->"+e);
            return null;
        }
    }
    public Map<String,String> BuildParameters(Map<String,String> map) throws UnsupportedEncodingException {


        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value == null) {
                MyLog("警告！！！："+key + " 为 null");
            }
        }

        String x_sid = map.get("sid");
        String x_uid = map.get("uid");
        String x_pv = map.get("pv");
        String x_nq = map.get("nq");
        String x_features = map.get("x-features");
        String x_app_conf_v = map.get("x-app-conf-v");
        String f_refer = map.get("f-refer");
     //   String x_ttid = map.get("ttid");
        String x_app_ver = map.get("x-app-ver");
        String x_c_traceid = map.get("x-c-traceid");
        String x_appKey = map.get("appKey");
        String x_t = map.get("t");

        String x_ttid = map.get("ttid") != null ? URLEncoder.encode(map.get("ttid"), "UTF-8") : null;

        String x_umt = map.get("umt") != null ? URLEncoder.encode(map.get("umt"), "UTF-8") : null;
        String x_sign = map.get("sign") != null ? URLEncoder.encode(map.get("sign"), "UTF-8") : null;
        String user_agent = map.get("user-agent") != null ? URLEncoder.encode(map.get("user-agent"), "UTF-8") : null;
        String x_utdid = map.get("utdid") != null ? URLEncoder.encode(map.get("utdid"), "UTF-8") : null;
        String x_mini_wua = map.get("x-mini-wua") != null ? URLEncoder.encode(map.get("x-mini-wua"), "UTF-8") : null;
        String x_sgext = map.get("x-sgext") != null ? URLEncoder.encode(map.get("x-sgext"), "UTF-8") : null;



        Map<String,String> reMap =new HashMap<>();
        reMap.put("content-type","application/x-www-form-urlencoded;charset\\u003dUTF-8");
        reMap.put("Content-Type","application/x-www-form-urlencoded;charset\\u003dUTF-8");
        reMap.put("x-bx-version","6.5.91");
        reMap.put("x-sign",x_sign);
        reMap.put("x-nettype","WIFI");
        reMap.put("x-sid",x_sid);
        reMap.put("x-uid",x_uid);
        reMap.put("x-pv",x_pv);
        reMap.put("x-nq",x_nq);
        reMap.put("x-sgext",x_sgext);
        reMap.put("x-features",x_features);
        reMap.put("x-app-conf-v",x_app_conf_v);
        reMap.put("x-mini-wua",x_mini_wua);
        reMap.put("f-refer",f_refer);
        reMap.put("x-ttid",x_ttid);
        reMap.put("x-app-ver",x_app_ver);
        //reMap.put("x-location","104.097982%2C30.534218");
        //reMap.put("x-c-traceid",x_c_traceid);
        reMap.put("x-c-traceid","Ykzu6pQkmGoDAJG%2BfHdOvCuw16866258436461583129735");
        reMap.put("x-umt",x_umt);
        reMap.put("x-utdid",x_utdid);
        reMap.put("x-appkey",x_appKey);
        reMap.put("x-t",x_t);
        reMap.put("user-agent",user_agent);
        reMap.put("Connection","Keep-Alive");
        //reMap.put("Accept-Encoding","gzip");
        reMap.put("Host","mtop.damai.cn");

/* val intent = Intent(service as Context, mainContext)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    val pendingIntent = PendingIntent.getActivity(service, 0, intent, 0)*/

        return reMap;
    }

    public void runShell(String packageName){
        try {
                // 构建命令
              /*  String[] command = {"su", "-c", "pm grant " + packageName + " android.permission.SYSTEM_ALERT_WINDOW"};

                // 执行命令
                Process process = Runtime.getRuntime().exec(command);*/

            String cmd = "cmd package set-permissions "+packageName+" android.permission.ACTION_MANAGE_OVERLAY_PERMISSION";
            String cmds = "pm grant "+packageName+" android.permission.SYSTEM_ALERT_WINDOW";

        //    executeRootCommand(cmds);


        } catch (Exception e) {
            MyLog("IOException->"+e);
        }

    }

    public static void executeRootCommand(String command)  {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();

            // 获取命令的输出结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String result = sb.toString();
            MyUtil.MyLog("result-->"+result);
        } catch (Exception e) {
            MyUtil.MyLog("GrantPermission-->"+e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

        MyLog("进入initZygote-->");
        try{
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.internal.policy.impl.PhoneWindowManager",this.getClass().getClassLoader()),"checkAddPermission",new XC_MethodHook(){
            public void beforeHookedMethod(MethodHookParam param){
                if(param.args[0] instanceof WindowManager.LayoutParams){
                    WindowManager.LayoutParams params=(WindowManager.LayoutParams)param.args[0];
                    if(params.type==WindowManager.LayoutParams.TYPE_SYSTEM_ERROR){
                        param.setResult(0);//当检测到是系统错误对话框时，返回0，即ok！
                    }
                }
            }
        });
        }catch(Throwable t){
            MyLog("hookSysApi-->"+Log.getStackTraceString(t));
        }
    }
}

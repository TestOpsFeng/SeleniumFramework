package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于读取properties文件时，可以：
 *   1. get(): 使用变量${}
 *   2. getTranslate(key,null): 获取当前年月日
 *   3. getTranslate(key,map):把map中的value转入properties文件key中。
 */
public class PropertiesUtil {
    private static Properties props;
    private PropertiesUtil(){}
    private static final String PROPERTIES = "config.properties";
    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");

    static{
        try {
            props = new Properties();
            InputStream ins = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES);
            props.load(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTranslate(String prop, Map<String, String> params){
        String path = get(prop);
        if(path==null||path.equals("")){
            return "";
        }
        if(params!=null && !params.keySet().isEmpty()){
            //需要替换的字段均放入map中，包括需要替换的日期
            for(String key : params.keySet()){
                path = path.replace("{"+key+"}", params.get(key));
            }
        }

        //如不指定日期，则替换为当前日期
        Calendar cal = Calendar.getInstance();
        path = path.replace("{year}", cal.get(Calendar.YEAR)+"")
                .replace("{month}", cal.get(Calendar.MONTH)+1>9?cal.get(Calendar.MONTH)+1+"":"0"+(cal.get(Calendar.MONTH)+1))
                .replace("{date}", cal.get(Calendar.DATE)>9?cal.get(Calendar.DATE)+"":"0"+cal.get(Calendar.DATE));

        return path;
    }

    public static String get(String prop){
        String value = props.getProperty(prop);
        return value==null?null:loop(value);
    }

    public static boolean getBool(String prop){
        String value = props.getProperty(prop);
        return Boolean.parseBoolean(value==null?null:loop(value));
    }

    @SuppressWarnings("static-access")
    private static String loop(String key){
        //定义matcher匹配其中的路径变量
        Matcher matcher = PATTERN.matcher(key);
        StringBuffer buffer = new StringBuffer();
        boolean flag = false;
        while (matcher.find()) {
            String matcherKey = matcher.group(1);//依次替换匹配到的路径变量
            String matchervalue = props.getProperty(matcherKey);
            if (matchervalue != null) {
                matcher.appendReplacement(buffer, matcher.quoteReplacement(matchervalue));//quoteReplacement方法对字符串中特殊字符进行转化
                flag = true;
            }
        }
        matcher.appendTail(buffer);
        //flag为false时说明已经匹配不到路径变量，则不需要再递归查找
        return flag?loop(buffer.toString()):key;
    }
    public static void main(String[]  args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Mydate","Mydate444");
        map.put("Mymonth","Mymonth444");
        map.put("Myyear","Myyear444");
        System.out.println("    [host] "+PropertiesUtil.get("host"));
        System.out.println("    [login_url] "+PropertiesUtil.getTranslate("login_url",null));
        System.out.println("    [login_url] "+PropertiesUtil.getTranslate("login_url",map));
    }
}
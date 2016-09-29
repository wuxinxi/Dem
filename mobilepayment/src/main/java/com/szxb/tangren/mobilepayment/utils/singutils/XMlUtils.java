package com.szxb.tangren.mobilepayment.utils.singutils;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class XMlUtils {
    /**
     * 解析xml
     *
     * @param content
     * @return 下午2:46:27
     * @author TangRen
     */
    public static Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("xml解析出现异常", e.toString());
        }
        return null;

    }

    // 新方法：将SortedMad 转换成XML
    public static String changeMapToXml(SortedMap<Object, Object> param) {

        Set<Map.Entry<Object, Object>> set = param.entrySet();
        Iterator<Map.Entry<Object, Object>> it = set.iterator();
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<xml>");
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            sBuilder.append("<" + entry.getKey() + ">");
            sBuilder.append(entry.getValue());
            sBuilder.append("</" + entry.getKey() + ">");

        }
        sBuilder.append("</xml>");
        try {
            return new String(sBuilder.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }
        return "";
    }

}

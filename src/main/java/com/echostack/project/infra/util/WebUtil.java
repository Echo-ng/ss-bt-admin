package com.echostack.project.infra.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.echostack.project.component.wapper.BodyReaderHttpServletRequestWrapper;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * @Author: Echo
 * @Date: 2019/4/2 18:14
 * @Description:
 */
public class WebUtil {

    public static String getParamByJsonRequest(HttpServletRequest httpServletRequest,String parameter) throws IOException {
        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        //从json中获取username和password
        String body = null;
        String result =  "";
        try {
            body = StreamUtils.copyToString(requestWrapper.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username = null, password = null;
        if(StringUtils.hasText(body)) {
            JSONObject jsonObj = JSON.parseObject(body);
            result = jsonObj.getString(parameter);
        }
        return result;
    }

    public static <T> T getParamByJsonRequest(HttpServletRequest httpServletRequest,Class<T> tClass) throws IOException, IllegalAccessException, InstantiationException {
        T instance = tClass.newInstance();
        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        //从json中获取username和password
        String body = null;
        String result =  "";
        try {
            body = StreamUtils.copyToString(requestWrapper.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = JSON.parseObject(body,tClass);
        return instance;
    }

    public static String createCode(Integer length,String source){
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < length; j++)
        {
            flag.append(source.charAt(rand.nextInt(source.length()-1)) + "");
        }
        return flag.toString();
    }
}

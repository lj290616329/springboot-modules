package com.tsingtec.tsingweb.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONTokener;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * XSS过滤处理
 *
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Whitelist whitelist = createWhitelist();

    private static final OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    private byte[] requestBody;
    private Charset charSet;

    private static Whitelist createWhitelist() {
        return Whitelist.relaxed()
                .removeProtocols("a", "href", "ftp", "http", "https", "mailto")
                .removeProtocols("img", "src", "http", "https")

                .addAttributes("a", "href", "title", "target")  // 官方默认会将 target 给过滤掉
                /**
                 * 在 Whitelist.relaxed() 之外添加额外的白名单规则
                 */
                .addTags("div", "span", "embed", "object", "param")
                .addAttributes(":all", "style", "class", "id", "name")
                .addAttributes("object", "width", "height", "classid", "codebase")
                .addAttributes("param", "name", "value")
                .addAttributes("embed", "src", "quality", "width", "height", "allowFullScreen", "allowScriptAccess", "flashvars", "name", "type", "pluginspage");
    }

    private static String[] filter(String[] values) {
        if (values != null) {
            for (int i = 0, len = values.length; i < len; i++) {
                if (values[i] != null && !"".equals(values[i])) {
                    values[i] = Jsoup.clean(values[i], "", whitelist, outputSettings).trim();
                }
            }
        }
        return values;
    }

    private static String filter(String value) {
        if (value != null) {
            value = Jsoup.clean(value, "", whitelist, outputSettings).trim();
        }
        return value;
    }

    /**
     * @param request
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        //缓存请求body
        try {
            String requestBodyStr = getRequestPostStr(request);
            if (!StringUtils.isEmpty(requestBodyStr)) {
                requestBodyStr = filter(requestBodyStr);
                Object json = new JSONTokener(requestBodyStr).nextValue();
                requestBody = json.toString().getBytes(charSet);
            } else {
                requestBody = new byte[0];
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        String charSetStr = request.getCharacterEncoding();
        if (charSetStr == null) {
            charSetStr = "UTF-8";
        }
        charSet = Charset.forName(charSetStr);

        return StreamUtils.copyToString(request.getInputStream(), charSet);
    }

    public ServletInputStream getInputStream() {
        if (requestBody == null) {
            requestBody = new byte[0];
        }

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }
}
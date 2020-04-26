package com.tsingtec.tsingweb.config.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@ServerEndpoint("/websocket/{uid}")
@Component
public class WebSocketServer {
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String sessionid = "";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam(value = "uid") String uid) {
        this.session = session;
        this.sessionid = uid;
        if(webSocketMap.containsKey(sessionid)){
            webSocketMap.remove(sessionid);
            webSocketMap.put(sessionid,this);
            //加入set中
        }else{
            webSocketMap.put(sessionid,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+sessionid+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+sessionid+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(sessionid)){
            webSocketMap.remove(sessionid);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+sessionid+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+sessionid+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(!StringUtils.isEmpty(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId",this.sessionid);
                String toUserId=jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if(!StringUtils.isEmpty(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else{
                    log.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.sessionid+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 遍历群发消息
     * 保持不掉线
     * @param text
     */
    public static void send(String text)  throws IOException {
        for (Map.Entry<String, WebSocketServer> entry : webSocketMap.entrySet()) {
            webSocketMap.get(entry.getKey()).sendMessage(text);
        }
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sessionid") String sessionid) throws IOException {
        log.info("发送消息到:"+sessionid+"，报文:"+message);
        if(!StringUtils.isEmpty(sessionid)&&webSocketMap.containsKey(sessionid)){
            webSocketMap.get(sessionid).sendMessage(message);
        }else{
            log.error("用户"+sessionid+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
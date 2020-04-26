package com.tsingtec.tsingweb.aop.start;

import lombok.Data;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 启动项目进行备份数据库
 * 此方法只针对福海项目
 * 正确的是应该采用定时器进行定时进行备份数据库
 */
@Data
//@Component
//@ConfigurationProperties(prefix = "dbback")
public class StartupListener implements ApplicationRunner{

    private String savePath;
    private String userName;
    private String password;
    private String mysqlPath;
    private String databaseName;
    private String port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }
        int if_success = 0;
        String OS = System.getProperty("os.name");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = databaseName+sdf.format(new Date())+".sql";
        String hostIP = "127.0.0.1";
        StringBuilder stringBuilder = new StringBuilder();
        if(OS.toLowerCase().indexOf("windows")>=0) {
            stringBuilder.append(" "+mysqlPath+"/bin/mysqldump");
        }else {
            stringBuilder.append(""+mysqlPath+"/bin/mysqldump");
        }
        stringBuilder.append(" --opt").append(" -h").append(hostIP).append(" -P").append(port);
        stringBuilder.append(" --user=").append(userName).append(" --password=").append(password).append(" --lock-all-tables=true");
        stringBuilder.append(" --result-file=").append(savePath+fileName).append(" --default-character-set=utf8 ").append(databaseName);
        stringBuilder.append(" --ignore-table=emai_test.emai_db");
        if(OS.toLowerCase().indexOf("windows")>=0) {
            /**
             * windows数据库备份
             */
            try {
                Process process = Runtime.getRuntime().exec(stringBuilder.toString());
                if(process.waitFor() == 0){//0 表示线程正常终止。
                    System.out.println("数据库备份成功！！！");
                }else {
                    if_success = process.waitFor();
                    System.out.println("数据库备份失败！！！"+if_success);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            /**
             * linux 数据库备份
             */
            try{
                /**
                 * /usr/bin/sh 和 -c首尾 一定不要加空格啊
                 */
                String[] cmd=new String[]{"/usr/bin/sh","-c",stringBuilder.toString()};
                Process process = Runtime.getRuntime().exec(cmd);
                if(process.waitFor() == 0){//0 表示线程正常终止。
                    System.out.println("数据库备份成功！！！");
                }else {
                    if_success = process.waitFor();
                    System.out.println("数据库备份失败！！！"+if_success);
                }
            }catch(Exception e){
                System.out.println(e.toString());
            }
        }
    }
}
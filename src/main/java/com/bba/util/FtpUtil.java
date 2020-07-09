package com.bba.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl;
import org.apache.commons.net.ftp.parser.FTPTimestampParserImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * FTP传输工具类
 * @author fanx
 */
public class FtpUtil {
    private static final String host = PropertyConfig.getProperty("ftp.ip");
    private static final int port = Integer.valueOf(PropertyConfig.getProperty("ftp.port"));
    private static final String username = PropertyConfig.getProperty("ftp.username");
    private static final String password = PropertyConfig.getProperty("ftp.password");

    /**
     * 获取FTPClient对象
     * @return FTPClient
     */
    public static FTPClient getFTPClient() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(host, port);// 连接FTP服务器
            ftpClient.login(username, password);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                System.out.println("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /**
     * 从FTP服务器下载文件
     * @param ftpPath FTP服务器中文件所在路径
     * @param localPath 下载到本地的位置 格式：H:/download
     * @param fileName 文件名称
     * 示例：FtpUtil.downloadFtpFile("bokee", "F:\\aaa", "aaa.pdf");
     * 参数1：FTP服务器中文件所在路径，bokee是根节点下的子节点
     * 参数2：保存到本地磁盘的位置，如F:\\ 盘
     * 参数3：下载的文件名
     */
    public static void downloadFtpFile(String ftpPath, String localPath, String fileName) {
        FTPClient ftpClient = getFTPClient();
        try {
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);

            File localFile = new File(localPath + File.separatorChar + fileName);
            OutputStream os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(fileName, os);
            os.close();
            ftpClient.logout();

        } catch (FileNotFoundException e) {
            System.out.println("没有找到" + ftpPath + "文件");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误。");
            e.printStackTrace();
        }

    }

    /**
     * 向FTP服务器上传文件
     * @param ftpPath  ftp服务器文件路径
     * @param fileName ftp文件名称
     * @param input 文件流
     * @return 成功返回true，否则返回false
     * 示例：FtpUtil.uploadFile(File.separatorChar + "", "aaa.pdf",
     *                 new FileInputStream(new File("F:\\ReferenceCard.pdf")));
     * 参数1：根节点路径，如果你下面有子节点a写入a即可
     * 参数2：上传到服务器的文件名称
     * 参数3：InputStream对象
     */
    public static boolean uploadFile(String ftpPath, String fileName, InputStream input) {
        boolean success = false;
        FTPClient ftpClient = getFTPClient();
        try {
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return success;
            }
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);
            ftpClient.storeFile(fileName, input);
            ftpClient.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 删除FTP服务器指定文件
     * @param pathName FTP服务器保存目录
     * @param fileName 要删除的文件名称
     * @return
     * 示例：FtpUtil.deleteFile("bokee", "aaa.pdf")
     * 参数1：根节点下的子节点bokee
     * 参数2：需要删除的文件名
     */
    public static boolean deleteFile(String pathName, String fileName){
        boolean success = false;
        FTPClient ftpClient = getFTPClient();
        try {
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathName);
            ftpClient.dele(fileName);
            ftpClient.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 有bug，暂无好的方法解决
     * ftp文件是否存在
     * @param path ftp路径
     * @return true or false
     * 示例：FtpUtil.existFile("bokee");
     * 参数1：需要判断的文件路径，true(存在) or false(不存在)
     */
    public static boolean existFile(String path) {
        FTPClient ftpClient = getFTPClient();
        FTPFile[] files = null;
        try {
            files = ftpClient.listFiles(path);//此处因为windows编码格式的原因无法获取列表信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files != null && files.length > 0 ? true : false;
    }

    /**
     * 创建目录
     * @param folderName 创建的目录名称
     * return 是否创建成功(true or false)
     * 示例：FtpUtil.makeDirectory("testFile");
     * 参数1：在根节点下创建的目录名称
     */
    public static boolean makeDirectory(String folderName) {
        boolean success = true;
        FTPClient ftpClient = getFTPClient();
        try {
            // 创建文件夹，返回值true(成功) or false(失败)
            success = ftpClient.makeDirectory(folderName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
     * @param folderName 文件夹名称
     * return 是否创建成功(true or false)
     * 示例：FtpUtil.createDirecroty("wechat" + File.separatorChar + "autographImages");
     * 参数1：创建多级文件夹名称，如wechat/autographImages
     */
    public static boolean createDirecroty(String folderName) {
        boolean success = true;
        String[] paths = folderName.replace(File.separatorChar + ""
                , " ").split("\\s+");
        StringBuffer create_path = new StringBuffer();
        for(String path : paths){
            create_path.append(path + File.separatorChar);
            success = makeDirectory(create_path + "");
//            if(!FtpUtil.existFile(create_path.substring(0, create_path.length() - 1))){
//                //如果路径不存在，则创建路径
//                success = makeDirectory(create_path + "");
//                System.out.println("文件不存在，执行创建操作");
//            } else {
//                System.out.println("文件已存在，不执行创建操作");
//            }
        }
        return success;
    }

    /**
     * 读取配置文件类
     */
    public static class PropertyConfig{
        public static Properties properties;

        private PropertyConfig(){
            System.out.println(properties);
        }

        static { loadProps(); }

        private synchronized static void loadProps(){
            properties = new Properties();
            InputStream in = null;
            try {
                // 第一种，通过类加载器进行获取properties文件流
                in = PropertyConfig.class.getClassLoader().getResourceAsStream("ftp.properties");
                // 第二种，通过类进行获取properties文件流
                //in = PropertyConfig.class.getResourceAsStream("/ftp.properties");
                properties.load(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(in != null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public static String getProperty(String key){
            if(null == properties) {
                loadProps();
            }
            return properties.getProperty(key);
        }

        public static String getProperty(String key, String defaultValue) {
            if(null == properties) {
                loadProps();
            }
            return properties.getProperty(key, defaultValue);
        }
    }

    public static void main(String[] args) throws FileNotFoundException{

//        //创建文件夹
//        FtpUtil.makeDirectory("niuTMS");

//        //创建多级文件夹
//        System.out.println(FtpUtil.createDirecroty("niuTMS"
//                + File.separatorChar + "goodsImages"));

//        //上传一个文件
//        FtpUtil.uploadFile("wechat" + File.separatorChar + "autographImages", "aaa.pdf",
//                new FileInputStream(new File("F:\\ReferenceCard.pdf")));

        //删除文件
//        System.out.println(FtpUtil.deleteFile("bokee"
//                ,"aaa.pdf"));

//        //判断文件路径是否存在
//        System.out.println(FtpUtil.existFile("bokee1"));

//        //在FTP服务器上生成一个文件，并将一个字符串写入到该文件中
//        try {
//            InputStream input = new ByteArrayInputStream("test ftp jyf".getBytes("GBK"));
//            boolean flag = FtpUtil.uploadFile(ftpPath, fileName,input);;
//            System.out.println(flag);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//        //下载一个文件
//        FtpUtil.downloadFtpFile("bokee", "F:\\aaa", "aaa.pdf");
    }
}

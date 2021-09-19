package util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 读写txt文件的工具类
 */
public class TxtIOUtils {

    /**
     * 读出txt文件
     * 将txt文件内容转化为 String字符串输出
     * @param file 文件对象
     * @return 文件内容
     */
    public static String readTxt(File file) throws IOException {
        String str = "";
        String strLine;
        // 将 txt文件按行读入 str中
        FileInputStream fileInputStream;
        if(!file.exists())
            throw new FileNotFoundException(file.getName()+"文件不存在！");
        try {
            fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 字符串拼接
            while ((strLine = bufferedReader.readLine()) != null) {
                str += strLine;
            }
            // 关闭资源
            inputStreamReader.close();
            bufferedReader.close();
            fileInputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 写入txt文件
     * 传入内容、文件绝对路径名，将内容写入文件并换行
     * @param str 待写入字符内容
     * @param txtPath 写入文件路径
     */
    public static void writeTxt(String str,String txtPath) throws IOException {
        File file = new File(txtPath);
        FileWriter fileWriter;
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!file.canWrite())
            throw new FileNotFoundException(file.getName()+"文件已存在且不可写！");
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(str);
            fileWriter.write("\r\n");
            // 关闭资源
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

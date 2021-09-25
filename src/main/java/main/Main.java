package main;

import service.Article;
import service.CompareTask;
import tool.TextException;
import  tool.TextTools;
import util.TxtIOUtils;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, TextException {
        long start = System.currentTimeMillis();
        Article a1 = TextTools.getArticle(args[0]);
        Article a2 = TextTools.getArticle(args[1]);
        String resultFilePath = args[2];
        CompareTask task = new CompareTask(a1, a2);
        task.execute();
        TxtIOUtils.writeTxt(task.getCompareReport().toString(), resultFilePath);
        long end = System.currentTimeMillis();
        System.out.println("完成！耗时 "+ (end - start) +" 毫秒。");
        System.out.println(task.getCompareReport().toString());
        System.exit(0);
    }
}

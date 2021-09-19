import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.seg.common.Term;
import main.Main;
import service.Article;
import service.CompareReport;
import service.CompareTask;
import service.WordFreq;
import tool.TextException;
import tool.TextTools;

import org.junit.Test;
import util.TxtIOUtils;

public class ProcessTest {

	private static String root = "3119005373/";

	@Test
	/*
	  测试分词
	 */
	public void segmentTest() {
		String str = "今天是星期天，天气晴，今天晚上我要去看电影。";
		List<Term> segmentList = TextTools.getSegmentList(str);
		System.out.println(segmentList);
	}

	@Test
	/*
	  测试统计词频
	 */
	public void wordFreqTest() {
		String str = "今天是星期天，天气晴，今天晚上我要去看电影。";
		List<Term> segmentList = TextTools.getSegmentList(str);
		List<WordFreq> wordFreqList = TextTools.getWordFrequency(segmentList);
		System.out.println(wordFreqList);
	}

	@Test
	/*
	  测试getArticle
	 */
	public void getArticleTest() throws IOException, TextException {
		Article article = TextTools.getArticle(root + "test_orig.txt");
		System.out.println(article.getName());
		System.out.println(article.getText());
		System.out.println(article.getSegmentList());
		System.out.println(article.getWordFreqList());
	}

	@Test
	/*
	  测试CompareTask和CompareReport
	 */
	public void compareTest() throws IOException, TextException {
		// TODO 涉及读写可用多线程FutureTask优化。
		Article a1 = TextTools.getArticle(root + "test_orig.txt");
		Article a2 = TextTools.getArticle(root + "test_plag.txt");
		CompareTask task = new CompareTask(a1, a2);
		task.execute();
		CompareReport report = task.getCompareReport();
		System.out.println(report);
	}

	@Test
	/*
	  报告多个测试
	 */
	public void compareMultiTest() throws IOException, TextException {
		List<CompareTask> tasks = new ArrayList<>();

		Article a1 = TextTools.getArticle(root + "orig.txt");
		Article b1 = TextTools.getArticle(root + "orig_0.8_add.txt");
		tasks.add(new CompareTask(a1, b1));
		System.out.println("load 1");

		Article a2 = TextTools.getArticle(root + "orig.txt");
		Article b2 = TextTools.getArticle(root + "orig_0.8_del.txt");
		tasks.add(new CompareTask(a2, b2));
		System.out.println("load 2");

		Article a3 = TextTools.getArticle(root + "orig.txt");
		Article b3 = TextTools.getArticle(root + "orig_0.8_dis_1.txt");
		tasks.add(new CompareTask(a3, b3));
		System.out.println("load 3");

		Article a4 = TextTools.getArticle(root + "orig.txt");
		Article b4 = TextTools.getArticle(root + "orig_0.8_dis_10.txt");
		tasks.add(new CompareTask(a4, b4));
		System.out.println("load 4");

		Article a5 = TextTools.getArticle(root + "orig.txt");
		Article b5 = TextTools.getArticle(root + "orig_0.8_dis_15.txt");
		tasks.add(new CompareTask(a5, b5));
		System.out.println("load 5");

		for (CompareTask task : tasks) {
			task.execute();
			System.out.println(task.getCompareReport().toString());
			System.out.println();
		}
	}

	@Test
	/*
	  测试文本文件不存在的异常
	 */
	public void fileNotExistTest() throws TextException {
		try {
			Article article = TextTools.getArticle(root + "test.txt");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	/*
	  测试文本内容为空的异常
	 */
	public void emptyTextTest() throws IOException {
		try {
			Article article = TextTools.getArticle(root + "test_empty.txt");
		}catch (TextException e) {
			e.printStackTrace();
		}
	}

	@Test
	/*
	  测试答案文件已存在且不可写的异常
	 */
	public void fileReadOnlyTest() {
		try {
			String str = "写不进去啊";
			TxtIOUtils.writeTxt(str, root+"test_readonly.txt");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}

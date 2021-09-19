package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 用于表示比较两篇文章的任务。 设置了任务状态。 可查看比较的结果。
 */
public class CompareTask {

	private Article a1;
	private Article a2;

	private boolean isFinished;

	private CompareReport compareReport;

	public CompareTask(Article a1, Article a2) {
		this.a1 = a1;
		this.a2 = a2;
		this.isFinished = false;
	}

	public void execute() {
		List<WordFreq> freq1 = a1.getWordFreqList();
		List<WordFreq> freq2 = a2.getWordFreqList();
		// 获取词汇并集
		ArrayList<WordFreq> union = new ArrayList<>(freq1);
		union.addAll(freq2);
		int totalWordFreq = 0; //总词频
		// 生成向量
		ArrayList<WordFreq> v1 = new ArrayList<>(freq1);
		ArrayList<WordFreq> v2 = new ArrayList<>(freq2);
		for (WordFreq wf : union) {
			// 向两个向量添加非共有词，并设词频为0
			if (!v1.contains(wf)) {
				v1.add(new WordFreq(wf.getWord(), 0));
			}
			if (!v2.contains(wf)) {
				v2.add(new WordFreq(wf.getWord(), 0));
			}
			// 计算总词频
			totalWordFreq += wf.getFreq();
		}
		// 根据词语排序以对齐向量，方便计算
		Comparator<WordFreq> strComp = new Comparator<WordFreq>() {
			public int compare(WordFreq a, WordFreq b) {
				return a.getWord().compareTo(b.getWord());
			}
		};
		v1.sort(strComp);
		v2.sort(strComp);

		//TF-IDF加权，求广义Jaccard系数
		double vproduct = 0;
		int sumSquare1 = 0;
		int sumSquare2 = 0;
		for (int i = 0; i < v1.size(); ++i) {
			// 获取IF
			int f1 = v1.get(i).getFreq();
			int f2 = v2.get(i).getFreq();
			// 计算IDF
			double idf = Math.abs(Math.log(totalWordFreq/(f1+f2)));
			// 计算权重IF*IDF
			double num1 = f1 * idf;
			double num2 = f2 * idf;
			// 向量点积
			vproduct += num1 * num2;
			// 求向量模的平方
			sumSquare1 += num1 * num1;
			sumSquare2 += num2 * num2;
		}
		// 广义Jaccard系数
		double similarity = vproduct / (sumSquare1 + sumSquare2 - vproduct);

		// 标记完成状态
		isFinished = true;
		makeReport(a1, a2, similarity);
	}

	private void makeReport(Article a1, Article a2, double similarity) {
		compareReport = new CompareReport(a1, a2, similarity);

	}

	public boolean isFinished() {
		return isFinished;
	}

	public CompareReport getCompareReport() {
		return compareReport;
	}

}

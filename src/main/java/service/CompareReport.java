package service;

public class CompareReport {
	private Article a1;
	private Article a2;

	private double similarity;

	public CompareReport(Article a1, Article a2, double similarity) {
		this.a1 = a1;
		this.a2 = a2;
		this.similarity = similarity;
	}

	@Override
	/*
	  返回比较结果的简单报告。
	 */
	public String toString() {
		return String.format("原文文件：%s\n抄袭版论文文件：%s\n重复率：%.2f%%", a1.getName(), a2.getName(), similarity*100);
	}

}

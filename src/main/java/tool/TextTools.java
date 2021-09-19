package tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import service.Article;
import service.WordFreq;
import util.TxtIOUtils;

public class TextTools {

	/**
	 * 从文件中获取数据，封装成对象。会将词频排序，在多个任务的情况下性能得到优化
	 * @param txtPath txt文件绝对路径
	 * @return Article对象
	 */
	public static Article getArticle(String txtPath) throws IOException, TextException {
		File file = new File(txtPath);
		String text = TxtIOUtils.readTxt(file);
		if(text.isEmpty())
			throw new TextException(file.getName()+"文本为空！");
		List<Term> segmentList = getSegmentList(text);
		List<WordFreq> wordFreqList = getWordFrequency(segmentList);
		// 词频从高到低排序
		Comparator<WordFreq> comp = new Comparator<WordFreq>() {
			public int compare(WordFreq a, WordFreq b) {
				if (a.getFreq() < b.getFreq()) {
					return 1;
				} else if (a.getFreq() > b.getFreq()) {
					return -1;
				} else {
					return 0;
				}
			}
		};
		wordFreqList.sort(comp);
		// 封装Article
		Article article = new Article();
		article.setName(file.getName());
		article.setText(text);
		article.setSegmentList(segmentList);
		article.setWordFreqList(wordFreqList);
		return article;
	}

	/**
	 * 将输入的字符串分词处理。
	 * @param text 文本
	 * @return 切分后的单词
	 */
	public static List<Term> getSegmentList(String text) {
		List<Term> segmentList = HanLP.segment(text);
		// 过滤器
		segmentList.removeIf(new Predicate<Term>() {
			/**
			 * 过滤掉：标点符号、代码
			 */
			public boolean test(Term term) {
				boolean flag = false;
				// 类型
				// 词性以w开头的，为各种标点符号
				if (term.nature.startsWith('w')) {
					flag = true;
				}
				// 过滤掉代码
				if (term.nature.equals(Nature.nx)) {// 字母专名
					flag = true;
				}
				return flag;
			}
		});
		return segmentList;
	}

	/**
	 * 根据分词集合统计词频
	 * @param segmentList 词汇集合
	 * @return 词频集合
	 */
	public static List<WordFreq> getWordFrequency(List<Term> segmentList) {
		// 统计词频
		Multiset<String> wordSet = HashMultiset.create();
		for (Term term : segmentList) {// 放入词汇集合
			wordSet.add(term.word);
		}
		// 从词汇集合取出单词和频次,放入词频集合
		List<WordFreq> wfList = new ArrayList<>();
		for (Entry<String> entry : wordSet.entrySet()) {
			wfList.add(new WordFreq(entry.getElement(), entry.getCount()));
		}
		return wfList;
	}

}

package com.tcc.common.wechat.material.model;

import java.util.List;

/**                    
 * @Filename News.java
 *
 * @Description 群发图文
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class News {
	
	private List<Article> articles;
	
	public List<Article> getArticles() {
		return articles;
	}
	
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	
}

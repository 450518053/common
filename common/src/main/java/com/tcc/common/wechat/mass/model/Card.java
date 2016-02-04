package com.tcc.common.wechat.mass.model;

/**                    
 * @Filename Card.java
 *
 * @Description 卡券消息
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class Card {
	
	private String card_id;
	
	/**
	 * 构建一个<code>Card.java</code>
	 */
	public Card() {
		super();
	}
	
	/**
	 * 构建一个<code>Card.java</code>
	 * @param card_id
	 */
	public Card(String card_id) {
		super();
		this.card_id = card_id;
	}
	
	public String getCard_id() {
		return card_id;
	}
	
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	
}

package com.tcc.common.webcrawl.sz.model;

/**                    
 * @Filename FundAccount.java
 *
 * @Description 公积金账户信息
 *
 * @author tcc 2015-12-18
 *
 */
public class FundAccount {
	
	private String	cardNum;	//身份证号
								
	private String	accNum;		//个人公积金账号
								
	private String	ssNum;		//人社保电脑号
								
	private double	balance;	//账户余额
								
	private double	sbbalance;	//社保移交金额
								
	private String	state;		//状态
								
	public String getCardNum() {
		return cardNum;
	}
	
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	public String getAccNum() {
		return accNum;
	}
	
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	
	public String getSsNum() {
		return ssNum;
	}
	
	public void setSsNum(String ssNum) {
		this.ssNum = ssNum;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getSbbalance() {
		return sbbalance;
	}
	
	public void setSbbalance(double sbbalance) {
		this.sbbalance = sbbalance;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
}

package com.tcc.common.webcrawl.base.model;

/**                    
 * @Filename Template.java
 *
 * @Description 模板
 *
 * @author tcc 2015-12-11
 *
 */
public class Template {
	
	private String	result;		//结果
									
	private int[][]	tempArray;		//模板二维数组
									
	private int		correctCount;	//模板二维数组有多少个1
									
	private int		width;			//宽
									
	private int		height;		//高
									
	/**
	 * 构建一个<code>Template.java</code>
	 */
	public Template() {
		super();
	}
	
	/**
	 * 构建一个<code>Template.java</code>
	 * @param result
	 * @param correctCount
	 */
	public Template(String result, int correctCount) {
		super();
		this.result = result;
		this.correctCount = correctCount;
	}
	
	/**
	 * 构建一个<code>Template.java</code>
	 * @param result
	 * @param tempArray
	 */
	public Template(String result, int[][] tempArray) {
		super();
		this.result = result;
		this.tempArray = tempArray;
		this.height = tempArray.length;
		this.width = tempArray[0].length;
		int count = 0;
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				if (tempArray[i][j] == 1) {
					count++;
				}
			}
		}
		this.correctCount = count;
	}
	
	/**
	 * 构建一个<code>Template.java</code>
	 * @param result
	 * @param tempArray
	 * @param correctCount
	 */
	public Template(String result, int[][] tempArray, int correctCount) {
		super();
		this.result = result;
		this.tempArray = tempArray;
		this.correctCount = correctCount;
		this.height = tempArray.length;
		this.width = tempArray[0].length;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int[][] getTempArray() {
		return tempArray;
	}
	
	public void setTempArray(int[][] tempArray) {
		this.tempArray = tempArray;
	}
	
	public int getCorrectCount() {
		return correctCount;
	}
	
	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
}

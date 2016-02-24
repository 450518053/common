package com.tcc.common.webcrawl.base.model;

/**                    
 * @Filename Piexl.java
 *
 * @Description 像素
 *
 * @author tcc 2015-12-17
 *
 */
public class Piexl {
	
	private int	X;
	
	private int	Y;
	
	/**
	 * 构建一个<code>Piexl.java</code>
	 */
	public Piexl() {
		super();
	}
	
	/**
	 * 构建一个<code>Piexl.java</code>
	 * @param x
	 * @param y
	 */
	public Piexl(int x, int y) {
		super();
		X = x;
		Y = y;
	}
	
	public int getX() {
		return X;
	}
	
	public void setX(int x) {
		X = x;
	}
	
	public int getY() {
		return Y;
	}
	
	public void setY(int y) {
		Y = y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + X;
		result = prime * result + Y;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piexl other = (Piexl) obj;
		if (X != other.X)
			return false;
		if (Y != other.Y)
			return false;
		return true;
	}
	
}

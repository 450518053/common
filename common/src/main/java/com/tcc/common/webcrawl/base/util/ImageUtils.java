package com.tcc.common.webcrawl.base.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.Validate;

import com.tcc.common.webcrawl.base.model.Template;

/**
 * @Filename ImageUtils.java
 *
 * @Description 图片处理工具类
 *
 * @author tcc 2015-12-15
 *
 */
public class ImageUtils {
	
	/**
	 * 白色rgb值
	 */
	private static final int	BLACK_RGB	= Color.BLACK.getRGB();
	
	/**
	 * 黑色rgb值
	 */
	private static final int	WHITE_RGB	= Color.WHITE.getRGB();
	
	/**
	 * 去灰度
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage grayImage(BufferedImage image) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				grayImage.setRGB(i, j, image.getRGB(i, j));
			}
		}
		return grayImage;
	}
	
	/**
	 * 二值化
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage binaryImage(BufferedImage image) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage binaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				binaryImage.setRGB(i, j, rgb);
			}
		}
		return binaryImage;
	}
	
	/**
	 * rgb平均值大于阀值
	 * @param rgb rgb值
	 * @param criticalValue 阀值
	 * @return true 白 false 黑
	 */
	public static boolean isWhite(int rgb, int criticalValue) {
		return !isBlack(rgb, criticalValue);
	}
	
	/**
	 * rgb平均值小于阀值
	 * @param rgb rgb值
	 * @param criticalValue 阀值
	 * @return true 黑 false 白
	 */
	public static boolean isBlack(int rgb, int criticalValue) {
		return getAvgRGB(rgb) < criticalValue;
	}
	
	/**
	 * 获取rgb合
	 * @param rgb
	 * @return
	 */
	public static int getSumRGB(int rgb) {
		Color color = new Color(rgb);
		return color.getRed() + color.getBlue() + color.getGreen();
	}
	
	/**
	 * 获取rgb平均值
	 * @param rgb
	 * @return
	 */
	public static int getAvgRGB(int rgb) {
		return getSumRGB(rgb) / 3;
	}
	
	/**
	 * 二维int数组转图片
	 * @param intArray 0视为白色，其他值视为黑色
	 * @param width 图片宽
	 * @param height 图片高
	 * @return
	 */
	public static BufferedImage intArrayToImg(int[][] intArray, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (intArray[y][x] == 0) {
					img.setRGB(x, y, WHITE_RGB);
				} else {
					img.setRGB(x, y, BLACK_RGB);
				}
			}
		}
		return img;
	}
	
	/**
	 * 二维int数组转img，图片宽高由二维int数组决定
	 * @param intArray 0视为白色，其他值视为黑色
	 * @return
	 */
	public static BufferedImage intArrayToImg(int[][] intArray) {
		return intArrayToImg(intArray, intArray[0].length, intArray.length);
	}
	
	/**
	 * 对二维int数组横纵坐标切割，剔除值只有0的横纵
	 * @param intArray 只包含一个模版
	 * @return
	 */
	public static int[][] clear(int[][] intArray) {
		int width = intArray[0].length;
		int height = intArray.length;
		int startX = 0;
		int startY = 0;
		int endX = 0;
		int endY = 0;
		boolean flag = false;
		boolean isStart = true;
		for (int x = 0; x < width; x++) {
			boolean hasBlack = false;
			for (int y = 0; y < height; y++) {
				if (intArray[y][x] == 1) {
					hasBlack = true;//此列有黑色块
					if (flag) {
						if (startY > y) {
							startY = y;
						}
						if (y > endY) {
							endY = y;
						}
					} else {
						startX = x;
						startY = y;
						endX = x;
						endY = y;
						flag = true;//开始查找边界
					}
				} else {
					if (flag) {
						if (!hasBlack && (y == height - 1)) {//最后一格是白色且该列无黑色块
							isStart = false;//结束
						}
					}
				}
			}
			if (hasBlack) {
				endX = x;
			}
			if (!isStart) {
				break;
			}
		}
		//		System.out.println("startX:" + startX);
		//		System.out.println("startY:" + startY);
		//		System.out.println("endX:" + endX);
		//		System.out.println("endY:" + endY);
		int[][] newInt = new int[endY - startY + 1][endX - startX + 1];
		//复制
		for (int i = 0; i < newInt.length; i++) {
			System.arraycopy(intArray[startY], startX, newInt[i], 0, newInt[i].length);
			startY++;
		}
		//				for (int[] aa : newInt) {
		//					System.out.println(Arrays.toString(aa));
		//				}
		//				System.out.println();
		return newInt;
	}
	
	/**
	 * 读取指定目录下的模板图片
	 * 		转换成解析验证码所需的模板，图片名字为其模板值
	 * @param dir
	 * @param criticalValue 临界值
	 * @return
	 * @throws IOException
	 */
	public static List<Template> getTemplateList(File dir, int criticalValue) throws IOException {
		// System.out.println(dir.getAbsolutePath());
		Validate.isTrue(dir.exists(), "文件'" + dir.getAbsolutePath() + "'不存在");
		Validate.isTrue(dir.isDirectory(), "文件'" + dir.getAbsolutePath() + "'必须是目录");
		List<Template> templateList = new ArrayList<Template>();
		File[] imgFileList = Validate.notEmpty(dir.listFiles(), "目录'" + dir.getAbsolutePath()
																+ "'下无文件");
		String imgName = null;
		for (File file : imgFileList) {
			imgName = file.getName();
			BufferedImage image = ImageIO.read(file);
			String result = imgName.substring(0, imgName.indexOf("."));
			int width = image.getWidth();
			int height = image.getHeight();
			int correctCount = 0;
			int[][] tempArray = new int[height][width];
			for (int i = 0; i < image.getWidth(); i++) {
				for (int j = 0; j < image.getHeight(); j++) {
					if (isWhite(image.getRGB(i, j), criticalValue)) {
						tempArray[j][i] = 0;
					} else {
						tempArray[j][i] = 1;
						correctCount++;
					}
				}
			}
			//						System.out.println();
			//						System.out.println(result);
			//						printIntArray(tempArray);
			templateList.add(new Template(result, tempArray, correctCount));
		}
		return templateList;
	}
	
	/**
	 * 读取指定目录下的模板图片
	 * 		转换成解析验证码所需的模板，图片名字为其模板值
	 * @param dir
	 * @param criticalValue 临界值
	 * @return
	 * @throws IOException
	 */
	public static List<Template> getTemplateList1(File dir, int criticalValue) throws IOException {
		// System.out.println(dir.getAbsolutePath());
		Validate.isTrue(dir.exists(), "文件'" + dir.getAbsolutePath() + "'不存在");
		Validate.isTrue(dir.isDirectory(), "文件'" + dir.getAbsolutePath() + "'必须是目录");
		File[] imgFileList = Validate.notEmpty(dir.listFiles(), "目录'" + dir.getAbsolutePath()
																+ "'下无文件");
		String imgName = null;
		Map<String, List<Template>> dataMap = new HashMap<String, List<Template>>();
		List<Template> tempList = null;//临时
		for (File file : imgFileList) {
			//格式:result-num.jpg
			imgName = file.getName();
			BufferedImage image = ImageIO.read(file);
			String result = imgName.split("-")[0];
			int width = image.getWidth();
			int height = image.getHeight();
			int correctCount = 0;
			int[][] tempArray = new int[height][width];
			for (int i = 0; i < image.getWidth(); i++) {
				for (int j = 0; j < image.getHeight(); j++) {
					if (isWhite(image.getRGB(i, j), criticalValue)) {
						tempArray[j][i] = 0;
					} else {
						tempArray[j][i] = 1;
						correctCount++;
					}
				}
			}
			//						System.out.println();
			//						System.out.println(result);
			//						printIntArray(tempArray);
			if (dataMap.containsKey(result)) {
				tempList = dataMap.get(result);
			} else {
				tempList = new ArrayList<Template>();
			}
			System.out.println(result + ":" + correctCount + ":" + Double.valueOf(correctCount)
								/ (image.getWidth() * image.getWidth()));
			tempList.add(new Template(result, tempArray, correctCount));
			dataMap.put(result, tempList);
		}
		List<Template> templateList = new ArrayList<Template>();
		for (Entry<String, List<Template>> entry : dataMap.entrySet()) {
			String result = entry.getKey();
			int sum = 0;
			for (Template t : entry.getValue()) {
				sum += t.getCorrectCount();
			}
			int avg = sum / entry.getValue().size();
			templateList.add(new Template(result, avg));
			//			System.out.println(result + ":" + avg);
		}
		return templateList;
	}
	
	/**
	 * 打印二维数组
	 * @param intArray
	 */
	public static void printIntArray(int[][] intArray) {
		StringBuilder b = new StringBuilder();
		b.append("{\r\n");
		for (int[] temp : intArray) {
			b.append("{");
			for (int i = 0; i < temp.length; i++) {
				if (i < temp.length - 1) {
					b.append(temp[i] + ",");
				} else {
					b.append(temp[i]);
				}
				
			}
			b.append("},\r\n");
		}
		b.append("};");
		System.out.println(b.toString());
	}
	
	/**
	 * 获取阀值
	 * @param image
	 * @return
	 */
	public static int getCriticalValue(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int sum = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				sum += getAvgRGB(image.getRGB(x, y));
			}
		}
		return sum / (width * height);
	}
	
	/**
	 * 保存图片
	 * @param image
	 * @param filePath
	 * @param imgStyle
	 * @throws IOException 
	 */
	public static void saveImg(BufferedImage image, String filePath, String imgStyle)
																						throws IOException {
		ImageIO.write(image, imgStyle, new File(filePath));
	}
	
	/**
	 * 使用油漆桶算法切割验证码图片，要求验证码图片多个字母或数字交错，但不粘连
	 * @param image
	 * @param criticalValue 阀值
	 * @return
	 */
	public static List<BufferedImage> getSubImgsByBucketAlgorithm(BufferedImage image,
																	int criticalValue) {
		ImagCut imagCut = new ImagCut(image, criticalValue);
		return imagCut.getSubImgs();
	}

	public static void main(String[] args) throws Exception {
//		ImageUtils.getTemplateList1(new File("template/sz"), 100);
		BufferedImage source = ImageIO.read(new File("C:\\Users\\cq02559\\Desktop\\sz\\error_code\\unifiedUserPlatform\\g-1.jpg"));
		ImageIO.write(resize(source, 15, 21), "jpg", new File("C:\\Users\\cq02559\\Desktop\\sz\\error_code\\unifiedUserPlatform\\aaa.jpg"));
	}
	/** 
	 * 实现图像的等比缩放 
	 * @param source 
	 * @param targetW 
	 * @param targetH 
	 * @return 
	 */
	public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
		// targetW，targetH分别表示目标长和宽  
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放  
		// 则将下面的if else语句注释即可  
		if (sx < sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade  
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:  
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}
	
	/**使用油漆桶算法切割验证码图片，要求验证码图片多个字母或数字交错，但不粘连*/
	private static class ImagCut {
		
		private final BufferedImage	image;
		
		private final int[][]		imgArray;
		
		private int					maxX;
		
		private int					maxY;
		
		private int[][]				temp;
		
		public ImagCut(BufferedImage image, int criticalValue) {
			super();
			int width = image.getWidth();
			int height = image.getHeight();
			this.image = image;
			//入参图片转成二维数组
			imgArray = new int[height][width];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (ImageUtils.isBlack(image.getRGB(x, y), criticalValue)) {
						imgArray[y][x] = 1;
					} else {
						imgArray[y][x] = 0;
					}
				}
			}
		}
		
		/**
		 * 获取切割结果
		 * @return
		 */
		public List<BufferedImage> getSubImgs() {
			int width = image.getWidth();
			int height = image.getHeight();
			List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (imgArray[y][x] == 1) {//黑色块
						//临时二维数组数组
						temp = new int[height][width];
						floodFill8(x, y);//调用油漆桶算法
						subImgs.add(intArrayToImg());
					}
				}
			}
			return subImgs;
		}
		
		/**
		 * 油漆桶算法算出来的temp二维数组转换成图片
		 * @return
		 */
		private BufferedImage intArrayToImg() {
			temp = ImageUtils.clear(temp);//对二维int数组横纵坐标切割，剔除值只有0的横纵
			BufferedImage img = ImageUtils.intArrayToImg(temp);//二维int数组转img，图片宽高由二维int数组决定
			maxX = 0;
			maxY = 0;
			//		for(int[] aa:temp){
			//			System.out.println(Arrays.toString(aa));
			//		}
			//		System.out.println();
			return img;
		}
		
		/**
		 * 油漆桶算法
		 * @see //http://blog.csdn.net/jia20003/article/details/8908464
		 * @param x
		 * @param y
		 */
		private void floodFill8(int x, int y) {
			if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()
				&& imgArray[y][x] == 1) {
				// 是要找的点
				if (maxX < x) {
					maxX = x;// 确定边界x坐标
				}
				if (maxY < y) {
					maxY = y;// 确定边界y坐标
				}
				temp[y][x] = 1;
				imgArray[y][x] = 0;// 置为白色
				floodFill8(x + 1, y);
				floodFill8(x - 1, y);
				floodFill8(x, y + 1);
				floodFill8(x, y - 1);
				floodFill8(x + 1, y + 1);
				floodFill8(x - 1, y - 1);
				floodFill8(x - 1, y + 1);
				floodFill8(x + 1, y - 1);
			}
		}
		
	}
	
}

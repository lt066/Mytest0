package com.example.interfaces;
/**
 * 涓娿�佷笅鎷夋帴鍙�
 * @author longhf
 * @date 2015-8-13
 * @version 2.0
 */
public interface Pullable {
	/**
	 * 鍒锋柊
	 * @return true false
	 */
	boolean canPullDown();

	/**
	 * 
	 * 鍔犺浇
	 * @return true false
	 */
	boolean canPullUp();

}

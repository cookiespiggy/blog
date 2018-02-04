package com.yuantek.myblog.util;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * ConstraintViolationException bean验证处理器
 * 
 * bean验证的时候,抛出多个异常,其实是一个数组,通过此类转化成一个字符串
 * 
 * @author mi.zhe
 *
 */
public class ConstraintViolationExceptionHandler {

	/**
	 * 获取批量异常信息
	 */
	public static String getMessage(ConstraintViolationException e) {
		List<String> msgList = Lists.newArrayList();
		e.getConstraintViolations().forEach(c -> {
			msgList.add(c.getMessage());
		});
		// common 组件的字符串工具类 见注释 StringUtils.join
		return StringUtils.join(msgList.toArray(), ";");
	}
}

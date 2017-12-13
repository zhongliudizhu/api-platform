package com.winstar.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: WebUitl
 * @Description: Web处理常用方法
 * @author zl
 * @date 2014年2月24日 上午17:00:06
 */
public class WebUitl {
	
	
	private static String SORT_ASC="ASC";
	@SuppressWarnings("unused")
	private static String SORT_DESC="DESC";
	/**
	 * 构造分页参数（排序）.
	 * 
	 * @param pageNumber
	 *            the page number
	 * @param sort
	 *            the sort
	 * @return the page request
	 */
	public static PageRequest buildPageRequest(int pageNumber, String sort) {
		List<Order> orders = new ArrayList<Order>();
		if (WsdUtils.isNotEmpty(sort)) {
			PageSort[] pageSorts = JSON.parseObject(sort, PageSort[].class);
			for (PageSort pageSort : pageSorts) {
				orders.add(new Order(Sort.Direction.valueOf(pageSort
						.getDirection()), pageSort.getProperty()));
			}
			Sort sorts = new Sort(orders);
			return new PageRequest(pageNumber - 1, 1, sorts);
		} else {
			return new PageRequest(pageNumber - 1, 1);
		}
	}

	/**
	 * 构造分页参数
	 * 
	 * @param pageNumber 页
	 * @param pageSize
	 *            页面大小(默认为10条)
	 * @param sort
	 *            排序字段
	 * @return
	 */
	public static PageRequest buildPageRequest(int pageNumber, int pageSize,
											   String sort) {

		List<Order> orders = new ArrayList<Order>();
		if (WsdUtils.isEmpty(pageSize)) {
			pageSize = 10;
		}
		if (WsdUtils.isNotEmpty(sort)) {
			PageSort[] pageSorts = JSON.parseObject(sort, PageSort[].class);
			for (PageSort pageSort : pageSorts) {
				if(SORT_ASC.equalsIgnoreCase(pageSort.getDirection())){
					orders.add(new Order(Sort.Direction.ASC, pageSort.getProperty()));
				}else{
					orders.add(new Order(Sort.Direction.DESC, pageSort.getProperty()));
				}
			}
			Sort sorts = new Sort(orders);
			return new PageRequest(pageNumber , pageSize, sorts);
		} else {
			return new PageRequest(pageNumber , pageSize);
		}
	}

	public static Sort buildSorts(String sort){
		List<Order> orders = new ArrayList<>();
		if (WsdUtils.isNotEmpty(sort)) {
			PageSort[] pageSorts = JSON.parseObject(sort, PageSort[].class);
			for (PageSort pageSort : pageSorts) {
				orders.add(new Order(Sort.Direction.valueOf(pageSort
						.getDirection()), pageSort.getProperty()));
			}
			Sort sorts = new Sort(orders);
			return sorts;
		}
		return null;
	}

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getParametersStartingWith(
			String jsonFilter, String prefix) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		if (WsdUtils.isNotEmpty(jsonFilter) && WsdUtils.isNotEmpty(prefix)) {
			Map<String, Object> map = JSON.parseObject(jsonFilter,
					HashMap.class);
			for (String key : map.keySet()) {
				if (key.startsWith(prefix)) {
					String unprefixed = key.substring(prefix.length());
					Object value = map.get(key);
					searchParams.put(unprefixed, value);
				}
			}
		}
		return searchParams;
	}
	
}

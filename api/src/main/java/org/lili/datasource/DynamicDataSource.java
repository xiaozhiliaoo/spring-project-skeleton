package org.lili.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 基于spring的mysql读写分离方案：https://blog.csdn.net/liu976180578/article/details/77684583
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return CustomerContextHolder.getCustomerType();
	}

}

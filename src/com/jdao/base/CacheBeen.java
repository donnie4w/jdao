package com.jdao.base;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheBeen extends Cache {
	private String domain = null;
	private int expire = 0;
	private StoreModel storeModel = StoreModel.STRONG;
	Map<Class<Table<?>>, Map<Object, List<?>>> map = new ConcurrentHashMap<Class<Table<?>>, Map<Object, List<?>>>();

	private CacheBeen() {
	}

	public static Cache newInstance() {
		return new CacheBeen();
	}

	static {
		new ClearThread().start();
	}

	@Override
	public Cache setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	@Override
	public Cache setExpire(int expire) {
		this.expire = expire;
		return this;
	}

	@Override
	public Cache setStoreModel(StoreModel storeModel) {
		this.storeModel = storeModel;
		return this;
	}

	@Override
	public boolean build() {
		if (domain == null || cacheMap.containsKey(domain)) {
			return false;
		}
		cacheMap.put(domain, this);
		return true;
	}

	@Override
	public int getExpire() {
		return this.expire;
	}

	@Override
	public StoreModel getStoreModel() {
		return this.storeModel;
	}
}

/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Cache {
	public abstract Cache setDomain(String domain);

	public abstract Cache setExpire(int expire);

	public abstract Cache setStoreModel(StoreModel storeModel);

	public abstract int getExpire();

	public abstract StoreModel getStoreModel();

	protected static Map<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

	private final static Log log = Log.newInstance(false, Cache.class);

	public void delDomain(String domain) {
		cacheMap.remove(domain);
	}

	public void clearAll() {
		cacheMap.clear();
	}

	public void delNode(String domain, String node) {
		if (domain == null || node == null)
			return;
		if ("".equals(node.trim()))
			delDomain(domain);
		if (cacheMap.containsKey(domain)) {
			CacheBeen cb = (CacheBeen) cacheMap.get(domain);
			for (Class<Table<?>> ct : cb.map.keySet()) {
				Map<Object, List<?>> m = cb.map.get(ct);
				for (Object o : m.keySet()) {
					if (node.equals(((Condition) o).getNode())) {
						m.remove(o);
					}
				}
			}
		}
	}

	public void delNode(String domain, Class<?> clazz, String node) {
		if (domain == null || clazz == null || node == null)
			return;
		if (cacheMap.containsKey(domain)) {
			CacheBeen cb = (CacheBeen) cacheMap.get(domain);
			if (cb.map.containsKey(clazz)) {
				Map<Object, List<?>> m = cb.map.get(clazz);
				for (Object o : m.keySet()) {
					if (node.equals(((Condition) o).getNode())) {
						m.remove(o);
					}
				}
			}
		}
	}

	public abstract boolean build();

	public static Object getCache(String domain, Class<?> clazz, Object condition) {
		if (!cacheMap.containsKey(domain) || condition == null) {
			return null;
		}
		CacheBeen cb = (CacheBeen) cacheMap.get(domain);
		Map<Object, List<?>> map = cb.map.get(clazz);
		if (map != null && map.containsKey(condition)) {
			List<?> list = map.get(condition);
			long createTime = Long.valueOf(list.get(0).toString());
			if (cb.getExpire() == 0 || Long.compare(System.currentTimeMillis(), (createTime + cb.getExpire())) == -1) {
				switch (cb.getStoreModel()) {
				case STRONG:
					return list.get(1);
				case SOFT:
					if (list.get(1) instanceof SoftReference) {
						return ((SoftReference) list.get(1)).get();
					} else {
						return null;
					}
				case WEAK:
					if (list.get(1) instanceof WeakReference) {
						return ((WeakReference) list.get(1)).get();
					} else {
						return null;
					}
				default:
					return list.get(1);
				}
			} else {
				list = null;
				map.remove(condition);
			}
		}
		return null;
	}

	public static void setCache(String domain, Class<Table<?>> clazz, Object condition, Object result) {
		CacheBeen cb = (CacheBeen) cacheMap.get(domain);
		if (cb == null)
			return;
		List<Object> list = new ArrayList<Object>();
		final long l = System.currentTimeMillis();
		list.add(l);
		Object o = result;
		switch (cb.getStoreModel()) {
		case SOFT:
			o = new SoftReference(result);
			break;
		case WEAK:
			o = new WeakReference(result);
			break;
		default:
			break;
		}
		list.add(o);
		if (cb.map.containsKey(clazz)) {
			cb.map.get(clazz).put(condition, list);
		} else {
			Map<Object, List<?>> map = new ConcurrentHashMap<Object, List<?>>();
			map.put(condition, list);
			cb.map.put(clazz, map);
		}
	}

	static class ClearThread extends Thread {
		{
			this.setDaemon(true);
		}

		public void run() {
			while (true) {
				try {
					for (String s : cacheMap.keySet()) {
						CacheBeen c = (CacheBeen) cacheMap.get(s);
						for (Class<Table<?>> k : c.map.keySet()) {
							Map<Object, List<?>> v = c.map.get(k);
							for (Object o : v.keySet()) {
								List<?> list = v.get(o);
								long createTime = (Long) list.get(0);
								if (c.getExpire() != 0 && Long.compare(System.currentTimeMillis(), (c.getExpire() + createTime)) == 1) {
									log.log("[REMOVE CACHE][" + o + "]");
									v.remove(o);
								}
								Thread.sleep(50);
							}
							Thread.sleep(50);
						}
					}
					Thread.sleep(2000);
				} catch (Exception e) {
				}
			}
		}
	}
}

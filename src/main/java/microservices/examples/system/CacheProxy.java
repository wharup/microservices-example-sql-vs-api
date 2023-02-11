package microservices.examples.system;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import net.sf.ehcache.statistics.StatisticsGateway;

public class CacheProxy<E> {

    private CacheManager cacheManager;

    public CacheManager getCacheManager() {
		return cacheManager;
	}

	private Cache cache;
    
	public CacheProxy(CacheManager cacheManager, String cacheName) {
		super();
		this.cacheManager = cacheManager;
		this.cache = cacheManager.getCache(cacheName);
	}
	
	public E get(String key) {
		ValueWrapper valueWrapper = cache.get(key);
		if (valueWrapper == null) {
			return null;
		}
		return (E)valueWrapper.get();
	}

	public void put(String key, E entity) {
		cache.put(key, entity);
	}
	
	public boolean contains(String key) {
		return cache.get(key) != null;
	}
	
	public String getStats() {
		net.sf.ehcache.Cache cacheImpl = (net.sf.ehcache.Cache)cache.getNativeCache();
		StatisticsGateway s = cacheImpl.getStatistics();
		
		StringBuffer sb = new StringBuffer();
		append(sb, "cache[%11s] size=%5d (heap=%7d, off=%d, disk=%d), hit=%4d, miss=%4d, exp=%d, evct=%d, put=%4d, rmvd=%d",
				cache.getName(),
				s.getSize(), s.getLocalHeapSizeInBytes(), s.getLocalOffHeapSizeInBytes(), s.getLocalDiskSizeInBytes(),
				s.cacheHitCount(), s.cacheMissCount(), s.cacheExpiredCount(), s.cacheEvictedCount(), s.cachePutCount(), s.cacheRemoveCount());
		return sb.toString();
	}

	private void append(StringBuffer sb, String format, Object... args) {
		sb.append(String.format(format, args));
	}
	
}


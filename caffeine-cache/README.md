in this project we want to use sample for caffeine cache provider
this is high performance in memory cache used google guava

this is 3 strategy for cache population
1. manual <br>
    &nbsp; in this strategy we manually create cache and get and put them in into cache
2. synchronous loading <br>
   &nbsp; in this strategy we load cache item synchronously .
   &nbsp; if cache dont find value with specified key synchronously load default value from cache configuration and load it to cache
    we create sync cache item like that<b>		LoadingCache<Object, String> cache = Caffeine.newBuilder()
   .expireAfterWrite(1, TimeUnit.MINUTES)
   .maximumSize(100)
   .build(k -> "hosein");</b>
    <br>
    and after that use cache.get() for synchronously get item
3. asynchronous loading <br>
    &nbsp; in this strategy we asynchronously get item from cache for achive this we retreived completableFuture
   &nbsp; we create async cache item like that <b> AsyncLoadingCache<Object, String> cache = Caffeine.newBuilder()
   .expireAfterWrite(1, TimeUnit.MINUTES)
   .maximumSize(100)
   .buildAsync(k -> {
   Thread.sleep(100);
   System.out.println("loaded");
   return "hosein";
   });</b>

caffeine has 3 strategy for cache eviction
1. size base <br>
    this strategy applied when configured limit of cache size is exceeded
2. time base
3. reference base
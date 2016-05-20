package com.andycugb.storage;

/**
 * Created by andycugb on 15-4-21. 缓存key (前缀)常量，统一放置以避免冲突
 */
public class CacheConst {
    /* sorted set 最大长度 */
    public static final int SORTEDSET_MAX_SIZE = 20000;
    /* redis批量插入记录时，map最大的元素数量，避免map太大，添加失败 */
    public static final int ZADD_MAX_MAP_SIZE = 10000;
}

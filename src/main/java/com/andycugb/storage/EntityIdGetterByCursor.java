package com.andycugb.storage;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Get timeline from cache and DB, and set uncached timelines to cache
 * 
 * @author andycugb
 */
public abstract class EntityIdGetterByCursor<T> {

    private T cursor;
    private int length;

    public EntityIdGetterByCursor(T cursor, int length) {
        this.cursor = cursor;
        this.length = length;
    }

    /**
     * LRU缓存策略，读取列表时需确保连续性
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public T[] exec() throws SQLException, IOException {
        T[] cacheResult = getFromCache(cursor, length);
        if (ArrayUtils.isEmpty(cacheResult)) {
            cacheResult = createArrayInstance(0);
        }
        if (ArrayUtils.isNotEmpty(cacheResult) && cacheResult.length >= length) {
            return cacheResult;
        }
        int lengthFromDB = length - cacheResult.length;
        T cursorFromDB = null;
        // check whether cacheResult is []
        if (cacheResult.length <= 0) {
            patchToCache(cursor);
            cursorFromDB = cursor;
        } else {
            cursorFromDB = cacheResult[cacheResult.length - 1];
        }
        T[] dbResult = getFromDB(cursorFromDB, lengthFromDB);
        if (ArrayUtils.isEmpty(dbResult)) {
            return cacheResult;
        }
        T[] result = createArrayInstance(cacheResult.length + dbResult.length);
        System.arraycopy(cacheResult, 0, result, 0, cacheResult.length);
        System.arraycopy(dbResult, 0, result, cacheResult.length, dbResult.length);
        // TODO 可以考虑异步处理
        saveToCache(dbResult);
        return result;
    }

    /**
     * 非持久化缓存中读取列表记录
     * 
     * @param cursor
     * @param length
     * @return
     * @throws SQLException
     * @throws IOException
     */
    protected abstract T[] getFromCache(T cursor, int length) throws SQLException, IOException;

    /**
     * DB中读取列表记录
     * 
     * @param cursor
     * @param length
     * @return
     * @throws SQLException
     */
    protected abstract T[] getFromDB(T cursor, int length) throws SQLException;

    /**
     * 实例化
     * 
     * @param length
     * @return
     */
    protected abstract T[] createArrayInstance(int length);

    /**
     * 保存DB查询结果
     * 
     * @param dbResult
     * @throws IOException
     */
    protected abstract void saveToCache(T[] dbResult) throws IOException;

    /**
     * 补全查询时间之前的丢失数据
     * 
     * @param cursor
     * @return
     * @throws SQLException
     * @throws IOException
     */
    protected T[] patchToCache(T cursor) throws SQLException, IOException {
        return null;
    }
}

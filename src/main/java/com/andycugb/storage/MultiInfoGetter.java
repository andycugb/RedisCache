package com.andycugb.storage;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by andycugb on 15-5-9.
 */
public abstract class MultiInfoGetter<T> implements Callable<T[]> {
    public static final Logger logger = LoggerFactory.getLogger(MultiInfoGetter.class);
    protected long[] ids;

    public MultiInfoGetter(long[] ids) {
        this.ids = ids;
    }

    public T[] call() {
        try {
            return exec();
        } catch (SQLException e) {
            logger.error("get multi objects error, ids=" + ArrayUtils.toString(ids), e);
        } catch (IOException e) {
            logger.error("get multi objects error, ids=" + ArrayUtils.toString(ids), e);
        }
        return null;
    }

    public T[] exec() throws SQLException, IOException {
        T[] emptyArray = createEmptyArray();
        if (ArrayUtils.isEmpty(ids)) {
            return emptyArray;
        }
        T[] results = getFromCache(ids);
        int length = ids.length;
        List<Long> missedIds = new ArrayList<Long>();
        List<Integer> missedIndexes = new ArrayList<Integer>();
        for (int i = 0; i < length; i++) {
            if (results == null || i >= results.length || results[i] == null) {
                missedIds.add(ids[i]);
                missedIndexes.add(i);
            }
        }
        int missedIdsLength = missedIds.size();
        if (missedIdsLength != 0) {
            long[] missedIdArray = ArrayUtils.toPrimitive(missedIds.toArray(new Long[0]));
            T[] dbResults = getFromDB(missedIdArray);
            if (ArrayUtils.isEmpty(dbResults)) {
                for (int i = 0; i < missedIdsLength; i++) {
                    results[missedIndexes.get(i)] = null;
                }
            }
            for (int i = 0; i < missedIdsLength; i++) {
                results[missedIndexes.get(i)] = dbResults[i];
            }
            if (missedIds.size() == dbResults.length) {
                setToCache(ArrayUtils.toPrimitive(missedIds.toArray(new Long[0])), dbResults);
            }
        }

        return results;
    }

    /**
     * Get infos from cache.
     *
     * @param ids
     * @return
     */
    protected abstract T[] getFromCache(long[] ids) throws IOException;

    /**
     * Get infos from DB.
     * 
     * @param ids
     * @return
     */
    protected abstract T[] getFromDB(long[] ids) throws SQLException;

    /**
     * Create a empty array instance for class T.
     * 
     * @return
     */
    protected abstract T[] createEmptyArray();

    /**
     * @param ids
     * @param objects
     */
    protected abstract void setToCache(long[] ids, T[] objects) throws IOException;
}

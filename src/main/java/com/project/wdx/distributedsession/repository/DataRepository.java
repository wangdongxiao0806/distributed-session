package com.project.wdx.distributedsession.repository;

/**
 *数据存储接口
 *
 *
 * @author wdx
 */
public interface DataRepository{

    /**
     * 在缓存系统中保存数据，key=sessionId
     * @param sessionId
     * @param obj
     */
    public <T> void save(String sessionId,T obj);

    /**
     * 根据sessionId查询数据
     * @param seesionId
     * @return
     */
    public String queryBySessionId(String seesionId);

    public <T> T queryBySessionId(String sessionId,Class<T> clazz);
}

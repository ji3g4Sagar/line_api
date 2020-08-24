package com.example.line.demo.dao.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.example.line.demo.common.JPAUtil;
import com.example.line.demo.common.ProjectLogging;
import com.example.line.demo.common.QueryBuilder;
import com.example.line.demo.dao.inf.IUserInfoDao;
import com.example.line.demo.entity.UserInfoModel;
import com.example.line.demo.vo.UserInfoVo;
import com.google.common.base.Throwables;

import org.apache.commons.lang3.StringUtils;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * user Dao
 * @author MikeyJou
 */
@Lazy
@Repository("IUserInfoDao")
public class UserInfoDaoImpl implements IUserInfoDao {

    private void setupQueryVo(UserInfoVo userVo, List<String> criList, Map<String, Object> paramMap,
            String tableName) {
        tableName = tableName.isEmpty() ? "" : tableName + ".";
        if (userVo.getId() != null) {
            criList.add(tableName + "id = :id");
            paramMap.put("id", userVo.getId());
        }

        if (userVo.getUserId() != null) {
            criList.add(tableName + "userId = :userId");
            paramMap.put("userId", userVo.getUserId());
        }
    }

    private StringBuilder getBasicSql() {
        return getBasicSql(null);
    }

    private StringBuilder getBasicSql(List<String> fields) {
        return QueryBuilder.getBasicSql(fields, "goodsInfoModel");
    }

    /**
     * log init.
     */
    static final ProjectLogging LOGGER = new ProjectLogging(UserInfoDaoImpl.class);

    @Override
    public final int userInfoPersist(final Optional<UserInfoModel> userOptional)
        throws Exception {
        LOGGER.info("Start goodsInfoPersist");

        // 回傳結果預設值
        int result = 0;
        // 建立連線物件
        EntityManager em = JPAUtil.getInstance().get().createEntityManager();

        // 建立資料交換物件
        EntityTransaction et = null;
        try {

        // 傳入物件是否為空
        if (userOptional.isPresent()) {
            // 轉換物件資訊
            UserInfoModel userModel = userOptional.orElse(new UserInfoModel());
            // goodsInfoModel.setCreateTime(LocalDateTime.now(Clock.systemUTC()));
            et = em.getTransaction();
            et.begin();
            // 寫入物件
            LOGGER.debug("Start persist");
            em.persist(userModel);
            et.commit();

            // if (0L != userModel.getId()) {
            //     result = 1;
            // }
        }
        } catch (Exception e) {

            LOGGER.error(Throwables.getStackTraceAsString(e));
            // Persist error rollback
            if (et != null && et.isActive()) {
                et.rollback();
            }
        } finally {
            // 關閉連線
            em.close();
        }

        LOGGER.info("End goodsInfoPersist");
        return result;
    }

    @Override
    public final int userInfoMerge(final Optional<UserInfoModel> userOptional) throws Exception {
        LOGGER.info("Start goodsInfoMerge");

        // 回傳結果預設值
        int result = 0;
        // 建立連線物件
        EntityManager em = JPAUtil.getInstance().get().createEntityManager();

        // 建立資料交換物件
        EntityTransaction et = null;
        try {

        // 傳入物件是否為空
        if (userOptional.isPresent()) {
            // 轉換物件資訊
            UserInfoModel userModel = userOptional.orElse(new UserInfoModel());
            // goodsInfoModel.setUpdateTime(LocalDateTime.now(Clock.systemUTC()));
            et = em.getTransaction();
            et.begin();
            // 更新物件
            LOGGER.debug("Start merge");
            em.merge(userModel);
            et.commit();

            // if (0L != userModel.getId()) {
            //     result = 1;
            // }
        }
        } catch (Exception e) {

            LOGGER.error(Throwables.getStackTraceAsString(e));
            // Merge error rollback
            if (et != null && et.isActive()) {
                et.rollback();
            }
        } finally {
            // 關閉連線
            em.close();
        }

        LOGGER.info("End goodsInfoMerge");
        return result;
    }

    @Override
    public final int userInfoRemove(final Optional<UserInfoModel> userOptional) throws Exception {
        LOGGER.info("Start goodsInfoRemove");

        // 回傳結果預設值
        int result = 0;
        // 建立連線物件
        EntityManager em = JPAUtil.getInstance().get().createEntityManager();

        // 建立資料交換物件
        EntityTransaction et = null;
        try {

        // 傳入物件是否為空
        if (userOptional.isPresent()) {
            // 轉換物件資訊
            UserInfoModel userModel = userOptional.orElse(new UserInfoModel());
            // goodsInfoModel.setUpdateTime(LocalDateTime.now(Clock.systemUTC()));
            // 設定邏輯刪除
            // goodsInfoModel.setLogicalDelete(1);
            et = em.getTransaction();
            et.begin();
            // 更新物件
            LOGGER.debug("Start merge");
            em.merge(userModel);
            et.commit();

            // if (0L != userModel.getId()) {
            //     result = 1;
            // }
        }
        } catch (Exception e) {

            LOGGER.error(Throwables.getStackTraceAsString(e));
            // Merge error rollback
            if (et != null && et.isActive()) {
                et.rollback();
            }
        } finally {
            // 關閉連線
            em.close();
        }

        LOGGER.info("End goodsInfoRemove");
        return result;
    }

    @Override
    public final Optional<UserInfoModel> userInfoFind(final UserInfoVo userVo) throws Exception {
        LOGGER.info("Start goodsInfoFind");

        // 建立連線物件
        EntityManager em = JPAUtil.getInstance().get().createEntityManager();

        // 建立搜尋字串
        StringBuilder sbSql = new StringBuilder();
        // 建立回傳物件
        Optional<UserInfoModel> goodsInfoOptional = Optional.empty();
        try {
            sbSql.append("From " + UserInfoModel.class.getSimpleName());

            // 加入查詢參數
            List<String> criList = new ArrayList<>();
            Map<String, Object> paramMap = new HashMap<>();

            // 判斷是否有加入條件
            String tableName = "";
            setupQueryVo(userVo, criList, paramMap, tableName);
            System.out.println(criList.toString());
            if (!criList.isEmpty()) {
                // 使用者輸入查詢條件
                sbSql.append(" WHERE ");
                sbSql.append(StringUtils.join(criList, " AND "));
                // criList.forEach((cri) -> sbSql.append(cri).append(" AND ").append(" "));
            }

            // sbSql.append(" Order By update_time Desc");
            
            LOGGER.debug("SQL : " + sbSql);
            System.out.println(sbSql);
            // 建立查詢物件
            TypedQuery<UserInfoModel> query = em.createQuery(sbSql.toString(), UserInfoModel.class);
            query.setMaxResults(1);
            // 設定參數
            paramMap
                .keySet()
                .forEach(
                    (parameterName) -> query.setParameter(parameterName, paramMap.get(parameterName)));

            LOGGER.debug("Start Find");
            List<UserInfoModel> userModelList = query.getResultList();
            // Fetch lazy properties
            if (!userModelList.isEmpty()) {
                UserInfoModel firstuser = userModelList.get(0);
            }
            // 取出查詢資訊
            goodsInfoOptional =
                Optional.ofNullable(userModelList.isEmpty() ? null : userModelList.get(0));
        } catch (Exception e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
        } finally {
            // 關閉連線
            em.close();
        }

        LOGGER.info("End goodsInfoFind");
        return goodsInfoOptional;
    }

    @Override
    public final Optional<List<UserInfoModel>> userInfoFindList(final UserInfoVo userVo)
        throws Exception {
        LOGGER.info("Start goodsInfoFind");

        // 建立連線物件
        EntityManager em = JPAUtil.getInstance().get().createEntityManager();

        // 建立搜尋字串
        StringBuilder sbSql = new StringBuilder();
        // 建立回傳物件
        Optional<List<UserInfoModel>> userListOptional = Optional.empty();
        try {
            sbSql.append("From " + UserInfoModel.class.getSimpleName());
            
            
            // 加入查詢參數
            List<String> criList = new ArrayList<>();
            Map<String, Object> paramMap = new HashMap<>();

            // 判斷是否有加入條件
            String tableName = "";
            setupQueryVo(userVo, criList, paramMap, tableName);

            if (!criList.isEmpty()) {
                // 使用者輸入查詢條件
                criList.forEach((cri) -> sbSql.append(" AND ").append(cri).append(" "));
            }

            LOGGER.debug("SQL : " + sbSql);
            System.out.println("SQL:" + sbSql);
            // 建立查詢物件
            TypedQuery<UserInfoModel> query = em.createQuery(sbSql.toString(), UserInfoModel.class);

            // if(userVo.getPageSize() > 0){
            //     query.setFirstResult(0);//从第一条记录开始
            //     query.setMaxResults(userVo.getPageSize());//取出四条记录
            // }   


            // 設定參數
            paramMap
                .keySet()
                .forEach(
                    (parameterName) -> query.setParameter(parameterName, paramMap.get(parameterName)));
            

            // 取出查詢資訊
            LOGGER.debug("Start Find");

            userListOptional = Optional.ofNullable(query.getResultList());
        } catch (Exception e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
        } finally {
            // 關閉連線
            em.close();
        }

        LOGGER.info("End goodsInfoFind");
        return userListOptional;
    }

}

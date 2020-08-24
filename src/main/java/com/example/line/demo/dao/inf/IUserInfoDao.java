package com.example.line.demo.dao.inf;

import java.util.List;
import java.util.Optional;

import com.example.line.demo.entity.UserInfoModel;
import com.example.line.demo.vo.UserInfoVo;


/**
 * user Dao Interface
 * @author MikeyJou
 */
public interface IUserInfoDao {

    int userInfoPersist(Optional<UserInfoModel> modelOptional) throws Exception;
  
    int userInfoMerge(Optional<UserInfoModel> modelOptional) throws Exception;
  
    int userInfoRemove(Optional<UserInfoModel> modelOptional) throws Exception;
  
    Optional<UserInfoModel> userInfoFind(UserInfoVo vo) throws Exception;
  
    Optional<List<UserInfoModel>> userInfoFindList(UserInfoVo vo) throws Exception;
}
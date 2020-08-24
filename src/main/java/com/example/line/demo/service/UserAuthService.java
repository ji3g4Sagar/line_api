package com.example.line.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.line.demo.common.Response;
import com.example.line.demo.common.Status;
import com.example.line.demo.common.WaCareResponse;
import com.example.line.demo.dao.impl.UserInfoDaoImpl;
import com.example.line.demo.entity.UserInfoModel;
import com.example.line.demo.security.WaCareSecurity;
import com.example.line.demo.vo.UserInfoVo;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/**
 * User Service
 * @author MikeyJou
 */
public class UserAuthService {
    
    private UserInfoDaoImpl userInfoDao;

    public UserAuthService() {
        this.userInfoDao = new UserInfoDaoImpl();
    }

    public WaCareResponse createUser(UserInfoModel userInfo) throws Exception {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setAccount(userInfo.getAccount());

        if(StringUtils.isEmpty(userInfo.getAccount())){
            return new WaCareResponse(Status.MISSING_PARAMETER);
        }

        // 檢查是否已經存在使用者
        Optional<UserInfoModel> result = userInfoDao.userInfoFind(userInfoVo);
        if (result.isPresent()) {
            return new WaCareResponse(Status.EXIST_USER);
        } else {
            JSONObject payLoad = new JSONObject();
            String userId = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println(userInfo.getPassword());
            userInfo.setPassword(WaCareSecurity.SHA512(userInfo.getPassword()));
            userInfo.setUserId(userId);
            userInfo.setIsLogicalDelete(0);
            userInfoDao.userInfoPersist(Optional.of(userInfo));
            
            return new WaCareResponse(Status.OK, payLoad);
        }
    }

    public WaCareResponse userBindLine(UserInfoModel userInfo) throws Exception {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(userInfo.getUserId());

        if(StringUtils.isEmpty(userInfo.getUserId())){
            return new WaCareResponse(Status.MISSING_PARAMETER);
        }

        // 檢查是否已經存在使用者
        Optional<UserInfoModel> result = userInfoDao.userInfoFind(userInfoVo);
        if (result.isPresent()) {
            UserInfoModel user = result.get();
            user.setLineUserId(userInfo.getLineUserId());
            userInfoDao.userInfoMerge(Optional.of(user));

            return new WaCareResponse(Status.OK);
        } else {
            return new WaCareResponse(Status.NON_EXISTENT_USER);
        }
    }
}
package com.example.line.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import com.example.line.demo.common.HttpHelper;
import com.example.line.demo.common.Status;
import com.example.line.demo.common.WaCareResponse;
import com.example.line.demo.dao.impl.UserInfoDaoImpl;
import com.example.line.demo.entity.UserInfoModel;
import com.example.line.demo.vo.UserInfoVo;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

public class LineService {
    private UserInfoDaoImpl userInfoDao;
    private HttpHelper httpHelper;
    public LineService() {
        this.userInfoDao = new UserInfoDaoImpl();
        this.httpHelper = new HttpHelper();
    }

    public WaCareResponse pushMessage(UserInfoModel userInfoModel, String message) throws Exception {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(userInfoModel.getUserId());

        if(StringUtils.isEmpty(userInfoModel.getUserId())){
            return new WaCareResponse(Status.MISSING_PARAMETER);
        }

        // 檢查是否已經存在使用者
        Optional<UserInfoModel> result = userInfoDao.userInfoFind(userInfoVo);
        if (result.isPresent()) {
            UserInfoModel user = result.get();
            
            if(StringUtils.isEmpty(user.getLineUserId())){
                return new WaCareResponse(Status.LINE_NOT_BIND);
            }

            String url = "https://api.line.me/v2/bot/message/multicast";
            JSONObject body = new JSONObject();
            String[] id = {user.getLineUserId()};
            
            JSONObject m = new JSONObject();
            m.put("type", "text");
            m.put("text", message);
            JSONObject[] messages = {m};
            body.put("to", id);
            body.put("messages", messages);
            System.out.println(body);
            httpHelper.getResponseBody(url, "o8ozlUAXbv4iKxt9OaRYNOUyowFMiZI289isb4bojuHOKrtgJbOHIpIfCL7hmhZs2c0gB795V6SFlI+/ShinGuETHIrrZuuOd3w8z1vWgEo2nJ2wV68hnp26SDKoAHMQ5u03YsgQSlWdMIknSOrRpAdB04t89/1O/w1cDnyilFU=", body);
            return new WaCareResponse(Status.OK);
        } else {
            return new WaCareResponse(Status.NON_EXISTENT_USER);
        }
    }
}
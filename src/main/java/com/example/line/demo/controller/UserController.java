package com.example.line.demo.controller;

import java.util.List;
import java.util.Map;

import com.example.line.demo.common.ProjectLogging;
import com.example.line.demo.common.Status;
import com.example.line.demo.common.WaCareGson;
import com.example.line.demo.common.WaCareResponse;
import com.example.line.demo.entity.UserInfoModel;
import com.example.line.demo.service.UserAuthService;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Organization Controller
 * @author MikeyJou
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ProjectLogging LOGGER = new ProjectLogging(UserController.class);
    
    private final Gson gson = WaCareGson.getWacareGson();
    private final UserAuthService userAuthService = new UserAuthService();

    @RequestMapping(value="/account", method=RequestMethod.POST)
    public ResponseEntity<String> createUser(
        @ApiParam(value = "使用者資訊JSONObject String") @RequestParam(value = "userInfo") String userInfo) {
            WaCareResponse response = new WaCareResponse();
            try {
                UserInfoModel info = gson.fromJson(userInfo, UserInfoModel.class);
                response = userAuthService.createUser(info);
            } catch (Exception ex) {
                LOGGER.error(Throwables.getStackTraceAsString(ex));
                response.setStatusCode(Status.FAILED);
            }
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(value="/account/line/bind", method=RequestMethod.POST)
    public ResponseEntity<String> userBindLine(
        @RequestParam(value = "userId") String userId,
        @RequestParam(value = "lineId") String lineId) {
            
            WaCareResponse response = new WaCareResponse();
            try {
                UserInfoModel user = new UserInfoModel();
                user.setUserId(userId);
                user.setLineUserId(lineId);
                response = userAuthService.userBindLine(user);
            } catch (Exception ex) {
                LOGGER.error(Throwables.getStackTraceAsString(ex));
                response.setStatusCode(Status.FAILED);
            }
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
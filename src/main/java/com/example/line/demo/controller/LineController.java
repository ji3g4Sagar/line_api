package com.example.line.demo.controller;

import com.example.line.demo.common.ProjectLogging;
import com.example.line.demo.common.Status;
import com.example.line.demo.common.WaCareGson;
import com.example.line.demo.common.WaCareResponse;
import com.example.line.demo.entity.UserInfoModel;
import com.example.line.demo.service.LineService;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/line")
public class LineController {
    private final ProjectLogging LOGGER = new ProjectLogging(LineController.class);
    
    private final Gson gson = WaCareGson.getWacareGson();

    private LineService lineService = new LineService();

    @RequestMapping(value="/message/push", method=RequestMethod.POST)
    public ResponseEntity<String> pushMessage(
        @ApiParam(value = "") @RequestParam(value = "userId") String userId,
        @ApiParam(value = "") @RequestParam(value = "message") String message) {
            WaCareResponse response = new WaCareResponse();
            try {
                UserInfoModel user = new UserInfoModel();
                user.setUserId(userId);
                response = lineService.pushMessage(user, message);
            } catch (Exception ex) {
                LOGGER.error(Throwables.getStackTraceAsString(ex));
                response.setStatusCode(Status.FAILED);
            }
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
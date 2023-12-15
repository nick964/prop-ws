package com.nick.propws.controller;

import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.dto.ShareGroupRequest;
import com.nick.propws.dto.UpdateConfig;
import com.nick.propws.entity.User;
import com.nick.propws.service.ConfigService;
import com.nick.propws.service.GroupService;
import com.nick.propws.service.TwilioService;
import com.nick.propws.service.UserDetailsImpl;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("config")
public class ConfigController {

    @Autowired
    ConfigService configService;


    @PostMapping("/update")
    public ResponseEntity<String> createGroup(@RequestBody UpdateConfig updateConfig) {
        configService.setConfig(updateConfig.getRule(), updateConfig.getEnabled());
        return ResponseEntity.ok("SUCCESS");
    }

}

package com.nick.propws.controller;

import com.nick.propws.dto.ConfigDto;
import com.nick.propws.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("config")
public class ConfigController {

    @Autowired
    ConfigService configService;


    @PostMapping("/update")
    public ResponseEntity<String> updateConfigRule(@RequestBody ConfigDto updateConfig) {
        configService.setConfig(updateConfig);
        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ConfigDto>> getConfigs() {
        return ResponseEntity.ok(configService.getAllConfigRules());
    }

    @GetMapping("/started")
    public ResponseEntity<Boolean> hasGameStarted() {
        return ResponseEntity.ok(configService.hasGameStarted());
    }

}

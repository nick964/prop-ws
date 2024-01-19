package com.nick.propws.service;

import com.nick.propws.dto.ConfigDto;
import com.nick.propws.dto.CreateGroupReq;
import com.nick.propws.dto.CreateGroupResponse;
import com.nick.propws.dto.GroupDetailsResponse;
import com.nick.propws.entity.ConfigSetup;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.User;
import com.nick.propws.exceptions.PropSheetException;
import com.nick.propws.repository.ConfigServiceRepository;
import com.nick.propws.repository.GroupRepository;
import com.nick.propws.repository.MemberRepository;
import com.nick.propws.repository.UserRepository;
import com.nick.propws.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ConfigServiceImpl implements ConfigService{

    @Autowired
    ConfigServiceRepository configServiceRepository;

    @Override
    public void setConfig(ConfigDto configDto) {
        ConfigSetup foundConfig = configServiceRepository.findConfigSetupByRule(configDto.getRule());
        if(foundConfig != null) {
            foundConfig.setEnabled(configDto.isEnabled());
            configServiceRepository.save(foundConfig);
        } else {
            ConfigSetup setup = new ConfigSetup();
            setup.setEnabled(configDto.isEnabled());
            setup.setRule(configDto.getRule());
            setup.setDescription(configDto.getDescription());
            configServiceRepository.save(setup);
        }
    }

    @Override
    public List<ConfigDto> getAllConfigRules() {
        List<ConfigSetup> configs = this.configServiceRepository.findAll();
        List<ConfigDto> configDtos = new ArrayList<>();
        for(ConfigSetup configSetup : configs) {
            configDtos.add(mapFromConfigEntity(configSetup));
        }
        return configDtos;
    }

    @Override
    public boolean hasGameStarted() {
        ConfigSetup configSetup = configServiceRepository.findConfigSetupByRule("game_started");
        if(configSetup == null) {
            return false;
        }
        return configSetup.isEnabled();
    }

    private ConfigDto mapFromConfigEntity(ConfigSetup c) {
        ConfigDto qDto = new ConfigDto();
        qDto.setRule(c.getRule());
        qDto.setEnabled(c.isEnabled());
        qDto.setDescription(c.getDescription());
        return qDto;
    }
}

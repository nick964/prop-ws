package com.nick.propws.service;


import com.nick.propws.dto.ConfigDto;

import java.util.List;

public interface ConfigService {

    void setConfig(ConfigDto configDto);

    List<ConfigDto> getAllConfigRules();


}

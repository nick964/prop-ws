package com.nick.propws.repository;

import com.nick.propws.entity.ConfigSetup;
import com.nick.propws.entity.MasterAnswer;
import com.nick.propws.service.ConfigService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigServiceRepository extends JpaRepository<ConfigSetup, Long> {

    ConfigSetup findConfigSetupByRule(String rule);


}

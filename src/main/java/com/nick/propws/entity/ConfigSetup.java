package com.nick.propws.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "configsetup")
@Data
public class ConfigSetup {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String rule;

    private boolean enabled;


}

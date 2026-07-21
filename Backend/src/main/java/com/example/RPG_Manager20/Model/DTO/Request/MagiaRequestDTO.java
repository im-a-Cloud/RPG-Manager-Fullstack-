package com.example.RPG_Manager20.Model.DTO.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MagiaRequestDTO(
    String name,
    String level,
    String casting_time,
    List<String> classes,
    Map<String, Object> components,
    String description,
    String duration,
    String range,
    boolean ritual,
    String school,
    List<String> tags,
    String type
)
{
    public MagiaRequestDTO{
        if(name == null) name = "";
        if(level == null) level = "";
        if(casting_time == null) casting_time = "";
        if(classes == null) classes = new ArrayList<>();
        if(components == null) components = new HashMap<>();
        if(description == null) description = "";
        if(duration == null) duration = "";
        if(range == null) range = "";
        if(ritual) ritual = false;
        if(school == null) school = "";
        if(tags == null) tags = new ArrayList<>();
        if(type == null) type = "";
    }
}

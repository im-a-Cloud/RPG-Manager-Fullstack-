package com.example.RPG_Manager20.Model.DTO.Request;

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
    String type)
{}

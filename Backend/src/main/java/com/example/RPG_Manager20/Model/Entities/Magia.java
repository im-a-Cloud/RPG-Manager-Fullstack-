package com.example.RPG_Manager20.Model.Entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name= "tb_magia")
public class Magia extends AbstractModel{
    private String casting_time;

    @Embedded
    private Components components;

    @Column(columnDefinition = "TEXT")

    private String description;
    private String duration;
    private String level;
    private String name;
    private String range;
    private boolean ritual;
    private String school;

    @ElementCollection
    @CollectionTable(name = "magia_classes", joinColumns = @JoinColumn(name = "magia_id"))
    @Column(name = "classe")
    private List<String> classes;

    @ElementCollection
    @CollectionTable(name = "magia_tags", joinColumns = @JoinColumn(name = "magia_id"))
    @Column(name = "tag")
    private List<String> tags;

    private String type;

    public Magia(){

    }

    public Magia(String casting_time, List<String> classes, Components components, String description, String duration, String level, String name, String range, boolean ritual, String school, List<String> tags, String type) {
        this.casting_time = casting_time;
        this.classes = classes;
        this.components = components;
        this.description = description;
        this.duration = duration;
        this.level = level;
        this.name = name;
        this.range = range;
        this.ritual = ritual;
        this.school = school;
        this.tags = tags;
        this.type = type;
    }

    //quando nescessário
    /*
    public int getLevelAsInt() {
        if (level == null) return 0;
        if (level.equalsIgnoreCase("cantrip") || level.equalsIgnoreCase("truque") || level.equals("0")) {
            return 0;
        }
        try {
            return Integer.parseInt(level);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

     */

    public String getCasting_time() {
        return casting_time;
    }

    public void setCasting_time(String casting_time) {
        this.casting_time = casting_time;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public boolean isRitual() {
        return ritual;
    }

    public void setRitual(boolean ritual) {
        this.ritual = ritual;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

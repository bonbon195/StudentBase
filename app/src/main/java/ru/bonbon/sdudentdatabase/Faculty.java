package ru.bonbon.sdudentdatabase;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private List<Group> groups;
    private String name;

    public Faculty(String name, List<Group> groups) {
        this.groups = groups;
        this.name = name;
    }
    public Faculty(String name) {
        this.name = name;
        groups = new ArrayList<>();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

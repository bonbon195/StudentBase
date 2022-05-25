package ru.bonbon.studentbase.entity;

public class Group {
    private int id;
    private String name;
    private int idFaculty;

    public Group(int id, String name, int idFaculty) {
        this.id = id;
        this.name = name;
        this.idFaculty = idFaculty;
    }

    public Group(String name, int idFaculty) {
        this.name = name;
        this.idFaculty = idFaculty;
    }

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdFaculty() {
        return idFaculty;
    }

    public void setIdFaculty(int idFaculty) {
        this.idFaculty = idFaculty;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idFaculty=" + idFaculty +
                '}';
    }
}



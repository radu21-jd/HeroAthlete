package com.example.l999;

public class Goal {
    String id;
    private String name;
    private boolean selected;
    private boolean completed;
    private boolean active;

    public Goal() {
    }

    public Goal(String id, String name, boolean selected, boolean completed, boolean active) {
        this.name = name;
        this.selected = selected;
        this.id=id;
        this.completed=completed;
        this.active=active;
    }
    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

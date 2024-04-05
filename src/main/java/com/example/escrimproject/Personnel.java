package com.example.escrimproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Personnel
{
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty role;
    private final StringProperty items;

    public Personnel()
    {
        id = new SimpleStringProperty(this, "id");
        name = new SimpleStringProperty(this, "name");
        role = new SimpleStringProperty(this, "role");
        items = new SimpleStringProperty(this, "items");
    }

    public StringProperty idProperty() { return id; }
    public String getId() { return id.get(); }
    public void setId(String newId) { id.set(newId); }

    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String newName) { name.set(newName); }

    public StringProperty roleProperty() { return role; }
    public String getRole() { return role.get(); }
    public void setRole(String newRole) { role.set(newRole); }

    public StringProperty itemsProperty() { return items; }
    public String getItems() { return items.get(); }
    public void setItems(String newItems) { items.set(newItems); }
}

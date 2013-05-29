package com.zhentao.rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "employee")
public class Employee {

    private Long id;
    private String name;

    public Employee() {
        // No-arg constructor required for annotations
    }

    public Employee(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement(name = "id")
    public long getId() {
        return this.id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).toString();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj != null && obj instanceof Employee) {
            Employee other = (Employee) obj;
            result = other.id == this.id && Objects.equal(other.name, this.name);
        }

        return result;
    }
}

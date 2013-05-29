package com.zhentao.rest.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EmployeeTest {

    @Test
    public void fieldsSetByConstructorCanBeRead() {
        long id = 3;
        Employee e = new Employee(id, "Alice");
        assertThat(e.getId(), is(id));
        assertThat(e.getName(), is("Alice"));
    }

    @Test
    public void fieldsSetBySettersCanBeRead() {
        long id = 5;
        Employee e = new Employee();
        e.setId(id);
        e.setName("Bob");
        assertThat(e.getId(), is(id));
        assertThat(e.getName(), is("Bob"));
    }

    @Test
    public void equalsUsesIdAndNameOnly() {
        assertThat(new Employee(7, "Alice"), equalTo(new Employee(7, "Alice")));
        assertThat(new Employee(7, "Alice"), not(equalTo(new Employee(17, "Alice"))));
        assertThat(new Employee(7, "Alice"), not(equalTo(new Employee(7, "Someone Else"))));
        assertFalse(new Employee(7, "Alice").equals("some string"));
        assertFalse(new Employee(7, "Alice").equals(null));
    }

    @Test
    public void hashCodeProducesId() {
        assertThat(new Employee(7, "Alice").hashCode(), is(7));
    }

    @Test
    public void testToString() {
        new Employee().toString();
    }
}

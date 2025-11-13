package io.github.sboe0705.sample.vaadin.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public record Author(Long id, String firstName, String lastName, LocalDate birthday, List<Book> books) {

    public String fullName() {
        return firstName() + " " + lastName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;
        return id.equals(author.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("Author{%s %s}", firstName, lastName);
    }
}

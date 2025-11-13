package io.github.sboe0705.sample.vaadin.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record Book(Long id, String title, Author author, int yearPublished) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("Book{%s}", title);
    }

}

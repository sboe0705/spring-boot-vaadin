package io.github.sboe0705.sample.vaadin.data;

import io.github.sboe0705.sample.vaadin.model.Author;
import io.github.sboe0705.sample.vaadin.model.Book;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {

    private final List<Author> authors = new ArrayList<>();

    private final List<Book> books = new ArrayList<>();

    public LibraryService() {
        List<Book> harryPotterBooks = new ArrayList<>();
        Author rowling = new Author(0L, "Joanne", "Rowling", LocalDate.of(1965, 7, 31), harryPotterBooks);
        harryPotterBooks.add(new Book(0L, "Philosopher's Stone", rowling, 1997));
        harryPotterBooks.add(new Book(1L, "Chamber of Secrets", rowling, 1998));
        harryPotterBooks.add(new Book(2L, "Prisoner of Azkaban", rowling, 1999));
        harryPotterBooks.add(new Book(3L, "Goblet of Fire", rowling, 2000));
        harryPotterBooks.add(new Book(4L, "Order of the Phoenix", rowling, 2003));
        harryPotterBooks.add(new Book(5L, "Half-Blood Prince", rowling, 2005));
        harryPotterBooks.add(new Book(6L, "Deathly Hallows", rowling, 2007));
        authors.add(rowling);
        books.addAll(harryPotterBooks);

        List<Book> lordOfTheRingsBooks = new ArrayList<>();
        Author tolkien = new Author(1L, "John Ronald Reuel", "tolkien", LocalDate.of(1892, 9, 2), lordOfTheRingsBooks);
        lordOfTheRingsBooks.add(new Book(7L, "The Hobbit", tolkien, 1937));
        lordOfTheRingsBooks.add(new Book(8L, "The Fellowship of the Ring", tolkien, 1954));
        lordOfTheRingsBooks.add(new Book(9L, "The Two Towers", tolkien, 1954));
        lordOfTheRingsBooks.add(new Book(10L, "The Return of the King", tolkien, 1955));
        authors.add(tolkien);
        books.addAll(lordOfTheRingsBooks);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Author getAuthorById(Long id) {
        return getAuthors().stream()
                .filter(author -> id.equals(author.id()))
                .findFirst().get();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Book> getBooksByAuthorId(Long authorId) {
        return getBooks().stream()
                .filter(book -> authorId.equals(book.author().id()))
                .toList();
    }

}

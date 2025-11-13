package io.github.sboe0705.sample.vaadin.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import io.github.sboe0705.sample.vaadin.data.LibraryService;
import io.github.sboe0705.sample.vaadin.model.Author;
import io.github.sboe0705.sample.vaadin.model.Book;

import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.ARROW_LEFT;

@Route
public class BookView extends VerticalLayout implements BeforeEnterObserver {

    private final LibraryService libraryService;

    private final H1 heading;

    private final Grid<Book> grid;

    public BookView(LibraryService libraryService) {
        this.libraryService = libraryService;
        add(new Button(new Icon(ARROW_LEFT), e -> UI.getCurrent().getPage().getHistory().back()));
        heading = new H1("Books");
        add(heading);
        grid = new Grid<>(Book.class);
        grid.removeAllColumns();
        grid.addColumns("title", "yearPublished");
        add(grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Map<String, List<String>> parameters = event.getLocation()
                .getQueryParameters()
                .getParameters();
        if (parameters.containsKey("authorId")) {
            Long authorId = Long.valueOf(parameters.get("authorId").getFirst());
            listBooks(authorId);
        }
    }

    private void listBooks(Long authorId) {
        if (authorId == null) {
            heading.setText("Books");
            grid.setItems(List.of());
        } else {
            Author author = libraryService.getAuthorById(authorId);
            heading.setText(author.fullName() + "'s Books");
            List<Book> books = libraryService.getBooksByAuthorId(authorId);
            grid.setItems(books);
        }
    }

}

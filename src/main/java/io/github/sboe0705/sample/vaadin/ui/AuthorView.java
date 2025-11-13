package io.github.sboe0705.sample.vaadin.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.github.sboe0705.sample.vaadin.data.LibraryService;
import io.github.sboe0705.sample.vaadin.model.Author;

import java.util.List;
import java.util.Map;

@Route("")
public class AuthorView extends VerticalLayout {

    private final LibraryService libraryService;

    private final Grid<Author> grid;

    public AuthorView(LibraryService libraryService) {
        this.libraryService = libraryService;
        H1 heading = new H1("Authors");
        heading.setId("authors-heading");
        add(new HorizontalLayout(heading, new Button(new Icon(VaadinIcon.REFRESH), e1 -> listAuthors())));
        grid = new Grid<>(Author.class);
        grid.removeAllColumns();
        grid.addColumns("firstName", "lastName", "birthday");
        grid.addItemDoubleClickListener(e -> {
            Long authorId = e.getItem().id();
            QueryParameters params = QueryParameters.simple(Map.of("authorId", "" + authorId));
            UI.getCurrent().navigate(BookView.class, params);
        });
        add(grid);
        listAuthors();
    }

    private void listAuthors() {
        List<Author> authors = libraryService.getAuthors();
        grid.setItems(authors);
    }

}

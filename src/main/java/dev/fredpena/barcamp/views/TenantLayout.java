package dev.fredpena.barcamp.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import dev.fredpena.barcamp.security.AuthenticatedUser;

/**
 * The main view is a top-level placeholder for other views.
 */
public class TenantLayout extends AppLayout {

    private final transient AuthenticatedUser authenticatedUser;

    public TenantLayout(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        addToNavbar(createHeaderContent());
        setDrawerOpened(false);
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        H1 appName = new H1("BarCamp - 2023");
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
        layout.add(appName);


        Span signOut = new Span("Sign out");
        signOut.addClickListener(event -> authenticatedUser.logout());
        signOut.getStyle().set("cursor", "pointer");


        Div div = new Div();
        div.add(signOut);
        div.getElement().getStyle().set("display", "flex");
        div.getElement().getStyle().set("gap", "var(--lumo-space-s)");

        layout.add(div);

        header.add(layout);

        return header;
    }


}

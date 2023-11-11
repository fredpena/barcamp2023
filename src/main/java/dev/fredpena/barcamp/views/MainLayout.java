package dev.fredpena.barcamp.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import dev.fredpena.barcamp.config.TenantContext;
import dev.fredpena.barcamp.data.tenant.entity.User;
import dev.fredpena.barcamp.security.AuthenticatedUser;
import dev.fredpena.barcamp.views.person.PersonView;
import org.springframework.util.StringUtils;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

    }

    private final transient AuthenticatedUser authenticatedUser;
    private final transient AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        addToNavbar(createHeaderContent());
        setDrawerOpened(false);
    }

    private Component createHeaderContent() {
        Header header = new Header();

        if (StringUtils.hasText(TenantContext.getCurrentTenant())) {
            header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

            Div layout = new Div();
            layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

            H1 appName = new H1("BarCamp - 2023");
            appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
            layout.add(appName);

            Optional<User> maybeUser = authenticatedUser.get();
            if (maybeUser.isPresent()) {
                User user = maybeUser.get();

                Avatar avatar = new Avatar(user.getName());
                StreamResource resource = new StreamResource("profile-pic",
                        () -> new ByteArrayInputStream(user.getProfilePicture() != null ? user.getProfilePicture() : new byte[]{}));
                avatar.setImageResource(resource);
                avatar.setThemeName("xsmall");
                avatar.getElement().setAttribute("tabindex", "-1");

                MenuBar userMenu = new MenuBar();
                userMenu.setThemeName("tertiary-inline contrast");

                MenuItem userName = userMenu.addItem("");

                authenticatedUser.userHasSomeTenant().ifPresent(some -> {
                    if (some) {
                        userName.getSubMenu().addItem("Change company", e -> {
                            authenticatedUser.clearSession();
                            UI.getCurrent().navigate("");
                        });
                    }
                });

                Div div = new Div();
                div.add(avatar);
                div.add(user.getName());
                div.add(new Icon("lumo", "dropdown"));
                div.getElement().getStyle().set("display", "flex");
                div.getElement().getStyle().set("align-items", "center");
                div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
                userName.add(div);
                userName.getSubMenu().addItem("Sign out", e -> authenticatedUser.logout());


                layout.add(userMenu);
            } else {
                Anchor loginLink = new Anchor("login", "Sign in");
                layout.add(loginLink);
            }

            Nav nav = new Nav();
            nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);

            // Wrap the links in a list; improves accessibility
            UnorderedList list = new UnorderedList();
            list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
            nav.add(list);

            for (MenuItemInfo menuItem : createMenuItems()) {
                if (accessChecker.hasAccess(menuItem.getView())) {
                    list.add(menuItem);
                }

            }

            header.add(layout, nav);
        }

        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //

                new MenuItemInfo("Persons", LineAwesomeIcon.USER.create(), PersonView.class), //

        };
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!StringUtils.hasText(TenantContext.getCurrentTenant())) {
            event.forwardTo("");
        }
    }

}

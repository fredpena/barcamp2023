package dev.fredpena.barcamp.views.person;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.fredpena.barcamp.data.tenant.entity.Person;
import dev.fredpena.barcamp.data.tenant.service.PersonService;
import dev.fredpena.barcamp.views.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PageTitle("List - Person")
@Route(value = "persons", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class PersonView extends Div {

    private Grid<Person> grid;

    private final Filters filters;
    private final transient PersonService personService;

    private final transient FormView formView;

    public PersonView(PersonService personService) {
        this.personService = personService;
        this.formView = new FormView(this, personService);

        setSizeFull();
        addClassNames("list-view");

        filters = new Filters(this::refreshGrid);
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public class Filters extends Div implements Specification<Person> {

        private final TextField name = new TextField("Person");
        private final TextField phone = new TextField("Contact");
        private final TextField occupation = new TextField("Occupation");
        private final DatePicker startDate = new DatePicker("Date of Birth");
        private final DatePicker endDate = new DatePicker();
        private final MultiSelectComboBox<String> roles = new MultiSelectComboBox<>("Role");
        private final CheckboxGroup<Boolean> importants = new CheckboxGroup<>("Important");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            name.setPlaceholder("First or last name");
            phone.setPlaceholder("Email or phone");


            roles.setItems("Worker", "Supervisor", "Manager", "External");

            importants.setItemLabelGenerator(l -> StringUtils.capitalize(l.toString()));
            importants.setItems(Boolean.TRUE, Boolean.FALSE);
            importants.addClassName("double-width");

            Arrays.asList(name, phone, occupation).forEach(c -> {
                c.setValueChangeMode(ValueChangeMode.EAGER);
                c.addValueChangeListener(e -> onSearch.run());
                c.setClearButtonVisible(true);
            });

            Arrays.asList(startDate, endDate).forEach(c -> {
                c.addValueChangeListener(e -> onSearch.run());
                c.setClearButtonVisible(true);
            });

            roles.addValueChangeListener(e -> onSearch.run());
            roles.setClearButtonVisible(true);
            importants.addValueChangeListener(e -> onSearch.run());

            // Action buttons
            Button addBtn = new Button("New Person", VaadinIcon.PLUS.create());
            addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            addBtn.addClickListener(event -> {
                Dialog dialog = formView.createDialog("New Person", null, PersonView.this::refreshGrid);
                dialog.open();
                add(dialog);
            });

            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            resetBtn.addClickListener(e -> {
                name.clear();
                phone.clear();
                startDate.clear();
                endDate.clear();
                occupation.clear();
                roles.clear();
                importants.clear();
                onSearch.run();
            });


            Div actions = new Div(resetBtn, addBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(name, phone, createDateRangeFilter(), occupation, roles, importants, actions);
        }

        private Component createDateRangeFilter() {
            startDate.setPlaceholder("From");

            endDate.setPlaceholder("To");

            // For screen readers
            startDate.setAriaLabel("From date");
            endDate.setAriaLabel("To date");

            FlexLayout dateRangeComponent = new FlexLayout(startDate, new Text(" – "), endDate);
            dateRangeComponent.setAlignItems(FlexComponent.Alignment.BASELINE);
            dateRangeComponent.addClassName(LumoUtility.Gap.XSMALL);

            return dateRangeComponent;
        }

        @Override
        public Predicate toPredicate(@NotNull Root<Person> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!name.isEmpty()) {
                String lowerCaseFilter = name.getValue().toLowerCase();
                Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        lowerCaseFilter + "%");

                Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                        lowerCaseFilter + "%");
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
            }
            if (!phone.isEmpty()) {
                String ignore = "- ()";

                String lowerCaseFilter = ignoreCharacters(ignore, phone.getValue().toLowerCase());
                Predicate phoneMatch = criteriaBuilder.like(
                        ignoreCharacters(ignore, criteriaBuilder, criteriaBuilder.lower(root.get("phone"))),
                        "%" + lowerCaseFilter + "%");

                Predicate emailMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        lowerCaseFilter + "%");

                predicates.add(criteriaBuilder.or(phoneMatch, emailMatch));

            }
            if (startDate.getValue() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"),
                        criteriaBuilder.literal(startDate.getValue())));
            }
            if (endDate.getValue() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.literal(endDate.getValue()),
                        root.get("dateOfBirth")));
            }

            if (!occupation.isEmpty()) {
                String lowerCaseFilter = occupation.getValue().toLowerCase();
                Predicate occupationMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("occupation")),
                        lowerCaseFilter + "%");
                predicates.add(occupationMatch);
            }

            if (!roles.isEmpty()) {
                List<Predicate> rolePredicates = new ArrayList<>();
                for (String role : roles.getValue()) {
                    rolePredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(role), root.get("role")));
                }
                predicates.add(criteriaBuilder.or(rolePredicates.toArray(Predicate[]::new)));
            }

            if (!importants.isEmpty()) {
                List<Predicate> rolePredicates = new ArrayList<>();
                for (Boolean important : importants.getValue()) {
                    rolePredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(important), root.get("important")));
                }
                predicates.add(criteriaBuilder.or(rolePredicates.toArray(Predicate[]::new)));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

        private String ignoreCharacters(String characters, String in) {
            String result = in;
            for (int i = 0; i < characters.length(); i++) {
                result = result.replace("" + characters.charAt(i), "");
            }
            return result;
        }

        private Expression<String> ignoreCharacters(String characters, CriteriaBuilder criteriaBuilder,
                                                    Expression<String> inExpression) {
            Expression<String> expression = inExpression;
            for (int i = 0; i < characters.length(); i++) {
                expression = criteriaBuilder.function("replace", String.class, expression,
                        criteriaBuilder.literal(characters.charAt(i)), criteriaBuilder.literal(""));
            }
            return expression;
        }

    }


    private void edit(Person r) {
        Dialog dialog = formView.createDialog("Edit Person", r, this::refreshGrid);
        dialog.open();
        add(dialog);
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }


    private Component createGrid() {
        grid = new Grid<>(Person.class, false);
        grid.addColumn(createNameRenderer()).setHeader("Person").setAutoWidth(true);
        grid.addColumn(createContactRenderer()).setHeader("Contact").setAutoWidth(true);
        grid.addColumn(new LocalDateRenderer<>(Person::getDateOfBirth, "MMM d, YYYY")).setHeader("Birth date").setAutoWidth(true);
        grid.addColumn(Person::getOccupation).setHeader("Occupation").setAutoWidth(true);
        grid.addColumn(Person::getRole).setHeader("Role").setAutoWidth(true);
        grid.addComponentColumn(c -> createPermissionIcon(c.isImportant())).setHeader("Is important").setAutoWidth(true);
        grid.addColumn(createActionButton(this::edit));

        grid.setItems(query -> personService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                filters).stream());
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }


    private static Renderer<Person> createActionButton(SerializableConsumer<Person> consumer) {
        return new ComponentRenderer<>(Button::new, (button, p) -> {
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
//            button.addClassName(LumoUtility.FontSize.XXSMALL);
//            button.getStyle()
//                    .set("--lumo-button-size", "var(--lumo-size-xxs)");
            button.addClickListener(e -> consumer.accept(p));
            button.setIcon(LumoIcon.EDIT.create());
        });
    }

    private static Icon createPermissionIcon(boolean isImportant) {
        Icon icon;
        if (isImportant) {
            icon = createIcon(VaadinIcon.CHECK, "Yes");
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = createIcon(VaadinIcon.CLOSE_SMALL, "No");
            icon.getElement().getThemeList().add("badge error");
        }
        return icon;
    }

    private static Icon createIcon(VaadinIcon vaadinIcon, String label) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", label);
        // Tooltip
        icon.getElement().setAttribute("title", label);
        return icon;
    }

    private static Renderer<Person> createNameRenderer() {
        return LitRenderer.<Person>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "<vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.lastName}\" alt=\"User avatar\"></vaadin-avatar>"
                        + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                        + "    <span style=\"font-size: var(--lumo-font-size-xs); color: var(--lumo-primary-text-color);\">"
                        + "      ${item.firstName}" + "    </span>"
                        + "    <span style=\"font-size: var(--lumo-font-size-m); \">"
                        + "      ${item.lastName}" + "    </span>"
                        + "  </vaadin-vertical-layout>"
                        + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", Person::getFirstName)
                .withProperty("firstName", Person::getFirstName)
                .withProperty("lastName", Person::getLastName);
    }

    private static Renderer<Person> createContactRenderer() {
        return LitRenderer.<Person>of(
                        "<vaadin-vertical-layout style=\"width: 100%; font-size: var(--lumo-font-size-s); line-height: var(--lumo-line-height-m);\">"
                        + " <a href=\"mailto:${item.email}\" style=\"display: ${item.emailDisplay}; align-items: center;\">"
                        + "     <vaadin-icon icon=\"vaadin:${item.emailIcon}\" style=\"margin-inline-end: var(--lumo-space-xs); width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s);\"></vaadin-icon>"
                        + "     <span>${item.email}</span>"
                        + " </a>"
                        + " "
                        + " <a href=\"tel:${item.phone}\" style=\"display: ${item.phoneDisplay}; align-items: center;\">"
                        + "     <vaadin-icon icon=\"vaadin:${item.phoneIcon}\" style=\"margin-inline-end: var(--lumo-space-xs); width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s);\"></vaadin-icon>"
                        + "     <span>${item.phone}</span>"
                        + " </a>"
                        + "</vaadin-vertical-layout>")
                .withProperty("email", r -> r.getEmail() != null ? r.getEmail() : "")
                .withProperty("emailIcon", r -> r.getEmail() != null ? "envelope" : "")
                .withProperty("emailDisplay", r -> r.getEmail() != null ? "flex" : "none")
                .withProperty("phone", r -> r.getPhone() != null ? r.getPhone() : "")
                .withProperty("phoneIcon", r -> r.getPhone() != null ? "phone" : "")
                .withProperty("phoneDisplay", r -> r.getPhone() != null ? "flex" : "none");
    }

}

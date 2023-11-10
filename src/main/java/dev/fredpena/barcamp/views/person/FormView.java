package dev.fredpena.barcamp.views.person;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.fredpena.barcamp.componect.ConfirmationDialog;
import dev.fredpena.barcamp.componect.CustomNotification;
import dev.fredpena.barcamp.data.tenant.entity.Person;
import dev.fredpena.barcamp.data.tenant.service.PersonService;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class FormView {

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final EmailField email = new EmailField("Email");
    private final TextField phone = new TextField("Phone Number");
    private final DatePicker dateOfBirth = new DatePicker("Birthday");
    private final TextField occupation = new TextField("Occupation");
    private final ComboBox<String> fieldRole = new ComboBox<>("Role");
    private final Checkbox important = new Checkbox("Is important?");

    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
private final HtmlContainer parent;
    private final PersonService personService;
    private final CustomNotification notification;
    private Person element;
    private final Binder<Person> binder;

    public FormView(HtmlContainer parent, PersonService personService) {
        this.parent = parent;
        this.personService = personService;
        this.notification = new CustomNotification();

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        fieldRole.setItems("Worker", "Supervisor", "Manager", "External");

        Map<String, Component> components = new HashMap<>();
        components.put("firstName", firstName);
        components.put("lastName", lastName);
        components.put("email", email);
        components.put("phone", phone);
        components.put("dateOfBirth", dateOfBirth);
        components.put("role", fieldRole);
        configureProperties(components, Person.class);


        // Bind fields. This is where you'd define e.g. validation rules
        // Configure Form
        binder = new BeanValidationBinder<>(Person.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        binder.forField(fieldRole)
                .asRequired("This field can not be blank.")
                .bind(Person::getRole, Person::setRole);


        save.addClickListener(this::saveOrUpdate);

        delete.addClickListener(this::delete);

    }

    private void saveOrUpdate(ClickEvent<Button> buttonClickEvent) {
        try {
            if (this.element == null) {
                this.element = new Person();
            }

            binder.writeBean(this.element);

            ConfirmationDialog confirmation = new ConfirmationDialog("Please confirm", "Are you sure you want to execute this action?",
                    d -> {
                        d.close();

                        personService.update(this.element);
                        notification.notificationSuccess();
//                        binder.setBean(this.element);
                    });
            confirmation.show(parent);


        } catch (ValidationException validationException) {
            notification.notificationError(validationException);
        }
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {

        ConfirmationDialog confirmation = new ConfirmationDialog("Please confirm", "Are you sure you want to delete this record?",
                d -> {
                    d.close();

                    personService.update(this.element);
                    notification.notificationSuccess();
                });
        confirmation.show(parent);
    }

    public Dialog createDialog(String title, Person element) {
        this.element = element;
        binder.setBean(element);
        delete.setVisible(element != null);

        Dialog dialog = new Dialog();
        dialog.setWidth("60%");
        dialog.setCloseOnOutsideClick(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setOpened(true);
        dialog.setHeaderTitle(title);
        dialog.add(createFormLayout(), new Hr(), createButtonLayout());

        cancel.addClickListener(event -> dialog.close());

        return dialog;
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.addClassNames(LumoUtility.Padding.SMALL);

        formLayout.add(firstName, lastName, email, phone, dateOfBirth, occupation, fieldRole, important);
        return formLayout;
    }

    private Component createButtonLayout() {
        Div deleteDiv = new Div();
        deleteDiv.getStyle().set("margin-inline-end", "auto");
        deleteDiv.add(delete);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.FlexWrap.WRAP, LumoUtility.JustifyContent.CENTER);
        buttonLayout.add(deleteDiv, cancel, save);
        buttonLayout.setAlignItems(FlexComponent.Alignment.END);
        buttonLayout.addClassNames(LumoUtility.Background.CONTRAST_5);
        return buttonLayout;
    }

    public void configureProperties(Map<String, Component> components, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                final Optional<Component> component = components.entrySet().stream()
                        .filter(f -> f.getKey().equals(field.getName()))
                        .map(Map.Entry::getValue).findFirst();

                if (component.isPresent()) {
                    final Column column = field.getAnnotation(Column.class);

                    component.get().getElement().setProperty("maxlength", column.length());
                }
            }
            if (field.isAnnotationPresent(NotNull.class)) {
                final Optional<Component> component = components.entrySet().stream().filter(f -> f.getKey().equals(field.getName()))
                        .map(Map.Entry::getValue).findFirst();

                component.ifPresent(comp -> comp.getElement().setProperty("required", true));
            }
        }
        components.forEach((key, value) -> value.getElement().setProperty("clearButtonVisible", true));
    }
}

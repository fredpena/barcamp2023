package dev.fredpena.barcamp.componect;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

public class ConfirmationDialog extends Dialog {

    private H2 title;
    private Paragraph question;
    private final transient Consumer<Dialog> listener;

    public ConfirmationDialog(Consumer<Dialog> listener) {
        super();
        super.setModal(true);
        this.listener = listener;
        setTitle("Confirm:");
        setQuestion("Are you sure you want to execute this action?");

        final VerticalLayout dialogLayout = createDialogLayout();
        super.add(dialogLayout);
    }

    public ConfirmationDialog(String title, String content, Consumer<Dialog> listener) {
        super();
        super.setModal(true);
        this.listener = listener;
        setTitle(title);
        setQuestion(content);

        final VerticalLayout dialogLayout = createDialogLayout();
        super.add(dialogLayout);
    }

    public void setTitle(String title) {
        this.title = new H2(title);
        this.title.getStyle().set("margin", "var(--lumo-space-m) 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
    }

    public void setQuestion(String question) {
        this.question = new Paragraph(question);
    }

    public void show(HtmlContainer parent) {
        super.open();
        parent.add(this);
    }

    private VerticalLayout createDialogLayout() {
        final Button abort = new Button("Cancel");
        abort.addClickListener(e -> super.close());

        final Button confirm = new Button("Confirm", event -> listener.accept(this));
        confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        final HorizontalLayout buttonLayout = new HorizontalLayout(abort, confirm);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        final VerticalLayout dialogLayout = new VerticalLayout(title, question, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

}
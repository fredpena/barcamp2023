package dev.fredpena.barcamp.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.fredpena.barcamp.config.TenantContext;
import dev.fredpena.barcamp.data.common.entity.Tenant;
import dev.fredpena.barcamp.data.common.service.TenantService;
import dev.fredpena.barcamp.security.CustomUserDetails;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Optional;

@PageTitle("Tenant")
@Route(value = "tenant", layout = TenantLayout.class)
@RouteAlias(value = "", layout = TenantLayout.class)
@PermitAll
public class TenantView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticationContext authenticationContext;
    private final TenantService tenantService;


    public TenantView(AuthenticationContext authenticationContext, TenantService tenantService) {
        this.authenticationContext = authenticationContext;
        this.tenantService = tenantService;
        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassNames(LumoUtility.Padding.XLARGE);
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (StringUtils.hasText(TenantContext.getCurrentTenant())) {
            event.forwardTo("persons");
        }

        Optional<CustomUserDetails> commonUsers = authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(ud -> (CustomUserDetails) ud);

        var variants = new ButtonVariant[]{ButtonVariant.LUMO_PRIMARY};
        var width = "200px";
        var height = "100px";

        commonUsers.ifPresent(user -> {

            if (user.getTenants().size() == 1) {
                VaadinSession.getCurrent().setAttribute(Tenant.class, user.getTenants().iterator().next());

                event.forwardTo("persons");
            } else {

                HorizontalLayout layout = new HorizontalLayout();
                layout.addClassNames(LumoUtility.AlignItems.END, LumoUtility.FlexWrap.WRAP, LumoUtility.JustifyContent.CENTER);
                layout.setMaxWidth("900px");

                add(layout);

                user.getTenants().forEach(tenant -> {


                    VerticalLayout tenantLayout = new VerticalLayout();
                    tenantLayout.addClassNames(LumoUtility.BoxShadow.SMALL, LumoUtility.BorderRadius.SMALL, LumoUtility.AlignItems.CENTER);
                    tenantLayout.setWidth("400px");
                    tenantLayout.setHeight("230px");
                    tenantLayout.getStyle().set("cursor", "pointer");
                    tenantLayout.addSingleClickListener(event1 -> {
                        VaadinSession.getCurrent().setAttribute(Tenant.class, tenant);

                        UI.getCurrent().navigate("persons");
                    });

                    Image img = new Image(tenant.getLogo(), tenant.getName());
                    img.setWidth("100px");
                    tenantLayout.add(img);

                    H4 name = new H4(tenant.getName());
                    H6 slogan = new H6(tenant.getSlogan());
                    H5 type = new H5(tenant.getType());


                    tenantLayout.add(name, type, slogan);


                    layout.add(tenantLayout);

//                    Button component = new Button(tenant.getName());
//                    component.addThemeVariants(variants);
//                    component.setWidth(width);
//                    component.setHeight(height);
//                    component.addClickListener(e -> {
//                        VaadinSession.getCurrent().setAttribute(Tenant.class, tenant);
//
//                        UI.getCurrent().navigate("persons");
//                    });
//                    add(component);
                });

            }

        });
    }
}

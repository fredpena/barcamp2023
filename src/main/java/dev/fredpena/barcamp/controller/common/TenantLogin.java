package dev.fredpena.barcamp.controller.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TenantLogin {


    @NotNull
    @JsonProperty("tenant_id")
    private String tenantId;
}

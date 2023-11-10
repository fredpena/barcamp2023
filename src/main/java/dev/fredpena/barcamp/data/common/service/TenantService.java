package dev.fredpena.barcamp.data.common.service;

import com.vaadin.flow.component.page.Page;
import dev.fredpena.barcamp.data.common.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository repository;

    public Set<Tenant> findTenantByUser(String username) {
        return repository.findByUsername(username);
    }

    public Optional<Tenant> findTenant(String code) {
        return repository.findById(code);
    }

    public List<Tenant> findAll(){
        return repository.findAll();
    }

}

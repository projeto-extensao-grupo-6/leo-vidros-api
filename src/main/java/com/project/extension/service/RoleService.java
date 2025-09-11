package com.project.extension.service;

import com.project.extension.entity.Role;
import com.project.extension.exception.naoencontrado.RoleNaoEncontradoException;
import com.project.extension.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public Role buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome)
                .orElseThrow(RoleNaoEncontradoException::new);
    }
}

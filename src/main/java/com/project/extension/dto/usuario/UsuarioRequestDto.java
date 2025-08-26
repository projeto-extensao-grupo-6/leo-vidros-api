package com.project.extension.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDto (

        @NotNull
         String nome,

         @NotNull
          String email,

         @NotNull
         String cpg,

         @NotNull
          String senha,

        @NotNull
        String role

) {}
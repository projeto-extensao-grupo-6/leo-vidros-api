package com.project.extension.controller.excel;

import com.project.extension.dto.excel.ExcelImportResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Excel", description= "Inserção de XSLX no banco")
public interface ExcelControllerDoc {

    @PostMapping(
            path = "/import/clientes",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Importação de XLSX no banco",
            description = "Insere dados de Cliente com origem em um arquivo XLSX no banco"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planilha importada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Formato de arquivo inválido"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar o arquivo")
    })
    ResponseEntity<ExcelImportResponseDto> inserirPlanilhaCliente(
            @RequestParam("file") MultipartFile file
    );

}

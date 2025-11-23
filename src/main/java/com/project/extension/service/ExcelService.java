package com.project.extension.service;

import com.project.extension.dto.excel.ExcelImportResponseDto;
import com.project.extension.entity.Cliente;
import com.project.extension.entity.Endereco;
import com.project.extension.entity.Status;
import com.project.extension.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final ClienteRepository clienteRepository;


    private String lerValorCelula(Row row, int index) {
        if (row.getCell(index) == null) {
            return "Não Informado";
        }

        try {
            return tratarVazios(row.getCell(index).getStringCellValue());
        } catch (Exception e) {
            return "Não Informado";
        }
    }

    public String tratarVazios(String valor){
        if(valor == null || valor.isBlank()){
            return "Não Informado";
        }
        return valor;
    }

    public ExcelImportResponseDto importarClientes(MultipartFile file) {
        List<Cliente> clientes = excelToClientes(file);

        clienteRepository.saveAll(clientes);
        return null;
    }

    public List<Cliente> excelToClientes(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Cliente> clientes = new ArrayList<>();

            Map<String, Cliente> clienteMap = new HashMap<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String nomeCliente = row.getCell(0).getStringCellValue();

                Cliente cliente = clienteMap.get(nomeCliente);

                if (cliente == null) {
                    cliente = new Cliente();
                    cliente.setNome(nomeCliente);
                    cliente.setTelefone(lerValorCelula(row,1));
                    cliente.setEmail(lerValorCelula(row, 2));
                    cliente.setStatus("Ativo");
                    cliente.setCpf("");

                }
                Endereco endereco = new Endereco();
                endereco.setRua(lerValorCelula(row, 3));
                endereco.setBairro(lerValorCelula(row, 4));
                endereco.setCep(lerValorCelula(row, 5));
                endereco.setComplemento(lerValorCelula(row, 6));
                endereco.setCidade(lerValorCelula(row, 7));
                endereco.setUf(lerValorCelula(row, 8));

                cliente.getEnderecos().add(endereco);
                clienteMap.put(nomeCliente, cliente);
            }

            return new ArrayList<>(clienteMap.values());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o Excel", e);
        }
    }

}
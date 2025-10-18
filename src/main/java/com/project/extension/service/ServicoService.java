//package com.project.extension.service;
//
//import com.project.extension.entity.Servico;
//import com.project.extension.entity.TipoMaterialAuxiliar;
//import com.project.extension.repository.ServicoRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class ServicoService {
//
//    private final ServicoRepository repository;
//
//    public Servico criarServico(Servico servico) {
//        double quantidadeVidro = calcularQuantidadeVidro(servico);
//        servico.setQuantidadeVidro(quantidadeVidro);
//
//        EstoqueVidro estoqueVidro = estoqueVidroService
//                .findByTipoVidro(servico.getTipoVidro())
//                .orElseThrow(() -> new RuntimeException("Vidro não encontrado no estoque"));
//
//        if (estoqueVidro.getQuantidadeEmM2() < quantidadeVidro) {
//            throw new RuntimeException("Vidro insuficiente no estoque");
//        }
//        estoqueVidro.setQuantidadeEmM2(estoqueVidro.getQuantidadeEmM2() - quantidadeVidro);
//        estoqueVidroRepository.save(estoqueVidro);
//
//        for (TipoMaterialAuxiliar material : servico.getTipoMaterialAuxiliares()) {
//            EstoqueMaterialAuxiliar estoqueMaterial = estoqueMaterialRepository
//                    .findByTipoMaterial(material)
//                    .orElseThrow(() -> new RuntimeException("Material " + material + " não encontrado no estoque"));
//
//            int qtdMaterial = calcularQuantidadeMaterial(servico, material);
//
//            if (estoqueMaterial.getQuantidade() < qtdMaterial) {
//                throw new RuntimeException("Material " + material + " insuficiente no estoque");
//            }
//
//            estoqueMaterial.setQuantidade(estoqueMaterial.getQuantidade() - qtdMaterial);
//            estoqueMaterialRepository.save(estoqueMaterial);
//        }
//
//        return servicoRepository.save(servico);
//    }
//
//    public List<Servico> listarServicos() {
//        return servicoRepository.findAll();
//    }
//
//    public Servico buscarPorId(Integer id) {
//        return servicoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
//    }
//
//    public void deletarServico(Integer id) {
//        servicoRepository.deleteById(id);
//    }
//
//    private double calcularQuantidadeVidro(Servico servico) {
//        if (servico.getAltura() == null || servico.getLargura() == null) return 0.0;
//        return servico.getAltura() * servico.getLargura(); // m²
//    }
//
//    private int calcularQuantidadeMaterial(Servico servico, TipoMaterialAuxiliar material) {
//        double area = servico.getAltura() * servico.getLargura();
//        switch (servico.getTipoServico()) {
//            case ESQUADRIAS:
//                if (material == TipoMaterialAuxiliar.ALUMINIO || material == TipoMaterialAuxiliar.MADEIRA) {
//                    return (int) Math.ceil(2 * (servico.getAltura() + servico.getLargura())); // perímetro aproximado
//                }
//                break;
//            case GUARDA_CORPOS:
//                if (material == TipoMaterialAuxiliar.ACO_INOX) {
//                    return (int) Math.ceil(area * 4); // suportes por m²
//                }
//                break;
//            default:
//                return 1;
//        }
//        return 0;
//    }
//}

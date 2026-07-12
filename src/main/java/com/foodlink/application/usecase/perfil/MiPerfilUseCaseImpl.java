package com.foodlink.application.usecase.perfil;

import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.application.dto.response.CompradorResponse;
import com.foodlink.application.dto.response.MiPerfilResponse;
import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.comprador.Comprador;
import com.foodlink.domain.port.input.MiPerfilUseCase;
import com.foodlink.domain.port.output.IRepositorioBeneficiario;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.domain.port.output.IRepositorioComprador;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MiPerfilUseCaseImpl implements MiPerfilUseCase {

    private final IRepositorioComercio repositorioComercio;
    private final IRepositorioBeneficiario repositorioBeneficiario;
    private final IRepositorioComprador repositorioComprador;

    public MiPerfilUseCaseImpl(
            IRepositorioComercio repositorioComercio,
            IRepositorioBeneficiario repositorioBeneficiario,
            IRepositorioComprador repositorioComprador) {
        this.repositorioComercio = repositorioComercio;
        this.repositorioBeneficiario = repositorioBeneficiario;
        this.repositorioComprador = repositorioComprador;
    }

    @Override
    public MiPerfilResponse obtenerMiPerfil(UUID usuarioId, String tipoUsuario) {
        return switch (tipoUsuario) {
            case "COMERCIO" -> {
                Comercio comercio = repositorioComercio.buscarPorId(usuarioId)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
                yield new MiPerfilResponse(comercio.getId(), comercio.getEmail().valor(), "COMERCIO",
                        "ROLE_COMERCIO", toResponse(comercio));
            }
            case "BENEFICIARIO" -> {
                Beneficiario beneficiario = repositorioBeneficiario.buscarPorId(usuarioId)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
                yield new MiPerfilResponse(beneficiario.getId(), beneficiario.getEmail().valor(), "BENEFICIARIO",
                        "ROLE_BENEFICIARIO", toResponse(beneficiario));
            }
            case "COMPRADOR" -> {
                Comprador comprador = repositorioComprador.buscarPorId(usuarioId)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
                yield new MiPerfilResponse(comprador.getId(), comprador.getEmail().valor(), "COMPRADOR",
                        "ROLE_COMPRADOR", toResponse(comprador));
            }
            default -> throw new IllegalArgumentException("Tipo de usuario no reconocido: " + tipoUsuario);
        };
    }

    private ComercioResponse toResponse(Comercio comercio) {
        return new ComercioResponse(
                comercio.getId(),
                comercio.getRuc().valor(),
                comercio.getNombre().valor(),
                comercio.getTelefono().valor(),
                comercio.getEmail().valor(),
                comercio.getEstado().name(),
                comercio.getFechaRegistro()
        );
    }

    private BeneficiarioResponse toResponse(Beneficiario beneficiario) {
        return new BeneficiarioResponse(
                beneficiario.getId(),
                beneficiario.getNombre().valor(),
                beneficiario.getRuc().valor(),
                beneficiario.getEmail().valor(),
                beneficiario.getEstadoVerificacion().name(),
                beneficiario.getFechaRegistro()
        );
    }

    private CompradorResponse toResponse(Comprador comprador) {
        return new CompradorResponse(
                comprador.getId(),
                comprador.getCedula().valor(),
                comprador.getNombre().valor(),
                comprador.getApellido().valor(),
                comprador.getEmail().valor(),
                comprador.estaActivo(),
                comprador.getFechaRegistro()
        );
    }
}

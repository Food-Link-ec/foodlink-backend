package com.foodlink.application.usecase.perfil;

import com.foodlink.application.dto.response.MiPerfilResponse;
import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.comprador.Comprador;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.port.output.IRepositorioBeneficiario;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.domain.port.output.IRepositorioComprador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MiPerfilUseCaseTest {

    private static final String RUC_VALIDO = "1792146739001";
    private static final String CEDULA_VALIDA = "1710034065";

    @Mock
    private IRepositorioComercio repositorioComercio;

    @Mock
    private IRepositorioBeneficiario repositorioBeneficiario;

    @Mock
    private IRepositorioComprador repositorioComprador;

    @InjectMocks
    private MiPerfilUseCaseImpl useCase;

    private Direccion direccionValida() {
        return new Direccion("Av. Amazonas N34-451", "Quito", "Pichincha", "Cerca del parque", null, null);
    }

    private Comercio comercioValido() {
        return Comercio.crear(RUC_VALIDO, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());
    }

    private Beneficiario beneficiarioValido() {
        return Beneficiario.registrar("Fundación Manos Unidas", RUC_VALIDO,
                "contacto@manosunidas.org", "0991234567", direccionValida());
    }

    private Comprador compradorValido() {
        return Comprador.registrar(CEDULA_VALIDA, "Carlos", "Mendoza", "carlos@gmail.com", "0991234567");
    }

    @Test
    void obtenerMiPerfilConComercioExistenteDeberiaRetornarTipoUsuarioComercio() {
        Comercio comercio = comercioValido();
        when(repositorioComercio.buscarPorId(comercio.getId())).thenReturn(Optional.of(comercio));

        MiPerfilResponse response = useCase.obtenerMiPerfil(comercio.getId(), "COMERCIO");

        assertEquals("COMERCIO", response.tipoUsuario());
    }

    @Test
    void obtenerMiPerfilConBeneficiarioExistenteDeberiaRetornarTipoUsuarioBeneficiario() {
        Beneficiario beneficiario = beneficiarioValido();
        when(repositorioBeneficiario.buscarPorId(beneficiario.getId())).thenReturn(Optional.of(beneficiario));

        MiPerfilResponse response = useCase.obtenerMiPerfil(beneficiario.getId(), "BENEFICIARIO");

        assertEquals("BENEFICIARIO", response.tipoUsuario());
    }

    @Test
    void obtenerMiPerfilConCompradorExistenteDeberiaRetornarTipoUsuarioComprador() {
        Comprador comprador = compradorValido();
        when(repositorioComprador.buscarPorId(comprador.getId())).thenReturn(Optional.of(comprador));

        MiPerfilResponse response = useCase.obtenerMiPerfil(comprador.getId(), "COMPRADOR");

        assertEquals("COMPRADOR", response.tipoUsuario());
    }

    @Test
    void obtenerMiPerfilConComercioNoEncontradoDeberiaLanzarIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.obtenerMiPerfil(id, "COMERCIO"));
    }

    @Test
    void obtenerMiPerfilConTipoDesconocidoDeberiaLanzarIllegalArgumentException() {
        UUID id = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> useCase.obtenerMiPerfil(id, "ADMIN"));
    }

    @Test
    void miPerfilResponsePerfilNoDeberiaSerNuloParaComercio() {
        Comercio comercio = comercioValido();
        when(repositorioComercio.buscarPorId(comercio.getId())).thenReturn(Optional.of(comercio));

        MiPerfilResponse response = useCase.obtenerMiPerfil(comercio.getId(), "COMERCIO");

        assertNotNull(response.perfil());
    }

    @Test
    void miPerfilResponseRolDeberiaSerRoleComercioParaComercio() {
        Comercio comercio = comercioValido();
        when(repositorioComercio.buscarPorId(comercio.getId())).thenReturn(Optional.of(comercio));

        MiPerfilResponse response = useCase.obtenerMiPerfil(comercio.getId(), "COMERCIO");

        assertEquals("ROLE_COMERCIO", response.rol());
    }
}

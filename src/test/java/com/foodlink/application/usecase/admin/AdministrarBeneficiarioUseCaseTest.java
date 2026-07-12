package com.foodlink.application.usecase.admin;

import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.beneficiario.EstadoVerificacion;
import com.foodlink.domain.model.beneficiario.exception.BeneficiarioInvalidoException;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.port.output.IRepositorioBeneficiario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministrarBeneficiarioUseCaseTest {

    private static final String RUC = "1792146739001";

    @Mock
    private IRepositorioBeneficiario repositorioBeneficiario;

    @InjectMocks
    private AdministrarBeneficiarioUseCaseImpl useCase;

    private void mockGuardarEcheandoElMismoBeneficiario() {
        when(repositorioBeneficiario.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    private Direccion direccionValida() {
        return new Direccion("Av. 6 de Diciembre N32-100", "Quito", "Pichincha", "Junto al parque", null, null);
    }

    private Beneficiario beneficiarioEnEstado(EstadoVerificacion estado) {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Manos Unidas", RUC,
                "contacto@manosunidas.org", "0991234567", direccionValida());
        if (estado == EstadoVerificacion.VERIFICADO) {
            beneficiario.verificar();
        } else if (estado == EstadoVerificacion.RECHAZADO) {
            beneficiario.rechazar("motivo");
        }
        return beneficiario;
    }

    @Test
    void verificarConBeneficiarioPendienteDeberiaRetornarBeneficiarioResponseConEstadoVerificado() {
        Beneficiario beneficiario = beneficiarioEnEstado(EstadoVerificacion.PENDIENTE);
        when(repositorioBeneficiario.buscarPorId(beneficiario.getId())).thenReturn(Optional.of(beneficiario));
        mockGuardarEcheandoElMismoBeneficiario();

        BeneficiarioResponse response = useCase.verificar(beneficiario.getId());

        assertEquals(EstadoVerificacion.VERIFICADO.name(), response.estadoVerificacion());
    }

    @Test
    void verificarConBeneficiarioNoEncontradoDeberiaLanzarIllegalArgumentException() {
        UUID beneficiarioId = UUID.randomUUID();
        when(repositorioBeneficiario.buscarPorId(beneficiarioId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.verificar(beneficiarioId));
    }

    @Test
    void verificarConBeneficiarioYaVerificadoDeberiaLanzarBeneficiarioInvalidoException() {
        Beneficiario beneficiario = beneficiarioEnEstado(EstadoVerificacion.VERIFICADO);
        when(repositorioBeneficiario.buscarPorId(beneficiario.getId())).thenReturn(Optional.of(beneficiario));

        assertThrows(BeneficiarioInvalidoException.class, () -> useCase.verificar(beneficiario.getId()));
    }

    @Test
    void rechazarConBeneficiarioPendienteDeberiaRetornarBeneficiarioResponseConEstadoRechazado() {
        Beneficiario beneficiario = beneficiarioEnEstado(EstadoVerificacion.PENDIENTE);
        when(repositorioBeneficiario.buscarPorId(beneficiario.getId())).thenReturn(Optional.of(beneficiario));
        mockGuardarEcheandoElMismoBeneficiario();

        BeneficiarioResponse response = useCase.rechazar(beneficiario.getId());

        assertEquals(EstadoVerificacion.RECHAZADO.name(), response.estadoVerificacion());
    }

    @Test
    void listarPendientesDeberiaLlamarBuscarPorEstadoPendiente() {
        when(repositorioBeneficiario.buscarPorEstado(EstadoVerificacion.PENDIENTE)).thenReturn(List.of());

        useCase.listarPendientes();

        verify(repositorioBeneficiario, times(1)).buscarPorEstado(EstadoVerificacion.PENDIENTE);
    }

    @Test
    void listarTodosDeberiaLlamarBuscarTodos() {
        when(repositorioBeneficiario.buscarTodos()).thenReturn(List.of());

        useCase.listarTodos();

        verify(repositorioBeneficiario, times(1)).buscarTodos();
    }
}
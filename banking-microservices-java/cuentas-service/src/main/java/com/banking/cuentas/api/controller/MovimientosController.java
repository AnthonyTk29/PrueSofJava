package com.banking.cuentas.api.controller;

import com.banking.cuentas.application.dto.MovimientoCreateRequest;
import com.banking.cuentas.application.dto.MovimientoResponse;
import com.banking.cuentas.application.dto.MovimientoUpdateRequest;
import com.banking.cuentas.application.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@Tag(name = "Movimientos", description = "Gestión de movimientos bancarios")
public class MovimientosController {

    private final MovimientoService movimientoService;

    public MovimientosController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los movimientos")
    public List<MovimientoResponse> getAll() {
        return movimientoService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID")
    public MovimientoResponse getById(@PathVariable Integer id) {
        return movimientoService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nuevo movimiento")
    public MovimientoResponse create(@Valid @RequestBody MovimientoCreateRequest request) {
        return movimientoService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar movimiento")
    public MovimientoResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody MovimientoUpdateRequest request) {
        return movimientoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar movimiento")
    public MovimientoResponse delete(@PathVariable Integer id) {
        return movimientoService.delete(id);
    }
}

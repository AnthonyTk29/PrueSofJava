package com.banking.cuentas.api.controller;

import com.banking.cuentas.application.dto.CuentaCreateRequest;
import com.banking.cuentas.application.dto.CuentaResponse;
import com.banking.cuentas.application.dto.CuentaUpdateRequest;
import com.banking.cuentas.application.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@Tag(name = "Cuentas", description = "Gestión de cuentas bancarias")
public class CuentasController {

    private final CuentaService cuentaService;

    public CuentasController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las cuentas")
    public List<CuentaResponse> getAll() {
        return cuentaService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuenta por ID")
    public CuentaResponse getById(@PathVariable Integer id) {
        return cuentaService.getById(id);
    }

    @GetMapping("/numero/{numeroCuenta}")
    @Operation(summary = "Obtener cuenta por número")
    public CuentaResponse getByNumeroCuenta(@PathVariable String numeroCuenta) {
        return cuentaService.getByNumeroCuenta(numeroCuenta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nueva cuenta")
    public CuentaResponse create(@Valid @RequestBody CuentaCreateRequest request) {
        return cuentaService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cuenta")
    public CuentaResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody CuentaUpdateRequest request) {
        return cuentaService.update(id, request);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de la cuenta")
    public CuentaResponse patchEstado(
            @PathVariable Integer id,
            @RequestParam boolean estado) {
        return cuentaService.patchEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cuenta")
    public CuentaResponse delete(@PathVariable Integer id) {
        return cuentaService.delete(id);
    }
}

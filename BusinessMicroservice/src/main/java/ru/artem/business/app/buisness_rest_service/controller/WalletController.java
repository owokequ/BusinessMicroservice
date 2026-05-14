package ru.artem.business.app.buisness_rest_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.artem.business.app.buisness_rest_service.dto.request.WalletCreateDto;
import ru.artem.business.app.buisness_rest_service.dto.response.WalletResponseDto;
import ru.artem.business.app.buisness_rest_service.service.WalletService;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletSercice;

    @PutMapping("/addMoney/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WalletResponseDto> addMoney(@PathVariable String id,
            @Valid @RequestBody WalletCreateDto dto) {
        WalletResponseDto res = walletSercice.addMoney(id, dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WalletResponseDto> getBalance(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(walletSercice.getBalance(id));
    }

    @PutMapping("/subtractMoney/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WalletResponseDto> subtractMoney(@PathVariable String id,
            @Valid @RequestBody WalletCreateDto dto) {
        WalletResponseDto res = walletSercice.subtractMoney(id, dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

}

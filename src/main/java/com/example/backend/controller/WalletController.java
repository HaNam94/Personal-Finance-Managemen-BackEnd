package com.example.backend.controller;

import com.example.backend.dto.response.ResponseSuccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/vi/public/wallet")

public class WalletController {
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        // Implement logic to delete wallet with given ID
        ResponseSuccess response = new ResponseSuccess();
        response.setMessage("Delete wallet");
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response.getMessage(),response.getStatus());
    }
}
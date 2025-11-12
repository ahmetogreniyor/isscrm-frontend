package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Tariff;
import com.isscrm.isscrm_backend.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public List<Tariff> getAllTariffs() {
        return tariffService.getAllTariffs();
    }

    @PostMapping
    public Tariff createTariff(@RequestBody Tariff tariff) {
        return tariffService.createTariff(tariff);
    }

    @PutMapping("/{id}")
    public Tariff updateTariff(@PathVariable Long id, @RequestBody Tariff tariff) {
        return tariffService.updateTariff(id, tariff);
    }

    @DeleteMapping("/{id}")
    public void deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariff(id);
    }
}

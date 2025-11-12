package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.OloResult;
import com.isscrm.isscrm_backend.service.OloService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/olo")
@CrossOrigin(origins = "*")
public class OloController {

    private final OloService oloService;

    public OloController(OloService oloService) {
        this.oloService = oloService;
    }

    @GetMapping("/check")
    public OloResult checkAddress(@RequestParam String addressCode) {
        return oloService.checkAddress(addressCode);
    }
}

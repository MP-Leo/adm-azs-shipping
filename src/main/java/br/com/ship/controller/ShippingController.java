package br.com.ship.controller;

import br.com.ship.dto.ShippingDTO;
import br.com.ship.entity.Shipping;
import br.com.ship.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private ShippingService service;


    @GetMapping
    public Page<Shipping> findAllShippings(Pageable pageable){

        return service.findAllShippings(pageable);
    }

    @GetMapping("/attributes/{param}")
    public Page<Shipping> findShippingByAnyAttribute(
            @PathVariable String param, Pageable pageable){

        return service.findByAnyAttribute(param, pageable);
    }

    @GetMapping("/{id}")
    public Shipping findShippingById(
            @PathVariable String id){

        return service.findShippingById(id);
    }

    @PostMapping
    public Shipping createShipping(@RequestBody ShippingDTO dto){

        return service.createShipping(dto);
    }

    @PutMapping("/{id}")
    public Shipping updateShipping(@PathVariable String id, @RequestBody ShippingDTO dto){

        return service.updateShipping(id, dto);
    }

    @DeleteMapping("/{id}")
    public Shipping deleteShipping(@PathVariable String id){

        return service.deleteShipping(id);
    }

}

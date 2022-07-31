package com.techsoft.api.controller;

import com.techsoft.api.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techsoft.api.domain.Product;
import com.techsoft.api.service.ProductService;

/**
 * @author Matheus Brito
 */
@Slf4j
@RestController
@RequestMapping("v1/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> findAll(@PageableDefault Pageable pageable) {
        log.info("Request get all products!");

        Page<Product> productPage = productService.list(pageable);

        if(productPage.hasContent()) {
            return ResponseEntity.ok(productPage);
        }

        log.warn("Product page not found!");

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> find(@PathVariable Long id) {
        log.info("Request get product of id #{}", id);
        Product product = productService.findById(id);

        if(product != null) {
            return ResponseEntity.ok(product);
        }

        log.warn("Product not found!");

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody ProductDto productDto) {
        log.info("Request to save product ({})", productDto.getName());

        Product productSave = productService.saveDto(productDto);

        return ResponseEntity.ok(productSave);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Request delete product of id #{}", id);

        productService.delete(id);
    }


}

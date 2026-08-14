package com.tundalabs.store.controllers;

import com.tundalabs.store.dtos.ProductDto;
import com.tundalabs.store.entities.Product;
import com.tundalabs.store.mappers.ProductMapper;
import com.tundalabs.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping()
    public List<ProductDto> getAllProducts(
           @RequestParam(name = "categoryId", required = false) Byte categoryId
    ){
        List<Product> products;
        if(categoryId != null){
            products = productRepository.findByCategoryId(categoryId);
        }else{
            products = productRepository.findAllWithCategory();
        }
        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDto> getProduct(){
//        var product = productRepository.findById(id).orElse(null);
//        if(product == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(productMapper.toDto(product));
//    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(product));
    }
}

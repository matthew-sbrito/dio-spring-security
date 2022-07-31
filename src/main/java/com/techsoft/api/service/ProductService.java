package com.techsoft.api.service;

import com.techsoft.api.common.AbstractService;
import com.techsoft.api.common.error.HttpResponseException;
import com.techsoft.api.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.techsoft.api.repository.ProductRepository;
import com.techsoft.api.domain.Product;

@Service
public class ProductService extends AbstractService<Product, ProductDto> {

    @Autowired
    ProductService(ProductRepository productRepository) {
        super(productRepository, Product.class);
    }

    @Override
    public Product saveDomain(Product product) {
        if(product.getPrice() < 0){
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "Price is not valid!");
        }

        return super.saveDomain(product);
    }
}

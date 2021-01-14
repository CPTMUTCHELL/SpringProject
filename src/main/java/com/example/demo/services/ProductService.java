package com.example.demo.services;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductService  {
    @Autowired
    private ProductRepo productRepository;

    public void add(Product product){
        productRepository.save(product);
    }
    public List<Product> findTop3Products(){

        return productRepository.findTop3ByOrderByViewsDesc(); }
    public Product getById(Long id){
        return productRepository.getOne(id);
    }
    public void remove(Product product){
        productRepository.delete(product);
    }
    public void incView(Product product){
        product.setViews(product.getViews()+1);
        productRepository.save(product);
    }
    public void update(Long id, int price,String title){
        productRepository.update(id,price,title);

    }
    public Page<Product> findPaginated(int pageNumber, int pageSize, Specification<Product> productSpecification,
                                       String sortField, String direction){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable=PageRequest.of(pageNumber-1,pageSize,sort);
        return productRepository.findAll(productSpecification,pageable);
    }

}

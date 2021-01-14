package com.example.demo.controllers;

import com.example.demo.entities.Product;
import com.example.demo.repositories.specifications.ProductSpecs;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping()
    private String viewHomePage(Model model,@RequestParam(value = "word",required = false) String word){
        return findPaginated(1,model,word, "id","desc");
    }
    @PostMapping("/add")
    private String addProduct(@ModelAttribute(name = "newProduct") Product product){
        service.add(product);
        return "redirect:/products";
    }
    @GetMapping("/show/{id}")
    private String showProduct(Model model, @PathVariable("id") Long id){
        Product p=service.getById(id);
        service.incView(p);
        model.addAttribute("product",p);
        return "productForm";
    }

    @GetMapping("/remove/{id}")
    private String removeProduct(@PathVariable Long id){
        Product p=service.getById(id);
        service.remove(p);
        return "redirect:/products";
    }
    @GetMapping("/show/edit/{id}")
    private String editProduct(Model model, @PathVariable Long id){
        Product p=service.getById(id);
        model.addAttribute("productToEdit",p);
        return "productEdit";
    }
    @PostMapping("/submitEdit")
    private String editProduct(@ModelAttribute(name ="productToEdit") Product p){
         service.update(p.getId(), p.getPrice(), p.getTitle());
        return "redirect:/products";
    }

    @GetMapping("/{pageNumber}")
    private String findPaginated(@PathVariable int pageNumber, Model model, String word,
                                @RequestParam String sortField, @RequestParam String dir){
        Specification<Product> spec=Specification.where(null);

        int pageSize=2;

        if (word!=null){
            spec=spec.and(ProductSpecs.titleContains(word));
            model.addAttribute("productList",service.findPaginated(
                    pageNumber,pageSize,spec,sortField,dir).getContent());
        }
        Page<Product> page=service.findPaginated(pageNumber,pageSize,spec,sortField,dir);
        List<Product> content = page.getContent();
        if (word==null)model.addAttribute("productList",content);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("totalProducts",page.getTotalElements());
        model.addAttribute("word",word);
        model.addAttribute("sortField",sortField);
        model.addAttribute("dir",dir);
        model.addAttribute("reverseDir", dir.equals("asc")?"desc":"asc");
        model.addAttribute("top3",service.findTop3Products());
        return "products";
    }

    @GetMapping("/addProduct")
    private String addProduct(Model model){
        Product product=new Product();
        model.addAttribute("newProduct",product);
        return "addProductForm";
    }

}

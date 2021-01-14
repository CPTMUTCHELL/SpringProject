package com.example.demo.repositories;

import com.example.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

    List<Product> findTop3ByOrderByViewsDesc();
    @Modifying
    @Transactional
    @Query("UPDATE Product p set p.price=?2, p.title=?3 where p.id=?1 ")
    void update(Long id, int price,String title);

}

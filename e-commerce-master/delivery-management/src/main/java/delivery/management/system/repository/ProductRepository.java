package delivery.management.system.repository;

import delivery.management.system.model.entity.Category;
import delivery.management.system.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.sql.ast.Clause.WHERE;
import static org.springframework.http.HttpHeaders.FROM;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.count = p.count - :count WHERE p.id = :productId")
    void decreaseProductCount(@Param("count") int count, @Param("productId") long productId);

    List<Product> findAllByStatusTrue();

    Product findByIdAndStatusTrue(long id);

    List<Product> findByCategories_Id(Long categoryId);






}

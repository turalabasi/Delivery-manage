package delivery.management.system.repository;

import delivery.management.system.model.entity.Order;
import delivery.management.system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT o FROM _order o JOIN o.details d  WHERE d.createdAt >= :creatAt")
    List<Order> findOrdersByDetailsCreatedAtProfit(LocalDateTime creatAt);

    List<Order> findOrdersByCart_User(User user);

    List<Order> findOrdersByDriver_User(User user);


    Optional<Order> findOrderByDriver_User(User user);}

package mate.academy.bookstore.repository;

import java.util.Optional;
import mate.academy.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}

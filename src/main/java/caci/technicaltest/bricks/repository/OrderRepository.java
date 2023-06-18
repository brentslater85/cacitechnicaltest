package caci.technicaltest.bricks.repository;

import caci.technicaltest.bricks.entities.BrickOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<BrickOrder, Long> {

    List<BrickOrder> findByOrderReference(UUID reference);
}

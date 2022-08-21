package org.oaristeidou.repository;

import java.util.List;
import org.oaristeidou.entity.ItemEntity;
import org.openapitools.model.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ItemRepository extends the JpaRepository for getting all the out-of-the-box database
 * operations and defines the additional methods.
 */
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
  List<ItemEntity> findAllByStatus(StatusType status);
  ItemEntity getAllDetailsById(Long id);

}

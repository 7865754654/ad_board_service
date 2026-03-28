package repository;

import entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>,
        JpaSpecificationExecutor<Advertisement>   {

    List<Advertisement> findByUserId(Long userId);
    void updateById(Long id);
}

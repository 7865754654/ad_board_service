package service;

import dto.request.AdvertisementFilterRequest;
import dto.request.AdvertisementRequest;
import dto.response.AdvertisementResponse;
import entity.Advertisement;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mapper.AdvertisementMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import repository.AdvertisementRepository;
import repository.UserRepository;
import service.specification.AdvertisementSpecification;


import java.util.List;

@Service
@AllArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService{
    public AdvertisementRepository advertisementRepository;
    public UserRepository userRepository;
    public AdvertisementMapper advertisementMapper;

    @Override
    public void createAdvertisement(AdvertisementRequest advertisementRequest) {

        System.out.println("Создано объявление " + advertisementRequest);

        advertisementRepository.save(advertisementMapper
                .toAdvertisementForCreate(advertisementRequest));
    }

    @Override
    public AdvertisementResponse getAdById(Long id) {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));


        return advertisementMapper.toAdvertisementResponse(advertisement);
    }

    @Override
    public List<AdvertisementResponse> getAdvertisementsByUserId(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        List<Advertisement> advertisementsList = advertisementRepository.findByUserId(id);

        return advertisementMapper.toListAdvertisementResponse(advertisementsList);

    }

    @Override
    public Page<AdvertisementResponse> getAdByFilters(AdvertisementFilterRequest advertisementFilterRequest, Pageable pageable) {
        Specification<Advertisement> specification =
                AdvertisementSpecification.withFilter(advertisementFilterRequest);

        Page<Advertisement> page = advertisementRepository.findAll(specification, pageable);

        return page.map(advertisementMapper::toAdvertisementResponse);
    }


    @Override
    public Page<AdvertisementResponse> getAllAds(Pageable pageable) {
        Page<Advertisement> page = advertisementRepository.findAll(pageable);
        return page.map(advertisementMapper::toAdvertisementResponse);
    }


    @Override
    public void updateAdvertisementById(Long id, AdvertisementRequest advertisementRequest) {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        advertisementMapper.toAdvertisementForUpdate(advertisementRequest, advertisement);


        advertisementRepository.save(advertisement);
    }


    @Override
    public void deleteAdvertisementById(Long id) {
        advertisementRepository.deleteById(id);
        System.out.println("Объявление было удален");
    }

}
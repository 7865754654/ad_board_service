package service;

import dto.request.AdvertisementFilterRequest;
import dto.request.AdvertisementRequest;
import dto.response.AdvertisementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdvertisementService {

    // Создание объявления
    public void createAdvertisement(AdvertisementRequest advertisementRequest);

    //Поиск объявления по id
    public AdvertisementResponse getAdById(Long id);

    //Получение всех объявлений, которые есть у определнного пользователя
    public List<AdvertisementResponse> getAdvertisementsByUserId(Long id);

    //Получение списка объявлений
    public Page<AdvertisementResponse> getAllAds(Pageable pageable);

    // Редактирование объявления
    public void updateAdvertisementById(Long id, AdvertisementRequest advertisementRequest);

    //Удаление объявления
    public void deleteAdvertisementById(Long id);
}
package com.example.service;

import com.example.dto.request.AdvertisementFilterRequest;
import com.example.dto.request.AdvertisementRequest;
import com.example.dto.response.AdvertisementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdvertisementService {

    // Создание объявления
    AdvertisementResponse createAdvertisement(AdvertisementRequest advertisementRequest, String username);

    //Поиск объявления по id - для всех
    AdvertisementResponse getAdvertisementById(Long id);

    //Получение всех объявлений, которые есть у определнного пользователя - для всех
    List<AdvertisementResponse> getAdvertisementsByUser(Long userId);


    //Поиск списка объявлений по фильтрам, указанных пользовтаелем - для всех
    Page<AdvertisementResponse> getAdvertisementsByFilters(AdvertisementFilterRequest advertisementFilterRequest,
                                                      Pageable pageable);

    //Получение списка объявлений - для всех
    Page<AdvertisementResponse> getAllAdvertisements(Pageable pageable);

    // Редактирование объявления
    AdvertisementResponse updateAdvertisementById(Long id, AdvertisementRequest advertisementRequest, String username);

    //Удаление объявления
    void deleteAdvertisementById(Long id, String username);
}
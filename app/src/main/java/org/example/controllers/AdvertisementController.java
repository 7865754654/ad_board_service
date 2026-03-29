package org.example.controllers;

import dto.request.AdvertisementFilterRequest;
import dto.request.AdvertisementRequest;
import dto.response.AdvertisementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import service.AdvertisementService;

import java.util.List;

@RestController
@RequestMapping("/v1/advertisement")
@RequiredArgsConstructor
@Tag(name = "Сервис работы с объявлениями", description = "API доступа к объявлениям")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового обюъявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление было создано"),
            @ApiResponse(responseCode = "400", description = "Неверно переданы данные"),
            @ApiResponse(responseCode = "500", description = "Проблемы работы сервиса")
    })
    public void createAdvertisement(@Parameter(description = "Подпись для создания данных")
            @RequestBody AdvertisementRequest advertisementRequest) {
        advertisementService.createAdvertisement(advertisementRequest);
    }

    @GetMapping("{id}")
    public AdvertisementResponse getAdvertisementById(@PathVariable Long id) {
        return advertisementService.getAdById(id);
    }

    @GetMapping("/user/{userId}")
    public List<AdvertisementResponse> getAdvertisementsByUserId (@PathVariable Long userId) {
        return advertisementService.getAdvertisementsByUserId(userId);
    }

    @GetMapping
    public Page<AdvertisementResponse> getAllAdvertisements(Pageable pageable) {
        return advertisementService.getAllAds(pageable);
    }

    @PostMapping("/filter") //посмотреть про статусы
    public Page<AdvertisementResponse> getAdByFilters(
            @RequestBody AdvertisementFilterRequest filter,
            Pageable pageable) {
        return advertisementService.getAdByFilters(filter, pageable);
    }

    @PutMapping("{id}")
    public void updateAdvertisementById(@PathVariable Long id,
                                     @RequestBody AdvertisementRequest advertisementRequest) {
     advertisementService.updateAdvertisementById(id, advertisementRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdvertisementById(@PathVariable Long id) {
        advertisementService.deleteAdvertisementById(id);
    }
}

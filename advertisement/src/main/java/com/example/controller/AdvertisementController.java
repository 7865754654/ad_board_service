package com.example.controller;



import com.example.dto.request.AdvertisementFilterRequest;
import com.example.dto.request.AdvertisementRequest;
import com.example.dto.response.AdvertisementResponse;
import com.example.jwt.models.Token;
import com.example.view.View;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.service.AdvertisementService;

import java.util.List;


@Validated
@RestController
@RequestMapping("/api/advertisement")
@RequiredArgsConstructor
@Tag(name = "Сервис работы с объявлениями", description = "API доступа к объявлениям")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @PostMapping
    @Operation(summary = "Создание нового обюъявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление было создано"),
            @ApiResponse(responseCode = "400", description = "Неверно переданы данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<AdvertisementResponse> createAdvertisement(
                                    @Parameter(description = "Данные для создания объявления")
                                    @RequestBody @Valid AdvertisementRequest advertisementRequest,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        AdvertisementResponse response = advertisementService.createAdvertisement(advertisementRequest, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получение объявления по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление было найдено"),
            @ApiResponse(responseCode = "404", description = "Объявление не было найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MappingJacksonValue> getAdvertisementById(@PathVariable("id") Long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        AdvertisementResponse advertisement = advertisementService.getAdvertisementById(id);
        MappingJacksonValue view = new MappingJacksonValue(advertisement);

        if (userDetails != null && !"anonymousUser".equals(userDetails.getUsername())) {
            view.setSerializationView(View.Private.class);
        } else {
            view.setSerializationView(View.Public.class);
        }

        return ResponseEntity.ok(view);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получение объявления(-ий) определенного пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление было найдено"),
            @ApiResponse(responseCode = "404", description = "Объявление не было найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MappingJacksonValue> getAdvertisementsByUserId(
            @PathVariable("userId") @Positive Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<AdvertisementResponse> advertisements = advertisementService.getAdvertisementsByUser(userId);

        boolean isAnonym = userDetails != null && !"anonymousUser".equals(userDetails.getUsername());

        MappingJacksonValue response = new MappingJacksonValue(advertisements);
        response.setSerializationView(isAnonym ? View.Private.class : View.Public.class);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Получение всех объявлений постранично")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MappingJacksonValue> getAllAdvertisements(@ParameterObject @PageableDefault(size = 10, sort = "created")
                                                                        Pageable pageable,
                                                            @AuthenticationPrincipal UserDetails userDetails) {

        Page<AdvertisementResponse> pageAdvertisements = advertisementService.getAllAdvertisements(pageable);

        boolean isAnonym = userDetails != null && !"anonymousUser".equals(userDetails.getUsername());

        MappingJacksonValue response = new MappingJacksonValue(pageAdvertisements);
        response.setSerializationView(isAnonym ? View.Private.class : View.Public.class);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/filter")
    @Operation(summary = "Получение всех объявлений постранично с фильтрами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений найден"),
            @ApiResponse(responseCode = "400", description = "Неверно переданы данные фильтра"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MappingJacksonValue> getAdvertisementsByFilters(@RequestBody @Valid AdvertisementFilterRequest filter,
                                                                  @ParameterObject @PageableDefault(size = 20, sort = "created")
                                                                  Pageable pageable,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {

        Page<AdvertisementResponse> pageAdvertisements = advertisementService.getAdvertisementsByFilters(filter, pageable);

        boolean isAnonym = userDetails != null && !"anonymousUser".equals(userDetails.getUsername());

        MappingJacksonValue response = new MappingJacksonValue(pageAdvertisements);

        response.setSerializationView(isAnonym ? View.Private.class : View.Public.class);

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "Обновление объявления по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
            @ApiResponse(responseCode = "400", description = "Неверно переданы данные"),
            @ApiResponse(responseCode = "403", description = "Нет прав на обновление"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementResponse updateAdvertisementById(@PathVariable("id") Long id,
                                                         @RequestBody @Valid AdvertisementRequest advertisementRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return advertisementService.updateAdvertisementById(id, advertisementRequest, username);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdvertisementById(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        advertisementService.deleteAdvertisementById(id, username);
    }
}

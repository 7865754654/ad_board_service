package com.example.mapper;

import com.example.entity.Advertisement;
import com.example.dto.request.AdvertisementRequest;
import com.example.dto.response.AdvertisementResponse;

import com.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "user", ignore = true)
    Advertisement toAdvertisementForCreate(AdvertisementRequest advertisementRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "user", ignore = true)
    Advertisement toAdvertisementForUpdate(AdvertisementRequest advertisementRequest,
                                           @MappingTarget Advertisement advertisement);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(target = "userFullName", expression = "java(getFullName(advertisement.getUser()))")
    AdvertisementResponse toAdvertisementResponse(Advertisement advertisement);

    List<AdvertisementResponse> toListAdvertisementResponse(List<Advertisement> advertisements);

    default String getFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}

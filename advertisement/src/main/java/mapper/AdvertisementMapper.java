package mapper;

import dto.request.AdvertisementRequest;
import dto.response.AdvertisementResponse;
import entity.Advertisement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

    // Для создания нового объявления — id игнорируем, база сама его создаёт
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "status", ignore = true)
    Advertisement toAdvertisementForCreate(AdvertisementRequest advertisementRequest);

    // Для изменения  объявления — id нельзя игнорировать, иначе MapStruct не будет знать, какую сущность обновлять в базе.
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "status", ignore = true)
    Advertisement toAdvertisementForUpdate(AdvertisementRequest advertisementRequest,
                                           @MappingTarget Advertisement advertisement);   // вот сюда будут маппиться поля);


    AdvertisementResponse toAdvertisementResponse(Advertisement advertisement);

    List<AdvertisementResponse> toListAdvertisementResponse(List<Advertisement> advertisements);


}

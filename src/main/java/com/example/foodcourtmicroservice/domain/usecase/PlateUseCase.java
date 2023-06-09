package com.example.foodcourtmicroservice.domain.usecase;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.PlateEntity;
import com.example.foodcourtmicroservice.adapters.driving.http.dto.response.PlatePaginationResponseDto;
import com.example.foodcourtmicroservice.configuration.Constants;
import com.example.foodcourtmicroservice.domain.api.IPlateServicePort;
import com.example.foodcourtmicroservice.domain.exceptions.IdPlateNotFoundException;
import com.example.foodcourtmicroservice.domain.model.Plate;
import com.example.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import com.example.foodcourtmicroservice.domain.spi.IPlatePersistencePort;
import org.springframework.data.domain.Page;


public class PlateUseCase implements IPlateServicePort {
    private final IPlatePersistencePort platePersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort,ICategoryPersistencePort categoryPersistencePort) {
        this.platePersistencePort = platePersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public void savePlate(Plate plate, Long idRestaurant, String categoryPlate) {
        Long idCategory = categoryPersistencePort.getCategoryByName(categoryPlate);
        plate.setIdCategory(idCategory);
        plate.setIdRestaurant(idRestaurant);
        platePersistencePort.savePlate(plate);

    }

    @Override
    public void updatePlate(Plate plate) {
        if (platePersistencePort.findById(plate.getId()).isPresent()) {
            plate.setPrice(plate.getPrice());
            plate.setDescription(plate.getDescription());
            platePersistencePort.savePlate(plate);
        } else {
            throw new IdPlateNotFoundException(Constants.ID_UPDATE_NOT_FOUND);
        }
    }

    //TODO: sino funciona, crear el persistence de update status
    @Override
    public void statusEnabledPlate(Boolean enabled, Plate plate) {
        platePersistencePort.statusEnabledPlate(enabled, plate);
    }

    @Override
    public Long getByNameCategory(String nameCategory) {
        return categoryPersistencePort.getCategoryByName(nameCategory);
    }

    @Override
    public Page<PlatePaginationResponseDto> getPaginationPlates(Long idRestaurant, Integer pageSize, String sortBy, Long idCategory) {
        return platePersistencePort.getPaginationPlates(idRestaurant, pageSize, sortBy, idCategory);
    }

}

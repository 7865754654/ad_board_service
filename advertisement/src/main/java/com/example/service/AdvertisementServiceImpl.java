package com.example.service;

import com.example.entity.Advertisement;
import com.example.entity.User;
import com.example.dto.request.AdvertisementFilterRequest;
import com.example.dto.request.AdvertisementRequest;
import com.example.dto.response.AdvertisementResponse;


import com.example.mapper.AdvertisementMapper;
import com.example.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.repository.AdvertisementRepository;

import com.example.service.specification.AdvertisementSpecification;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.example.enums.Status.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor

public class AdvertisementServiceImpl implements AdvertisementService{
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final AdvertisementMapper advertisementMapper;



    @Override
    @CacheEvict(value = {"allAdvertisements", "filteredAdvertisements"}, allEntries = true)
    @Transactional
    public AdvertisementResponse createAdvertisement(AdvertisementRequest advertisementRequest, String username) {

        User user = getByUsername(username);

        Advertisement advertisement = advertisementMapper.toAdvertisementForCreate(advertisementRequest);

        advertisement.setStatus(ACTIVE);
        advertisement.setUser(user);

        Advertisement saved = advertisementRepository.save(advertisement);

        log.info("Creating advertisement for user: {}", username);
        return advertisementMapper.toAdvertisementResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "advertisements", key = "#id")
    public AdvertisementResponse getAdvertisementById(Long id) {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));

        log.info("Advertisement found with id: {}", id);
        return advertisementMapper.toAdvertisementResponse(advertisement);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userAdvertisements", key = "#userId")
    public List<AdvertisementResponse> getAdvertisementsByUser(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", userId)));

        List<Advertisement> advertisements  = advertisementRepository.findByUserId(userId);

        log.info("Found {} advertisements for user {}", advertisements.size(), userId);
        return advertisementMapper.toListAdvertisementResponse(advertisements);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<AdvertisementResponse> getAdvertisementsByFilters(AdvertisementFilterRequest advertisementFilterRequest,
                                                      Pageable pageable) {

        if (advertisementFilterRequest == null) {
            advertisementFilterRequest = new AdvertisementFilterRequest();
        }

        Specification<Advertisement> specification =
                AdvertisementSpecification.withFilter(advertisementFilterRequest);

        Page<Advertisement> page = advertisementRepository.findAll(specification, pageable);

        log.info("Found {} advertisements by filters: ", page.getNumberOfElements());
        return page.map(advertisementMapper::toAdvertisementResponse);
    }


    @Override
    @Cacheable(value = "allAdvertisements", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<AdvertisementResponse> getAllAdvertisements(Pageable pageable) {
        Page<Advertisement> page = advertisementRepository.findAll(pageable);

        log.info("Loaded {} of {} advertisements", page.getNumberOfElements(), page.getTotalElements());
        return page.map(advertisementMapper::toAdvertisementResponse);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "advertisements", key = "#id"),
            @CacheEvict(value = "allAdvertisements", allEntries = true),
            @CacheEvict(value = "userAdvertisements", key = "#userId")
    })
    @Transactional
    public AdvertisementResponse updateAdvertisementById(Long id, AdvertisementRequest advertisementRequest, String username) {

        User user = getByUsername(username);

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));


        if (!advertisement.getUser().getId().equals(user.getId()) && !isAdmin(user)) {
            throw new AccessDeniedException("You can update only your own advertisements");
        }

        advertisementMapper.toAdvertisementForUpdate(advertisementRequest, advertisement);


        Advertisement updated = advertisementRepository.save(advertisement);

        log.info("Updated advertisement with id: {}", id);

        return advertisementMapper.toAdvertisementResponse(updated);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "advertisements", key = "#id"),
            @CacheEvict(value = "allAdvertisements", allEntries = true),
            @CacheEvict(value = "userAdvertisements", key = "#userId")
    })
    @Transactional
    public void deleteAdvertisementById(Long id, String username) {

        User user = getByUsername(username);

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found with id: " + id));

        if (!advertisement.getUser().getId().equals(user.getId()) && !isAdmin(user)) {
            throw new AccessDeniedException("You can delete only your own advertisements");
        }

        advertisementRepository.deleteById(id);
        log.info("Advertisement with id: {} deleted by user: {}", id, username);
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    private User getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(
                        "No user with this name was found: '%s'",
                        username)));
        return user;
    }
}

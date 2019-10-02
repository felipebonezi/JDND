package com.udacity.vehicles.service;

import com.alibaba.fastjson.JSON;
import com.udacity.vehicles.client.maps.Address;
import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final WebClient mapsWS;
    private final WebClient pricingWS;
    private final CarRepository repository;

    public CarService(CarRepository repository,
                      @Qualifier("pricing") WebClient pricingWS,
                      @Qualifier("maps") WebClient mapsWS) {
        this.mapsWS = mapsWS;
        this.pricingWS = pricingWS;
        this.repository = repository;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        Car car = this.repository.findById(id)
                .orElseThrow(CarNotFoundException::new);

        Price price = this.pricingWS.get()
                .uri(String.format("/services/price?vehicleId=%d", id))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8)
                .ifNoneMatch("*")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> JSON.parseObject(response, Price.class))
                .blockOptional(Duration.ofMinutes(5L))
                .orElseThrow(CarNotFoundException::new);

        car.setPrice(price.getPriceFormatted());

        Location location = car.getLocation();
        Address address = this.mapsWS.get()
                .uri(String.format("/maps?lat=%s&lon=%s", location.getLat(), location.getLon()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8)
                .ifNoneMatch("*")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> JSON.parseObject(response, Address.class))
                .blockOptional(Duration.ofMinutes(5L))
                .orElseThrow(CarNotFoundException::new);

        location.setAddress(address.getAddress());
        location.setCity(address.getCity());
        location.setState(address.getState());
        location.setZip(address.getZip());

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        Car car = this.repository.findById(id)
                .orElseThrow(CarNotFoundException::new);

        this.repository.delete(car);
    }

}

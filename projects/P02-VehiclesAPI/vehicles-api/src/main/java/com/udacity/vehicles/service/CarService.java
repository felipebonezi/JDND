package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final MapsClient mapsClient;
    private final PriceClient priceClient;
    private final CarRepository repository;

    public CarService(CarRepository repository,
                      PriceClient priceClient,
                      MapsClient mapsClient) {
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;
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

        // Get price and location info from other services.
        this.fillPriceAndLocationInfo(id, car);
        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        Long id = car.getId();

        Car carSaved;
        if (id != null && id > 0) {
            carSaved = this.repository.findById(id)
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setPrice(car.getPrice());
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return this.repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        } else {
            carSaved = this.repository.save(car);
        }

        this.fillPriceAndLocationInfo(carSaved.getId(), carSaved);
        return carSaved;
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

    /**
     * Gets car price from pricing micro service and location from boogle-maps web services.
     * @param id the ID number of the car to gather information on
     * @param car the Car entity to fill the information on
     */
    private void fillPriceAndLocationInfo(Long id, Car car) {
        String price = this.priceClient.getPrice(id);
        car.setPrice(price);

        Location newLocation = this.mapsClient.getAddress(car.getLocation());
        car.setLocation(newLocation);
    }

}

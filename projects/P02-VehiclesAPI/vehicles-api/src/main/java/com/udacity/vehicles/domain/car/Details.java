package com.udacity.vehicles.domain.car;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Declares the additional detail variables for each Car object,
 * along with related methods for access and setting.
 */
@Embeddable
public class Details {

    @NotBlank
    private String body;

    @NotBlank
    private String model;

    @NotNull
    @ManyToOne
    private Manufacturer manufacturer;

    private Integer numberOfDoors;

    private String fuelType;

    private String engine;

    private Integer mileage;

    private Integer modelYear;

    private Integer productionYear;

    private String externalColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Details)) return false;
        Details details = (Details) o;
        return getBody().equals(details.getBody()) &&
                getModel().equals(details.getModel()) &&
                getNumberOfDoors().equals(details.getNumberOfDoors()) &&
                getFuelType().equals(details.getFuelType()) &&
                getEngine().equals(details.getEngine()) &&
                getMileage().equals(details.getMileage()) &&
                getModelYear().equals(details.getModelYear()) &&
                getProductionYear().equals(details.getProductionYear()) &&
                getExternalColor().equals(details.getExternalColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBody(), getModel(), getNumberOfDoors(), getFuelType(), getEngine(), getMileage(), getModelYear(), getProductionYear(), getExternalColor());
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Integer numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public String getExternalColor() {
        return externalColor;
    }

    public void setExternalColor(String externalColor) {
        this.externalColor = externalColor;
    }
}

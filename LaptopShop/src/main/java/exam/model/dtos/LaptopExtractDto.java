package exam.model.dtos;

import java.math.BigDecimal;

public class LaptopExtractDto {

    private String macAddress;
    private Double cpuSpeed;
    private Integer ram;
    private Integer storage;
    private BigDecimal price;
    private String shop;
    private String town;

    public LaptopExtractDto(String macAddress, Double cpuSpeed, Integer ram, Integer storage, BigDecimal price, String shop, String town) {
        this.macAddress = macAddress;
        this.cpuSpeed = cpuSpeed;
        this.ram = ram;
        this.storage = storage;
        this.price = price;
        this.shop = shop;
        this.town = town;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(Double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}

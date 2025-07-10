package model;

public class Package {
    private int packageId;
   
    private String name;
     private int durationInDays;
    private double priceModifier;

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceModifier() {
        return priceModifier;
    }

    public void setPriceModifier(double priceModifier) {
        this.priceModifier = priceModifier;
    }

    @Override
    public String toString() {
        return "Package{" +
                "packageId=" + packageId +
                ", durationInDays=" + durationInDays +
                ", name='" + name + '\'' +
                ", priceModifier=" + priceModifier +
                '}';
    }
}

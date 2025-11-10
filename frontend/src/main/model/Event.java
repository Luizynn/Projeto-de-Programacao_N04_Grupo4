package main.model;

public class Event {
    private Long id;
    private String name;
    private String localizationAddress;
    private String localizationNeighborhood;
    private double price;

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocalizationAddress() { return localizationAddress; }
    public void setLocalizationAddress(String localizationAddress) { this.localizationAddress = localizationAddress; }

    public String getLocalizationNeighborhood() { return localizationNeighborhood; }
    public void setLocalizationNeighborhood(String localizationNeighborhood) { this.localizationNeighborhood = localizationNeighborhood; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

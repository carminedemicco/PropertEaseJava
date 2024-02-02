package builder;

import javafx.scene.image.Image;

public class Director {
    public House constructApartment(IBuilder ibuilder, int id, String address, int floor, boolean elevator, int balconies, int terrace,
                                    int garden, int accessories, int bedrooms, int sqm, int price, Image[] pics) {
        ibuilder.setId(id);
        ibuilder.setAddress(address);
        ibuilder.setFloor(floor);
        ibuilder.setElevator(elevator);
        ibuilder.setTerrace(terrace);
        ibuilder.setBalconies(balconies);
        ibuilder.setAccessories(accessories);
        ibuilder.setBedrooms(bedrooms);
        ibuilder.setSqm(sqm);
        ibuilder.setPrice(price);
        //ibuilder.setPics(pics);

        return ibuilder.getResult();
    }

    public House constructGarage(IBuilder ibuilder) {
        ibuilder.setAddress("Via Don Giuseppe");
        ibuilder.setSqm(120);
        ibuilder.setPrice(18000);
        //ibuilder.setPics();

        return ibuilder.getResult();
    }

    public House constructIndependent(IBuilder ibuilder) {
        ibuilder.setAddress("Via Don Giuseppe");
        ibuilder.setTerrace(23);
        ibuilder.setBalconies(3);
        ibuilder.setGarden(32);
        ibuilder.setAccessories(5);
        ibuilder.setBedrooms(6);
        ibuilder.setSqm(120);
        ibuilder.setPrice(260000);
        //ibuilder.setPics();

        return ibuilder.getResult();
    }
}

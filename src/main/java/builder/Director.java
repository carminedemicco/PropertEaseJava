package builder;

public class Director {
    public House constructApartment(IBuilder ibuilder) {
        ibuilder.setAddress("Via Don Giuseppe");
        ibuilder.setFloor(2);
        ibuilder.setElevator(true);
        ibuilder.setTerrace(23);
        ibuilder.setBalconies(3);
        ibuilder.setAccessories(5);
        ibuilder.setBedrooms(6);
        ibuilder.setSqm(120);
        ibuilder.setPrice(120000);
        //ibuilder.setPics();

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

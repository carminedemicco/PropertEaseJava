package main.propertease.builder;

public class _Application
{
    public static void main(String[] args){
        IBuilder ibuilder = new GarageBuilder();
        Director director = new Director();
        House apartment = director.constructGarage(ibuilder);
        System.out.print(apartment.getAccessories());
    }
}

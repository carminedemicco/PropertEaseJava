package main.propertease;

public class User {

    public User(String name, int perm) {
        _name = name;
        _perm = perm;
    }

    public String get_name() {
        return _name;
    }

    public int get_perm() {
        return _perm;
    }

    String _name;
    int _perm;
}
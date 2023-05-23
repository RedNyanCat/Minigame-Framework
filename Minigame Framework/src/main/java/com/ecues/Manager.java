package com.ecues;

public class Manager {

    public static Manager manager;

    // read from configuration files for values

    public static Manager getManager() {

        // Lazy initialization, checked if null
        if (manager == null){
            manager = new Manager();
        }

        return manager;
    }

}

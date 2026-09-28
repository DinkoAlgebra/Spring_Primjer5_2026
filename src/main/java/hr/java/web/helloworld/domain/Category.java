package hr.java.web.helloworld.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

    CARS(1, "Cars and motorcycles", "New and used"),
    PROPERTIES(2, "Apartments and houses", "New and used");

    private final Integer id;
    private final String name;
    private final String description;
}

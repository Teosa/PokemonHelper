package ru.teosa.pokemonhelper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Location {

    SANTALUN(
            "Санталун",
            "loc_santalunecity",
            null,
            "//*[@id=\"coord-layer\"]/div[312]",
            "//*[@id=\"coord-layer\"]/div[313]"
    ),
    SNOWBELLECITY (
            "Сновбейлл",
            "loc_snowbellecity",
            null,
            "//*[@id=\"coord-layer\"]/div[387]",
            "//*[@id=\"coord-layer\"]/div[238]"
    ),

    PATH_19("Тропа 19", "loc_route_19",  SNOWBELLECITY, null, null),
    PATH_3("Тропа 3", "loc_route_3", Location.SANTALUN, null, null),
    ;

    private final String locationName;

    private final String locationClassName;

    private final Location parent;

    private final String cityCenterDoor;

    private final String startPosition;

    public static Location getByName(String name) {
        for (Location location : Location.values()) {
            if (location.getLocationName().equals(name)) return location;
        }

        throw new RuntimeException("Локация по имени " + name + " не найдена");
    }

}

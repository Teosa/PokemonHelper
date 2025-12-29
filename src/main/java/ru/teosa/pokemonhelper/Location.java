package ru.teosa.pokemonhelper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.teosa.pokemonhelper.dto.MoveCoordinates;

@Getter
@RequiredArgsConstructor
public enum Location {

    SANTALUN(
            "Санталун",
            "loc_santalunecity",
            null,
            new MoveCoordinates(832, 864),
            new MoveCoordinates(864, 864)
    ),
    SNOWBELLECITY(
            "Сновбейлл",
            "loc_snowbellecity",
            null,
            new MoveCoordinates(384, 960),
            new MoveCoordinates(384, 864)
    ),

    PATH_19("Тропа 19", "loc_route_19", SNOWBELLECITY, null, null),
    PATH_3("Тропа 3", "loc_route_3", Location.SANTALUN, null, null),
    ;

    private final String locationName;

    private final String locationClassName;

    private final Location parent;

    private final MoveCoordinates cityCenterDoor;

    private final MoveCoordinates startPosition;

    public static Location getByName(String name) {
        for (Location location : Location.values()) {
            if (location.getLocationName().equals(name)) return location;
        }

        throw new RuntimeException("Локация по имени " + name + " не найдена");
    }

}

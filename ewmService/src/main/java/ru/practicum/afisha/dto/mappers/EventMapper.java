package ru.practicum.afisha.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.afisha.dto.EventDto;
import ru.practicum.afisha.dto.NewEventDto;
import ru.practicum.afisha.enums.EventState;
import ru.practicum.afisha.enums.StateAction;
import ru.practicum.afisha.models.Event;
import ru.practicum.afisha.models.Location;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Named("arrayToLocation")
    static Location arrayToLocation(double[] locationList) {
        return new Location(locationList[0], locationList[1]);
    }

    @Mapping(source = "locationList", target = "location", qualifiedByName = "arrayToLocation")
    EventDto toEventDto(Event event);

    @Mapping(source = "location", target = "locationList", qualifiedByName = "locationToArray")
    @Mapping(target = "category", ignore = true)
    Event toEvent(NewEventDto eventDto);

    @Named("locationToArray")
    static double[] locationToArray(Location location) {
        return new double[] {location.getLat(), location.getLon()};
    }

    @Named("actionToState")
    static EventState actionToState(StateAction stateAction) {
        return stateAction == StateAction.PUBLISH_EVENT ? EventState.PUBLISHED : EventState.CANCELED;
    }
}

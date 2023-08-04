package ru.practicum.afisha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.afisha.enums.RequestState;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestState status;
}

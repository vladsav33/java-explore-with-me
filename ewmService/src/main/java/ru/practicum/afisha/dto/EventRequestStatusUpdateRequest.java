package ru.practicum.afisha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.afisha.enums.RequestState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventRequestStatusUpdateRequest {
    long[] requestIds;
    RequestState status;
}

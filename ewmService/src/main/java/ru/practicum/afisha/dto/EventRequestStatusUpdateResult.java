package ru.practicum.afisha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class EventRequestStatusUpdateResult {
    RequestDto[] confirmedRequests;
    RequestDto[] rejectedRequests;
}

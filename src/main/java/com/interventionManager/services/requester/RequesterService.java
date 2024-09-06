package com.interventionManager.services.requester;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.entities.Request;

import java.util.Date;
import java.util.List;

public interface RequesterService {
    RequestDto createRequest(RequestDto requestDto);
    List<RequestDto> getAllRequests();
    void deleteRequest(Long requestId);
    RequestDto getRequestByRequestId(Long requestId);
    RequestDto updateRequest(Long requestId, RequestDto requestDto);
    List<RequestDto> searchRequestByTitle(String title);
}

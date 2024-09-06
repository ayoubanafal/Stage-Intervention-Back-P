package com.interventionManager.services.technician;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.entities.User;

import java.util.List;

public interface TechnicianService {
    List<RequestDto> getAllAvailableRequests();
    List<RequestDto> searchRequestByTitle(String title);
    RequestDto claimRequest(Long requestId,Long technicianId);//,RequestDto requestDto);
    RequestDto UnClaimRequest(Long requestId);
    RequestDto getRequestByRequestId(Long requestId);
    RequestDto updateRequestStatus(Long requestId, String status);
    List<RequestDto> getAllClaimedRequests(Long technicianId);
    void archiveRequest(Long requestId);
}

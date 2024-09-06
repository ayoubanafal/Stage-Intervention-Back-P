package com.interventionManager.controller.technician;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.services.report.DataBasePDFService;
import com.interventionManager.services.requester.RequesterService;
import com.interventionManager.services.technician.TechnicianService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/technician")
@CrossOrigin("*")
public class TechnicianController {

    private final TechnicianService technicianService;
    private final DataBasePDFService dataBasePDFService;

    @GetMapping("/requests")
    public ResponseEntity<?> getAllAvailableRequests() {
        return ResponseEntity.ok(technicianService.getAllAvailableRequests());
    }
    @GetMapping("/requests/{technicianId}")
    public ResponseEntity<?> getAllClaimedRequests(@PathVariable Long technicianId) {
        return ResponseEntity.ok(technicianService.getAllClaimedRequests(technicianId));
    }
    @GetMapping("/requests/search/{title}")
    public ResponseEntity<List<RequestDto>> searchRequest(@PathVariable String title){
        return ResponseEntity.ok(technicianService.searchRequestByTitle(title));
    }
    @GetMapping("/requestById/{requestId}")
    public ResponseEntity<RequestDto> getRequestById(@PathVariable Long requestId)
    {
        return ResponseEntity.ok(technicianService.getRequestByRequestId(requestId));
    }
    @PutMapping("/request/update/{requestId}/{status}")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long requestId, @PathVariable String status){
        RequestDto updatedRequestStatus = technicianService.updateRequestStatus(requestId,status);
        if(updatedRequestStatus == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedRequestStatus);
    }
    @PutMapping("/request/archive/{requestId}")
    public ResponseEntity<?> archiveRequest(@PathVariable Long requestId) {
        try {
            technicianService.archiveRequest(requestId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Request archived successfully");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value="/report/{requestId}" , produces=MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> createReport(@PathVariable Long requestId)
    {
        RequestDto requestDto=technicianService.getRequestByRequestId(requestId);
        ByteArrayInputStream bis = dataBasePDFService.requestPDFReport(requestDto);
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + requestDto.getTitle() + "'s Report.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/request/{requestId}/{technicianId}")
    public ResponseEntity<?> claimRequest(@PathVariable Long requestId, @PathVariable Long technicianId){
        RequestDto ClaimedRequestDto = technicianService.claimRequest(requestId,technicianId);
        if(ClaimedRequestDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ClaimedRequestDto);
    }
    @PutMapping("/request/{requestId}")
    public ResponseEntity<?> UnClaimRequest(@PathVariable Long requestId){
        RequestDto UnClaimedRequestDto = technicianService.UnClaimRequest(requestId);
        if(UnClaimedRequestDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UnClaimedRequestDto);
    }

}

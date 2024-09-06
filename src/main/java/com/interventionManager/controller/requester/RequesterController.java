package com.interventionManager.controller.requester;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.entities.Request;
import com.interventionManager.services.requester.RequesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requester")
@CrossOrigin("*")
public class RequesterController {
    private final RequesterService requesterService;

    @PostMapping("/request")
    public ResponseEntity<RequestDto> createRequestC(@RequestBody RequestDto requestDto) {
        RequestDto CreatedRequestDto = requesterService.createRequest(requestDto);
        if(CreatedRequestDto == null) {
            System.out.println("here");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
        System.out.println(CreatedRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreatedRequestDto);
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(requesterService.getAllRequests());
    }
    @DeleteMapping("/request/{requestId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long requestId) {
        requesterService.deleteRequest(requestId);
        return ResponseEntity.ok(null);
    }
    @GetMapping("/request/{requestId}")
    public ResponseEntity<RequestDto> getRequestById(@PathVariable Long requestId)
    {
        return ResponseEntity.ok(requesterService.getRequestByRequestId(requestId));
    }
    @PutMapping("/request/{requestId}")
    public ResponseEntity<?> updateRequest(@PathVariable Long requestId, @RequestBody RequestDto requestDto) {
        RequestDto UpdatedRequestDto = requesterService.updateRequest(requestId, requestDto);
        if(UpdatedRequestDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UpdatedRequestDto);
    }

    @GetMapping("/requests/search/{title}")
    public ResponseEntity<List<RequestDto>> searchRequest(@PathVariable String title){
        return ResponseEntity.ok(requesterService.searchRequestByTitle(title));
    }
}

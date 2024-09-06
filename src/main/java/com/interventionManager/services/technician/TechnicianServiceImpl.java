package com.interventionManager.services.technician;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.entities.Comment;
import com.interventionManager.entities.Request;
import com.interventionManager.entities.RequestHistory;
import com.interventionManager.entities.User;
import com.interventionManager.enums.RequestStatus;
import com.interventionManager.repositories.CommentRepository;
import com.interventionManager.repositories.RequestHistoryRepository;
import com.interventionManager.repositories.RequestRepository;
import com.interventionManager.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianServiceImpl implements TechnicianService{
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestHistoryRepository requestHistoryRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<RequestDto> getAllAvailableRequests() {
        Long adminId=1L;
        Optional<User> admin = userRepository.findById(adminId);
        return requestRepository.findAllByTechnician(admin)
                .stream()
                .sorted(Comparator.comparing(Request::getLastUpdate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestDto> searchRequestByTitle(String title) {
        return requestRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Request::getLastUpdate))
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }
    @Override
    public RequestDto claimRequest(Long requestId,Long technicianId){
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isPresent())
        {
            Request request = requestOptional.get();
            User user=userRepository.findById(technicianId).get();
                request.setTechnician(user);
            return requestRepository.save(request).getRequestDto();
        }
        return null;
    }
    @Override
    public RequestDto UnClaimRequest(Long requestId){
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isPresent())
        {
            Request request = requestOptional.get();
            Long AdminId=1L;
            User user=userRepository.findById(AdminId).get();
            request.setTechnician(user);
            return requestRepository.save(request).getRequestDto();
        }
        return null;
    }
    @Override
    public List<RequestDto> getAllClaimedRequests(Long technicianId){
        Optional<User> admin = userRepository.findById(technicianId);
        return requestRepository.findAllByTechnician(admin)
                .stream()
                .sorted(Comparator.comparing(Request::getLastUpdate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto getRequestByRequestId(Long requestId){
        Optional<Request> request = requestRepository.findById(requestId);
        return request.map(Request::getRequestDto).orElse(null);
    }
    @Override
    public RequestDto updateRequestStatus(Long requestId, String status){
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isPresent()) {
            Request request = requestOptional.get();

            if (status != null) {
                request.setStatus(mapStringToRequestStatus(status));
            }

            return requestRepository.save(request).getRequestDto();
        }
        return null;
    }
    private RequestStatus mapStringToRequestStatus(String status)
    {
        return switch (status) {
            case "PENDING" -> RequestStatus.PENDING;
            case "INPROGRESS" -> RequestStatus.INPROGRESS;
            case "COMPLETED" -> RequestStatus.COMPLETED;
            case "DEFERRED" -> RequestStatus.DEFERRED;
            default -> RequestStatus.CANCELLED;
        };
    }

    @Override
    public void archiveRequest(Long requestId) {
            Optional<Request> requestOptional = requestRepository.findById(requestId);
            if (requestOptional.isPresent()) {
                Request request = requestOptional.get();
                RequestHistory requestHistory = new RequestHistory(request);
                requestHistoryRepository.save(requestHistory);
                List<Comment> comments=commentRepository.findByRequestId(requestId);
                for (Comment comment : comments) {
                    commentRepository.deleteById(comment.getCommentId());
                }
                requestRepository.delete(request);
            } else {
                throw new EntityNotFoundException("Request not found");
            }
        }
}



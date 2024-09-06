package com.interventionManager.services.requester;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.entities.Request;
import com.interventionManager.entities.User;
import com.interventionManager.enums.RequestStatus;
import com.interventionManager.repositories.RequestRepository;
import com.interventionManager.repositories.UserRepository;
import com.interventionManager.services.email.EmailSenderService;
import com.interventionManager.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.interventionManager.enums.UserRole;


import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequesterServiceImpl implements RequesterService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final NotificationService notificationService;

    @Override
    public RequestDto createRequest(RequestDto requestDto) {
        Long adminId=1L;
        Optional<User> requester = userRepository.findById(requestDto.getRequesterId());
        Optional<User> admin = userRepository.findById(adminId);

        //User technician = userRepository.findById(technicianId);
        //Material material = materialRepository.findById(materialId);
        if (requester.isPresent() && admin.isPresent()) {
            Request request = new Request();
            request.setRequester(requester.get());
            request.setTitle(requestDto.getTitle());
            request.setDescription(requestDto.getDescription());
            request.setStatus(RequestStatus.INPROGRESS);
            request.setPriority(requestDto.getPriority());
            request.setCreationDate(requestDto.getCreationDate());
            request.setLastUpdate(requestDto.getLastUpdate());
            request.setTechnician(admin.get());
            Request savedRequest = requestRepository.save(request);
            notificationService.sendNotificationToTechnician(adminId, "A new request has been Created to you. With the Title : "+savedRequest.getTitle());
            sendEmailToTechnicians(savedRequest);
            return savedRequest.getRequestDto();
        }
        return null;
    }
    private void sendEmailToTechnicians(Request request) {
        Optional<User> technicianOpt = userRepository.findByUserRole(UserRole.Admin);
        if (technicianOpt.isPresent()) {
            User technician = technicianOpt.get();
            String subject = "New Intervention Request Created";
            String body = String.format(
                    "A new intervention request has been created with the following details :%n%n" +
                            "Requester: %s%n" +
                            "Title: %s%n" +
                            "Description: %s%n" +
                            "Priority: %s%n" +
                            "Creation Date: %s%n" +
                            "Due Date: %s%n%n" +
                            "Please check the system for more details.",
                    request.getRequester().getName(),
                    request.getTitle(),
                    request.getDescription(),
                    request.getPriority(),
                    request.getCreationDate(),
                    request.getLastUpdate()
            );
            emailSenderService.sendEmail(technician.getEmail(), subject, body);
            System.out.println("Email sent to technician successfully....");
        } else {
            System.out.println("No technician found to send email.");
        }
    }

    @Override
    public List<RequestDto> getAllRequests() {
        return requestRepository.findAll()
                .stream()
                //.sorted(Comparator.comparing(Request::getLastUpdate).reversed())
                .sorted(Comparator.comparing(Request::getLastUpdate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRequest(Long requestId){
        requestRepository.deleteById(requestId);
    }
    @Override
    public RequestDto getRequestByRequestId(Long requestId){
        Optional<Request> request = requestRepository.findById(requestId);
        return request.map(Request::getRequestDto).orElse(null);
    }
    @Override
    public RequestDto updateRequest(Long requestId, RequestDto requestDto) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isPresent()) {
            Request request = requestOptional.get();

            // Update only if the value in requestDto is not null
            if (requestDto.getTitle() != null) {
                request.setTitle(requestDto.getTitle());
            }
            if (requestDto.getDescription() != null) {
                request.setDescription(requestDto.getDescription());
            }
            if (requestDto.getPriority() != null) {
                request.setPriority(requestDto.getPriority());
            }
            if (requestDto.getCreationDate() != null) {
                request.setCreationDate(requestDto.getCreationDate());
            }
            if (requestDto.getLastUpdate() != null) {
                request.setLastUpdate(requestDto.getLastUpdate());
            }
            if (requestDto.getStatus() != null) {
                request.setStatus(mapStringToRequestStatus(String.valueOf(requestDto.getStatus())));
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
    public List<RequestDto> searchRequestByTitle(String title) {
        return requestRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Request::getLastUpdate))
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }
}

package com.interventionManager.services.admin;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.dto.UserDto;
import com.interventionManager.entities.*;
import com.interventionManager.enums.RequestStatus;
import com.interventionManager.enums.UserRole;
import com.interventionManager.repositories.*;
import com.interventionManager.services.email.EmailSenderService;
import com.interventionManager.services.notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EmailSenderService emailSenderService;
    private final MaterialRepository materialRepository;
    private final MaterialHistoryRepository materialHistoryRepository;
    private final NotificationService notificationService;
    private final RequestHistoryRepository requestHistoryRepository;
    @Override
    public List<RequestHistory> getAllArchivedRequestsByTechnicianId(Long technicianId){
        //Optional<User> admin = userRepository.findById(technicianId);
        return requestHistoryRepository.findAllByTechnicianId(technicianId)
                .stream()
                .sorted(Comparator.comparing(RequestHistory::getArchiveDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestHistory> searchArchivedRequestByTitle(String title){
        return requestHistoryRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(RequestHistory::getArchiveDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole()!= UserRole.Admin)
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<UserDto> searchUserByName(String Name){
        return userRepository.findAllByNameContaining(Name)
                .stream()
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAllUnAssignedRequests() {
        Long Id=1L;
        Optional<User> admin = userRepository.findById(Id);
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
    public RequestDto getRequestByRequestId(Long requestId){
        Optional<Request> request = requestRepository.findById(requestId);
        return request.map(Request::getRequestDto).orElse(null);
    }
    @Override
    public RequestDto updateRequestClaimer(Long requestId, Long technicianId){
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isPresent()) {
            Request request = requestOptional.get();

            if (technicianId != null) {
                if(request.getTechnician().getUserId()==1)
                request.setTechnician(userRepository.findById(technicianId).get());
                else {
                    request.setTechnician(userRepository.findById(technicianId).get());
                    request.setStatus(RequestStatus.DEFERRED);
                }
            }
            sendEmailToTechnicians(request,technicianId);
            notificationService.sendNotificationToTechnician(technicianId, "A new request has been assigned to you. With the Title : "+request.getTitle());
            return requestRepository.save(request).getRequestDto();
        }
        return null;
    }
    private void sendEmailToTechnicians(Request request,Long technicianId) {
        Optional<User> technicianOpt = userRepository.findById(technicianId);
        if (technicianOpt.isPresent()) {
            User technician = technicianOpt.get();
            String subject = "New Intervention Request Assigment";
            String body = String.format(
                    "A new intervention request has been Assigned to you  with the following details :%n%n" +
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
    public List<UserDto> getAllTechnicians(){
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole()== UserRole.Technician)
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }
    @Override
    public UserDto setAsTechnician(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserRole(UserRole.Technician);
            return userRepository.save(user).getUserDto();
        }
        return null;
    }
    @Override
    public UserDto setAsRequester(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserRole(UserRole.Requester);
            return userRepository.save(user).getUserDto();
        }
        return null;
    }


///////////////////////////////////////////////////////////////////////////
@Override
public Material addMaterial(Material material) {
    return materialRepository.save(material);
}

    @Override
    public Material updateMaterial(Long materialId,Material materialdto) {
        Optional<Material> materialOptional = materialRepository.findById(materialId);
        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();

            if (materialdto.getName() != null) {
                material.setName(materialdto.getName());
            }
            if (materialdto.getDetailedDescription() != null) {
                material.setDetailedDescription(materialdto.getDetailedDescription());
            }
            if (materialdto.getLocation() != null) {
                material.setLocation(materialdto.getLocation());
            }
            if (materialdto.getLifeSpan() != null) {
                material.setLifeSpan(materialdto.getLifeSpan());
            }
            if (materialdto.getStatus() != null) {
                material.setStatus(materialdto.getStatus());
            }
            if (materialdto.getIntegrationDate() != null) {
                material.setIntegrationDate(materialdto.getIntegrationDate());
            }

            return materialRepository.save(material);
        }
        return null;
    }

    @Override
    public void deleteMaterial(Long materialId) {
        materialRepository.deleteById(materialId);
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Material::getIntegrationDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Material> searchMaterialByTitle(String title) {
        return materialRepository.findByNameContaining(title)
                .stream()
                .sorted(Comparator.comparing(Material::getIntegrationDate))
                .collect(Collectors.toList());
    }

    @Override
    public Material getMaterialByMaterialId(Long materialId) {
        Optional<Material> material = materialRepository.findById(materialId);
        return material.orElse(null);
    }

    @Override
    public void archiveMaterial(Long materialId,String deletionReason) {
        Optional<Material> materialOptional = materialRepository.findById(materialId);
        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();
            MaterialHistory materialHistory = new MaterialHistory(material,deletionReason);
            materialHistoryRepository.save(materialHistory);
            materialRepository.delete(material);
        } else {
            throw new EntityNotFoundException("Material not found");
        }
    }
    /////////////////////////////////////////////////////////////
    @Override
    public long countUnassignedRequestsByTechnicianId() {
        Long technicianId=1L;
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getTechnician() != null && request.getTechnician().getUserId().equals(technicianId))
                .count();
    }
    @Override
    public long countAllAssignedRequests() {
        Long technicianId=1L;
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getTechnician() != null && !request.getTechnician().getUserId().equals(technicianId))
                .count();
    }
    @Override
    public long countAllAssignedRequestsByStatus(String status) {
        Long technicianId=1L;
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getTechnician() != null && !request.getTechnician().getUserId().equals(technicianId))
                .filter(request -> request.getStatus() != null && request.getStatus().equals(RequestStatus.valueOf(status)))
                .count();
    }


    @Override
    public long countAssignedRequestsByTechnicianId(Long technicianId) {
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getTechnician() != null && request.getTechnician().getUserId().equals(technicianId))
                .count();
    }

    @Override
    public long countRequestsByStatusAndTechnicianId(Long technicianId, String status) {
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getTechnician() != null && request.getTechnician().getUserId().equals(technicianId))
                .filter(request -> request.getStatus() != null && request.getStatus().equals(RequestStatus.valueOf(status)))
                .count();
    }
}

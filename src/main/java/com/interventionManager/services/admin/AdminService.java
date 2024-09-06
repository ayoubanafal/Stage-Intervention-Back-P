package com.interventionManager.services.admin;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.dto.UserDto;
import com.interventionManager.entities.Material;
import com.interventionManager.entities.RequestHistory;

import java.util.List;

public interface AdminService {
    List<UserDto> getUsers();
    List<UserDto> searchUserByName(String Name);
    List<RequestDto> getAllUnAssignedRequests();
    List<RequestDto> searchRequestByTitle(String title);
    RequestDto getRequestByRequestId(Long requestId);
    RequestDto updateRequestClaimer(Long requestId, Long technicianId);
    List<UserDto> getAllTechnicians();
    UserDto setAsTechnician(Long UserId);
    UserDto setAsRequester(Long UserId);
    /////////////////////////////////////////////////////////////
    Material addMaterial(Material material);
    Material updateMaterial(Long materialId, Material material);
    void deleteMaterial(Long materialId);
    List<Material> getAllMaterials();
    List<Material> searchMaterialByTitle(String title);
    Material getMaterialByMaterialId(Long materialId);
    void archiveMaterial(Long materialId,String deletionReason);
    //////////////////////////////////////////////////////////
    // New methods for counting requests
    long countUnassignedRequestsByTechnicianId();
    long countAllAssignedRequests();
    long countAllAssignedRequestsByStatus(String status);
    long countAssignedRequestsByTechnicianId(Long technicianId);
    long countRequestsByStatusAndTechnicianId(Long technicianId, String status);
    /////////////////////////////////////////////////////////////
    List<RequestHistory> getAllArchivedRequestsByTechnicianId(Long technicianId);
    //List<RequestHistory> searchArchivedRequestByTitle(String Name);
    List<RequestHistory> searchArchivedRequestByTitle(String title);
}

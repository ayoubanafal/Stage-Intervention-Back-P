package com.interventionManager.controller.admin;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.dto.UserDto;
import com.interventionManager.entities.Material;
import com.interventionManager.entities.RequestHistory;
import com.interventionManager.entities.User;
import com.interventionManager.repositories.UserRepository;
import com.interventionManager.services.admin.AdminService;
import com.interventionManager.services.report.DataBasePDFService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
    private final AdminService adminService;
    private final DataBasePDFService dataBasePDFService;
    private final UserRepository userRepository;
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }
    @PutMapping("/request/setAsTechnician/{userId}")
    public ResponseEntity<?> setAsTechnician(@PathVariable Long userId) {
        UserDto user = adminService.setAsTechnician(userId);
        if(user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
    @PutMapping("/request/setAsRequester/{userId}")
    public ResponseEntity<?> setAsRequester(@PathVariable Long userId) {
        UserDto user = adminService.setAsRequester(userId);
        if(user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
    @GetMapping("/usersT")
    public ResponseEntity<List<UserDto>> getTechnicians() {
        return ResponseEntity.ok(adminService.getAllTechnicians());
    }
    @GetMapping("/users/{Name}")
    public ResponseEntity<?> searchUser(@PathVariable String Name){
        return ResponseEntity.ok(adminService.searchUserByName(Name));
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(adminService.getAllUnAssignedRequests());
    }
    @GetMapping("/requests/search/{title}")
    public ResponseEntity<List<RequestDto>> searchRequest(@PathVariable String title){
        return ResponseEntity.ok(adminService.searchRequestByTitle(title));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<RequestDto> getRequestById(@PathVariable Long requestId)
    {
        return ResponseEntity.ok(adminService.getRequestByRequestId(requestId));
    }

    @PutMapping("/request/{requestId}/{technicianId}")
    public ResponseEntity<?> updateRequest(@PathVariable Long requestId, @PathVariable Long technicianId) {
        RequestDto UpdatedRequestDto = adminService.updateRequestClaimer(requestId, technicianId);
        if(UpdatedRequestDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UpdatedRequestDto);
    }
//////////////////////////////////////////////////////////////////

    @PostMapping("/add")
    public ResponseEntity<Material> addMaterial(@RequestBody Material material) {
        Material savedMaterial = adminService.addMaterial(material);
        return ResponseEntity.ok(savedMaterial);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Material> updateMaterial(@RequestBody Material material, @PathVariable Long id) {
        Material updatedMaterial = adminService.updateMaterial(id,  material);
        return ResponseEntity.ok(updatedMaterial);
    }

    @DeleteMapping("/delete/{materialId}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long materialId) {
        adminService.deleteMaterial(materialId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = adminService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<Material>> searchMaterialByTitle(@PathVariable String title) {
        List<Material> materials = adminService.searchMaterialByTitle(title);
        return ResponseEntity.ok(materials);
    }
    @GetMapping("/material/{materialId}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long materialId)
    {
        return ResponseEntity.ok(adminService.getMaterialByMaterialId(materialId));
    }
    @PutMapping("/material/archive/{materialId}")
    public ResponseEntity<?> archiveMaterial(@PathVariable Long materialId,@RequestBody String deletionReason) {
        try {
            adminService.archiveMaterial(materialId, deletionReason);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Material archived successfully");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/requests/unassigned")
    public ResponseEntity<Long> countUnassignedRequestsByTechnicianId() {
        long count = adminService.countUnassignedRequestsByTechnicianId();
        return ResponseEntity.ok(count);
    }
    @GetMapping("/requests/AllAssigned")
    public ResponseEntity<Long> countALLAssignedRequests() {
        long count = adminService.countAllAssignedRequests();
        return ResponseEntity.ok(count);
    }
    @GetMapping("/requests/status/{status}")
    public ResponseEntity<Long> countRequestsByStatus(@PathVariable String status) {
        long count = adminService.countAllAssignedRequestsByStatus( status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/requests/assigned/{technicianId}")
    public ResponseEntity<Long> countAssignedRequestsByTechnicianId(@PathVariable Long technicianId) {
        long count = adminService.countAssignedRequestsByTechnicianId(technicianId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/requests/status/{technicianId}/{status}")
    public ResponseEntity<Long> countRequestsByStatusAndTechnicianId(@PathVariable Long technicianId, @PathVariable String status) {
        long count = adminService.countRequestsByStatusAndTechnicianId(technicianId, status);
        return ResponseEntity.ok(count);
    }
    /////////////////////////////////////////////////////////////////////////
    @GetMapping("/getArchivedRequests/{technicianId}")
    public ResponseEntity<?> getAllArchivedRequestsByTechnician(@PathVariable Long technicianId) {
        return ResponseEntity.ok(adminService.getAllArchivedRequestsByTechnicianId(technicianId));
    }
    @GetMapping("/searchArchivedRequests/search/{title}")
    public ResponseEntity<List<RequestHistory>> searchArchivedRequest(@PathVariable String title){
        return ResponseEntity.ok(adminService.searchArchivedRequestByTitle(title));
    }
    ///////////////////////////////////////////////////////////////
    @GetMapping(value = "/report/{technicianId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> createTechnicianReport(
            @PathVariable Long technicianId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        Optional<User> technician = userRepository.findById(technicianId);

        // Generate the report with the provided date range
        ByteArrayInputStream bis = dataBasePDFService.technicianPerformanceReport(technicianId, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + technician.get().getName() + "'s Performance Report.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

package com.interventionManager.services.report;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.dto.UserDto;
import com.interventionManager.entities.Request;
import com.interventionManager.entities.RequestHistory;
import com.interventionManager.entities.User;
import com.interventionManager.enums.RequestStatus;
import com.interventionManager.repositories.RequestHistoryRepository;
import com.interventionManager.repositories.RequestRepository;
import com.interventionManager.repositories.UserRepository;
import com.interventionManager.services.jwt.UserService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
//import javax.swing.text.Document;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DataBasePDFService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestHistoryRepository requestHistoryRepository;

    public ByteArrayInputStream requestPDFReport(RequestDto requestDto) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add Title Page
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.BLACK);
            Paragraph title = new Paragraph("Request Report", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Add Subtitle
            Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.GRAY);
            Paragraph subtitle = new Paragraph("Request Details", fontSubtitle);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);

            // Fetching user details
            UserDto requester = userService.getUserById(requestDto.getRequesterId());
            UserDto technician = userService.getUserById(requestDto.getTechnicianId());

            // Add request details in a table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font fontTableCell = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

            // Table Header
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.DARK_GRAY);
            cell.setPadding(5);

            cell.setPhrase(new Phrase("Attribute", fontTableHeader));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Details", fontTableHeader));
            table.addCell(cell);

            // Table Rows
            table.addCell(new Phrase("Request ID:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(requestDto.getRequestId()), fontTableCell));

            table.addCell(new Phrase("Requester:", fontTableCell));
            table.addCell(new Phrase(requester.getName(), fontTableCell));

            table.addCell(new Phrase("Technician:", fontTableCell));
            table.addCell(new Phrase(technician.getName(), fontTableCell));

            table.addCell(new Phrase("Employee Name:", fontTableCell));
            table.addCell(new Phrase(requestDto.getEmployeeName(), fontTableCell));

            table.addCell(new Phrase("Title:", fontTableCell));
            table.addCell(new Phrase(requestDto.getTitle(), fontTableCell));

            table.addCell(new Phrase("Description:", fontTableCell));
            table.addCell(new Phrase(requestDto.getDescription(), fontTableCell));

            table.addCell(new Phrase("Priority:", fontTableCell));
            table.addCell(new Phrase(requestDto.getPriority(), fontTableCell));

            table.addCell(new Phrase("Status:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(requestDto.getStatus()), fontTableCell));

            table.addCell(new Phrase("Creation Date:", fontTableCell));
            table.addCell(new Phrase(requestDto.getCreationDate().toString(), fontTableCell));

            table.addCell(new Phrase("Last Update:", fontTableCell));
            table.addCell(new Phrase(requestDto.getLastUpdate().toString(), fontTableCell));

            document.add(table);

            // Add Footer
            Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Paragraph footer = new Paragraph("Generated by Intervention Manager System", fontFooter);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }


    // Method to fetch requests by technician ID and date range
    public List<RequestDto> getRequestsByTechnicianAndDateRange(Long technicianId, Date fromDate, Date toDate) {
        Optional<User> technician = userRepository.findById(technicianId);
        List<Request> requests = requestRepository.findAllByTechnician(technician);
        List<Request> requests2 = requests.stream()
                .filter(request -> !request.getCreationDate().before(fromDate) && !request.getCreationDate().after(toDate))
                .collect(Collectors.toList());
        return requests2.stream()
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }
    public List<RequestHistory> getCompletedRequestsByTechnicianAndDateRange(Long technicianId, Date fromDate, Date toDate) {
        List<RequestHistory> allRequests = requestHistoryRepository.findAllByTechnicianId(technicianId);
        List<RequestHistory> requestsCompleted = allRequests.stream()
                .filter(request -> !request.getArchiveDate().before(fromDate) && !request.getArchiveDate().after(toDate))
                .collect(Collectors.toList());
        return requestsCompleted.stream()
                .sorted(Comparator.comparing(RequestHistory::getArchiveDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }


    public ByteArrayInputStream technicianPerformanceReport(Long technicianId, Date fromDate, Date toDate) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add Title Page
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.BLACK);
            Paragraph title = new Paragraph("Technician Performance Report", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Add Subtitle
            Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.GRAY);
            Paragraph subtitle = new Paragraph("Performance Details", fontSubtitle);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);

            // Fetch technician details
            UserDto technician = userService.getUserById(technicianId);

            // Fetching assigned requests within the date range
            List<RequestDto> assignedRequests = getRequestsByTechnicianAndDateRange(technicianId, fromDate, toDate);
            List<RequestHistory> assignedCompletedRequests = getCompletedRequestsByTechnicianAndDateRange(technicianId, fromDate, toDate);


            // Count by status
            long completedRequests = assignedCompletedRequests.stream()
                    .filter(requestHistory -> requestHistory.getStatus() == RequestStatus.COMPLETED)
                    .count();
            long inProgressRequests = assignedRequests.stream()
                    .filter(request -> request.getStatus() == RequestStatus.INPROGRESS)
                    .count();
            long deferedRequests = assignedRequests.stream()
                    .filter(request -> request.getStatus() == RequestStatus.DEFERRED)
                    .count();
            long pendingRequests = assignedRequests.stream()
                    .filter(request -> request.getStatus() == RequestStatus.PENDING)
                    .count();
            long cancelledRequests = assignedCompletedRequests.stream()
                    .filter(requestHistory -> requestHistory.getStatus() == RequestStatus.CANCELLED)
                    .count();


            // Add performance details in a table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font fontTableCell = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

            // Table Header
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.DARK_GRAY);
            cell.setPadding(5);

            cell.setPhrase(new Phrase("Attribute", fontTableHeader));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Details", fontTableHeader));
            table.addCell(cell);

            // Table Rows
            table.addCell(new Phrase("Technician Name:", fontTableCell));
            table.addCell(new Phrase(technician.getName(), fontTableCell));

            table.addCell(new Phrase("Since Date:", fontTableCell));
            table.addCell(new Phrase(fromDate.toString(), fontTableCell));

            table.addCell(new Phrase("To Date:", fontTableCell));
            table.addCell(new Phrase(toDate.toString(), fontTableCell));

            table.addCell(new Phrase("Total Assigned Requests:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(assignedRequests.size()), fontTableCell));

            table.addCell(new Phrase("Completed Requests:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(completedRequests), fontTableCell));

            table.addCell(new Phrase("In Progress Requests:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(inProgressRequests), fontTableCell));
            table.addCell(new Phrase("Pending Requests:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(pendingRequests), fontTableCell));
            table.addCell(new Phrase("Deferred Requests:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(deferedRequests), fontTableCell));
            table.addCell(new Phrase("Cancelled Requests:", fontTableCell));
            table.addCell(new Phrase(String.valueOf(cancelledRequests), fontTableCell));

            document.add(table);

            // Add Footer
            Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Paragraph footer = new Paragraph("Generated by Intervention Manager System", fontFooter);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }


}
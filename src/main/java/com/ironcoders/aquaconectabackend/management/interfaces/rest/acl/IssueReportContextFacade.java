package com.ironcoders.aquaconectabackend.management.interfaces.rest.acl;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllIssueReportsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.IssueReportQueryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IssueReportContextFacade {

    private final IssueReportQueryService issueReportQueryService;

    public IssueReportContextFacade(IssueReportQueryService issueReportQueryService) {
        this.issueReportQueryService = issueReportQueryService;
    }

    /**
     * Fetches all issue reports by residentId.
     *
     * @param residentId the resident id
     * @return a list of IssueReport
     */
    public List<IssueReport> fetchIssueReportsByResidentId(Long residentId) {
        return issueReportQueryService.handle(new GetAllIssueReportsByResidentIdQuery(residentId));
    }
}

package com.payflowapi.repository;

import java.util.List;
import java.time.LocalDate;

import com.payflowapi.entity.EmployeeLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Long> {
    List<EmployeeLeave> findByEmployeeIdIn(List<Long> employeeIds);

    List<EmployeeLeave> findByEmployeeId(Long employeeId);

    List<EmployeeLeave> findByManagerId(Long managerId);

    List<EmployeeLeave> findByEmployeeIdAndStatus(Long employeeId, String status);

    @Query("SELECT el FROM EmployeeLeave el WHERE el.employeeId = :employeeId AND el.status = 'ACCEPTED' " +
            "AND el.toDate >= :startDate AND el.fromDate <= :endDate")
    List<EmployeeLeave> findApprovedLeavesInMonth(@Param("employeeId") Long employeeId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    // Find approved paid leaves for the current year
    @Query("SELECT el FROM EmployeeLeave el WHERE el.employeeId = :employeeId AND el.status = 'ACCEPTED' " +
            "AND el.isPaid = true AND YEAR(el.fromDate) = :year")
    List<EmployeeLeave> findApprovedPaidLeavesInYear(@Param("employeeId") Long employeeId,
                                                     @Param("year") int year);

    // Find approved unpaid leaves for a specific month
    @Query("SELECT el FROM EmployeeLeave el WHERE el.employeeId = :employeeId AND el.status = 'ACCEPTED' " +
            "AND el.isPaid = false AND YEAR(el.fromDate) = :year AND MONTH(el.fromDate) = :month")
    List<EmployeeLeave> findApprovedUnpaidLeavesInMonth(@Param("employeeId") Long employeeId,
                                                        @Param("year") int year,
                                                        @Param("month") int month);

    // Find all approved unpaid leaves for the current year
    @Query("SELECT el FROM EmployeeLeave el WHERE el.employeeId = :employeeId AND el.status = 'ACCEPTED' " +
            "AND el.isPaid = false AND YEAR(el.fromDate) = :year")
    List<EmployeeLeave> findApprovedUnpaidLeavesInYear(@Param("employeeId") Long employeeId,
                                                       @Param("year") int year);

    // Find overlapping leave requests for the same employee
    @Query("SELECT el FROM EmployeeLeave el WHERE el.employeeId = :employeeId " +
            "AND el.status != 'DENIED' " +
            "AND ((el.fromDate <= :toDate AND el.toDate >= :fromDate))")
    List<EmployeeLeave> findOverlappingLeaves(@Param("employeeId") Long employeeId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);
}

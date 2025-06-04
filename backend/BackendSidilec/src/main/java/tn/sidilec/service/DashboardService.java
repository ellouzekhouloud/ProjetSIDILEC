package tn.sidilec.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tn.sidilec.Repository.ProduitRepository;
import tn.sidilec.dto.QuantiteFournisseurDTO;

@Service
public class DashboardService {
	@Autowired
    private ProduitRepository produitRepository;
	

	public List<QuantiteFournisseurDTO> getQuantitesParFournisseur(String filterType, String date, String startDateStr, String endDateStr) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate startDate;
	    LocalDate endDate;

	    switch (filterType) {
	        case "month":
	            startDate = LocalDate.parse(date, formatter).withDayOfMonth(1);
	            endDate = startDate.plusMonths(1).minusDays(1);
	            break;

	        case "quarter":
	            int currentQuarter = (LocalDate.parse(date, formatter).getMonthValue() - 1) / 3;
	            startDate = LocalDate.parse(date, formatter).withMonth(currentQuarter * 3 + 1).withDayOfMonth(1);
	            endDate = startDate.plusMonths(3).minusDays(1);
	            break;

	        case "semester":
	            int currentSemester = (LocalDate.parse(date, formatter).getMonthValue() - 1) / 6;
	            startDate = LocalDate.parse(date, formatter).withMonth(currentSemester * 6 + 1).withDayOfMonth(1);
	            endDate = startDate.plusMonths(6).minusDays(1);
	            break;

	        case "year":
	            startDate = LocalDate.parse(date, formatter).withDayOfYear(1);
	            endDate = startDate.plusYears(1).minusDays(1);
	            break;

	        case "custom":
	            if (startDateStr == null || endDateStr == null) {
	                throw new IllegalArgumentException("startDate et endDate sont requis pour le filtre personnalisé.");
	            }
	            startDate = LocalDate.parse(startDateStr, formatter);
	            endDate = LocalDate.parse(endDateStr, formatter);
	            break;

	        default:
	            throw new IllegalArgumentException("Type de filtre non valide : " + filterType);
	    }

	    return produitRepository.findQuantitesParFournisseur(startDate, endDate);
	}

}

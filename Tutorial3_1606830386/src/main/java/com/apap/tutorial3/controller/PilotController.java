package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id",required = true) String id,
					@RequestParam(value = "licenseNumber",required = true) String licenseNumber,
					@RequestParam(value = "name",required = true) String name,
					@RequestParam(value = "flyHour",required = true) int flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}
	
	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}
	
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		model.addAttribute("listPilot", archive);
		return "view-all";
	}
	
	@RequestMapping("/pilot/view/license-number/{licenseNumber}")
	public String viewPilot(@PathVariable Optional<String> licenseNumber, Model model) {
		if(licenseNumber.isPresent()) {
			String pilotLN = licenseNumber.get();
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(pilotLN);
			if(archive!= null) {
				model.addAttribute("pilot", archive);
				return "view-pilot";
			}
			else {
				return "error";
			}
		}
		else {
			return "error";
		}
	}
	
	@RequestMapping("/pilot/update/license-number/{licenseNumber}/fly-hour/{flyHour}")
	public String updatePilot(@PathVariable Optional<String> licenseNumber, 
							@PathVariable Optional<Integer> flyHour, Model model) {
		System.out.println("testing");
		if(licenseNumber.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			System.out.println(archive);
			if(archive!=null) {
				archive.setFlyHour(flyHour.get());
				return "update-pilot";
			}
			else {
				System.out.println("salah disini bro");
				return "error-update";
			}
		}
		else {
			System.out.println("SALAAHHH BROOOOOO");
			return "error-update";
		}
	}
	
	@RequestMapping("/pilot/delete/id/{id}")
	public String delete(@PathVariable Optional<String> id, Model model) {
		if(id.isPresent()) {
			boolean found = false;
			for(PilotModel pilot:pilotService.getPilotList()) {
				if(pilot.getId().equals(id.get())) {
					found = true;
					pilotService.deletePilot(pilot);
				}
			}
			if(found) {
				return "delete-pilot";
			}
			else {
				return "error-delete";
			}
		}
		else {
			return "error-delete";
		}
	}
}

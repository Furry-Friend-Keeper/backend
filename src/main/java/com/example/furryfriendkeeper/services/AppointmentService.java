package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.AppointmentDTO;
import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.dtos.DisableAppointmentDTO;
import com.example.furryfriendkeeper.dtos.ReviewDTO;
import com.example.furryfriendkeeper.entities.*;
import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.repositories.*;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    @Autowired
    private AppointmentScheduleRepository appointmentScheduleRepository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final OwnerRepository ownerRepository;

    private final DisableScheduleRepository disableScheduleRepository;

    private final CategoriesRepository categoriesRepository;

    private final PetkeeperRepository petkeeperRepository;

    private final ReviewRepository reviewRepository;

    public List<Appointmentschedule> getAllSchedule(){
        List<Appointmentschedule> appointmentschedule = appointmentScheduleRepository.findAll();
        return appointmentschedule;
    }

    public List<AppointmentScheduleDTO> getAllScheduleByPetkeeper(Integer petkeeperId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer petkeeper = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        if(role.equals("PetKeeper") && petkeeper == petkeeperId) {
            List<Appointmentschedule> listAppointment = appointmentScheduleRepository.getAppointmentByPetkeeper(petkeeperId);
            List<AppointmentScheduleDTO> listDto = listMapper.mapList(listAppointment,AppointmentScheduleDTO.class,modelMapper);
            return listDto;
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission.");
    }

    public List<AppointmentScheduleDTO> getAllScheduleByPetOwner(Integer petownerId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer petowner = ownerRepository.getPetownerIdByEmail(emailCheck);

        if(role.equals("Owner") && petowner ==  petownerId) {
            List<Appointmentschedule> listAppointment = appointmentScheduleRepository.getAppointmentByPetOwner(petownerId);
            if(!listAppointment.isEmpty()) {
                List<AppointmentScheduleDTO> listDto = listMapper.mapList(listAppointment, AppointmentScheduleDTO.class, modelMapper);
                for (int i = 0 ; i < listAppointment.size(); i++) {
                    listDto.get(i).setKeeperImg(listAppointment.get(i).getPetKeeper().getImg());
                    listDto.get(i).setKeeperId(listAppointment.get(i).getPetKeeper().getId());
                    listDto.get(i).setKeeperPhone(listAppointment.get(i).getPetKeeper().getPhone());
                }
                return listDto;
            }else throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found!");
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission.");

    }

    @Transactional
    public AppointmentDTO createRequest(AppointmentDTO newAppointment,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        List<Integer> checkCategories = categoriesRepository.FindKeeperCategories(newAppointment.getPetKeeperId());
        Petkeepers checkKeeper = petkeeperRepository.getById(newAppointment.getPetKeeperId());
        Petowner checkOwner = ownerRepository.getById(newAppointment.getPetOwnerId());
        if(checkKeeper == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is no Petkeeper.");
        }
        if(checkOwner == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is no Petowner.");
        }
        if(newAppointment.getEndDate().isBefore(newAppointment.getStartDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End Date Cannot be before Start Date.");
        }
        if(newAppointment.getStartDate().isBefore(ZonedDateTime.now()) || !(newAppointment.getStartDate().toLocalDate().isAfter(ZonedDateTime.now().plusDays(3).toLocalDate()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date input.");
        }
        if(!checkCategories.contains(newAppointment.getCategoryId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Category,Please try again.");
        }
        String[] days = checkKeeper.getClosedDay().toLowerCase().split(",\\s*");
        List<String> closedList = Arrays.asList(days);
        String startDay = newAppointment.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()).toLowerCase();
        String endDay = newAppointment.getEndDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()).toLowerCase();
        if(closedList.contains(startDay) || closedList.contains(endDay)){
            //Closed Day
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Day.(Petkeeper closed!)");
        }
        if(role.equals("Owner") && ownerId == newAppointment.getPetOwnerId()) {
            List<Disableappointmentschedule> checkDate = disableScheduleRepository.getDisableScheduleByPetkeeper(newAppointment.getPetKeeperId());
            LocalDate startDateAppointment = newAppointment.getStartDate().toLocalDate();
            LocalDate endDateAppointment = newAppointment.getEndDate().toLocalDate();
            for (Disableappointmentschedule checkDate1: checkDate) {
                if((startDateAppointment.isBefore(checkDate1.getStartDate()) && endDateAppointment.isBefore(checkDate1.getStartDate()))
                        || startDateAppointment.isAfter(checkDate1.getEndDate())
                        || (startDateAppointment.isBefore(checkDate1.getStartDate()) && endDateAppointment.isAfter(checkDate1.getEndDate()))) {
                    //No Overlap
                }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot make appointment at these period.");
            }         

                try {
                    newAppointment.setStatusId(1);
                    modelMapper.getConfiguration().setAmbiguityIgnored(true);
                    Appointmentschedule appointmentschedule = modelMapper.map(newAppointment, Appointmentschedule.class);
                    appointmentScheduleRepository.saveAndFlush(appointmentschedule);
                } catch (Exception e) {
                    throw e;
                }

        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission.");

        return newAppointment;

    }

    @Transactional
    public String confirmAppointment(Integer appointmentId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        if(appointmentschedule.getStatus().getId() == 1) {
            if (role.equals("PetKeeper") && keeperId == appointmentschedule.getPetKeeper().getId()) {
                appointmentScheduleRepository.updateStatus(2, appointmentId);
            } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
        }
        return keeperId +  " has confirm Appointment from "+ appointmentschedule.getPetOwner().getId() + " Successfully!";

    }

    @Transactional
    public String cancelAppointment(Integer appointmentId, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        if(appointmentschedule.getStatus().getId() != 4 || appointmentschedule.getStatus().getId() != 5 || appointmentschedule.getStatus().getId() != 6) {
            if (role.equals("PetKeeper")) {
                Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
                if (keeperId == appointmentschedule.getPetKeeper().getId()) {
                    appointmentScheduleRepository.updateStatus(3, appointmentId);
                } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
            } else if (role.equals("Owner")) {
                Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
                if (ownerId == appointmentschedule.getPetOwner().getId()) {
                    appointmentScheduleRepository.updateStatus(3, appointmentId);
                } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
            }
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid Appointment Status.");
        return "Appointment :" + appointmentId + " - Cancelled";
    }

    @Transactional
    public String inCareAppointment(Integer appointmentId, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        if(appointmentschedule.getStatus().getId() == 2){
            if(role.equals("PetKeeper") && keeperId == appointmentschedule.getPetKeeper().getId()){
                appointmentScheduleRepository.updateStatus(4, appointmentId);
            }
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid Appointment Status.");
        return "Appointment :" + appointmentId + " - In Care";
    }

    @Transactional
    public String keeperCompletedAppointment(Integer appointmentId, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        if(appointmentschedule.getStatus().getId() == 4){
            if(role.equals("PetKeeper") && keeperId == appointmentschedule.getPetKeeper().getId()){
                appointmentScheduleRepository.updateStatus(5, appointmentId);
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid Appointment Status.");
        return "Appointment :" + appointmentId + " - Keeper Completed";
    }

    @Transactional
    public ReviewDTO ownerCompletedAppointment(Integer appointmentId, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        if(appointmentschedule.getStatus().getId() == 5){
            if(role.equals("Owner") && ownerId == appointmentschedule.getPetOwner().getId()){
                appointmentScheduleRepository.updateStatus(6, appointmentId);
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid Appointment Status.");

        Review review = reviewRepository.findReviewByPetowner(appointmentschedule.getPetKeeper().getId(),appointmentschedule.getPetOwner().getId());
        if(review != null) {
            ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
            return reviewDTO;
        }else return null;
    }

    public DisableAppointmentDTO addDisableAppointment(Integer petkeeperId, DisableAppointmentDTO disableAppointment,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        if(role.equals("PetKeeper") && petkeeperId == keeperId) {
            Disableappointmentschedule disableSchedule = modelMapper.map(disableAppointment, Disableappointmentschedule.class);
            Petkeepers petkeeper = petkeeperRepository.getById(petkeeperId);
            disableSchedule.setPetKeeper(petkeeper);
            disableScheduleRepository.saveAndFlush(disableSchedule);
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission.");
        return disableAppointment;
    }

    public String deleteDisableAppointment(Integer disableScheduleId,String token) {
        Disableappointmentschedule disableappointmentscheduleCheck = disableScheduleRepository.findById(disableScheduleId).orElseThrow(()-> new ResponseStatusException
                (HttpStatus.NOT_FOUND, "This disableschedule does not exist!"));
        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Disableappointmentschedule disableappointmentschedule = disableScheduleRepository.getById(disableScheduleId);
        if (role.equals("PetKeeper") && keeperId == disableappointmentschedule.getPetKeeper().getId()) {
            disableScheduleRepository.deleteById(disableScheduleId);
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission");
        return "Delete Successfully";
    }




}

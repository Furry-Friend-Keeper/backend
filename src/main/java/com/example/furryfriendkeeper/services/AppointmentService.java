package com.example.furryfriendkeeper.services;

import com.example.furryfriendkeeper.dtos.*;
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

    private final NotificationService notificationService;

    private final PetkeeperNotificationRepository petkeeperNotificationRepository;

    private final PetownerNotificationRepository petownerNotificationRepository;

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
        if(checkKeeper.getClosedDay() != null && !checkKeeper.getClosedDay().isEmpty()) {
            String[] days = checkKeeper.getClosedDay().toLowerCase().split(",\\s*");
            List<String> closedList = Arrays.asList(days);
            String startDay = newAppointment.getStartDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()).toLowerCase();
            String endDay = newAppointment.getEndDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()).toLowerCase();
            if (closedList.contains(startDay) || closedList.contains(endDay)) {
                //Closed Day
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Day.(Petkeeper closed!)");
            }
        }
        if(role.equals("Owner") && ownerId == newAppointment.getPetOwnerId()) {
            List<Disableappointmentschedule> checkDate = disableScheduleRepository.getDisableScheduleByPetkeeper(newAppointment.getPetKeeperId());
            LocalDate startDateAppointment = newAppointment.getStartDate().toLocalDate();
            LocalDate endDateAppointment = newAppointment.getEndDate().toLocalDate();
            for (Disableappointmentschedule checkDate1: checkDate) {
                if((startDateAppointment.isBefore(checkDate1.getStartDate()) && endDateAppointment.isBefore(checkDate1.getStartDate()))
                        || startDateAppointment.isAfter(checkDate1.getEndDate())){
//                        || (startDateAppointment.isBefore(checkDate1.getStartDate()) && endDateAppointment.isAfter(checkDate1.getEndDate()))) {
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

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setDateStart(ZonedDateTime.now());
        notificationDTO.setMessage("You got new request from" + checkOwner.toString());
        notificationDTO.setStatusId(1);
        notificationDTO.setPetkeeperId(newAppointment.getPetKeeperId());
        notificationDTO.setReadStatus(0);
        Petkeepernotification petkeepernotification = modelMapper.map(notificationDTO,Petkeepernotification.class);
        ResponseMessage message = new ResponseMessage("You got new request from " + checkOwner.toString(),ZonedDateTime.now(),0,checkOwner.toString(),petkeepernotification.getStatus());

        petkeeperNotificationRepository.saveAndFlush(petkeepernotification);
        notificationService.sendRequestNotification(checkKeeper.getEmail().getId().toString(),message);
        return newAppointment;

    }

    @Transactional
    public String confirmAppointment(Integer appointmentId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        Petowner checkOwner = ownerRepository.getById(appointmentschedule.getPetOwner().getId());
        if(appointmentschedule.getStatus().getId() == 1) {
            if (role.equals("PetKeeper") && keeperId == appointmentschedule.getPetKeeper().getId()) {
                appointmentScheduleRepository.updateStatus(2, appointmentId);
            } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
        }
        String response = "Appointment: " + appointmentschedule.getPetKeeper().getName() + " - Confirmed";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(response);
        notificationDTO.setDateStart(ZonedDateTime.now());
        notificationDTO.setReadStatus(0);
        notificationDTO.setStatusId(2);

        Petownernotification petownernotification = modelMapper.map(notificationDTO,Petownernotification.class);
        petownernotification.setPetOwner(checkOwner);
        ResponseMessage responseMessage = new ResponseMessage(response,ZonedDateTime.now(),0,appointmentschedule.getPetKeeper().getName(),petownernotification.getStatus());
        notificationService.sendConfirmNotification(checkOwner.getEmail().getId().toString(),responseMessage);
        petownerNotificationRepository.saveAndFlush(petownernotification);
        return keeperId +  " has confirm Appointment from "+ appointmentschedule.getPetOwner().getId() + " Successfully!";
    }

    @Transactional
    public String cancelAppointment(Integer appointmentId,String message, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        Petkeepers checkKeeper = petkeeperRepository.getById(appointmentschedule.getPetKeeper().getId());
        Petowner checkOwner = ownerRepository.getById(appointmentschedule.getPetOwner().getId());
        if(appointmentschedule.getStatus().getId() != 4 && appointmentschedule.getStatus().getId() != 5 && appointmentschedule.getStatus().getId() != 6) {
            if (role.equals("PetKeeper")) {
                Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
                if (keeperId == appointmentschedule.getPetKeeper().getId()) {
                    appointmentScheduleRepository.updateMessage(message,appointmentId);
                    appointmentScheduleRepository.updateStatus(3, appointmentId);
                } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
            } else if (role.equals("Owner")) {
                Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
                if (ownerId == appointmentschedule.getPetOwner().getId()) {
                    appointmentScheduleRepository.updateMessage(message,appointmentId);
                    appointmentScheduleRepository.updateStatus(3, appointmentId);
                } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
            }
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid Appointment Status.");

        String response1 = "Appointment : " + checkKeeper.getName() + " - Cancelled";
        String response2 = "Appointment : " + checkOwner.toString() + " - Cancelled";

        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.setReadStatus(0);
        notificationDTO1.setDateStart(ZonedDateTime.now());
        notificationDTO1.setStatusId(3);
        notificationDTO1.setMessage(response1);
        Petownernotification petownernotification = modelMapper.map(notificationDTO1,Petownernotification.class);
        petownernotification.setPetOwner(checkOwner);
        petownerNotificationRepository.saveAndFlush(petownernotification);

        NotificationDTO notificationDTO2 = new NotificationDTO();
        notificationDTO2.setReadStatus(0);
        notificationDTO2.setDateStart(ZonedDateTime.now());
        notificationDTO2.setStatusId(3);
        notificationDTO2.setMessage(response2);
        notificationDTO2.setPetkeeperId(checkKeeper.getId());

        Petkeepernotification petkeepernotification = modelMapper.map(notificationDTO2,Petkeepernotification.class);
        petkeeperNotificationRepository.saveAndFlush(petkeepernotification);
        ResponseMessage responseMessage1 = new ResponseMessage(response1,ZonedDateTime.now(),0,checkKeeper.getName(),petownernotification.getStatus());
        ResponseMessage responseMessage2 = new ResponseMessage(response2,ZonedDateTime.now(),0,checkOwner.toString(),petkeepernotification.getStatus());
        notificationService.sendRequestCancelNotification(checkKeeper.getEmail().getId().toString(),responseMessage2);
        notificationService.sendRequestCancelNotification(checkOwner.getEmail().getId().toString(),responseMessage1);
        return "Appointment : " + appointmentId + " - Cancelled";
    }

    @Transactional
    public String inCareAppointment(Integer appointmentId, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        Petowner checkOwner = ownerRepository.getById(appointmentschedule.getPetOwner().getId());

        if(appointmentschedule.getStatus().getId() == 2){
            if(role.equals("PetKeeper") && keeperId == appointmentschedule.getPetKeeper().getId()){
                appointmentScheduleRepository.updateStatus(4, appointmentId);
            }
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid Appointment Status.");
        String response = "Appointment : " + appointmentschedule.getPetKeeper().getName() + " - In care";
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(response);
        notificationDTO.setReadStatus(0);
        notificationDTO.setStatusId(4);
        notificationDTO.setDateStart(ZonedDateTime.now());
        Petownernotification petownernotification = modelMapper.map(notificationDTO,Petownernotification.class);
        ResponseMessage responseMessage = new ResponseMessage(response,ZonedDateTime.now(),0,appointmentschedule.getPetKeeper().getName(),petownernotification.getStatus());
        notificationService.sendIncareNotification(checkOwner.getEmail().getId().toString(),responseMessage);


        petownernotification.setPetOwner(checkOwner);

        petownerNotificationRepository.saveAndFlush(petownernotification);

        return "Appointment :" + appointmentId + " - In Care";
    }

    @Transactional
    public String keeperCompletedAppointment(Integer appointmentId, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Appointmentschedule appointmentschedule = appointmentScheduleRepository.getAppointmentscheduleById(appointmentId);
        Petowner checkOwner = ownerRepository.getById(appointmentschedule.getPetOwner().getId());
        System.out.println(appointmentschedule.getStatus().getId());
        if(appointmentschedule.getStatus().getId() == 4){
            if(role.equals("PetKeeper") && keeperId == appointmentschedule.getPetKeeper().getId()){
                appointmentScheduleRepository.updateStatus(5, appointmentId);
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission!");
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid Appointment Status." );
        String response = "Appointment :" + appointmentschedule.getPetKeeper().getName() + " - Keeper Completed";
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(response);
        notificationDTO.setReadStatus(0);
        notificationDTO.setStatusId(5);
        notificationDTO.setDateStart(ZonedDateTime.now());
        Petownernotification petownernotification = modelMapper.map(notificationDTO,Petownernotification.class);
        petownernotification.setPetOwner(appointmentschedule.getPetOwner());
        ResponseMessage responseMessage = new ResponseMessage(response,ZonedDateTime.now(),0,appointmentschedule.getPetKeeper().getName(),petownernotification.getStatus());
        notificationService.sendKeeperCompleteNotification(checkOwner.getEmail().getId().toString(),responseMessage);



        petownerNotificationRepository.saveAndFlush(petownernotification);
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
                String response = "Appointment :" + appointmentschedule.getPetOwner().toString() + " - Owner Completed";
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setStatusId(6);
                notificationDTO.setReadStatus(0);
                notificationDTO.setDateStart(ZonedDateTime.now());
                notificationDTO.setMessage(response);
                notificationDTO.setPetkeeperId(appointmentschedule.getPetKeeper().getId());
                Petkeepernotification petkeepernotification = modelMapper.map(notificationDTO,Petkeepernotification.class);
                ResponseMessage responseMessage = new ResponseMessage(response,ZonedDateTime.now(),0,appointmentschedule.getPetOwner().toString(), petkeepernotification.getStatus());
                notificationService.sendOwnerCompleteNotification(appointmentschedule.getPetOwner().getId().toString(),responseMessage);


                petkeeperNotificationRepository.saveAndFlush(petkeepernotification);

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

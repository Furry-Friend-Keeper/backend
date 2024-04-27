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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PetkeeperService {

    @Autowired
    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final PetkeeperRepository petkeeperRepository;

    private final PetRepository petRepository;

    private final CategoriesRepository categoriesRepository;

    private final ReviewRepository reviewRepository;

    private final GalleryRepository galleryRepository;

    private final FileService fileService;

    private final AddressRepository addressRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final DisableScheduleRepository disableScheduleRepository;

    public List<PetkeeperDTO> getPetkeeperList(){
        List<Petkeepers> petkeepersList = petkeeperRepository.findAll();
        List<PetkeeperDTO> keepers = listMapper.mapList(petkeepersList, PetkeeperDTO.class, modelMapper);

        for(int i = 0; i < petkeepersList.size(); i++){
            Set<String> categories = new LinkedHashSet<>();
            List<Integer> petcats = categoriesRepository.FindKeeperCategories(petkeepersList.get(i).getId());
            Double avgStars = reviewRepository.avgStars(petkeepersList.get(i).getId());
            List<String> map = addressRepository.getMapByPetkeeperId(petkeepersList.get(i).getId());
            if(avgStars == null){
                avgStars = 0.0;
            }
            keepers.get(i).setReviewStars(avgStars);
            keepers.get(i).setMap(map);
            for(int a = 0; a < petcats.size(); a++){
                String name = petRepository.CateName(petcats.get(a));
                categories.add(name);
                keepers.get(i).setCategories(categories);

            }

        }

        return keepers;

    }

    public PetkeeperDetailDTO getPetkeeperDetails(Integer petkeepersId){
        Optional<Petkeepers> petkeeperDetails = petkeeperRepository.findById(petkeepersId);
        List<String> galleries = galleryRepository.findGalleriesByPetkeeperId(petkeepersId);
        PetkeeperDetailDTO petkeeperDetailDTO = modelMapper.map(petkeeperDetails, PetkeeperDetailDTO.class);
        List<Disableappointmentschedule> disableappointmentschedule = disableScheduleRepository.getDisableScheduleByPetkeeper(petkeepersId);
        List<DisableDateDTO> disableDto = listMapper.mapList(disableappointmentschedule,DisableDateDTO.class,modelMapper);
        if(petkeeperDetailDTO == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Petkeeper Not Found");
        }
        Double avgReviewStar = reviewRepository.avgStars(petkeepersId);
        List<String> categories = categoriesRepository.FindCategoriesByPetkeeperId(petkeepersId);
        if(avgReviewStar == null){
            avgReviewStar = 0.0;
        }

        String formatStar = String.format("%.1f",avgReviewStar);
        Double avgStar = Double.parseDouble(formatStar);
        petkeeperDetailDTO.setGallery(galleries);
        petkeeperDetailDTO.setReviewStars(avgStar);
        petkeeperDetailDTO.setCategories(categories);
        for(int i = 0; i < disableDto.size() ; i++){
            ZonedDateTime a = disableappointmentschedule.get(i).getStartDate().atStartOfDay(ZoneId.systemDefault());
            ZonedDateTime b = disableappointmentschedule.get(i).getEndDate().atStartOfDay(ZoneId.systemDefault());
            disableDto.get(i).setStartDate(a);
            disableDto.get(i).setEndDate(b);
        }
        petkeeperDetailDTO.setDisableAppointment(disableDto);
        return petkeeperDetailDTO;

    }
    public Petkeepers save(PetkeeperDetailDTO newPetkeeper){
        return petkeeperRepository.saveAndFlush(modelMapper.map(newPetkeeper, Petkeepers.class));
    }

    public List<CategoriesDTO> AllCategories(){
        List<Pet> pet = petRepository.findAll();
        return listMapper.mapList(pet, CategoriesDTO.class, modelMapper);
    }

    @Transactional(rollbackOn = Exception.class)
    public Petkeepers updatePetkeeper(PetKeeperEditDTO updatePetkeepers, Integer petkeeperId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        String keeperEmail = petkeeperRepository.getPetkeeperEmailById(petkeeperId);

        if (updatePetkeepers.getCategories() != null) {
            categoriesRepository.DeleteCategoriesByPetkeeperId(petkeeperId);
            for (Integer category : updatePetkeepers.getCategories()) {
                Pet petcategory = petRepository.getById(category);
                Petcategory newPetCategory = new Petcategory(petkeeperId, petcategory);
                categoriesRepository.saveAndFlush(newPetCategory);
            }

        }


        if(role.equals("PetKeeper") && emailCheck.equals(keeperEmail)) {
            try {
                Petkeepers petkeeperDetail = modelMapper.map(updatePetkeepers, Petkeepers.class);
                Petkeepers petkeepers = petkeeperRepository.findById(petkeeperId).map(oldDetail -> mapPetkeeper(oldDetail, petkeeperDetail)).orElseGet(() ->
                {
                    petkeeperDetail.setId(petkeeperId);
                    return petkeeperDetail;
                });

                if (updatePetkeepers.getAddress() != null) {
                    Address oldAddress = addressRepository.findAddressDetailByPetkeeperId(petkeeperId);
                    mapAddress(oldAddress, updatePetkeepers);
                    addressRepository.saveAndFlush(oldAddress);

                }

                return petkeeperRepository.saveAndFlush(petkeepers);
            } catch (Exception e) {
                throw e;
            }
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission!");
    }


    private Petkeepers mapPetkeeper(Petkeepers oldDetail, Petkeepers newDetail){
        if(newDetail.getName() != null) {
            oldDetail.setName(newDetail.getName());
        }
        if(newDetail.getContact() != null) {
            oldDetail.setContact(newDetail.getContact());
        }
        if (newDetail.getDetail() != null) {
            oldDetail.setDetail(newDetail.getDetail());
        }
        if (newDetail.getPhone() != null) {
            oldDetail.setPhone(newDetail.getPhone());
        }
        if (newDetail.getAvailable() != null) {
            oldDetail.setAvailable(newDetail.getAvailable());
        }
        return oldDetail;
    }
    public Address mapAddress(Address oldAddress, PetKeeperEditDTO newDetail){


        if(newDetail.getAddress().getAddress() != null){
            oldAddress.setAddress(newDetail.getAddress().getAddress());
        }
        if (newDetail.getAddress().getDistrict() != null){
            oldAddress.setDistrict(newDetail.getAddress().getDistrict());
        }
        if(newDetail.getAddress().getProvince() != null){
            oldAddress.setProvince(newDetail.getAddress().getProvince());
        }
        if(newDetail.getAddress().getPostalCode() != null){
            oldAddress.setPostalCode(newDetail.getAddress().getPostalCode());
        }
        if(newDetail.getAddress().getMap() != null){
            oldAddress.setMap(newDetail.getAddress().getMap());
        }
        return oldAddress;
    }


    @Transactional(rollbackOn = RuntimeException.class)
    public String uploadProfile(Integer keeperId, MultipartFile file, String token){

        Petkeepers petkeeper = modelMapper.map(petkeeperRepository.findById(keeperId), Petkeepers.class);
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        String keeperEmail = petkeeperRepository.getPetkeeperEmailById(keeperId);
        if(role.equals("PetKeeper") && emailCheck.equals(keeperEmail)) {
            if(fileService.isSupportedContentType(file.getContentType())) {
                try {
                    if (petkeeper.getImg() != null && file != null) {
                        boolean isImageExist = fileService.doesImageExist(petkeeper.getImg(), keeperId,role);
                        if (isImageExist) {
                            fileService.deleteProfileImg(petkeeper.getImg(), keeperId,role);
                        }
                    }
                    if (file == null || file.isEmpty()) {
                        petkeeper.setImg(null);
                        petkeeperRepository.saveAndFlush(petkeeper);
                    } else {
                        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                        fileService.store(file, keeperId, role);
                        petkeeper.setImg(fileName);
                        petkeeperRepository.saveAndFlush(petkeeper);
                    }
                    return "Upload Profile Succesfully!";

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid file type(jpg,png and jpeg only),please try again");
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission!");
    }


    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<List<String>> uploadGallery(Integer keeperId, List<MultipartFile> files, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        String keeperEmail = petkeeperRepository.getPetkeeperEmailById(keeperId);

        try {
            if(role.equals("PetKeeper") && emailCheck.equals(keeperEmail)) {
                return new ResponseEntity<>(fileService.storeMultiple(files, keeperId), HttpStatus.OK);
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission!");
        } catch (Exception e){
            throw new RuntimeException("There is error",e);
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public String deleteGalley(Integer keeperId,List<String> delete, String token) {
        List<String> deletedList = new ArrayList<>();
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        String keeperEmail = petkeeperRepository.getPetkeeperEmailById(keeperId);
        try {
            if(role.equals("PetKeeper") && emailCheck.equals(keeperEmail)) {
                if (delete != null) {
                    for (String name : delete) {
                        galleryRepository.deleteGalleryByName(keeperId, name);
                        deletedList.add(name);
                    }

                    fileService.deleteGallery(delete, keeperId);

                    return "Deleted Successfully!" + deletedList;
                } else return "No images deleted!";
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission!");
        }catch (Exception ex){
            throw new RuntimeException("There is error!",ex);
        }
    }

    @Transactional
    public String updateClosedDay(Integer keeperId,List<String> closedDay,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer keeper = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        String closedDays = "";
        if(!(role.equals("PetKeeper") && keeper == keeperId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You dont have permission!");
        }
        for (int i = 0; i < closedDay.size(); i++){
            if(i != closedDay.size()-1) {
                closedDays = closedDays + closedDay.get(i) + ", ";
            }else closedDays = closedDays + closedDay.get(i);
        }
        petkeeperRepository.updateClosedDay(closedDays,keeperId);
        return "update " + keeperId + ": " + closedDays;
    }

    @Scheduled(cron = "0 0 0 * * *",zone ="Asia/Bangkok")
    @Transactional
    public String updateAvailableDay(){
        List<Petkeepers> petkeepers = petkeeperRepository.findAll();
        String today = ZonedDateTime.now(ZoneId.of("Asia/Bangkok")).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()).toLowerCase();
        System.out.println("start");
        if (petkeepers.isEmpty()) {
            return "No petkeepers found.";
        }
        for (Petkeepers keepers : petkeepers) {
            String closedDay = keepers.getClosedDay();
            if (closedDay != null) {
                String[] closedDays = closedDay.toLowerCase().split("\\s*,\\s*");
                List<String> closedList = Arrays.asList(closedDays);
                System.out.println("Date" + today);
                System.out.println("Close" + closedList);
                if (closedList.contains(today) && keepers.getAvailable() == 1) {
                    petkeeperRepository.updateAvailable(0, keepers.getId());
                    System.out.println("Update for Petkeeper ID:" + keepers.getId() + " - Closed");
                } else if (!closedList.contains(today) && keepers.getAvailable() == 0) {
                    System.out.println("Update for Petkeeper ID:" + keepers.getId() + " - Opened");
                    petkeeperRepository.updateAvailable(1, keepers.getId());
                    System.out.println("Update for Petkeeper ID:" + keepers.getId() + " - Opened");
                }
            } else {
                System.out.println("No update for Petkeeper ID: " + keepers.getId());
            }
        }
        return "Availability updated successfully.";
    }

    @Transactional
    public String closedPetkeeper(Integer keeperId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer checkId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Petkeepers keeper = petkeeperRepository.getById(keeperId);
        String today = ZonedDateTime.now(ZoneId.of("Asia/Bangkok")).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()).toLowerCase();
        String closedDay = keeper.getClosedDay();
        if (closedDay != null) {
            String[] closedDays = closedDay.toLowerCase().split("\\s*,\\s*");
            List<String> closedList = Arrays.asList(closedDays);
            if (role.equals("PetKeeper") && keeperId == checkId) {
                if (keeper.getAvailable() == 0 && !closedList.contains(today)) {
                    petkeeperRepository.updateAvailable(1, keeperId);
                } else if(keeper.getAvailable() == 1 && closedList.contains(today)) {
                    petkeeperRepository.updateAvailable(0, keeperId);
                    }
            }
        }
        return "Availability updated successfully.";
    }
}

package com.project.registration_service.service;

import com.project.registration_service.dao.repository.RegistrationRepository;
import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.enumeration.Level;
import com.project.registration_service.mapper.RegistrationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private RegistrationMapper registrationMapper;

    private Registration registration;
    private RegistrationDTO registrationDTO;
    private Registration registration1;

    private List<Registration> registrationList;
    private List<RegistrationDTO> registrationDTOList;

    @BeforeEach
    void setUp(){
        // Initialisation des objets de test
        registration = new Registration();
        registration.setRegistrationNumber(1L);
        registration.setLevel(Level.L1);

        registration1 = new Registration();
        registration1.setRegistrationNumber(2L);
        registration1.setLevel(Level.M2);

        registrationDTO = new RegistrationDTO(1L, Level.L1);

        registrationList = List.of(registration, registration1);
        // Initialize the instance variable 'this.registrationDTOList'
        registrationDTOList = List.of(
                new RegistrationDTO(registration.getRegistrationNumber(), registration.getLevel()),
                new RegistrationDTO(registration1.getRegistrationNumber(), registration1.getLevel())
        );


    }
    @Test
    void shouldFindRegistrationById(){

        // When findById(1L) is called, it must return the 'registration' object that we've created.
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        // Behavior of the mapping
        //When we call the toDto method with our objet 'registration', we expect to have the good result.
        when(registrationMapper.toDto(registration)).thenReturn(registrationDTO);

        // Call of the service that we're testing
        RegistrationDTO found = registrationService.getRegistration(1L);

        // Assertions : to check if our results are good
        assertThat(found).isNotNull();
        assertThat(found.level()).isEqualTo(registrationDTO.level());

        // Verify the repository was call once
        verify(registrationRepository, times(1)).findById(1L);

    }

    @Test
    void itShouldFindAllRegistrations(){

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(registrationMapper.toDtoList(registrationList)).thenReturn(registrationDTOList);

        List<RegistrationDTO> foundList = registrationService.getAllRegistrations();


        assertThat(foundList).isNotNull();
        assertEquals(registrationDTOList.size(), foundList.size());
        // Check if the returned list is exactly the one mocked
        assertEquals(registrationDTOList, foundList);
        assertThat(foundList.getFirst().registrationNumber()).isEqualTo(registrationList.getFirst().getRegistrationNumber());

        verify(registrationRepository, times(1)).findAll();
        // Verify that toDtoList() was called exactly once on the mapper with the correct list
        verify(registrationMapper, times(1)).toDtoList(registrationList);
    }

    @Test
    void itShouldCreateRegistration(){
        when(registrationRepository.existsByRegistrationNumber(registration.getRegistrationNumber()))
                .thenReturn(false);

        when(registrationMapper.toEntity(registrationDTO))
                .thenReturn(registration);

        when(registrationRepository.save(any(Registration.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        RegistrationDTO created = registrationService.createRegistration(registrationDTO);

        assertThat(created).isNotNull();
        assertEquals(registration.getRegistrationNumber(), created.registrationNumber());
        assertEquals(registration.getLevel(), created.level());

        verify(registrationRepository, times(1)).existsByRegistrationNumber(registration.getRegistrationNumber());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void itShouldUpdateRegistration(){
        //Arrange

        Long id = 4L;

        Registration existingRegistration = new Registration();
        existingRegistration.setId(id);
        existingRegistration.setRegistrationNumber(405L);
        existingRegistration.setLevel(Level.L2);

        RegistrationDTO inputDTO = new RegistrationDTO(5L,Level.M2);

        Registration updated = new Registration();
        updated.setId(id);
        updated.setRegistrationNumber(inputDTO.registrationNumber());
        updated.setLevel(inputDTO.level());

        RegistrationDTO expectedDTO = new RegistrationDTO(5L, Level.M2);

        // Mock behavior
        when(this.registrationRepository.findById(id)).thenReturn(Optional.of(existingRegistration));

        when(registrationRepository.save(Mockito.any())).thenAnswer(invocation -> {
            Registration savedArg = invocation.getArgument(0);
            assertEquals(5L, savedArg.getRegistrationNumber());
            assertEquals(Level.M2, savedArg.getLevel());
            return updated;
        });
        when(registrationMapper.toDto(updated)).thenReturn(expectedDTO);

        // Act
        RegistrationDTO result = registrationService.updateRegistration(id, inputDTO);

        assertEquals(expectedDTO.registrationNumber(), result.registrationNumber());
        assertEquals(expectedDTO.level(), result.level());

        Mockito.verify(registrationRepository, times(1)).findById(id);
        Mockito.verify(registrationRepository, times(1)).save(existingRegistration);
        Mockito.verify(registrationMapper, times(1)).toDto(updated);


    }
}

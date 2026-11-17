package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.DeleteRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.NewRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.SelectAllRolesUseCase;
import org.vedruna.palcole.domain.ports.inbound.SelectRoleByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.UpdateRoleUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.RoleInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.InsertRoleDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.RoleDTO;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Mock private NewRoleUseCase newRoleUseCase;
  @Mock private DeleteRoleUseCase deleteRoleUseCase;
  @Mock private SelectAllRolesUseCase selectAllRolesUseCase;
  @Mock private SelectRoleByIdUseCase selectRoleByIdUseCase;
  @Mock private UpdateRoleUseCase updateRoleUseCase;
  @Mock private RoleInConverter roleInConverter;

  @InjectMocks private RoleController roleController;

  private Role roleDomain;
  private RoleDTO roleDTO;
  private InsertRoleDTO insertRoleDTO;

  @BeforeEach
  void setUp() {
    // Se registra PageableHandlerMethodArgumentResolver para que MockMvc resuelva el parámetro
    // Pageable
    mockMvc =
        MockMvcBuilders.standaloneSetup(roleController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();

    roleDomain = new Role();
    roleDomain.setRoleId(1);
    roleDomain.setName("ADMIN");

    roleDTO = new RoleDTO();
    roleDTO.setRoleId(1);
    roleDTO.setName("ADMIN");

    insertRoleDTO = new InsertRoleDTO();
    insertRoleDTO.setName("ADMIN");
  }

  @Test
  void getAllRoles_Returns200() throws Exception {
    PageableModel pageableModel = new PageableModel();
    PageModel<Role> rolePageModel = new PageModel<>();
    rolePageModel.setContent(List.of(roleDomain));

    Page<RoleDTO> dtoPage = new PageImpl<>(List.of(roleDTO));

    when(roleInConverter.toPageableModel(any(Pageable.class))).thenReturn(pageableModel);
    when(selectAllRolesUseCase.selectAllRoles(pageableModel)).thenReturn(rolePageModel);
    when(roleInConverter.toRolesPage(rolePageModel, pageableModel)).thenReturn(dtoPage);

    mockMvc
        .perform(get("/api/v1/roles/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].roleId").value(1))
        .andExpect(jsonPath("$.content[0].name").value("ADMIN"));

    verify(selectAllRolesUseCase).selectAllRoles(pageableModel);
  }

  @Test
  void insertIntoRole_Returns200() throws Exception {
    when(roleInConverter.toDomainInsert(any(InsertRoleDTO.class))).thenReturn(roleDomain);
    when(newRoleUseCase.newRole(roleDomain)).thenReturn(roleDomain);
    when(roleInConverter.toDto(roleDomain)).thenReturn(roleDTO);

    mockMvc
        .perform(
            post("/api/v1/roles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insertRoleDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.roleId").value(1))
        .andExpect(jsonPath("$.name").value("ADMIN"));

    verify(newRoleUseCase).newRole(roleDomain);
  }

  @Test
  void getRoleById_Returns200() throws Exception {
    when(selectRoleByIdUseCase.selectRoleById(1)).thenReturn(roleDomain);
    when(roleInConverter.toDto(roleDomain)).thenReturn(roleDTO);

    mockMvc
        .perform(get("/api/v1/roles/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.roleId").value(1))
        .andExpect(jsonPath("$.name").value("ADMIN"));

    verify(selectRoleByIdUseCase).selectRoleById(1);
  }

  @Test
  void updateRole_Returns200() throws Exception {
    Role updatedDomain = new Role();
    updatedDomain.setRoleId(1);
    updatedDomain.setName("TEACHER");

    RoleDTO updatedDTO = new RoleDTO();
    updatedDTO.setRoleId(1);
    updatedDTO.setName("TEACHER");

    InsertRoleDTO updateRequest = new InsertRoleDTO();
    updateRequest.setName("TEACHER");

    when(roleInConverter.toDomainInsert(any(InsertRoleDTO.class))).thenReturn(updatedDomain);
    when(updateRoleUseCase.updateRole(eq(1), any(Role.class))).thenReturn(updatedDomain);
    when(roleInConverter.toDto(updatedDomain)).thenReturn(updatedDTO);

    mockMvc
        .perform(
            put("/api/v1/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.roleId").value(1))
        .andExpect(jsonPath("$.name").value("TEACHER"));

    verify(updateRoleUseCase).updateRole(eq(1), any(Role.class));
  }

  @Test
  void deleteRole_Returns204() throws Exception {
    doNothing().when(deleteRoleUseCase).deleteRole(1);

    mockMvc.perform(delete("/api/v1/roles/1")).andExpect(status().isNoContent());

    verify(deleteRoleUseCase).deleteRole(1);
  }
}

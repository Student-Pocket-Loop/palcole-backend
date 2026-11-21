package org.vedruna.palcole.config;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vedruna.palcole.application.User.DeleteUserUseCaseImpl;
import org.vedruna.palcole.application.User.NewUserUseCaseImpl;
import org.vedruna.palcole.application.User.SelectAllUsersUseCaseImpl;
import org.vedruna.palcole.application.User.SelectUserByIdUseCaseImpl;
import org.vedruna.palcole.application.User.UpdateUserUseCaseImpl;
import org.vedruna.palcole.application.academicItems.DeleteAcademicItemUseCaseImpl;
import org.vedruna.palcole.application.academicItems.NewAcademicItemUseCaseImpl;
import org.vedruna.palcole.application.academicItems.SelectAcademicItemByIdUseCaseImpl;
import org.vedruna.palcole.application.academicItems.SelectAllAcademicItemsUseCaseImpl;
import org.vedruna.palcole.application.academicItems.UpdateAcademicItemUseCaseImpl;
import org.vedruna.palcole.application.categories.DeleteCategoryUseCaseImpl;
import org.vedruna.palcole.application.categories.NewCategoryUseCaseImpl;
import org.vedruna.palcole.application.categories.SelectAllCategoriesUseCaseImpl;
import org.vedruna.palcole.application.categories.SelectCategoryByIdUseCaseImpl;
import org.vedruna.palcole.application.categories.UpdateCategoryUseCaseImpl;
import org.vedruna.palcole.application.conversations.DeleteConversationUseCaseImpl;
import org.vedruna.palcole.application.conversations.NewConversationUseCaseImpl;
import org.vedruna.palcole.application.conversations.SelectAllConversationsUseCaseImpl;
import org.vedruna.palcole.application.conversations.SelectConversationByIdUseCaseImpl;
import org.vedruna.palcole.application.favoritePromotions.DeleteFavoritePromotionUseCaseImpl;
import org.vedruna.palcole.application.favoritePromotions.NewFavoritePromotionUseCaseImpl;
import org.vedruna.palcole.application.favoritePromotions.SelectAllFavoritePromotionsUseCaseImpl;
import org.vedruna.palcole.application.favoritePromotions.SelectFavoritePromotionByIdUseCaseImpl;
import org.vedruna.palcole.application.promotions.DeletePromotionUseCaseImpl;
import org.vedruna.palcole.application.promotions.NewPromotionUseCaseImpl;
import org.vedruna.palcole.application.promotions.SelectAllPromotionsUseCaseImpl;
import org.vedruna.palcole.application.promotions.SelectPromotionByIdUseCaseImpl;
import org.vedruna.palcole.application.promotions.UpdatePromotionUseCaseImpl;
import org.vedruna.palcole.application.role.DeleteRoleUseCaseImpl;
import org.vedruna.palcole.application.role.NewRoleUseCaseImpl;
import org.vedruna.palcole.application.role.SelectAllRolesUseCaseImpl;
import org.vedruna.palcole.application.role.SelectRoleByIdUseCaseImpl;
import org.vedruna.palcole.application.role.UpdateRoleUseCaseImpl;
import org.vedruna.palcole.domain.ports.inbound.academicItems.DeleteAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.NewAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAcademicItemByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAllAcademicItemsUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.UpdateAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.DeleteCategoryUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.NewCategoryUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectAllCategoriesUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectCategoryByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.UpdateCategoryUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.DeleteConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.NewConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectAllConversationsUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectConversationByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.DeleteFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.NewFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectAllFavoritePromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectFavoritePromotionByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.DeletePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.NewPromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectAllPromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectPromotionByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.UpdatePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.DeleteRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.NewRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.SelectAllRolesUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.SelectRoleByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.UpdateRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.DeleteUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.NewUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectAllUsersUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectUserByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.UpdateUserUseCase;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;
import org.vedruna.palcole.persistance.repository.UserRepository;

/**
 * Clase de configuración principal para definir los Beans relacionados con la seguridad de la
 * aplicación (Spring Security).
 *
 * <p>Es responsable de configurar el gestor de autenticación, el proveedor de autenticación, el
 * servicio de detalles de usuario y el codificador de contraseñas.
 */
@Configuration
@ConfigurationPropertiesScan
@AllArgsConstructor
public class ApplicationConfig {

  /** Repositorio utilizado para acceder a los datos de los usuarios. */
  private final UserRepository userRepo;

  /**
   * Define el Bean de AuthenticationManager.
   *
   * <p>AuthenticationManager es el componente principal de Spring Security que delega la
   * autenticación a los AuthenticationProviders.
   *
   * @param config La configuración de autenticación proporcionada por Spring Security.
   * @return El gestor de autenticación configurado.
   * @throws Exception Si ocurre un error al obtener el gestor de autenticación.
   */
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Define el Bean de AuthenticationProvider.
   *
   * <p>Configura un DaoAuthenticationProvider, que es el encargado de buscar los detalles del
   * usuario a través del UserDetailsService y de verificar la contraseña usando el PasswordEncoder.
   *
   * @param config La configuración de autenticación (aunque no se utiliza directamente en este
   *     método, se mantiene para consistencia si se requiere en el futuro).
   * @return El proveedor de autenticación configurado.
   */
  @Bean
  AuthenticationProvider authenticationProvider(AuthenticationConfiguration config) {
    // Crea un proveedor que usa nuestro UserDetailsService personalizado
    DaoAuthenticationProvider authenticationProvider =
        new DaoAuthenticationProvider(userDetailService());

    // Asigna el codificador de contraseñas
    authenticationProvider.setPasswordEncoder(passwordEncoder());

    return authenticationProvider;
  }

  /**
   * Define el Bean de UserDetailsService.
   *
   * <p>Este es un contrato de Spring Security que define cómo se carga la información del usuario
   * (incluyendo roles y contraseña codificada) a partir de un nombre de usuario.
   *
   * <p>Aquí se implementa buscando al usuario por nombre de usuario en el UserRepository.
   *
   * @return Una implementación de UserDetailsService.
   * @throws NoSuchElementException Si el usuario no es encontrado.
   */
  // TODO: Excepcion personalizada
  @Bean
  UserDetailsService userDetailService() {
    return username ->
        userRepo
            .findByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("User not found"));
  }

  /**
   * Define el Bean de PasswordEncoder.
   *
   * <p>Especifica qué algoritmo se utilizará para codificar y verificar las contraseñas.
   * BCryptPasswordEncoder es el estándar recomendado por Spring Security.
   *
   * @return Una instancia de BCryptPasswordEncoder.
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  NewRoleUseCase getNewRoleUseCase(RoleStorage roleStorage) {
    return new NewRoleUseCaseImpl(roleStorage);
  }

  @Bean
  SelectAllRolesUseCase getSelectAllRolesUseCase(RoleStorage roleStorage) {
    return new SelectAllRolesUseCaseImpl(roleStorage);
  }

  @Bean
  DeleteRoleUseCase getDeleteRoleUseCase(RoleStorage roleStorage) {
    return new DeleteRoleUseCaseImpl(roleStorage);
  }

  @Bean
  SelectRoleByIdUseCase getSelectRoleByIdUseCase(RoleStorage roleStorage) {
    return new SelectRoleByIdUseCaseImpl(roleStorage);
  }

  @Bean
  UpdateRoleUseCase getUpdateRoleUseCase(RoleStorage roleStorage) {
    return new UpdateRoleUseCaseImpl(roleStorage);
  }

  @Bean
  NewUserUseCase getNewUserUseCase(UserStorage userStorage) {
    return new NewUserUseCaseImpl(userStorage);
  }

  @Bean
  SelectAllUsersUseCase getSelectAllUsersUseCase(UserStorage userStorage) {
    return new SelectAllUsersUseCaseImpl(userStorage);
  }

  @Bean
  DeleteUserUseCase getDeleteUserUseCase(UserStorage userStorage) {
    return new DeleteUserUseCaseImpl(userStorage);
  }

  @Bean
  SelectUserByIdUseCase getSelectUserByIdUseCase(UserStorage userStorage) {
    return new SelectUserByIdUseCaseImpl(userStorage);
  }

  @Bean
  UpdateUserUseCase getUpdateUserUseCase(UserStorage userStorage) {
    return new UpdateUserUseCaseImpl(userStorage);
  }

  @Bean
  NewCategoryUseCase getNewCategoryUseCase(CategoryStorage categoryStorage) {
    return new NewCategoryUseCaseImpl(categoryStorage);
  }

  @Bean
  SelectAllCategoriesUseCase getSelectAllCategoriesUseCase(CategoryStorage categoryStorage) {
    return new SelectAllCategoriesUseCaseImpl(categoryStorage);
  }

  @Bean
  SelectCategoryByIdUseCase getSelectCategoryByIdUseCase(CategoryStorage categoryStorage) {
    return new SelectCategoryByIdUseCaseImpl(categoryStorage);
  }

  @Bean
  UpdateCategoryUseCase getUpdateCategoryUseCase(CategoryStorage categoryStorage) {
    return new UpdateCategoryUseCaseImpl(categoryStorage);
  }

  @Bean
  DeleteCategoryUseCase getDeleteCategoryUseCase(CategoryStorage categoryStorage) {
    return new DeleteCategoryUseCaseImpl(categoryStorage);
  }

  @Bean
  NewPromotionUseCase getNewPromotionUseCase(PromotionStorage promotionStorage) {
    return new NewPromotionUseCaseImpl(promotionStorage);
  }

  @Bean
  SelectAllPromotionsUseCase getSelectAllPromotionsUseCase(PromotionStorage promotionStorage) {
    return new SelectAllPromotionsUseCaseImpl(promotionStorage);
  }

  @Bean
  SelectPromotionByIdUseCase getSelectPromotionByIdUseCase(PromotionStorage promotionStorage) {
    return new SelectPromotionByIdUseCaseImpl(promotionStorage);
  }

  @Bean
  UpdatePromotionUseCase getUpdatePromotionUseCase(PromotionStorage promotionStorage) {
    return new UpdatePromotionUseCaseImpl(promotionStorage);
  }

  @Bean
  DeletePromotionUseCase getDeletePromotionUseCase(PromotionStorage promotionStorage) {
    return new DeletePromotionUseCaseImpl(promotionStorage);
  }

  @Bean
  NewAcademicItemUseCase getNewAcademicItemUseCase(AcademicItemStorage academicItemStorage) {
    return new NewAcademicItemUseCaseImpl(academicItemStorage);
  }

  @Bean
  SelectAllAcademicItemsUseCase getSelectAllAcademicItemsUseCase(
      AcademicItemStorage academicItemStorage) {
    return new SelectAllAcademicItemsUseCaseImpl(academicItemStorage);
  }

  @Bean
  SelectAcademicItemByIdUseCase getSelectAcademicItemByIdUseCase(
      AcademicItemStorage academicItemStorage) {
    return new SelectAcademicItemByIdUseCaseImpl(academicItemStorage);
  }

  @Bean
  UpdateAcademicItemUseCase getUpdateAcademicItemUseCase(AcademicItemStorage academicItemStorage) {
    return new UpdateAcademicItemUseCaseImpl(academicItemStorage);
  }

  @Bean
  DeleteAcademicItemUseCase getDeleteAcademicItemUseCase(AcademicItemStorage academicItemStorage) {
    return new DeleteAcademicItemUseCaseImpl(academicItemStorage);
  }

  @Bean
  NewFavoritePromotionUseCase getNewFavoritePromotionUseCase(
      FavoritePromotionStorage favoritePromotionStorage) {
    return new NewFavoritePromotionUseCaseImpl(favoritePromotionStorage);
  }

  @Bean
  SelectAllFavoritePromotionsUseCase getSelectAllFavoritePromotionsUseCase(
      FavoritePromotionStorage favoritePromotionStorage) {
    return new SelectAllFavoritePromotionsUseCaseImpl(favoritePromotionStorage);
  }

  @Bean
  SelectFavoritePromotionByIdUseCase getSelectFavoritePromotionByIdUseCase(
      FavoritePromotionStorage favoritePromotionStorage) {
    return new SelectFavoritePromotionByIdUseCaseImpl(favoritePromotionStorage);
  }

  @Bean
  DeleteFavoritePromotionUseCase getDeleteFavoritePromotionUseCase(
      FavoritePromotionStorage favoritePromotionStorage) {
    return new DeleteFavoritePromotionUseCaseImpl(favoritePromotionStorage);
  }

  @Bean
  NewConversationUseCase getNewConversationUseCase(ConversationStorage conversationStorage) {
    return new NewConversationUseCaseImpl(conversationStorage);
  }

  @Bean
  SelectAllConversationsUseCase getSelectAllConversationsUseCase(
      ConversationStorage conversationStorage) {
    return new SelectAllConversationsUseCaseImpl(conversationStorage);
  }

  @Bean
  SelectConversationByIdUseCase getSelectConversationByIdUseCase(
      ConversationStorage conversationStorage) {
    return new SelectConversationByIdUseCaseImpl(conversationStorage);
  }

  @Bean
  DeleteConversationUseCase getDeleteConversationUseCase(ConversationStorage conversationStorage) {
    return new DeleteConversationUseCaseImpl(conversationStorage);
  }
}

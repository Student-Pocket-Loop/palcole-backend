package org.vedruna.palcole.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.ConversationEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.MessageEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.PromotionEntity;

/** Entidad que representa un usuario y sus detalles de seguridad (implementa UserDetails). */
@Data
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  Integer userId;

  @Column(name = "username", unique = true, nullable = false)
  String username;

  @Column(name = "email", unique = true, nullable = false)
  String email;

  @Column(name = "password", nullable = false)
  String password;

  @Column(name = "educational_center", nullable = true)
  String educationalCenter;

  @Column(name = "create_date", nullable = false)
  LocalDateTime createDate;

  @Column(name = "account_expiration_date")
  LocalDateTime accountExpirationDate;

  @Column(name = "is_locked")
  Boolean isLocked;

  @Column(name = "credentials_expiration_date")
  LocalDateTime credentialsExpirationDate;

  @Column(name = "is_enabled")
  Boolean isEnabled;

  // IMPORTANTE EAGER para que se cargue el rol en la consulta
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "roles_role_id", referencedColumnName = "role_id")
  RoleEntity userRol;

  // 1:N. La FK academic_items.student_id es ON DELETE CASCADE: al borrar un usuario, la base de
  // datos borra sus artículos (por eso @OnDelete y no un cascade de JPA).
  @OneToMany(mappedBy = "student")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  List<AcademicItemEntity> academicItems;

  // 1:N. La FK conversations.buyer_id es ON DELETE CASCADE.
  @OneToMany(mappedBy = "buyer")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  List<ConversationEntity> conversations;

  // 1:N sin cascade. La FK messages.sender_id no tiene ON DELETE (RESTRICT).
  @OneToMany(mappedBy = "sender")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  List<MessageEntity> messages;

  // N:M con los artículos. favorite_items es una tabla puente pura (solo user_id e item_id), por
  // eso se mapea con @ManyToMany y no necesita entity propia. UserEntity es el lado dueño.
  @ManyToMany
  @JoinTable(
      name = "favorite_items",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  Set<AcademicItemEntity> favoriteItems;

  // N:M con las promociones (tabla puente favorite_promotions).
  @ManyToMany
  @JoinTable(
      name = "favorite_promotions",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "promotion_id"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  Set<PromotionEntity> favoritePromotions;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Si el rol es nulo (aunque esto debería ser prevenido por la lógica de negocio/BD)
    if (this.userRol == null || this.userRol.getName() == null) {
      return Collections.emptyList();
    }

    // Mapea el nombre del rol a una autoridad de Spring Security
    // Se añade el prefijo "ROLE_" como buena práctica de Spring Security.
    String authorityName = "ROLE_" + this.userRol.getName().toUpperCase();

    return List.of(new SimpleGrantedAuthority(authorityName));
  }

  /**
   * Indica si la cuenta del usuario ha expirado.
   *
   * @return true si la cuenta es válida (nunca expira en esta implementación).
   */
  @Override
  public boolean isAccountNonExpired() {
    return LocalDateTime.now().isBefore(accountExpirationDate);
  }

  /**
   * Indica si la cuenta del usuario está bloqueada.
   *
   * @return true si la cuenta no está bloqueada (nunca se bloquea en esta implementación).
   */
  @Override
  public boolean isAccountNonLocked() {
    return !isLocked;
  }

  /**
   * Indica si las credenciales (contraseña) del usuario han expirado.
   *
   * @return true si las credenciales son válidas (nunca expiran en esta implementación).
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return LocalDateTime.now().isBefore(credentialsExpirationDate);
  }

  /**
   * Indica si el usuario está habilitado o deshabilitado.
   *
   * @return true si el usuario está habilitado (siempre habilitado en esta implementación).
   */
  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}

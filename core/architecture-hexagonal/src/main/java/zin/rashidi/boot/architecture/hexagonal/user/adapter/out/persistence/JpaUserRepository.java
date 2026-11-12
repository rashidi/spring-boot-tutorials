package zin.rashidi.boot.architecture.hexagonal.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
}

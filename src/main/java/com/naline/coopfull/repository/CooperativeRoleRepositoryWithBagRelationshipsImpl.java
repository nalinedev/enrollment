package com.naline.coopfull.repository;

import com.naline.coopfull.domain.CooperativeRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CooperativeRoleRepositoryWithBagRelationshipsImpl implements CooperativeRoleRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String COOPERATIVEROLES_PARAMETER = "cooperativeRoles";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CooperativeRole> fetchBagRelationships(Optional<CooperativeRole> cooperativeRole) {
        return cooperativeRole.map(this::fetchPermissionses);
    }

    @Override
    public Page<CooperativeRole> fetchBagRelationships(Page<CooperativeRole> cooperativeRoles) {
        return new PageImpl<>(
            fetchBagRelationships(cooperativeRoles.getContent()),
            cooperativeRoles.getPageable(),
            cooperativeRoles.getTotalElements()
        );
    }

    @Override
    public List<CooperativeRole> fetchBagRelationships(List<CooperativeRole> cooperativeRoles) {
        return Optional.of(cooperativeRoles).map(this::fetchPermissionses).orElse(List.of());
    }

    CooperativeRole fetchPermissionses(CooperativeRole result) {
        return entityManager
            .createQuery(
                "select cooperativeRole from CooperativeRole cooperativeRole left join fetch cooperativeRole.permissionses where cooperativeRole.id = :id",
                CooperativeRole.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<CooperativeRole> fetchPermissionses(List<CooperativeRole> cooperativeRoles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cooperativeRoles.size()).forEach(index -> order.put(cooperativeRoles.get(index).getId(), index));
        List<CooperativeRole> result = entityManager
            .createQuery(
                "select cooperativeRole from CooperativeRole cooperativeRole left join fetch cooperativeRole.permissionses where cooperativeRole in :cooperativeRoles",
                CooperativeRole.class
            )
            .setParameter(COOPERATIVEROLES_PARAMETER, cooperativeRoles)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
